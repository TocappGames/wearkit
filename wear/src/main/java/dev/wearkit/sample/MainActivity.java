package dev.wearkit.sample;

import dev.wearkit.core.activity.WearGameActivity;
import dev.wearkit.core.data.AssetOrnamentLoader;
import dev.wearkit.core.data.Loader;
import dev.wearkit.core.data.UnityAssetBodyLoader;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.example.CarDriving;

public class MainActivity extends WearGameActivity {

    @Override
    protected Game getGame() {
        Loader<Body> bodies = new UnityAssetBodyLoader(this);
        Loader<Ornament> ornaments = new AssetOrnamentLoader(this);
        return new CarDriving(bodies, ornaments);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
