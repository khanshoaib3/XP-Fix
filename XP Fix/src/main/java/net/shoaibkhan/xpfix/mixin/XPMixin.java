package net.shoaibkhan.xpfix.mixin;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.shoaibkhan.xpfix.ClientMod;
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
		Config.loadConfig();
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

				float nn = 222;
				float reqHeight = 50;
				String st1 = Config.getString(Config.getXpFixPositionYKey());
				st1 = st1.toLowerCase().trim();

				if(st1.contains("center")||st1.contains("centre")){
					nn = (height - client.inGameHud.getFontRenderer().getStringBoundedHeight(string,client.inGameHud.getFontRenderer().getWidth(string)))/2;
				} else if(st1.contains("offset")) {
					st1 = st1.replace("offset","");
					if(st1.contains(":")) st1 = st1.replace(":","");
					try{
						nn = ((height - client.inGameHud.getFontRenderer().getStringBoundedHeight(string,client.inGameHud.getFontRenderer().getWidth(string))) / 2) + Float.parseFloat(st1);
					} catch (Exception e){
						nn = ((height - client.inGameHud.getFontRenderer().getStringBoundedHeight(string,client.inGameHud.getFontRenderer().getWidth(string))) / 2) + 0;
					}
				} else {
					try{
						reqHeight = Float.parseFloat(Config.getString(Config.getXpFixPositionYKey()));
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("An error occures:\t"+e.getMessage());
						reqHeight = 40;
					}
					nn = (int) ( height - reqHeight );
				}


				float mm = 222;
				float reqWidth = 50;
				String st = Config.getString(Config.getXpFixPositionXKey());
				st = st.toLowerCase().trim();

				if(st.contains("center")||st.contains("centre")){
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

				int opacity = 100;

				try{
					opacity = Integer.parseInt(Config.getString(Config.getXP_Fix_Opacity_key()));
				} catch (Exception e){
					opacity = 100;
				}
				String color = Config.getString(Config.getXP_Fix_Color_Key());
				color = color.toLowerCase().trim();
				if(!color.contains("transparent")){
					client.inGameHud.getFontRenderer().draw(matrixStack, (String)string, (float)(mm + 1), (float)nn, ClientMod.colors("0",opacity));
					client.inGameHud.getFontRenderer().draw(matrixStack, (String)string, (float)(mm - 1), (float)nn, ClientMod.colors("0",opacity));
					client.inGameHud.getFontRenderer().draw(matrixStack, (String)string, (float)mm, (float)(nn + 1), ClientMod.colors("0",opacity));
					client.inGameHud.getFontRenderer().draw(matrixStack, (String)string, (float)mm, (float)(nn - 1), ClientMod.colors("0",opacity));
					client.inGameHud.getFontRenderer().draw(matrixStack, string, (float) mm, (float) nn, ClientMod.colors(color,opacity));
				}
				matrixStack.pop();
			}

			info.cancel();
		}
	}
}
