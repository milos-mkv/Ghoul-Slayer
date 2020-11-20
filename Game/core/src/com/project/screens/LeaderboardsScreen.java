package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;

public class LeaderboardsScreen implements Screen {

    private final Core game;
    private final Stage stage;
    private TextButton backButton;
    private Label [] label;
    private Label title;

    private final SpriteBatch spriteBatch;

    public LeaderboardsScreen(Core game) {
        spriteBatch = new SpriteBatch();
        this.game = game;
        Assets.Music.get("game").stop();
        stage = new Stage(new FitViewport(Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT));
        setWidgets();
        configureWidgets();
        setListeners();
    }

    private void setWidgets() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/karma future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        backButton = new TextButton("Back", Assets.Skins.get("neon"));
        new Label("test", Assets.Skins.get("neon")).getStyle().font = fontGenerator.generateFont(parameter);
        title = new Label("Leaderboards", Assets.Skins.get("neon"));
        label = new Label[5];
        for(int i = 0; i < 5; i++) {
            label[i] = new Label(i + 1 + ") " + Settings.Usernames.get(i) + " - " + Settings.Highscores.get(i), Assets.Skins.get("neon"));
        }
    }

    private void configureWidgets() {
        backButton.setSize(500, 150);
        backButton.setPosition(Core.SCREEN_WIDTH - 550, 50);
        stage.addActor(backButton);


        title.setFontScale(4);
        title.setPosition(50, Core.SCREEN_HEIGHT - 50 - title.getHeight());
        stage.addActor(title);

        int y = 0;
        for (Label value : label) {
            value.setFontScale(3);
            value.setPosition(50, Core.SCREEN_HEIGHT - value.getHeight() - 200 - y);
            y += 96;
            stage.addActor(value);
        }
    }

    private void setListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(Assets.Textures.get("lbw"), 0, 0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
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
