package eu.okaeri.commonstest;

import eu.okaeri.commons.classpath.ClasspathResourceType;
import eu.okaeri.commons.classpath.ClasspathScanner;
import org.junit.jupiter.api.Test;

public class TestClasspathScanner {

    @Test
    public void test_finds_itself() {
        ClasspathScanner.of(Thread.currentThread().getContextClassLoader())
            .findResources("eu.okaeri.commonstest")
            .filter(resource -> resource.getName().equals(this.getClass().getSimpleName()))
            .filter(resource -> resource.getQualifiedName().equals(this.getClass().getName()))
            .filter(resource -> resource.getType() == ClasspathResourceType.CLASS)
            .findAny()
            .orElseThrow(RuntimeException::new);
    }
}
