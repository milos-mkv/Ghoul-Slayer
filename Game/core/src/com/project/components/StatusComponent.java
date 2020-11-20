package com.project.components;

import com.badlogic.ashley.core.Component;
import com.project.managers.EnemyAnimations;

public class StatusComponent implements Component
{
    public boolean alive, running;
    public float hurtStateTime;
    private AnimationComponent animationComponent;
    public float aliveStateTime;
    public float attackStateTime;
    public boolean isAbleToAttack = true;

    public boolean checkHurtTime()
    {
        return hurtStateTime > 1;
    }

    public boolean checkAttackTime()
    {
        return attackStateTime > 1;
    }

    public StatusComponent(AnimationComponent animationComponent)
    {
        this.animationComponent = animationComponent;
        alive = true;
        running = true;
    }

    public void update(float delta)
    {
        if(!alive) aliveStateTime += delta;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
        playDeathAnimation();
    }

    private void playDeathAnimation()
    {
        animationComponent.animate(EnemyAnimations.DeadAnimation[ (int) (Math.random() * 10) % 3 ], 1, 0.5f);
    }

}
