package dev.wearkit.core.data;

import dev.wearkit.core.exceptions.LoadException;

public interface Loader<DST> {
    DST load(String key) throws LoadException;
}
