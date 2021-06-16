package de.pfannekuchen.lotas.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class NewButtonWidget extends ButtonWidget {

    //#if MC>=11601
    //$$ public NewButtonWidget(int x, int y, int width, int height, String text, ButtonWidget.PressAction ac) {
    //$$     super(x, y, width, height, new LiteralText(text), ac);
    //$$ }
    //#else
    public NewButtonWidget(int x, int y, int width, int height, String message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }
    //#endif

    
}
