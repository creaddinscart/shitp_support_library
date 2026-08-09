package com.shitp.support.screen;

import com.shitp.support.util.FileHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.List;

public class ResourceManagerScreen extends Screen {
    private final Screen parentScreen;
    private String statusMessage = "Drag and drop .zip or .jar files here";
    private int statusColor = 0xAAAAAA;

    public ResourceManagerScreen(Screen parent) {
        super(Text.literal("Shitp Resource Manager"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Back"), button -> {
                if (this.client != null) {
                    this.client.setScreen(this.parentScreen);
                }
            })
            .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
            .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(statusMessage), this.width / 2, this.height / 2, statusColor);
        super.render(context, mouseX, mouseY, delta);
    }

    public void filesDropped(List<Path> paths) {
        int successCount = 0;
        boolean hasMod = false;

        for (Path path : paths) {
            int result = FileHandler.processDroppedFile(path, this.client);
            if (result > 0) {
                successCount++;
                if (result == 2) {
                    hasMod = true;
                }
            }
        }

        if (successCount > 0) {
            statusColor = 0x55FF55;
            if (hasMod) {
                statusMessage = "Loaded " + successCount + " files. Restart required for mods.";
            } else {
                statusMessage = "Loaded " + successCount + " files. Rejoin world/server to apply.";
            }
        } else {
            statusColor = 0xFF5555;
            statusMessage = "Failed to process files.";
        }
    }
}
