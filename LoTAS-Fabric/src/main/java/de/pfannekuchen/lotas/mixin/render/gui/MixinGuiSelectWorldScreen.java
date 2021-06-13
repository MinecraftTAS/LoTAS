package de.pfannekuchen.lotas.mixin.render.gui;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.gui.widgets.ChallengeMapEntry;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.screen.world.WorldListWidget.Entry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(SelectWorldScreen.class)
public abstract class MixinGuiSelectWorldScreen extends Screen {
	
    protected MixinGuiSelectWorldScreen(Text title) {
		super(title);
	}

    private SmallCheckboxWidget widget;
    private SmallCheckboxWidget challenges;
    @Shadow
    private WorldListWidget levelList;
    
	@Inject(at = @At("TAIL"), method = "init")
    public void injectinit(CallbackInfo ci) {
        this.addButton(new ButtonWidget(2, 2, 98, 20, "Seed List", button -> {
            this.minecraft.openScreen(new SeedListScreen());
        }));
        this.addButton(widget = new SmallCheckboxWidget(width - 160, 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape"), b -> {
        	ConfigUtils.setBoolean("tools", "hitEscape", widget.isChecked());
        	ConfigUtils.save();
        }));
        this.addButton(challenges = new SmallCheckboxWidget(width - 160, 16, "Show TAS Challenge Maps", !ConfigUtils.getBoolean("tools", "hideMaps"), b -> {
        	ConfigUtils.setBoolean("tools", "hideMaps", !challenges.isChecked());
        	ConfigUtils.save();
    		for (Entry entry : new ArrayList<>(levelList.children())) {
    			if (entry instanceof ChallengeMapEntry) levelList.children().remove(entry); 
    		}
    		if (!ConfigUtils.getBoolean("tools", "hideMaps")) {
    			for (ChallengeMap map : LoTASModContainer.maps) {
    				ChallengeMapEntry entry = new ChallengeMapEntry(levelList, map);
    				entry.loc = map.resourceLoc;
    				levelList.children().add(entry);
    			}
    		}
        }));
        
		if (!ConfigUtils.getBoolean("tools", "hideMaps")) {
			for (ChallengeMap map : LoTASModContainer.maps) {
				ChallengeMapEntry entry = new ChallengeMapEntry(levelList, map);
				entry.loc = map.resourceLoc;
				levelList.children().add(entry);
			}
		}
	}
	
}
