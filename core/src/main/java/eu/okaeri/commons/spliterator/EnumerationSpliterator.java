package eu.okaeri.commons.spliterator;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class EnumerationSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Enumeration<T> enumeration;

    public EnumerationSpliterator(Enumeration<T> enumeration) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        this.enumeration = enumeration;
    }

    @SuppressWarnings("MethodParameterNamingConvention")
    public EnumerationSpliterator(long est, int additionalCharacteristics, Enumeration<T> enumeration) {
        super(est, additionalCharacteristics);
        this.enumeration = enumeration;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (this.enumeration.hasMoreElements()) {
            action.accept(this.enumeration.nextElement());
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (this.enumeration.hasMoreElements()) {
            action.accept(this.enumeration.nextElement());
        }
    }
}