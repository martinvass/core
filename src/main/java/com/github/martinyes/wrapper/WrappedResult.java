package com.github.martinyes.wrapper;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WrappedResult<T> {

    private final Collection<T> data;
    private final Predicate<T> predicate;

    public WrappedResult(Collection<T> data) {
        this.data = data;
        this.predicate = null;
    }

    public WrappedResult(Collection<T> data, Predicate<T> predicate) {
        this.data = data;
        this.predicate = predicate;
    }

    public T get() {
        if (predicate != null)
            return this.data.stream().filter(predicate).findFirst().orElse(null);

        return this.data.stream().findFirst().orElse(null);
    }

    public Collection<T> getData() {
        if (predicate != null)
            return data.stream().filter(predicate).collect(Collectors.toList());

        return data;
    }
}