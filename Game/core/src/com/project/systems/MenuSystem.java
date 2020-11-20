package com.project.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.project.components.*;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.EnemyAnimations;
import com.project.screens.GameScreen;
import com.project.states.GameState;

import java.util.HashMap;

public class MenuSystem extends EntitySystem {

    private final Core game;
    private final SpriteBatch spriteBatch;
    private final ModelBatch modelBatch;
    private final HashMap<String, Sprite> sprites;

    private ImmutableArray<Entity> entities;

    private PerspectiveCamera perspectiveCamera;
    private Environment environment;
    private DirectionalShadowLight shadowLight;
    private ModelBatch shadowBatch;

    private int counter = 0;

    public MenuSystem(Core game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        sprites = new HashMap<String, Sprite>();
        sprites.put("title", new Sprite(Assets.Textures.get("gameTitle")));

        initializePerspectveCamera();
        initializeEnvironment();
        modelBatch = new ModelBatch();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        GameState.Elapsed += deltaTime;
        drawShadows(deltaTime);
        modelBatch.begin(perspectiveCamera);
        if(GameState.StartingGame) {
            counter += 1;
            if((perspectiveCamera.far -= 1.0f) <= -1f) {
                game.setScreen(new GameScreen(game));
            }
        }

        for(Entity e : entities) {
            modelBatch.render(e.getComponent(ModelComponent.class).instance, environment);
            if(e.getComponent(AnimationComponent.class) != null) {
                e.getComponent(AnimationComponent.class).update(deltaTime);
            }
        }

        if(counter == 40) {
            entities.get(5).getComponent(AnimationComponent.class).animate(EnemyAnimations.DeadAnimation[1], 1, 0.5f);
        }
        if(counter == 80) {
            entities.get(4).getComponent(AnimationComponent.class).animate(EnemyAnimations.DeadAnimation[0], 1, 0.5f);
        }
        if(counter == 120) {
            entities.get(3).getComponent(AnimationComponent.class).animate(EnemyAnimations.DeadAnimation[2], 1, 0.5f);
        }
        modelBatch.end();

        spriteBatch.begin();
        if(GameState.RenderMenuTitle) {
            spriteBatch.draw(sprites.get("title"), (float) Core.SCREEN_WIDTH / 4 , (float) Core.SCREEN_HEIGHT / 3 ,
                    (float) Core.SCREEN_WIDTH / 2 , (float) Core.SCREEN_HEIGHT / 3);
        }
        spriteBatch.end();
    }

    private void initializePerspectveCamera() {
        perspectiveCamera = new PerspectiveCamera(GameState.FOV, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        perspectiveCamera.position.set(0, 2, 0);
        perspectiveCamera.lookAt(1, 2 ,0);
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();
    }

    private void initializeEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add((this.shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 100f)).set(1f, 1f, 0.2f, 0.0f, -35f, -35f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0, 0, 0, 1f));
        environment.shadowMap = this.shadowLight;
        environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -1f, -0.8f, -0.2f));
        shadowBatch = new ModelBatch(new DepthShaderProvider());
    }

    private void drawShadows(float delta) {
        shadowLight.begin(Vector3.Zero, perspectiveCamera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        for(int x = 0; x < entities.size(); x++) {
            ModelComponent mod = entities.get(x).getComponent(ModelComponent.class);
            shadowBatch.render(mod.instance);
        }
        shadowBatch.end();
        shadowLight.end();
    }
}
