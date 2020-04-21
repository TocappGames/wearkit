package dev.wearkit.core.common;

public interface Scalable<T extends Scalable> {
    T scale(double rate);
}
