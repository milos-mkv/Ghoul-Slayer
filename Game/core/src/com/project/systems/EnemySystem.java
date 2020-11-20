package com.project.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.project.components.*;
import com.project.managers.EnemyAnimations;
import com.project.managers.EntityFactory;
import com.project.states.GameState;
import com.project.worlds.GameWorld;

import java.util.Random;

public class EnemySystem extends EntitySystem implements EntityListener
{
    private ImmutableArray<Entity> entities;
    private ComponentMapper<CharacterComponent> cm = ComponentMapper.getFor(CharacterComponent.class);

    private Entity player;
    private Quaternion quat = new Quaternion();
    private Engine engine;
    private GameWorld gameWorld;

    private float[] xSpawns = {1, -1, 122, -122};
    private float[] zSpawns = {-122, 122, -1, 1};

    public EnemySystem(GameWorld gameWorld)
    {
        GameState.EnemySpeed = GameState.EnemyStartingSpeed;
        GameState.EnemySpawnNumber = 1;
        this.gameWorld = gameWorld;
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        engine.addEntityListener(Family.all(PlayerComponent.class).get(), this);
        this.engine = engine;
    }

    @Override
    public void update(float delta)
    {
        if(entities.size() < GameState.EnemySpawnNumber)
        {
            spawnEnemy(getRandomSpawnIndex());
        }

        for(Entity e : entities)
        {
            EnemyComponent enemyComponent = e.getComponent(EnemyComponent.class);
            StatusComponent enemyStatusComponent = e.getComponent(StatusComponent.class);
            AnimationComponent enemyAnimationComponent = e.getComponent(AnimationComponent.class);

            if(!enemyStatusComponent.alive)
            {
                enemyComponent.state = EnemyComponent.STATE.DEAD;
            }

            ModelComponent enemyModelComponent = e.getComponent(ModelComponent.class);
            ModelComponent playerModelComponent = player.getComponent(ModelComponent.class);

            Vector3 enemyPosition = new Vector3();
            Vector3 playerPosition = new Vector3();

            playerPosition = playerModelComponent.instance.transform.getTranslation(playerPosition);
            enemyPosition = enemyModelComponent.instance.transform.getTranslation(enemyPosition);

            float dX = playerPosition.x - enemyPosition.x;
            float dZ = playerPosition.z - enemyPosition.z;
            float theta = (float) (Math.atan2(dX, dZ));

            Quaternion rot = quat.setFromAxis(0, 1, 0, (float) Math.toDegrees(theta) + 90);

            Matrix4 ghost = new Matrix4();
            Vector3 translation = new Vector3();

            cm.get(e).ghostObject.getWorldTransform(ghost);
            ghost.getTranslation(translation);

            if(e.getComponent(StatusComponent.class).alive)
            {
                enemyModelComponent.instance.transform.set(translation.x, translation.y, translation.z, rot.x, rot.y, rot.z, rot.w);
            }
            else
            {
                enemyModelComponent.instance.transform.set(translation.x, translation.y, translation.z, rot.x, rot.y, rot.z, rot.w);
            }

            cm.get(e).characterDirection.set(-1, 0, 0).rot(enemyModelComponent.instance.transform);
            cm.get(e).walkDirection.set(0, 0, 0);
            cm.get(e).walkDirection.add(cm.get(e).characterDirection);

            if(e.getComponent(EnemyComponent.class).state == EnemyComponent.STATE.HUNTING)
            {
                GameState.EnemySpeed = GameState.EnemyStartingSpeed + (float) PlayerComponent.KillCounter / 400;
                cm.get(e).walkDirection.scl(GameState.EnemySpeed);
            }
            else if(e.getComponent(EnemyComponent.class).state == EnemyComponent.STATE.DEAD)
            {
                cm.get(e).walkDirection.scl(0);
            }
            else if(e.getComponent(EnemyComponent.class).state == EnemyComponent.STATE.HURT)
            {
                enemyStatusComponent.isAbleToAttack = true;
                cm.get(e).walkDirection.scl(0);
                enemyStatusComponent.hurtStateTime += delta;

                if(e.getComponent(StatusComponent.class).checkHurtTime())
                {
                    enemyComponent.state = EnemyComponent.STATE.HUNTING;
                    enemyStatusComponent.hurtStateTime = 0;
                    if(GameState.EnemySpeed >= GameState.EnemyRunningSpeed)
                    {
                        enemyAnimationComponent.animate(EnemyAnimations.RunAnimation, -1, 0.5f);
                    }
                    else
                    {
                        enemyAnimationComponent.animate(EnemyAnimations.WalkAnimation[(int) (Math.random() * 10) % 2], -1, 0.5f);
                    }
                }
            }
            else if(enemyComponent.state == EnemyComponent.STATE.ATTACKING)
            {
                cm.get(e).walkDirection.scl(0 * delta);
                enemyStatusComponent.attackStateTime += delta;

                if(enemyStatusComponent.checkAttackTime())
                {
                    enemyComponent.state = EnemyComponent.STATE.HUNTING;
                    enemyStatusComponent.attackStateTime = 0;

                    if(GameState.EnemySpeed >= GameState.EnemyRunningSpeed)
                    {
                        enemyAnimationComponent.animate(EnemyAnimations.RunAnimation, -1, 0.5f);
                    }
                    else
                    {
                        enemyAnimationComponent.animate(EnemyAnimations.WalkAnimation[(int) (Math.random() * 10) % 2], -1, 0.5f);
                    }

                    enemyStatusComponent.isAbleToAttack = true;
                }
            }

            cm.get(e).characterController.setWalkDirection(cm.get(e).walkDirection);
        }
    }

    private void spawnEnemy(int randomSpawnIndex)
    {
        engine.addEntity(EntityFactory.createEnemy(gameWorld.getBulletSystem(), xSpawns[randomSpawnIndex], 43, zSpawns[randomSpawnIndex]));
    }

    public int getRandomSpawnIndex()
    {
        return new Random().nextInt(xSpawns.length);
    }

    @Override
    public void entityAdded(Entity entity)
    {
        player = entity;
    }

    @Override
    public void entityRemoved(Entity entity) { }
}
