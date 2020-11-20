package com.project.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.project.components.AmmoComponent;
import com.project.components.AnimationComponent;
import com.project.components.HealComponent;
import com.project.components.StatusComponent;
import com.project.worlds.GameWorld;

public class StatusSystem extends EntitySystem
{
    private ImmutableArray<Entity> entities;
    private GameWorld gameWorld;

    public StatusSystem(GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        entities = engine.getEntitiesFor(Family.all(StatusComponent.class).get());
    }

    @Override
    public void update(float deltaTime)
    {
        for(int i=0;i<entities.size();i++)
        {
            Entity entity = entities.get(i);
            entity.getComponent(StatusComponent.class).update(deltaTime);

            if((entity.getComponent(AmmoComponent.class) != null || entity.getComponent(HealComponent.class) != null ) && !entity.getComponent(StatusComponent.class).alive)
            {
                gameWorld.remove(entity);
                continue;
            }
            if(entity.getComponent(StatusComponent.class).aliveStateTime >= 1f)
            {
                gameWorld.remove(entity);
            }
        }
    }
}
