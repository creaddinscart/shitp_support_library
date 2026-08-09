package com.shitp.support.mixin;

import com.shitp.support.screen.ResourceManagerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
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

    @Inject(method = "onFilesDropped", at = @At("HEAD"))
    private void interceptFilesDropped(long window, int count, long names, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ResourceManagerScreen screen && count > 0) {
            List<Path> paths = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String pathStr = org.lwjgl.glfw.GLFWDropCallbacks.getName(names, i);
                paths.add(Paths.get(pathStr));
            }
            screen.filesDropped(paths);
        }
    }
}
