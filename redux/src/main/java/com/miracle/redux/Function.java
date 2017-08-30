package com.miracle.redux;

public interface Function<T, R> {
    R apply(T value);
}
