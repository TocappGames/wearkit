package dev.wearkit.sample;

import dev.wearkit.core.activity.MobileGameActivity;
import dev.wearkit.core.data.Loader;
import dev.wearkit.core.data.PhysicsEditorBodyLoader;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.example.ComplexBodies;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        Loader<Body> loader = new PhysicsEditorBodyLoader(this);
        return new ComplexBodies(loader);
    }

}
