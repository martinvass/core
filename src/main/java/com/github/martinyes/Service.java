package com.github.martinyes;

import com.github.martinyes.wrapper.WrappedResult;

import java.util.function.Predicate;

public interface Service<T> {

    long load();

    T create(T t);

    WrappedResult<T> find(Predicate<T> predicate);

    int totalBy(Predicate<T> predicate);

    int total();
}