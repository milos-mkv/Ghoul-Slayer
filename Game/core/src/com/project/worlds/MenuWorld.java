package com.project.worlds;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.project.components.AnimationComponent;
import com.project.components.ModelComponent;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.EnemyAnimations;
import com.project.systems.MenuSystem;

public class MenuWorld {

    private final Engine engine;

    public MenuWorld(Core game) {
        engine = new Engine();
        engine.addSystem(new MenuSystem(game));
        initializeModels();
    }

    public void render(float delta) {
        engine.update(delta);
    }

    private void initializeModels() {
        engine.addEntity(new Entity().add(new ModelComponent(Assets.Models.get("ground"), 0, 0, 0)));
        engine.addEntity(new Entity().add(new ModelComponent(Assets.Models.get("wall"), 0, 0, 0)));
        engine.addEntity(new Entity().add(new ModelComponent(Assets.Models.get("skull"), 0 ,0, 0)));

        float[] rotations = { 1.0f, -30.0f, 10.0f };
        Vector3[] positions = { new Vector3(10.0f, 3.6f, 2.0f ),
                                new Vector3(15.0f, 3.6f, -9.0f),
                                new Vector3(25.0f, 3.6f, 16.0f), };

        for(int i = 0; i < 3; i++) {
            Entity ghoul = new Entity();
            ModelComponent modelComponent = new ModelComponent(Assets.Models.get("ghoul"), positions[i].x, positions[i].y, positions[i].z);
            modelComponent.instance.transform.rotate(Vector3.Y, rotations[i]);
            ghoul.add(modelComponent);
            AnimationComponent animationComponent = new AnimationComponent(modelComponent.instance);
            animationComponent.animate(EnemyAnimations.IdleAnimation, -1, 0.5f);
            ghoul.add(animationComponent);
            engine.addEntity(ghoul);
        }
    }

    public void dispose() { }
}
