package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;

public class SettingsScreen implements Screen {

    private final Core game;
    private Stage stage;

    private CheckBox enableMusic;
    private CheckBox enableSound;

    private final SpriteBatch spriteBatch;

    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont fontForTitle;

    public SettingsScreen(Core game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        initializeFonts();
        initializeWidgets();
        Gdx.input.setInputProcessor(stage);
    }


    private void initializeFonts() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter title = new FreeTypeFontGenerator.FreeTypeFontParameter();
        title.size = 80;
        fontForTitle = fontGenerator.generateFont(title);
    }

    private void initializeWidgets() {
        stage = new Stage(new FitViewport(Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT));

        enableMusic = new CheckBox("Enable music", Assets.Skins.get("neon"));
        enableMusic.getImage().setScaling(Scaling.fill);
        enableMusic.getImageCell().size(50);
        enableSound = new CheckBox("Enable sound", Assets.Skins.get("neon"));
        enableSound.getImage().setScaling(Scaling.stretch);
        enableSound.getImageCell().size(50);

        TextButton backButton = new TextButton("Back", Assets.Skins.get("neon"));

        backButton.setSize(500, 150);
        backButton.setPosition(Core.SCREEN_WIDTH - 550, 50);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        enableMusic.setSize(500, 100);
        enableMusic.setPosition(50, Core.SCREEN_HEIGHT - 250);
        enableMusic.setChecked(Settings.MusicEnabled);
        enableMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.MusicEnabled = enableMusic.isChecked();
                if(!Settings.MusicEnabled) {
                    Assets.Music.get("menu").stop();
                }
                else {
                    Assets.Music.get("menu").play();
                }
            }
        });

        enableSound.setSize(500, 100);
        enableSound.setPosition(50, Core.SCREEN_HEIGHT - 300);
        enableSound.setChecked(Settings.SoundEnabled);
        enableSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.SoundEnabled = enableSound.isChecked();
            }
        });

        stage.addActor(enableMusic);
        stage.addActor(enableSound);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(Assets.Textures.get("lbw"), 0, 0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);

        fontForTitle.draw(spriteBatch, "Settings", (float) Core.SCREEN_WIDTH / 2 - 300 , Core.SCREEN_HEIGHT - 50);
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        fontForTitle.dispose();
        fontGenerator.dispose();
        spriteBatch.dispose();
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
