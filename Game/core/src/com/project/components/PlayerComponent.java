package com.project.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component
{
    public static int Score;
    public static int TotalAmmo;
    public static int ShootAmmo;
    public static boolean IsReloading;
    public static boolean IsAmmoEmpty;
    public static int KillCounter;
    public static boolean DoneReloading;
    public static boolean IsDead;
    public static int Rest;

    public float health;
    public int damage;
    public boolean jumpable;
    public float reloadingTime;

    public PlayerComponent()
    {
        reset();
        reloadingTime = 0;
        jumpable = false;
        health = 100;
        damage = GunComponent.Damage;
    }

    private static void reset()
    {
        DoneReloading = false;
        KillCounter = 0;
        IsReloading = false;
        TotalAmmo = 150;
        ShootAmmo = 15;
        Score = 0;
        IsDead = false;
        Rest = 0;
    }
}
