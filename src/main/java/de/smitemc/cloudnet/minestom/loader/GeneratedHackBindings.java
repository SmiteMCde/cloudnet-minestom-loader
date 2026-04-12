package de.smitemc.cloudnet.minestom.loader;

import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.ext.platforminject.api.inject.BindingsInstaller;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

final class GeneratedHackBindings implements BindingsInstaller {

    /**
     * Applies the bindings for the extension. This method is intended to be called
     * from the static initializer of the extension's main class, and should not be called manually.
     *
     * @param injectionLayer the injection layer to apply the bindings to
     */
    @Override
    public void applyBindings(@NotNull InjectionLayer<?> injectionLayer) {
        try {
            var bindingClassCarrier = CloudNetExtensionLoader.BINDINGS_CLASS;
            if (bindingClassCarrier.isBound()) {
                var bindingsClass = bindingClassCarrier.get();
                var bindingsClassInstance = bindingsClass.getConstructor().newInstance();
                bindingsClassInstance.applyBindings(injectionLayer);
            }
        } catch (Exception exception) {
            MinecraftServer.LOGGER.error("Failed to do bindings installation for extension", exception);
        }
    }
}
