package net.shoaibkhan.xpfix.mixin;

import com.mojang.blaze3d.systems.RenderSystem;

import net.shoaibkhan.xpfix.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Locale;

@Mixin(InGameHud.class)
public class XPMixin {

	@Inject(at =  @At(value = "INVOKE", target="Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I"), method = "renderExperienceBar",cancellable = true)
	private void init(MatrixStack matrixStack,int x, CallbackInfo info) {
		if(Config.get(Config.getXpFixKey())) {
			MinecraftClient client = MinecraftClient.getInstance();
			matrixStack = new MatrixStack();
			RenderSystem.enableBlend();

			if (client.player.experienceLevel > 0) {
				matrixStack.push();
				matrixStack.scale(1, 1, 1);
				String string = "" + client.player.experienceLevel;
				int height = client.getWindow().getScaledHeight();
				int width = client.getWindow().getScaledWidth();

				float reqHeight = 40;
				try{
					reqHeight = Float.parseFloat(Config.getString(Config.getXpFixPositionYKey()));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("An error occures:\t"+e.getMessage());
					reqHeight = 40;
				}
				float reqWidth = 50;

				float nn = (int) height - reqHeight;
				float mm = 222;
				String st = Config.getString(Config.getXpFixPositionXKey());
				st = st.toLowerCase().trim();

				if(st.equalsIgnoreCase("center")||st.equalsIgnoreCase("centre")){
					mm = (width - client.inGameHud.getFontRenderer().getWidth(string))/2;
				} else if(st.contains("offset")) {
					st = st.replace("offset","");
					if(st.contains(":")) st = st.replace(":","");
					try{
						mm = ((width - client.inGameHud.getFontRenderer().getWidth(string)) / 2) + Float.parseFloat(st);
					} catch (Exception e){
						mm = ((width - client.inGameHud.getFontRenderer().getWidth(string)) / 2) + 0;
					}
				} else {
					try{
						reqWidth = Float.parseFloat(Config.getString(Config.getXpFixPositionXKey()));
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("An error occures:\t"+e.getMessage());
						reqWidth = 50;
					}
					mm = (int) (( width * reqWidth / 100 ) + client.inGameHud.getFontRenderer().getWidth(string));
				}
				client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) (mm + 1), (float) nn, 0);
				client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) (mm - 1), (float) nn, 0);
				client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) mm, (float) (nn + 1), 0);
				client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) mm, (float) (nn - 1), 0);
				client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) mm, (float) nn, 8453920);
				matrixStack.pop();
				Config.loadConfig();
			}

			info.cancel();
		}
	}
}
