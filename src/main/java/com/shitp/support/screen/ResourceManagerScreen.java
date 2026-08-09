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
    private String statusMessage = "Drag and drop Mods (.jar), Resource Packs (.zip), or Shader Packs here";
    private int statusColor = 0xAAAAAA;
    private boolean isProcessing = false;
    private int progressAnim = 0;

    public ResourceManagerScreen(Screen parent) {
        super(Text.literal("Shitp Resource & Shader Manager"));
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
            .dimensions(this.width / 2 - 50, this.height - 40, 100, 20)
            .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(statusMessage), this.width / 2, this.height / 2 - 10, statusColor);

        if (isProcessing) {
            progressAnim = (progressAnim + 1) % 100;
            String dots = ".".repeat((progressAnim / 25) + 1);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Processing and detecting files" + dots), this.width / 2, this.height / 2 + 15, 0xFFFF55);
            
            int barWidth = 200;
            int barHeight = 6;
            int barX = this.width / 2 - barWidth / 2;
            int barY = this.height / 2 + 35;
            context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF222222);
            int filledWidth = (int) (barWidth * (progressAnim / 100.0f));
            context.fill(barX, barY, barX + filledWidth, barY + barHeight, 0xFF55FF55);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    public void filesDropped(List<Path> paths) {
        isProcessing = true;
        statusMessage = "Analyzing dropped files...";
        statusColor = 0xFFFF55;

        int successCount = 0;
        boolean hasMod = false;
        boolean hasShaderOrResource = false;

        for (Path path : paths) {
            int result = FileHandler.processDroppedFile(path, this.client);
            if (result > 0) {
                successCount++;
                if (result == 2) {
                    hasMod = true;
                } else if (result == 1 || result == 3) {
                    hasShaderOrResource = true;
                }
            }
        }

        isProcessing = false;
        if (successCount > 0) {
            statusColor = 0x55FF55;
            StringBuilder sb = new StringBuilder("Successfully imported " + successCount + " file(s). ");
            if (hasMod) {
                sb.append("Restart required for mods. ");
            }
            if (hasShaderOrResource) {
                sb.append("Shader/Resource loaded! Rejoin world to apply.");
            }
            statusMessage = sb.toString();
        } else {
            statusColor = 0xFF5555;
            statusMessage = "Failed to process files. Unsupported format.";
        }
    }
}
