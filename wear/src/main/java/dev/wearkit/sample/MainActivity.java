package dev.wearkit.sample;

import java.util.Map;

import dev.wearkit.core.activity.WearGameActivity;
import dev.wearkit.core.data.Loader;
import dev.wearkit.core.data.PhysicsEditorLoader;
import dev.wearkit.core.data.UnityAssetLoader;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.example.CarDriving;
import dev.wearkit.example.ComplexBodies;

public class MainActivity extends WearGameActivity {

    @Override
    protected Game getGame() {
        Loader<Body> loader = new UnityAssetLoader(this);
        return new CarDriving(loader);
    }
}
