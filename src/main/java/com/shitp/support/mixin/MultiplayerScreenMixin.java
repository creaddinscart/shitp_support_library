package com.shitp.support.mixin;

import com.shitp.support.screen.ResourceManagerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MultiplayerScreen.class, TitleScreen.class})
public class ScreenMixin extends Screen {

    protected ScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addShitpButton(CallbackInfo ci) {
        int posX = 5;
        int posY = 5;

        if ((Object) this instanceof TitleScreen) {
            posY = 25;
        }

        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Shitp Manager"), button -> {
                if (this.client != null) {
                    this.client.setScreen(new ResourceManagerScreen(this));
                }
            })
            .dimensions(posX, posY, 120, 20)
            .build()
        );
    }
}
