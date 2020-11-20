package com.project.ui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;
import com.project.screens.GameScreen;
import com.project.screens.MenuScreen;
import com.project.ui.GameUI;

public class PauseWidget extends Actor {
    private Core game;
    public Window window;

    private TextButton closeDialog, restartButton, quitButton, menuButton;
    private Stage stage;
    private Table table;
    private Slider slider;
    private Label label, text;

    public PauseWidget(Core game, Stage stage) {
        this.game = game;
        this.stage = stage;
        setWidgets();
        configureWidgets();
        setListeners();
    }

    private void setWidgets()
    {
        table = new Table();
        slider = new Slider(0.01f, 0.5f, 0.01f, false, Assets.Skins.get("neon"));
        window = new Window("Pause", Assets.Skins.get("neon"));
        slider.setValue(Settings.MouseSensitivity);
        closeDialog = new TextButton("x", Assets.Skins.get("neon"));
        closeDialog.setSize(10, 10);
        restartButton = new TextButton("Restart", Assets.Skins.get("neon"));
        quitButton = new TextButton("Quit", Assets.Skins.get("neon"));
        menuButton = new TextButton("Menu", Assets.Skins.get("neon"));

        label = new Label("", Assets.Skins.get("neon"));
        text = new Label("Mouse sensitivity", Assets.Skins.get("neon"));
    }

    private void configureWidgets() {
        window.getTitleTable().add(closeDialog).height(window.getPadTop());

        table.add(restartButton).width(400).height(100);
        table.row();
        table.add(label);
        table.row();
        table.add(menuButton).width(400).height(100);
        table.row();
        table.add(label);
        table.row();
        table.add(quitButton).width(400).height(100);
        table.row();
        table.add(label);
        table.row();
        table.add(text);
        table.row();
        table.add(slider).width(400).height(100);

        window.add(table);
    }

    private void setListeners() {
        super.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    handleUpdates();
                    return true;
                }
                return false;
            }
        });

        closeDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleUpdates();
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

       slider.addListener(new DragListener() {
           @Override
           public void drag(InputEvent event, float x, float y, int pointer) {
               Settings.MouseSensitivity = slider.getValue();
           }
       });

       slider.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Settings.MouseSensitivity = slider.getValue();
           }
       });
    }

    public void handleUpdates() {
        if(window.getStage() == null) {
            stage.addActor(window);
            Gdx.input.setCursorCatched(false);
            Settings.Paused = true;

            GameUI.jumpButton.setVisible(false);
            GameUI.shootButton.setVisible(false);
            GameUI.menuButton.setVisible(false);
            GameUI.reloadButton.setVisible(false);


        }
        else {
            window.remove();
            Gdx.input.setCursorCatched(true);
            Settings.Paused = false;
            GameUI.jumpButton.setVisible(true);
            GameUI.shootButton.setVisible(true);
            GameUI.menuButton.setVisible(true);
            GameUI.reloadButton.setVisible(true);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        window.setPosition((float) Core.SCREEN_WIDTH / 2 - window.getWidth() / 2, (float) Core.SCREEN_HEIGHT / 2 - window.getHeight() / 2);
    }

    @Override
    public void setSize(float width, float height)
    {
        super.setSize(width, height);
        window.setSize(width * 2, height * 2);
    }

}
