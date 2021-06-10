package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.lang.reflect.Field;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorage;

public class ChallengeMenuScreen extends GameMenuScreen {
	
	public ChallengeMenuScreen() {
		super(true);
	}

	SmallCheckboxWidget fw = null; // do not pay attention
	
	@Override
    public void init() {
        this.buttons.clear();
        
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 - 16, 204, 20, "Exit", btn -> {
        	 boolean bl = this.minecraft.isInSingleplayer();
             boolean bl2 = this.minecraft.isConnectedToRealms();
             this.minecraft.world.disconnect();
             btn.active = false;
             if (bl) {
                this.minecraft.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel", new Object[0])));
             } else {
                this.minecraft.disconnect();
             }

             if (bl) {
                this.minecraft.openScreen(new TitleScreen());
             } else if (bl2) {
                RealmsBridge realmsBridge = new RealmsBridge();
                realmsBridge.switchToRealms(new TitleScreen());
             } else {
                this.minecraft.openScreen(new MultiplayerScreen(new TitleScreen()));
             }
             
             ChallengeLoader.map = null;
             try {
            	 Field h = MinecraftClient.class.getDeclaredField("levelStorage");
            	 h.setAccessible(true);
            	 h.set(MinecraftClient.getInstance(), new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("saves"), MinecraftClient.getInstance().runDirectory.toPath().resolve("backups"), MinecraftClient.getInstance().getDataFixer()));
             } catch (Exception e) {
            	 e.printStackTrace();
             }
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 24 + -16, 204, 20, "Continue", btn -> {
        	this.minecraft.openScreen((Screen)null);
            this.minecraft.mouse.lockCursor();
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 48 + -16, 204, 20, "Options", btn -> {
        	this.minecraft.openScreen(new SettingsScreen(this, this.minecraft.options));
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 72 + -16, 204, 20, "Leaderboard", btn -> {
        	MinecraftClient.getInstance().openScreen(new LeaderboardScreen());
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96 + -16, 204, 20, "Restart", btn -> {
        	try {
				ChallengeLoader.reload();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }));
        
        addButton(new ButtonWidget(5, 15, 48, 20, "+", b -> {
            TickrateChangerMod.index++;
            TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
            TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
        }));
        addButton(new ButtonWidget( 55, 15, 48, 20, "-", b -> {
        	TickrateChangerMod.index--;
        	TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
        	TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
        }));
        this.addButton(new ButtonWidget(5, 55, 98, 20, "Save Items",btn -> {
			DupeMod.save(minecraft);
			btn.active = false;
		}));
		this.addButton(new ButtonWidget(5, 75, 98, 20, "Load Items", btn -> {
			DupeMod.load(minecraft);
			btn.active = false;
		}));
        
		this.addButton(new ButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			this.minecraft.openScreen(new LootManipulationScreen((GameMenuScreen) (Object) this));
		}));
		this.addButton(new ButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			this.minecraft.openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
		})).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
		this.addButton(new ButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			this.minecraft.openScreen(new SpawnManipulationScreen());
		}));
		this.addButton(new ButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			this.minecraft.openScreen(new AIManipulationScreen());
		}));
        addButton(new ButtonWidget(37, 115, 66, 20, "Jump ticks", btn -> {
        	TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.active = false;
			btn.setMessage("Jumping...");
        }));
        addButton(new ButtonWidget(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
        	TickrateChangerMod.ji++;
			if (TickrateChangerMod.ji > 10) TickrateChangerMod.ji = 1;
			buttons.clear();
			init();
        }));
        addButton(new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), b -> {
            ConfigUtils.setBoolean("tools", "takeDamage", !b.isChecked());
            ConfigUtils.save();
        }));
       
        final SmallCheckboxWidget tw = addButton(new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), b -> {
            ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", b.isChecked());
            if (b.isChecked()) {
                ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
                fw.silentPress(false);
            }
            ConfigUtils.save();
        }));
        fw = addButton(new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), b -> {
            ConfigUtils.setBoolean("tools", "manipulateVelocityAway", b.isChecked());
            if (b.isChecked()) {
                ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
                tw.silentPress(false);
            }
            ConfigUtils.save();
        }));
        addButton(new SmallCheckboxWidget(2, height - 56 - 15 , "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), b -> {
            ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", b.isChecked());
            ConfigUtils.save();
        }));
        addButton(new SmallCheckboxWidget(2, height - 68 - 15 , "Left Auto Clicker", ConfigUtils.getBoolean("tools", "lAutoClicker"), b -> {
            ConfigUtils.setBoolean("tools", "lAutoClicker", b.isChecked());
            ConfigUtils.save();
        }));
    }
	
	@Override
	public void render(int mouseX, int mouseY, float delta) {
		drawString(minecraft.textRenderer, "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		minecraft.textRenderer.drawWithShadow("Tickjump", 10, 105, 0xFFFFFF);
		if(buttons.get(18).active==false) {
			minecraft.textRenderer.drawWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			minecraft.textRenderer.drawWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}
		minecraft.textRenderer.drawWithShadow("Duping", 10, 45, 0xFFFFFF);
		super.render(mouseX, mouseY, delta);
	}
	
}
