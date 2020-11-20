package com.project.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component
{
    public enum STATE
    {
        IDLE, HURT, HUNTING, DEAD, ATTACKING, WALK
    }

    public int health;
    public STATE state;

    public EnemyComponent(STATE state)
    {
        this.state = state;
        health = 30;
    }
}
