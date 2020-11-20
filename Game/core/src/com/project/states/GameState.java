package com.project.states;

public class GameState {
    public static boolean StartingGame;
    public static boolean RenderMenuTitle;
    public static float Elapsed;
    public static boolean RenderBlood;
    public static int EnemySpawnNumber;
    public static float EnemySpeed;
    public static final float EnemyRunningSpeed = 0.35f;
    public static final float EnemyStartingSpeed = 0.3f;
    public static float PlayerSpeed = 0.4f;
    public static boolean RenderFrameRate;
    public static float FOV = 67f;
    public static final boolean DebugMode = false;
    public static int FrameCounter = 0;
    public static float DeadPlayerCounterScreen;
    public static boolean StartDeadCounter;
    public static boolean Colliding;
}
