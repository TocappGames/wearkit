package dev.wearkit.core.engine;

import dev.wearkit.core.common.Renderable;

import java.util.List;
import java.util.Map;

public interface Scene {
    Map<Integer, List<Renderable>> getDecoration();
    void addOrnament(Renderable renderable);
    void addOrnament(Renderable renderable, int zIndex);
    void removeOrnament(Renderable renderable);
    void removeOrnament(Renderable renderable, int zIndex);

    void clear();
    void clear(int zIndex);
}
