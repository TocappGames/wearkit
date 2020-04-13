package dev.wearkit.sample;

import java.util.Map;

import dev.wearkit.core.activity.WearGameActivity;
import dev.wearkit.core.data.Loader;
import dev.wearkit.core.data.PhysicsEditorLoader;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.example.ComplexBodies;

public class MainActivity extends WearGameActivity {

    @Override
    protected Game getGame() {
        Loader<Map<String, Body>> loader = new PhysicsEditorLoader(this);
        return new ComplexBodies(loader);
    }
}
