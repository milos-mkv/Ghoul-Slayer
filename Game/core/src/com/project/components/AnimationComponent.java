package com.project.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimationComponent implements Component
{
    private AnimationController animationController;

    public AnimationComponent(ModelInstance modelInstance)
    {
        animationController = new AnimationController(modelInstance);
        animationController.allowSameAnimation = true;
    }

    public void animate(final String id, final int loops, final float speed)
    {
        animationController.animate(id, loops, speed, null, 0);
    }

    public void update(float delta)
    {
        animationController.update(delta);
    }
}
