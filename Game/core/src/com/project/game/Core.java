package com.project.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.project.managers.Settings;
import com.project.screens.LoadingScreen;

public class Core extends ApplicationAdapter {

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	private Screen screen;

	@Override
	public void create() {
		Settings.load();
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		setScreen(new LoadingScreen(this));
	}

	public void setScreen(Screen screen) {
		if(this.screen != null) {
			this.screen.hide();
			this.screen.dispose();
		}
		if((this.screen = screen) != null) {
			this.screen.show();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		screen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		Assets.dispose();
		Settings.save();
		screen.dispose();
	}
}
