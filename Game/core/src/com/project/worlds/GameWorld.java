package com.project.worlds;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.project.components.CharacterComponent;
import com.project.game.Assets;
import com.project.managers.EntityFactory;
import com.project.managers.Settings;
import com.project.states.GameState;
import com.project.systems.*;
import com.project.ui.GameUI;

public class GameWorld
{
    private DebugDrawer debugDrawer;

    private Engine engine;
    private BulletSystem bulletSystem;
    private RenderSystem renderSystem;
    public PlayerSystem playerSystem;

    private Entity character, gun;

    public GameWorld(GameUI gameUI)
    {
        setDebug();
        initializeEngine(gameUI);
        addEntities();
        GameState.StartDeadCounter = false;
        GameState.DeadPlayerCounterScreen = 0;

        if(Settings.MusicEnabled)
        {
            Assets.Music.get("menu").stop();
            Assets.Music.get("game").setLooping(true);
            Assets.Music.get("game").play();
        }
    }


    private void setDebug()
    {
        if(GameState.DebugMode)
        {
            debugDrawer = new DebugDrawer();
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        }
    }

    public BulletSystem getBulletSystem()
    {
        return bulletSystem;
    }

    private void addEntities()
    {
        loadLevel();
        engine.addEntity(character = EntityFactory.createPlayer(bulletSystem, 0, 6, 0));
        engine.addEntity(gun = EntityFactory.loadGun(4 ,4, -15));
        playerSystem.gun = gun;
        renderSystem.gun = gun;
    }

    public void render(float delta)
    {
        renderWorld(delta);
        checkPause();
        if(GameState.StartDeadCounter)
        {
            GameState.DeadPlayerCounterScreen += delta;
        }
    }

    protected void renderWorld(float delta)
    {
        engine.update(delta);

        if(GameState.DebugMode)
        {
            debugDrawer.begin(renderSystem.perspectiveCamera);
            bulletSystem.collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }
    }

    private void loadLevel()
    {
        engine.addEntity(EntityFactory.loadScene(0, 0, 0));
        engine.addEntity(EntityFactory.loadWall(0, 0, 0));
        engine.addEntity(EntityFactory.loadSkulls(0, 0, 0));
    }

    private void checkPause()
    {
        if(Settings.Paused)
        {
            engine.getSystem(PlayerSystem.class).setProcessing(false);
            engine.getSystem(EnemySystem.class).setProcessing(false);
            engine.getSystem(StatusSystem.class).setProcessing(false);
            engine.getSystem(BulletSystem.class).setProcessing(false);
            engine.getSystem(SupplySystem.class).setProcessing(false);
        }
        else
        {
            engine.getSystem(PlayerSystem.class).setProcessing(true);
            engine.getSystem(EnemySystem.class).setProcessing(true);
            engine.getSystem(StatusSystem.class).setProcessing(true);
            engine.getSystem(BulletSystem.class).setProcessing(true);
            engine.getSystem(SupplySystem.class).setProcessing(true);
        }
    }

    private void initializeEngine(GameUI gameUI)
    {
        engine = new Engine();
        engine.addSystem(renderSystem = new RenderSystem());
        engine.addSystem(bulletSystem = new BulletSystem());
        engine.addSystem(playerSystem = new PlayerSystem(renderSystem.perspectiveCamera,this, gameUI));
        engine.addSystem(new EnemySystem(this));
        engine.addSystem(new StatusSystem(this));
        engine.addSystem(new SupplySystem());

        if(GameState.DebugMode)
        {
            bulletSystem.collisionWorld.setDebugDrawer(debugDrawer);
        }
    }

    public void dispose()
    {
        bulletSystem.collisionWorld.removeAction(character.getComponent(CharacterComponent.class).characterController);
        bulletSystem.collisionWorld.removeCollisionObject(character.getComponent(CharacterComponent.class).ghostObject);
        character.getComponent(CharacterComponent.class).characterController.dispose();
        character.getComponent(CharacterComponent.class).ghostShape.dispose();
        bulletSystem.dispose();
        renderSystem.dispose();
    }

    public void remove(Entity entity)
    {
        engine.removeEntity(entity);
        bulletSystem.removeBody(entity);
    }

}
