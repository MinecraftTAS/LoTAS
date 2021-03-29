package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.gui.toasts.GuiToast;

/**
 * This mixin tries to make the animation of the advancement toasts dependent on the tickrate while keeping the code as vanilla as possible<br>
 * <br>
 * While I spent a long amount of time watching this code, I still don't quite fully understand the math behind this...<br>
 * <br>
 * Here's what I could find out: Toasts have 2 different states represented in visibility in the ToastInstance.<br>
 * And if it's set to SHOW the fly in animation and sound will play, the same goes with HIDE where it flies out after a certain amount of time<br>
 * After a lot of trial and error I found out that animationTimer, which was originally "i", is the way to go...<br>
 * <br>
 * So just as RenderItem and GuiSubtitleOverlay, things are done with an offset for tickrate 0 and simple multiplication<br>
 * Also I used a copy of the vanilla ToastInstance-class to make it work for every subtitle on screen... If you seek to make it work for only 1, at the end is a commented code that shows you how to use @ModyfyVarable<br>
 * <br>
 * There is one compromise I had to make... When you change the tickrate while a toast is showing, it will stay at the old tickrate until it's done...<br>
 * Maybe I still fix this and get into this mess once more, but for now this will do and don't make the subtitles stuck in a loop until you change to the old tickrate<br>
 * Am I doing this right with commenting code? I hope so...<br>
 * <br>
 * Update 02.03.21:<br>
 * Well, it's been roughly a year since I have touched this code and I am finally back with updating this. And while I was at it, I removed the compromise mentioned earlier.<br>
 * </p>
 * @author ScribbleLP
 * 
 * == Rewrite by Pancake ==
 * Yeah, so with newly gained knowledge of ModifyVariable, I have just completly rewritten this mixin. It's readable now :)
 * 
 * 21.03.21
 * I did it again, I rewrote it again. Looks smoother now
 */
@Mixin(GuiToast.ToastInstance.class)
public abstract class BindToast{
	
	@ModifyVariable(method = "render(II)Z", at = @At(value = "STORE", ordinal=0))
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChanger.getMilliseconds();
	}
}