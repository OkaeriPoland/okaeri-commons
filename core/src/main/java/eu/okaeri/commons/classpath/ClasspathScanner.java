package eu.okaeri.commons.classpath;

import eu.okaeri.commons.cache.CacheMap;
import eu.okaeri.commons.spliterator.EnumerationSpliterator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;

@RequiredArgsConstructor(staticName = "of")
public class ClasspathScanner {

    private static final boolean TRACE = Boolean.parseBoolean(System.getProperty("okaeri.platform.trace", "false"));
    private static final Logger LOGGER = Logger.getLogger(ClasspathScanner.class.getSimpleName());

    private final Map<Path, List<String>> jarCache = new CacheMap<>(32);
    private final ClassLoader classLoader;

    private static String qualifiedName(String packageName, String name) {
        return packageName + "." + name;
    }

    public Optional<ClasspathResource> findPackage(@NonNull String packageName) {

        int lastDot = packageName.lastIndexOf('.');
        if (lastDot == -1) {
            return Optional.empty();
        }

        return this.findResources(packageName.substring(0, lastDot))
            .filter(resource -> resource.getType() == ClasspathResourceType.PACKAGE)
            .filter(resource -> resource.getQualifiedName().equals(packageName))
            .findAny();
    }

    public Stream<ClasspathResource> findResources(@NonNull String packageName, boolean deep) {
        return deep
            ? this.findResources(packageName).flatMap(ClasspathResource::stream)
            : this.findResources(packageName);
    }

    @SneakyThrows
    public Stream<ClasspathResource> findResources(@NonNull String packageName) {

        String packagePath = packageName.replace(".", "/");
        URL packageURL;
        try {
            packageURL = this.classLoader.getResource(packagePath);
        } catch (Exception exception) {
            throw new ClasspathScannerException("Failed ClassLoader#getResources(String) with '" + packagePath + "'", exception);
        }

        if (packageURL == null) {
            return Stream.of();
        }

        String protocol = packageURL.getProtocol();
        if (TRACE) {
            LOGGER.info("Scanning '" + packagePath + "' '" + packageURL + "' (protocol: " + protocol + ")");
        }

        if ("jar".equals(protocol)) {

            // FIXME: possible less obscure way? https://stackoverflow.com/a/48298758
            String fileName = packageURL.getFile();
            String pathString = fileName.substring(0, fileName.indexOf("!"));
            Path jarPath;

            try {
                jarPath = Paths.get(new URI(pathString));
            } catch (Exception exception) {
                throw new ClasspathScannerException("Failed to jar path from '" + pathString + "'", exception);
            }

            List<String> jarEntries = this.jarCache.computeIfAbsent(jarPath, path -> {
                try {
                    JarFile jarFile = new JarFile(jarPath.toFile());
                    Enumeration<JarEntry> entries = jarFile.entries();

                    return StreamSupport.stream(new EnumerationSpliterator<>(entries), false)
                        .map(ZipEntry::getName)
                        .collect(Collectors.toList());
                } catch (IOException exception) {
                    throw new RuntimeException("Failed to read jar", exception);
                }
            });

            if (TRACE) {
                LOGGER.info("Resolved jar path to '" + jarPath + "' and found " + jarEntries.size() + " entries");
            }

            return jarEntries.stream()
                .filter(name -> name.startsWith(packagePath + "/"))
                .map(name -> name.substring(packagePath.length() + 1))
                .map(name -> name.split("/")[0])
                .distinct()
                .map(name -> this.resourceFromName(packageName, name));
        }

        if ("file".equals(protocol)) {
            return Files.list(Paths.get(packageURL.toURI()))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(name -> this.resourceFromName(packageName, name));
        }

        throw new IllegalArgumentException("Unknown protocol (" + protocol + "): " + packageURL);
    }

    private ClasspathResource resourceFromName(String packageName, String name) {

        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return new ClasspathResource(this, name, qualifiedName(packageName, name), ClasspathResourceType.PACKAGE);
        }

        if (name.endsWith(".class")) {
            String className = name.substring(0, lastDot);
            return new ClasspathResource(this, className, qualifiedName(packageName, className), ClasspathResourceType.CLASS);
        }

        return new ClasspathResource(this, name, qualifiedName(packageName, name), ClasspathResourceType.UNKNOWN);
    }
}
