package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.project.game.Core;
import com.project.managers.Settings;


public class SubmitScoreScreen implements Screen, InputProcessor
{
    private Core game;
    private String username = "";
    private int score;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont fontForText;
    private String title = "Username:";
    private SpriteBatch spriteBatch;

    public SubmitScoreScreen(Core game, int score)
    {
        this.game = game;
        this.score = score;
        spriteBatch = new SpriteBatch();
        initializeFonts();
        Gdx.input.setInputProcessor(this);
    }

    private void initializeFonts()
    {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        fontForText = fontGenerator.generateFont(parameter);
    }


    @Override
    public void render(float delta)
    {

        spriteBatch.begin();
        fontForText.draw(spriteBatch, title, (float)Core.SCREEN_WIDTH / 2 - 180, Core.SCREEN_HEIGHT - 200);
        fontForText.draw(spriteBatch, username, (float) Core.SCREEN_WIDTH / 2 - username.length() * 20.5f, Core.SCREEN_HEIGHT - 300);
        spriteBatch.end();
    }

    @Override
    public void dispose()
    {
        fontForText.dispose();
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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == 66) {
            Settings.addScore(score, username);
            game.setScreen(new MenuScreen(game));
        }
        if(keycode == 67) {
            if(username.length() > 0) {
                username = username.substring(0, username.length() - 1);
            }
            return false;
        }
        String chr = Input.Keys.toString(keycode);
        username += chr.length() == 1 ? chr : " ";
        if(username.length() > 20)
            username = username.substring(0, username.length() - 1);


        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
