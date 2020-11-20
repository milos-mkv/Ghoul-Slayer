package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.project.game.Assets;
import com.project.game.Core;

public class LoadingScreen implements Screen {

    private final Core game;

    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont fontForText;

    private final Sprite frame;
    private final SpriteBatch spriteBatch;

    private float time = 0;

    public LoadingScreen(Core game) {
        this.game = game;
        initializeFonts();
        frame = new Sprite(new Texture(Gdx.files.internal("loadwall.jpg")));
        spriteBatch = new SpriteBatch();
        Gdx.input.setCursorCatched(true);
    }

    private void initializeFonts() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        fontForText = fontGenerator.generateFont(parameter);
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(frame, 0, 0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
        fontForText.draw(spriteBatch, "Loading please wait", (float) Core.SCREEN_WIDTH / 2 - 350, (float) Core.SCREEN_HEIGHT / 2 - 450);
        spriteBatch.end();

        if((time += delta) > 1) {
            Assets.load();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        fontGenerator.dispose();
        fontForText.dispose();
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
