package com.project.ui;

import com.badlogic.gdx.Gdx;
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
import com.project.ui.widgets.*;


public class GameUI {

    private final Core game;
    public Stage stage;
    public HealthWidget healthWidget;
    private ScoreWidget scoreWidget;
    private PauseWidget pauseWidget;
    private AmmoWidget ammoWidget;
    private CrosshairWidget crosshairWidget;
    public GameOverWidget gameOverWidget;
    private ControllerWidget controllerWidget;
    public static boolean Jump = false;
    public static boolean Shoot = false;
    public static boolean Reload = false;

    public static TextButton menuButton;
    public static TextButton jumpButton;
    public static TextButton reloadButton;
    public static TextButton shootButton;

    public GameUI(Core game) {
        this.game = game;
        stage = new Stage(new FitViewport(Core.SCREEN_WIDTH, Core.SCREEN_HEIGHT));
        setWidgets();
        configureWidgets();
    }

    private void setWidgets() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/karma future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        new Label("test", Assets.Skins.get("neon")).getStyle().font = fontGenerator.generateFont(parameter);

        menuButton = new TextButton("Menu", Assets.Skins.get("neon"));
        jumpButton = new TextButton("Jump", Assets.Skins.get("neon"));
        reloadButton = new TextButton("Reload", Assets.Skins.get("neon"));
        shootButton = new TextButton("Shoot", Assets.Skins.get("neon"));

        controllerWidget = new ControllerWidget();
        healthWidget = new HealthWidget();
        scoreWidget = new ScoreWidget();
        pauseWidget = new PauseWidget(game, stage);
        crosshairWidget = new CrosshairWidget();
        gameOverWidget = new GameOverWidget(game, stage);
        ammoWidget = new AmmoWidget();
    }

    private void configureWidgets() {
        controllerWidget.addToStage(stage);

        healthWidget.setSize(500, 70);
        healthWidget.setPosition(10, Core.SCREEN_HEIGHT - 150);

        scoreWidget.setSize(150, 50);
        scoreWidget.setPosition((float) Core.SCREEN_WIDTH / 2 - scoreWidget.getWidth() / 2, Core.SCREEN_HEIGHT -  scoreWidget.getHeight());

        pauseWidget.setSize(300, 300);
        pauseWidget.setPosition(Core.SCREEN_WIDTH - pauseWidget.getWidth(), Core.SCREEN_HEIGHT - pauseWidget.getHeight());
        gameOverWidget.setSize(280, 100);
        gameOverWidget.setPosition((float) Core.SCREEN_WIDTH / 2 - 280f / 2, (float) Core.SCREEN_HEIGHT / 2);
        crosshairWidget.setSize(32, 32);
        crosshairWidget.setPosition((float) Core.SCREEN_WIDTH / 2 - 16, (float) Core.SCREEN_HEIGHT / 2 - 16);
        ammoWidget.setSize(150 , 50);
        ammoWidget.setPosition(10, Core.SCREEN_HEIGHT - ammoWidget.getHeight() - 10);

        menuButton.setSize(300, 100);
        menuButton.setPosition(Core.SCREEN_WIDTH - 310, Core.SCREEN_HEIGHT - 110);

        reloadButton.setSize(300, 200);
        reloadButton.setPosition(Core.SCREEN_WIDTH - 310, Core.SCREEN_HEIGHT - 510);

        shootButton.setSize(300, 200);
        shootButton.setPosition(Core.SCREEN_WIDTH - 310, Core.SCREEN_HEIGHT - 710);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseWidget.handleUpdates();
            }
        });


        shootButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Shoot = true;
            }
        });
        reloadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Reload = true;
            }
        });
        jumpButton.setSize(500, 100);
        jumpButton.setPosition(Core.SCREEN_WIDTH / 2 - 500 / 2, 10);
        jumpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameUI.Jump = true;
            }
        });
        stage.addActor(healthWidget);
        stage.addActor(scoreWidget);
        stage.addActor(crosshairWidget);
        stage.addActor(ammoWidget);
        stage.setKeyboardFocus(pauseWidget);
        stage.addActor(menuButton);
        stage.addActor(jumpButton);
        stage.addActor(reloadButton);
        stage.addActor(shootButton);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}