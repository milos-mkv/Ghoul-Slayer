package com.project.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.project.components.BulletComponent;
import com.project.components.CharacterComponent;
import com.project.managers.MyContactListener;

public class BulletSystem extends EntitySystem implements EntityListener
{
    private final btCollisionConfiguration collisionConfiguration;
    private final btCollisionDispatcher dispatcher;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver solver;

    public final btDiscreteDynamicsWorld collisionWorld;

    private btGhostPairCallback ghostPairCallback;
    private MyContactListener myContactListener;
    private int maxSubSteps = 5;
    private float fixedTimeStep = 1f / 60f;

    public BulletSystem()
    {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphase = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        ghostPairCallback = new btGhostPairCallback();
        broadphase.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        collisionWorld.setGravity(new Vector3(0, -10f, 0));
        myContactListener = new MyContactListener();
        myContactListener.enable();
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        engine.addEntityListener(Family.all(BulletComponent.class).get(), this);
    }

    @Override
    public void update(float deltaTime)
    {
        collisionWorld.stepSimulation(deltaTime, maxSubSteps, fixedTimeStep);
    }

    @Override
    public void entityAdded(Entity entity)
    {
        BulletComponent bulletComponent = entity.getComponent(BulletComponent.class);
        if(bulletComponent.body != null)
        {
            collisionWorld.addRigidBody((btRigidBody)bulletComponent.body);
        }
    }

    public void removeBody(Entity entity)
    {
        BulletComponent bulletComponent = entity.getComponent(BulletComponent.class);
        if (bulletComponent != null) {
            collisionWorld.removeCollisionObject(bulletComponent.body);
        }

        CharacterComponent character = entity.getComponent(CharacterComponent.class);
        if(character != null)
        {
          collisionWorld.removeAction(character.characterController);
          collisionWorld.removeCollisionObject(character.ghostObject);
        }
    }

    @Override
    public void entityRemoved(Entity entity) { }

    public void dispose()
    {
        collisionWorld.dispose();
        solver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
        ghostPairCallback.dispose();
    }
}
