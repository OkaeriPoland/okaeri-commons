package eu.okaeri.commons.classpath;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.stream.Stream;

@Data
@ToString(exclude = "scanner")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ClasspathResource {

    private final ClasspathScanner scanner;
    private final String name;
    private final String qualifiedName;
    private final ClasspathResourceType type;

    public Stream<ClasspathResource> stream() {
        return (this.type == ClasspathResourceType.PACKAGE)
                ? Stream.concat(Stream.of(this), this.children())
                : Stream.of(this);
    }

    public Stream<ClasspathResource> children() {
        return this.scanner.findResources(this.qualifiedName)
                .flatMap(ClasspathResource::stream);
    }
}
