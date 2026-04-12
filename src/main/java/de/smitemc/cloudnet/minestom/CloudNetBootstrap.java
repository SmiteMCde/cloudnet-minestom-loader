package de.smitemc.cloudnet.minestom;

import de.smitemc.cloudnet.minestom.loader.CloudNetExtensionLoader;
import eu.cloudnetservice.modules.bridge.impl.platform.minestom.GeneratedMinestomCloudNet_BridgeEntrypoint;
import eu.cloudnetservice.modules.bridge.impl.platform.minestom.MinestomBridgeExtension;
import eu.cloudnetservice.modules.signs.impl.platform.minestom.GeneratedMinestomCloudNet_SignsEntrypoint;
import eu.cloudnetservice.modules.signs.impl.platform.minestom.MinestomSignsExtension;

public class CloudNetBootstrap {

    /**
     * Boots the CloudNet-Bridge extension for Minestom. This method should be called in the main method of the Minestom server.
     */
    public static void bootBridge() {
        CloudNetExtensionLoader.loadCloudNetExtension("CloudNet_Bridge", GeneratedMinestomCloudNet_BridgeEntrypoint.class, MinestomBridgeExtension.class);
    }

    /**
     * Boots the CloudNet-Signs extension for Minestom. This method should be called in the main method of the Minestom server.
     */
    public static void bootSigns() {
        CloudNetExtensionLoader.loadCloudNetExtension("CloudNet_Signs", GeneratedMinestomCloudNet_SignsEntrypoint.class,  MinestomSignsExtension.class);
    }
}
