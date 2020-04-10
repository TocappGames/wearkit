package dev.wearkit.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.wearkit.core.activity.MobileGameActivity;
import dev.wearkit.core.common.Factory;
import dev.wearkit.core.data.Loader;
import dev.wearkit.core.data.PhysicsEditorBodyLoader;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.example.ComplexBodies;
import dev.wearkit.example.FloatingBalls;

public class ExamplesActivity extends MobileGameActivity {

    public static final List<String> EXAMPLE_NAMES = new ArrayList<>();
    public static final List<Factory<Context, Game>> EXAMPLES = new ArrayList<>();


    static {
        EXAMPLE_NAMES.add("FloatingBalls");
        EXAMPLES.add((Context ctx) -> new FloatingBalls());


        EXAMPLE_NAMES.add("ComplexBodies");
        EXAMPLES.add((Context ctx) -> {
            Loader<Body> loader = new PhysicsEditorBodyLoader(ctx);
            return new ComplexBodies(loader);
        });
    }

    private String example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        this.example = intent.getStringExtra(MenuActivity.EXTRA_EXAMPLE_NAME);

        super.onCreate(savedInstanceState);
    }

    protected Game getGame(){
        return Objects.requireNonNull(EXAMPLES.get(EXAMPLE_NAMES.indexOf(this.example)))
                .build(this);
    }

}
