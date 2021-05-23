package de.pfannekuchen.lotas.gui.widgets;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageWidget extends ButtonWidget {
    private boolean toggled;

    private ResourceLocation pic;

    public ImageWidget(int x, int y, ButtonWidget.PressAction action, ResourceLocation pic) {
        super(x, y, 20, 20, "", action);
        this.pic = pic;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
    
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, (int) mouseX, (int) mouseY)) {
			toggled = !toggled;
			p.trigger(this);
			return true;
		} else {
			return false;
		}
	}
    
	//#if MC>=11200
	public void render(int mouseX, int mouseY, float delta) {
		drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
	}
	//#endif
	
    @Override
    //#if MC>=11200
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float delta) {
        super.drawButton(mc, mouseX, mouseY, delta);
    //#else
    //$$ public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    //$$ 	super.drawButton(mc, mouseX, mouseY);
    //#endif
        Minecraft.getMinecraft().getTextureManager().bindTexture(pic);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(MCVer.x(this), MCVer.y(this), 0.0F, 0.0F, 20, 20, 20, 20);
        GlStateManager.popMatrix();
    }

}
