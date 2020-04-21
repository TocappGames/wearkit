package dev.wearkit.core.data;

public interface Pool<T> {
    T get(T object);
    boolean has(T object);
    T store(T object);
}
