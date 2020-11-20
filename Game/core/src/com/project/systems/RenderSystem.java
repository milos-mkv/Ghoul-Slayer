package com.project.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.project.managers.Settings;
import com.project.states.GameState;

public class RenderSystem extends EntitySystem {

    private DirectionalShadowLight shadowLight;
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private ImmutableArray<Entity> entities;
    private final ModelBatch modelBatch;
    private ModelBatch shadowBatch;
    private Environment environment, gunEnv;
    private Engine engine;

    private BitmapFont fontForText;

    private final Sprite blood = new Sprite(Assets.Textures.get("blood"));
    public Entity gun;
    public PerspectiveCamera perspectiveCamera, gunCamera;

    private float bloodTime = 0;
    private boolean renderFrameRate = false;
    public RenderSystem() {
        GameState.RenderBlood = false;
        GameState.RenderFrameRate = false;
        initializeFonts();
        initializePerspectiveCamera();
        initializeGunCamera();
        initializeEnvironment();
        modelBatch = new ModelBatch();
    }

    private void initializeFonts() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        fontForText = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
    }

    private void initializePerspectiveCamera() {
        perspectiveCamera = new PerspectiveCamera(GameState.FOV, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        perspectiveCamera.far = 1000f;
    }

    private void initializeGunCamera() {
        gunCamera = new PerspectiveCamera(GameState.FOV, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        gunCamera.far = 100f;
        gunEnv = new Environment();
        gunEnv.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f));
        gunEnv.add(new DirectionalLight().set(0.3f, 0.3f, 0.3f, 1f, -0.8f, -0.2f));
    }

    private void initializeEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -1f, -0.8f, -0.2f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0, 0, 0, 1f));
        shadowLight = new DirectionalShadowLight(1024 * 5, 1024 * 5, 200f, 200f, 1f, 300f);
        shadowLight.set(0.5f, 0.5f, 0.5f, 0, -0.1f, 0.1f);
        environment.add(shadowLight);
        environment.shadowMap = shadowLight;
        shadowBatch = new ModelBatch(new DepthShaderProvider());
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        spriteBatch.begin();
        spriteBatch.draw(Assets.Textures.get("sky"), 0, 0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        spriteBatch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            renderFrameRate = ! renderFrameRate;
        }

        drawShadows(deltaTime);
        drawModels(deltaTime);

        if(renderFrameRate) {
            Assets.FrameRate.update();
            Assets.FrameRate.render();
        }

        if(GameState.StartDeadCounter) {
            spriteBatch.begin();
            fontForText.draw(spriteBatch, "Score  :  " + PlayerComponent.Score, Core.SCREEN_WIDTH / 2 - 150 , Core.SCREEN_HEIGHT / 2 + 10 );
            spriteBatch.end();
        }
    }

    private void drawModels(float delta) {
        modelBatch.begin(perspectiveCamera);
        for(int i=0;i<entities.size();i++) {
            if (entities.get(i).getComponent(GunComponent.class) == null) {
                ModelComponent modelComponent = entities.get(i).getComponent(ModelComponent.class);
                modelBatch.render(modelComponent.instance, environment);
                if(entities.get(i).getComponent(AnimationComponent.class) != null && !Settings.Paused) {
                    entities.get(i).getComponent(AnimationComponent.class).update(delta);
                }
            }
        }
        modelBatch.end();
        GameState.Elapsed += delta;

        drawGun(delta);

        if(PlayerComponent.IsDead) {
            Gdx.gl.glClearColor(0, 0, 0, 0.2f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        if(GameState.RenderBlood || PlayerComponent.IsDead) {
            if(!Settings.Paused) {
                bloodTime += delta;
            }

            spriteBatch.begin();
            spriteBatch.draw(blood,0 ,0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
            spriteBatch.end();

            if(bloodTime > 1) {
                bloodTime = 0;
                GameState.RenderBlood = false;
            }
        }
    }

    private void drawShadows(float delta) {
        shadowLight.begin(Vector3.Zero, perspectiveCamera.direction);
        shadowBatch.begin(shadowLight.getCamera());

        for(int x = 0; x < entities.size(); x++) {
            if(entities.get(x).getComponent(EnemyComponent.class) != null) {
                ModelComponent mod = entities.get(x).getComponent(ModelComponent.class);
                shadowBatch.render(mod.instance);
            }
            if(entities.get(x).getComponent(AnimationComponent.class) != null && !Settings.Paused) {
                entities.get(x).getComponent(AnimationComponent.class).update(delta);
            }
        }
        shadowBatch.end();
        shadowLight.end();
    }

    private void drawGun(float delta) {
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(gunCamera);
        modelBatch.render(gun.getComponent(ModelComponent.class).instance, gunEnv);
        gun.getComponent(AnimationComponent.class).update(delta);
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
        spriteBatch.dispose();
    }
}
