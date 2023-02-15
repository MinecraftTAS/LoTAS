package com.minecrafttas.lotas.mixin.duck;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.minecrafttas.lotas.duck.ServerPlayerDuck;
import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDuckApplier extends Player implements ServerPlayerDuck{

	@Shadow
	private boolean isChangingDimension;
	@Shadow
	private boolean wonGame;
	@Shadow
	private ServerGamePacketListenerImpl connection;
	@Shadow
	private boolean seenCredits;
	@Shadow
	private MinecraftServer server;
	@Shadow
	private Vec3 enteredNetherPosition;
	@Shadow
	private ServerPlayerGameMode gameMode;
	@Shadow
	private int lastSentExp;
	@Shadow
	private float lastSentHealth;
	@Shadow
	private int lastSentFood;

	public ServerPlayerDuckApplier(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Override
	public Entity changeDimensionNoPortal(DimensionType dimensionType) {
		this.isChangingDimension = true;
        DimensionType dimensionType2 = this.dimension;
        if (dimensionType2 == DimensionType.THE_END && dimensionType == DimensionType.OVERWORLD) {
            this.unRide();
            this.getLevel().removePlayerImmediately((ServerPlayer)(Object)this);
            if (!this.wonGame) {
                this.wonGame = true;
                this.connection.send(new ClientboundGameEventPacket(4, this.seenCredits ? 0.0f : 1.0f));
                this.seenCredits = true;
            }
            return this;
        }
        ServerLevel serverLevel = this.server.getLevel(dimensionType2);
        this.dimension = dimensionType;
        ServerLevel serverLevel2 = this.server.getLevel(dimensionType);
        LevelData levelData = this.level.getLevelData();
        this.connection.send(new ClientboundRespawnPacket(dimensionType, levelData.getGeneratorType(), this.gameMode.getGameModeForPlayer()));
        this.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
        PlayerList playerList = this.server.getPlayerList();
        playerList.sendPlayerPermissionLevel((ServerPlayer)(Object)this);
        serverLevel.removePlayerImmediately((ServerPlayer)(Object)this);
        this.removed = false;
        double d = this.x;
        double e = this.y;
        double f = this.z;
        float g = this.xRot;
        float h = this.yRot;
        double i = 8.0;
        float j = h;
        serverLevel.getProfiler().push("moving");
        if (dimensionType2 == DimensionType.OVERWORLD && dimensionType == DimensionType.NETHER) {
            this.enteredNetherPosition = new Vec3(this.x, this.y, this.z);
            d /= 8.0;
            f /= 8.0;
        } else if (dimensionType2 == DimensionType.NETHER && dimensionType == DimensionType.OVERWORLD) {
            d *= 8.0;
            f *= 8.0;
        } else if (dimensionType2 == DimensionType.OVERWORLD && dimensionType == DimensionType.THE_END) {
            BlockPos blockPos = serverLevel2.getDimensionSpecificSpawn();
            d = blockPos.getX();
            e = blockPos.getY();
            f = blockPos.getZ();
            h = 90.0f;
            g = 0.0f;
        }
        this.moveTo(d, e, f, h, g);
        serverLevel.getProfiler().pop();
        serverLevel.getProfiler().push("placing");
        double k = Math.min(-2.9999872E7, serverLevel2.getWorldBorder().getMinX() + 16.0);
        double l = Math.min(-2.9999872E7, serverLevel2.getWorldBorder().getMinZ() + 16.0);
        double m = Math.min(2.9999872E7, serverLevel2.getWorldBorder().getMaxX() - 16.0);
        double n = Math.min(2.9999872E7, serverLevel2.getWorldBorder().getMaxZ() - 16.0);
        d = Mth.clamp(d, k, m);
        f = Mth.clamp(f, l, n);
        this.moveTo(d, e, f, h, g);
        serverLevel.getProfiler().pop();
        this.setLevel(serverLevel2);
        serverLevel2.addDuringPortalTeleport((ServerPlayer)(Object)this);
        this.triggerDimensionChangeTriggers(serverLevel);
        this.connection.teleport(this.x, this.y, this.z, h, g);
        this.gameMode.setLevel(serverLevel2);
        this.connection.send(new ClientboundPlayerAbilitiesPacket(this.abilities));
        playerList.sendLevelInfo((ServerPlayer)(Object)this, serverLevel2);
        playerList.sendAllPlayerInfo((ServerPlayer)(Object)this);
        for (MobEffectInstance mobEffectInstance : this.getActiveEffects()) {
            this.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), mobEffectInstance));
        }
        this.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
        this.lastSentExp = -1;
        this.lastSentHealth = -1.0f;
        this.lastSentFood = -1;
        return (ServerPlayer)(Object)this;
	}

	@Shadow
	protected abstract void triggerDimensionChangeTriggers(ServerLevel serverLevel);

	@Shadow
	protected abstract ServerLevel getLevel();
	
}
