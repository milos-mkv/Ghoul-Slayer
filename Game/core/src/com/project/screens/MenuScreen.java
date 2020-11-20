package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;
import com.project.states.GameState;
import com.project.worlds.MenuWorld;

import java.util.HashMap;

public class MenuScreen implements Screen {

    private final Core game;
    private Stage stage;
    private final HashMap<String, TextButton> buttons = new HashMap<>();
    private MenuWorld menuWorld;

    public MenuScreen(Core game) {
        if(Settings.MusicEnabled) {
            Assets.Music.get("menu").setLooping(true);
            Assets.Music.get("menu").play();
        }
        this.game = game;
        initializeGameState();
        initializeMenuWorldAndStage();
        setWidgets();
        configureWidgets();
        setListeners();
    }

    private void initializeGameState() {
        Assets.Music.get("game").stop();
        GameState.RenderMenuTitle = true;
        GameState.StartingGame = false;
    }

    private void initializeMenuWorldAndStage() {
        menuWorld = new MenuWorld(game);
        stage = new Stage(new FitViewport(Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT));
    }

    private void setWidgets() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        new TextButton("Test", Assets.Skins.get("neon")).getStyle().font = fontGenerator.generateFont(parameter);
        new CheckBox("Test", Assets.Skins.get("neon")).getStyle().font = fontGenerator.generateFont(parameter);

        buttons.put("play", new TextButton("Play", Assets.Skins.get("neon")));
        buttons.put("leaderboards", new TextButton("Leaderboards", Assets.Skins.get("neon")));
        buttons.put("quit", new TextButton("Quit", Assets.Skins.get("neon")));
        buttons.put("settings", new TextButton("Settings", Assets.Skins.get("neon")));
    }

    private void configureWidgets() {
        buttons.get("play").setSize(550, 150);
        buttons.get("play").setPosition(50, 200);

        buttons.get("leaderboards").setSize(550, 150);
        buttons.get("leaderboards").setPosition(50, 50);

        buttons.get("quit").setSize(550, 150);
        buttons.get("quit").setPosition((float) Core.SCREEN_WIDTH - 600 , 50);

        buttons.get("settings").setSize(550, 150);
        buttons.get("settings").setPosition((float) Core.SCREEN_WIDTH - 600, 200);

        stage.addActor(buttons.get("play"));
        stage.addActor(buttons.get("leaderboards"));
        stage.addActor(buttons.get("quit"));
        stage.addActor(buttons.get("settings"));
    }

    private void setListeners() {
        buttons.get("play").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameState.RenderMenuTitle = false;
                GameState.StartingGame = true;
                buttons.get("play").remove();
                buttons.get("leaderboards").remove();
                buttons.get("quit").remove();
                buttons.get("settings").remove();
                Gdx.input.setCursorCatched(true);
            }
        });

        buttons.get("leaderboards").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardsScreen(game));
            }
        });

        buttons.get("quit").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(Settings.MusicEnabled) {
                    Assets.Music.get("menu").stop();
                }
                Gdx.app.exit();
            }
        });

        buttons.get("settings").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        menuWorld.render(delta);
        stage.draw();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

}
