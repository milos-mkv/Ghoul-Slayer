package com.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.Settings;
import com.project.states.GameState;

public class InstructionScreen implements Screen
{

    private Core game;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter1;
    private BitmapFont fontForText;
    private BitmapFont fontForText1;
    private String loadingText = "Press space to begin";
    private String [] instructions = new String []
            {
                    "Try to survive as long as you can by killing Ghouls",
                    "Ghouls spawn from 4 big skulls on each side of the wall",
                    "One bullet (shoot) deal 10 damage to the ghouh",
                    "All ghouls have 30 health total",
                    "For every killed ghoul game gets harder",
                    "Every time you reload gun, you lose 15 ammo",
                    "Supply box will spawn on every 25 seconds"
            };

    private SpriteBatch spriteBatch;
    private boolean printAllInstructions = false;

    public InstructionScreen(Core game)
    {
        initializeGame(game);
        initializeFonts();
        initializeSpriteBatch();
        setupGdxInputs();
    }

    private void initializeGame(Core game)
    {
        this.game = game;
    }

    private void setupGdxInputs()
    {
        Gdx.input.setCursorCatched(true);
    }

    private void initializeFonts()
    {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/8-BITWONDER.TTF"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 15;
        parameter1.color = Color.GREEN;
        fontForText = fontGenerator.generateFont(parameter);
        fontForText1 = fontGenerator.generateFont(parameter1);
    }

    private void initializeSpriteBatch()
    {
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta)
    {
        GameState.Elapsed += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            printAllInstructions = true;
        }

        spriteBatch.begin();
        if(printAllInstructions)
        {
            for(int i = 1; i < instructions.length; i++)
            {
                fontForText1.draw(spriteBatch, instructions[i], Core.SCREEN_WIDTH / 2 + 15 - instructions[i].length() * 7, Core.SCREEN_HEIGHT - 200 - i * 30);
            }
            fontForText.draw(spriteBatch, loadingText, (float) Core.SCREEN_WIDTH / 2 - 270, (float) Core.SCREEN_HEIGHT / 2 - 30);
        }
        else
        {
            if(Settings.ChristmasTheme)
            {
                spriteBatch.draw(Assets.SnowFallAnimation.getKeyFrame(GameState.Elapsed), 0, 0, Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT);
            }
            fontForText1.draw(spriteBatch, instructions[0], (float) Core.SCREEN_WIDTH / 2 - 350, (float) Core.SCREEN_HEIGHT / 2 + 40);
            fontForText1.draw(spriteBatch, "Press M for more information", (float) Core.SCREEN_WIDTH / 2 - 200, (float) Core.SCREEN_HEIGHT / 2 + 10);
            fontForText.draw(spriteBatch, loadingText, (float) Core.SCREEN_WIDTH / 2 - 270, (float) Core.SCREEN_HEIGHT / 2 - 30);
        }
        spriteBatch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            Assets.Music.get("menu").stop();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void dispose()
    {
        fontGenerator.dispose();
        fontForText1.dispose();
        fontForText.dispose();
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
