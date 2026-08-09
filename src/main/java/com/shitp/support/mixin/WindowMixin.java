package com.shitp.support.mixin;

import com.shitp.support.screen.ResourceManagerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFWDropCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "onFilesDropped", at = @At("HEAD"), cancellable = true)
    private void onFilesDropped(long window, long count, long names, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ResourceManagerScreen screen && count > 0) {
            List<Path> paths = new ArrayList<>();
            int fileCount = (int) count;
            for (int i = 0; i < fileCount; i++) {
                String pathStr = GLFWDropCallback.getName(names, i);
                if (pathStr != null) {
                    paths.add(Paths.get(pathStr));
                }
            }
            if (!paths.isEmpty()) {
                screen.filesDropped(paths);
            }
        }
    }
}
