package de.pfannekuchen.lotas.core;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * Core Plugin for the Forge Modloader that loads Mixin for versions that don't automatically
 * @author Jonas Herzig
 * @since v1.2
 * @version v1.2
 */
public class LoadingPlugin implements IFMLLoadingPlugin {

	public LoadingPlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("lotas.mixin.json");

        CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            URL location = codeSource.getLocation();
            try {
                File file = new File(location.toURI());
                if (file.isFile()) {
                    // This forces forge to reexamine the jar file for FML mods
                    // Should eventually be handled by Mixin itself, maybe?
                    //#if MC>=10809
                    CoreModManager.getIgnoredMods().remove(file.getName());
                    //#else
                    //$$ CoreModManager.getLoadedCoremods().remove(file.getName());
                    //#endif
                }
            } catch (URISyntaxException e) {
            	e.printStackTrace();
            }
        } else {
            System.err.println("No CodeSource, if this is not a development environment we might run into problems!");
            System.err.println(getClass().getProtectionDomain());
        }
    }

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
