package de.smitemc.cloudnet.minestom.loader;

import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.loader.PlatformInjectSupportLoader;
import net.minestom.server.extensions.Extension;
import org.jetbrains.annotations.NotNull;

final class GeneratedHackEntrypoint {

    /**
     * Calls the plugin manager to load the extension. This method is intended to be called from the static initializer of the generated entrypoint class.
     * @param fakeExtensionInstance a fake instance of the extension to load. This instance is not used for anything and can be created using any constructor of the extension class, as long as it does not throw an exception.
     * @param platformEntrypoint the platform entrypoint class to load. This class must implement {@link PlatformEntrypoint} and must be accessible from the class loader of this class.
     */
    static void callPluginManager(
            @NotNull Extension fakeExtensionInstance,
            @NotNull Class<? extends PlatformEntrypoint> platformEntrypoint
    ) {
        PlatformInjectSupportLoader.loadPlugin(
                "minestom",
                platformEntrypoint,
                fakeExtensionInstance,
                GeneratedHackEntrypoint.class.getClassLoader());
    }
}
