package dev.wearkit.core.common;

public interface Factory<SRC, DST> {
    DST build(SRC src);
}
