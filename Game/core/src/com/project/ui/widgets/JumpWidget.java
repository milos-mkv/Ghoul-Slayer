package com.project.ui.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.components.PlayerComponent;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;
import com.project.ui.GameUI;

public class JumpWidget extends Actor {
    TextButton tb;

    public JumpWidget() {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        tb.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(Settings.Paused) return;
        tb.draw(batch, parentAlpha);
    }

}
