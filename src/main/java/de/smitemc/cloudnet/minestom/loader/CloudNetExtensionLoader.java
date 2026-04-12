package de.smitemc.cloudnet.minestom.loader;

import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.inject.BindingsInstaller;
import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

public final class CloudNetExtensionLoader {

    static final ScopedValue<Class<? extends BindingsInstaller>> BINDINGS_CLASS = ScopedValue.newInstance();

    private static final Field DE_NAME;
    private static final Field DE_ENTRYPOINT;

    static {
        try {
            DE_NAME = DiscoveredExtension.class.getDeclaredField("name");
            DE_NAME.setAccessible(true);
            DE_ENTRYPOINT = DiscoveredExtension.class.getDeclaredField("entrypoint");
            DE_ENTRYPOINT.setAccessible(true);

            var extensionManagerField = ExtensionBootstrap.class.getDeclaredField("extensions");
            extensionManagerField.setAccessible(true);
            extensionManagerField.set(null, new ExtensionManager(MinecraftServer.process()));
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    /**
     * Finds a class with the given name using the class loader of this class. Returns null if the class cannot be found or loaded.
     *
     * @param name the fully qualified name of the class to find
     * @return the Class object representing the class with the given name, or null if it cannot be found or loaded
     */
    private static @Nullable Class<?> findClass(@NotNull String name) {
        try {
            return Class.forName(name, false, CloudNetExtensionLoader.class.getClassLoader());
        } catch (Exception _) {
            return null;
        }
    }

    /**
     * Loads a CloudNet extension into the Minestom server. This method is intended to be called from the static initializer
     *
     * @param name the name of the extension to load
     * @param minestomEntrypoint the main class of the Minestom extension, which must extend {@link Extension} and be annotated with
     * @param cloudnetEntrypoint the main class of the CloudNet extension, which must implement {@link PlatformEntrypoint}
     */
    @SuppressWarnings("unchecked")
    public static void loadCloudNetExtension(
            @NotNull String name,
            @NotNull Class<? extends Extension> minestomEntrypoint,
            @NotNull Class<? extends PlatformEntrypoint> cloudnetEntrypoint
    ) {
        try {
            var extensionOrigin = new DiscoveredExtension();
            DE_NAME.set(extensionOrigin, name);
            DE_ENTRYPOINT.set(extensionOrigin, minestomEntrypoint.getName());
            DiscoveredExtension.verifyIntegrity(extensionOrigin);
            var fakeExtension = new FakeExtension(extensionOrigin);

            var bindingsClassName = minestomEntrypoint.getName().replaceFirst("(?s)(.*)Entrypoint", "$1Bindings");
            var bindingsClass = (Class<? extends BindingsInstaller>) findClass(bindingsClassName);
            ScopedValue.where(BINDINGS_CLASS, bindingsClass).run(() -> GeneratedHackEntrypoint.callPluginManager(fakeExtension, cloudnetEntrypoint));
        } catch (Exception exception) {
            MinecraftServer.LOGGER.error("Failed to initialize cloudnet extension", exception);
        }
    }

    private static final class FakeExtension extends Extension {

        private final ComponentLogger logger;
        private final EventNode<Event> eventNode;
        private final DiscoveredExtension origin;

        public FakeExtension(DiscoveredExtension origin) {
            this.origin = origin;
            this.logger = ComponentLogger.logger(origin.getName());

            this.eventNode = EventNode.all(origin.getName());
            MinecraftServer.getGlobalEventHandler().addChild(this.eventNode);
        }

        @Override
        public void initialize() {
            // no-op
        }

        @Override
        public void terminate() {
            // no-op
        }

        @Override
        public @NotNull DiscoveredExtension getOrigin() {
            return this.origin;
        }

        @Override
        public @NotNull ComponentLogger getLogger() {
            return this.logger;
        }

        @Override
        public @NotNull EventNode<Event> getEventNode() {
            return this.eventNode;
        }

        @Override
        public @Nullable InputStream getPackagedResource(@NotNull String fileName) {
            try {
                final URL url = CloudNetExtensionLoader.class.getResource(fileName);
                if (url == null) {
                    getLogger().debug("Resource not found: {}", fileName);
                    return null;
                }

                return url.openConnection().getInputStream();
            } catch (IOException exception) {
                getLogger().debug("Failed to load resource {}.", fileName, exception);
                return null;
            }
        }
    }
}
