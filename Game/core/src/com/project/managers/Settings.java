package com.project.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class Settings
{
    public static boolean Paused;
    public static boolean SoundEnabled = true;
    public static boolean MusicEnabled = true;
    public static boolean ChristmasTheme = true;
    public static float MouseSensitivity = 0.5f;

    public static ArrayList<String> Usernames = new ArrayList<String>();
    public static ArrayList<Integer> Highscores = new ArrayList<Integer>();
    public static final String file = ".game";

    public static void load() {
        for(int i = 0; i < 5; i++) {
            Highscores.add(500);
            Usernames.add("undefined");
        }
        try {
            FileHandle fileHandle = Gdx.files.external(file);
            String[] strings = fileHandle.readString().split("\n");
            SoundEnabled = Boolean.parseBoolean(strings[0]);
            MusicEnabled = Boolean.parseBoolean(strings[1]);
            ChristmasTheme = Boolean.parseBoolean(strings[12]);
            for(int i=2;i<=10;i+=2) {
                Highscores.add(Integer.parseInt(strings[i]) );
                Usernames.add(strings[i + 1]);
            }
            MouseSensitivity = Float.parseFloat(strings[13]);
            sort();
        }
        catch (Throwable e) {
            sort();
        }
    }

    private static void sort() {
        for(int i = 0; i < Highscores.size() - 1; i++) {
            for(int j = i + 1; j < Highscores.size(); j++) {
                if(Highscores.get(i) <= Highscores.get(j)) {
                    int scoreTemp = Highscores.get(i);
                    String nameTemp = Usernames.get(i);
                    Highscores.set(i, Highscores.get(j));
                    Highscores.set(j, scoreTemp);
                    Usernames.set(i, Usernames.get(j));
                    Usernames.set(j, nameTemp);
                }
            }
        }
    }

    public static void save() {
        try {
            FileHandle fileHandle = Gdx.files.external(file);
            fileHandle.writeString(Boolean.toString(SoundEnabled) + "\n", false);
            fileHandle.writeString(Boolean.toString(MusicEnabled) + "\n", true);
            for(int i = 0; i < 5; i++) {
                fileHandle.writeString(Integer.toString(Highscores.get(i)) + "\n" + Usernames.get(i) + "\n", true);
            }
            fileHandle.writeString(Boolean.toString(ChristmasTheme) + "\n", true);
            fileHandle.writeString(Float.toString(MouseSensitivity) + "\n", true);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void addScore(int score, String username) {
        Highscores.add(score);
        Usernames.add(username);
        sort();
    }
}
