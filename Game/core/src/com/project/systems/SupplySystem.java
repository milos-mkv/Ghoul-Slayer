package com.project.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.project.managers.EntityFactory;
import com.project.states.GameState;

public class SupplySystem extends EntitySystem
{
    private Engine engine;


    public SupplySystem() { }

    @Override
    public void addedToEngine(Engine engine)
    {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime)
    {
        GameState.FrameCounter += 1;
        if(GameState.FrameCounter > 10000000)
        {
            GameState.FrameCounter = 0;
        }
        if(GameState.FrameCounter % ( 25 * 120) == 0)
        {
            engine.addEntity( (int) (Math.random() * 10) % 3 == 0 ? EntityFactory.createHeal( (int) (Math.random() * 10) % 2 == 0 ? -1 * (int) (Math.random() * 70) : (int) (Math.random() * 70), 20, (int) (Math.random() * 10) % 2 == 0 ? -1 * (int) (Math.random() * 70) : (int) (Math.random() * 70)) : EntityFactory.createAmmo((int) (Math.random() * 10) % 2 == 0 ? -1 * (int) (Math.random() * 70) : (int) (Math.random() * 70), 20, (int) (Math.random() * 10) % 2 == 0 ? -1 * (int) (Math.random() * 70) : (int) (Math.random() * 70)));
        }
    }
}
