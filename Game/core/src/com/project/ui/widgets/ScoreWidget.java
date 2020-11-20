package com.project.ui.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.project.components.PlayerComponent;
import com.project.game.Assets;
import com.project.managers.Settings;

public class ScoreWidget extends Actor
{
    Label label;

    public ScoreWidget()
    {
        label = new Label("", Assets.Skins.get("neon"));
    }

    @Override
    public void act(float delta)
    {
        label.act(delta);
        label.setText("Score: " + PlayerComponent.Score);
        label.scaleBy(2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(Settings.Paused) return;
        label.draw(batch, parentAlpha);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        label.setPosition(x, y);
    }

    @Override
    public void setSize(float width, float height)
    {
        super.setSize(width, height);
        label.setSize(width, height);
    }
}
