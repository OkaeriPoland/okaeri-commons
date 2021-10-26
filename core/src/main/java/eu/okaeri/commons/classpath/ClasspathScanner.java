package eu.okaeri.commons.classpath;

import eu.okaeri.commons.spliterator.EnumerationSpliterator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;

@RequiredArgsConstructor(staticName = "of")
public class ClasspathScanner {

    private final ClassLoader classLoader;

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
        URL packageURL = this.classLoader.getResource(packagePath);

        if (packageURL == null) {
            return Stream.of();
        }

        if ("jar".equals(packageURL.getProtocol())) {

            List<ClasspathResource> resources = new ArrayList<>();
            String fileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            URI jarUri = new URI(fileName.substring(0, fileName.indexOf("!"))); // file:/mnt/c/.../app.jar!/my/package
            Enumeration<JarEntry> entries = new JarFile(Paths.get(jarUri).toFile()).entries();

            return StreamSupport.stream(new EnumerationSpliterator<>(entries), false)
                    .map(ZipEntry::getName)
                    .filter(name -> name.startsWith(packagePath + "/"))
                    .map(name -> name.substring(packagePath.length() + 1))
                    .map(name -> name.split("/")[0])
                    .distinct()
                    .map(name -> this.resourceFromName(packageName, name));
        }

        if ("file".equals(packageURL.getProtocol())) {
            return Files.list(Paths.get(packageURL.toURI()))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> this.resourceFromName(packageName, name));
        }

        throw new IllegalArgumentException("Unknown protocol: " + packageURL.getProtocol());
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

    private static String qualifiedName(String packageName, String name) {
        return packageName + "." + name;
    }
}
