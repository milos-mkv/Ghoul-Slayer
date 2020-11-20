package com.project.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.project.managers.FrameRate;
import com.project.managers.GifDecoder;

import java.util.HashMap;

public class Assets {

    public static HashMap<String, Sound> Sounds = new HashMap<>();
    public static HashMap<String, Music> Music = new HashMap<>();
    public static HashMap<String, Skin> Skins = new HashMap<>();
    public static HashMap<String, Model> Models = new HashMap<>();
    public static FrameRate FrameRate = new FrameRate();
    public static Animation<TextureRegion> SnowFallAnimation;
    public static HashMap<String, Texture> Textures = new HashMap<>();

    public static void load() {
        initializeSkins();
        initializeSounds();
        initializeSpritesAndTextures();
        initializeModels();
    }

    private static void initializeSkins() {
        Skins.put("neon", new Skin());
        {
            FileHandle fileHandle = Gdx.files.internal("core/assets/neon-skin/neon-ui.json");
            FileHandle atlasFile = fileHandle.sibling("neon-ui.atlas");
            if (atlasFile.exists()) {
                Skins.get("neon").addRegions(new TextureAtlas(atlasFile));
            }
            Skins.get("neon").load(fileHandle);
        }
        Skins.put("default", new Skin());
        {
            FileHandle fileHandle = Gdx.files.internal("core/assets/skin1/uiskin.json");
            FileHandle atlasFile = fileHandle.sibling("uiskin.atlas");
            if (atlasFile.exists()) {
                Skins.get("default").addRegions(new TextureAtlas(atlasFile));
            }
            Skins.get("default").load(fileHandle);
        }
    }

    private static void initializeSounds() {
        Music.put("game", Gdx.audio.newMusic(Gdx.files.internal("core/assets/music/gameMusic.mp3")));
        Music.put("menu", Gdx.audio.newMusic(Gdx.files.internal("core/assets/music/menu.mp3")));

        Sounds.put("gunShot", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/shot.wav")));
        Sounds.put("footStep", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/step1.wav")));
        Sounds.put("gunReload", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/reload.wav")));
        Sounds.put("ghoul", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/ghoul.wav")));
        Sounds.put("ghoulDead", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/skeleton.wav")));
        Sounds.put("hurt", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/hurt.wav")));
        Sounds.put("collect", Gdx.audio.newSound(Gdx.files.internal("core/assets/music/collect.wav")));
    }

    private static void initializeSpritesAndTextures() {
        Textures.put("sky", new Texture(Gdx.files.internal("core/assets/textures/sky.jpg")));
        Textures.put("gameTitle", new Texture(Gdx.files.internal("core/assets/textures/Ghoul-Slayer.png")));
        Textures.put("blood", new Texture(Gdx.files.internal("core/assets/textures/blood.png")));
        Textures.put("player", new Texture(Gdx.files.internal("core/assets/badlogic.jpg")));
        Textures.put("crosshairDot", new Texture(Gdx.files.internal("core/assets/textures/crossHairInnerRing.png")));
        Textures.put("gameOver", new Texture(Gdx.files.internal("core/assets/textures/Game-Over.png")));
        Textures.put("ammo", new Texture(Gdx.files.internal("core/assets/textures/ammo3.png")));
        Textures.put("heal", new Texture(Gdx.files.internal("core/assets/textures/heal.png")));
        Textures.put("lbw", new Texture(Gdx.files.internal("core/assets/textures/lbw.jpg")));

    }

    private static void initializeModels() {
        ObjLoader objLoader = new ObjLoader();
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());

        Models.put("ghoul", new Model(modelLoader.loadModelData(Gdx.files.internal("core/assets/models/try.g3dj")), new TextureProvider.FileTextureProvider()));
        Models.put("gun", new Model(modelLoader.loadModelData(Gdx.files.internal("core/assets/models/P90.g3dj")), new TextureProvider.FileTextureProvider()));
        Models.put("ground", objLoader.loadModel(Gdx.files.internal("core/assets/untitled.obj")));
        Models.put("wall", objLoader.loadModel(Gdx.files.internal("core/assets/plains.obj")));
        Models.put("skull", objLoader.loadModel(Gdx.files.internal("core/assets/skull.obj")));
    }

    public static void dispose() {
        for (String key : Skins.keySet())
            Skins.get(key).dispose();
        for (String key : Music.keySet())
            Music.get(key).dispose();
        for (String key : Sounds.keySet())
            Sounds.get(key).dispose();
        for (String key : Textures.keySet())
            Textures.get(key).dispose();
        for (String key : Models.keySet())
            Models.get(key).dispose();
        FrameRate.dispose();
    }
}
