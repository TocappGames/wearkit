package dev.wearkit.core.data;

import dev.wearkit.core.exceptions.LoadException;

public interface Loader<T> {
    T load(String key) throws LoadException;
}
