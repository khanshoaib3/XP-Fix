package net.shoaibkhan.xpfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class XPMixin {

	@Inject(at =  @At(value = "INVOKE", target="Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I"), method = "renderExperienceBar",cancellable = true)
	private void init(MatrixStack matrixStack,int x, CallbackInfo info) {
		MinecraftClient client = MinecraftClient.getInstance();
		info.cancel();
	}
}
