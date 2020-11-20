package com.project.ui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project.components.PlayerComponent;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.screens.GameScreen;
import com.project.screens.MenuScreen;
import com.project.screens.SubmitScoreScreen;

public class GameOverWidget extends Actor
{
    private final Core game;
    private final Stage stage;
    private Image image;
    private TextButton menuB;

    public GameOverWidget(Core game, Stage stage) {
        this.game = game;
        this.stage = stage;
        setWidgets();
        setListeners();
    }

    private void setWidgets()
    {
        image = new Image(Assets.Textures.get("gameOver"));
        image.setSize(Core.SCREEN_WIDTH / 2, Core.SCREEN_HEIGHT / 2);
        menuB = new TextButton("Menu", Assets.Skins.get("neon"));
    }

    private void setListeners() {

        menuB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(0, 0);
        image.setPosition(Core.SCREEN_WIDTH / 4, Core.SCREEN_HEIGHT / 4);
        menuB.setPosition(Core.SCREEN_WIDTH / 2 - menuB.getWidth() / 2, 100);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        menuB.setSize(600, 150);
    }

    public void gameOver() {
        stage.addActor(image);
    }

    public void gameOverButtons() {
        stage.addActor(menuB);
        stage.unfocus(stage.getKeyboardFocus());
        Gdx.input.setCursorCatched(false);
    }
}
