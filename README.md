# cloudnet-minestom-loader

Small helper library to boot CloudNet modules inside a Minestom server.

## What it does

This project provides a simple bootstrap API for CloudNet modules in Minestom:

- CloudNet Bridge (`CloudNetBootstrap.bootBridge()`)
- CloudNet Signs (`CloudNetBootstrap.bootSigns()`)

## Requirements

- Java 25
- Maven 3.9+

## Build

```bash
mvn clean package
```

After building, the JAR files are in `target/`.

## Quick usage

Call the bootstrap methods in your Minestom startup code.

```java
import de.smitemc.cloudnet.minestom.CloudNetBootstrap;

public final class Main {
    static void main(String[] args) {
        // Start your Minestom server setup first

        CloudNetBootstrap.bootBridge();
        CloudNetBootstrap.bootSigns();

        // Continue with your server startup
    }
}
```

## Project structure

- `src/main/java/de/smitemc/cloudnet/minestom/CloudNetBootstrap.java` - public bootstrap API
- `src/main/java/de/smitemc/cloudnet/minestom/loader/CloudNetExtensionLoader.java` - extension loading internals
