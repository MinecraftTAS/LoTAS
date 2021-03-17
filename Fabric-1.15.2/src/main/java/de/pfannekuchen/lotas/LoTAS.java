package de.pfannekuchen.lotas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.gui.AiRigScreen;
import de.pfannekuchen.lotas.gui.EntitySpawnerScreen;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.hotkeys.Hotkeys;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.Screen;
import rlog.RLogAPI;

public class LoTAS implements ClientModInitializer {

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
    	RLogAPI.logDebug("[Version] 1.15.2");
    	
    	try {
			RLogAPI.instantiate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	RLogAPI.logDebug("[PreInit] Initializing Configuration");
        try {
            ConfigManager.init(new File("lotas.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RLogAPI.logDebug("[PreInit] Initializing Keybindings");
        Hotkeys.initKeybindings();
        RLogAPI.logDebug("[PreInit] Downloading Seeds");
        try {
            loadSeeds();
        } catch (Exception e) {
        	RLogAPI.logError(e, "[PreInit] Reading Seeds File failed #0");
        }
    }

    /**
     * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/seeds3.txt</a> and creates a List
     * @throws IOException
     */
    public void loadSeeds() throws Exception {
        File file = new File("seeddata.txt");
        try {
            URL url = new URL("http://mgnet.work/seeds/seeds3.txt");
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(5000);
            file.createNewFile();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
        } catch (Exception e) {
        	RLogAPI.logError(e, "[Init] Downloading Seeds failed #1");
        }
        List<String> strings = Files.readAllLines(file.toPath());
        for (String line : strings) {
            String seed = line.split(":")[0];
            String name = line.split(":")[1];
            String description = line.split(":")[2];

            SeedListScreen.seeds.add(new SeedListScreen.Seed(seed, name, description));
        }
        RLogAPI.logDebug("[PreInit] " + SeedListScreen.seeds.size() + " seeds loaded and attached to Seed List.");
    }

	public static void renderEvent(Screen gui) {
		if (gui instanceof EntitySpawnerScreen) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			RenderUtils.applyRenderOffset();
			
			double renderX = ((double) ((EntitySpawnerScreen) gui).spawnX - 0.5f);
			double renderY = ((double) ((EntitySpawnerScreen) gui).spawnY);
			double renderZ = ((double) ((EntitySpawnerScreen) gui).spawnZ - 0.5F);
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 1F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		} else if (gui instanceof AiRigScreen) {
			if (AiRigScreen.entities.size() == 0) return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			RenderUtils.applyRenderOffset();
			
			double renderX = ((double) AiRigScreen.entities.get(AiRigScreen.selectedIndex).x - 0.5f);
			double renderY = ((double) AiRigScreen.entities.get(AiRigScreen.selectedIndex).y);
			double renderZ = ((double) AiRigScreen.entities.get(AiRigScreen.selectedIndex).z - 0.5F);
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(1F, 0F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			// Draw output
			
			RenderUtils.applyRenderOffset();
			
			renderX = AiRigScreen.spawnX;
			renderY = AiRigScreen.spawnY;
			renderZ = AiRigScreen.spawnZ;
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 1, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 0F, 1F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		}
	}

}
