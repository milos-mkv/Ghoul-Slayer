package com.project.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.project.components.*;
import com.project.game.Assets;
import com.project.states.GameState;

public class MyContactListener extends ContactListener {
    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
        if (colObj0.userData instanceof Entity && colObj1.userData instanceof Entity) {
            Entity entity0 = (Entity) colObj0.userData;
            Entity entity1 = (Entity) colObj1.userData;

            if (entity0.getComponent(EnemyComponent.class) != null && entity1.getComponent(EnemyComponent.class) != null)
                return;

            if (entity0.getComponent(AmmoComponent.class) != null && entity1.getComponent(PlayerComponent.class) != null) {
                entity0.getComponent(StatusComponent.class).alive = false;
                if (Settings.SoundEnabled) {
                    Assets.Sounds.get("collect").play(1f);
                }
                PlayerComponent.TotalAmmo += 50;
            }
            else if (entity0.getComponent(PlayerComponent.class) != null && entity1.getComponent(AmmoComponent.class) != null) {
                entity1.getComponent(StatusComponent.class).alive = false;
                if (Settings.SoundEnabled) {
                    Assets.Sounds.get("collect").play(1f);
                }
                PlayerComponent.TotalAmmo += 50;
            }

            if (entity0.getComponent(HealComponent.class) != null && entity1.getComponent(PlayerComponent.class) != null) {
                entity0.getComponent(StatusComponent.class).alive = false;
                if (Settings.SoundEnabled) {
                    Assets.Sounds.get("collect").play(1f);
                }
                entity1.getComponent(PlayerComponent.class).health += 30;
                if (entity1.getComponent(PlayerComponent.class).health > 100) {
                    entity1.getComponent(PlayerComponent.class).health = 100;
                }
            }
            else if (entity0.getComponent(PlayerComponent.class) != null && entity1.getComponent(HealComponent.class) != null) {
                entity1.getComponent(StatusComponent.class).alive = false;
                if (Settings.SoundEnabled) {
                    Assets.Sounds.get("collect").play(1f);
                }
                entity0.getComponent(PlayerComponent.class).health += 30;
                if (entity0.getComponent(PlayerComponent.class).health > 100) {
                    entity0.getComponent(PlayerComponent.class).health = 100;
                }
            }

            if (entity0.getComponent(CharacterComponent.class) != null && entity1.getComponent(CharacterComponent.class) != null) {
                if (entity0.getComponent(EnemyComponent.class) != null) {
                    if (entity0.getComponent(StatusComponent.class).alive && entity0.getComponent(StatusComponent.class).isAbleToAttack) {
                        entity1.getComponent(PlayerComponent.class).health -= 10;
                        entity0.getComponent(EnemyComponent.class).state = EnemyComponent.STATE.ATTACKING;
                        entity0.getComponent(AnimationComponent.class).animate(EnemyAnimations.AttackAnimation[(int) (Math.random() * 10) % 4], 1, 1f);
                        entity0.getComponent(StatusComponent.class).isAbleToAttack = false;
                        GameState.RenderBlood = true;
                        GameState.Colliding = true;
                        if (Settings.SoundEnabled) {
                            Assets.Sounds.get("hurt").play();
                        }
                    }
                }
                else {
                    if (entity1.getComponent(StatusComponent.class).alive && entity1.getComponent(StatusComponent.class).isAbleToAttack) {
                        entity0.getComponent(PlayerComponent.class).health -= 10;
                        entity1.getComponent(EnemyComponent.class).state = EnemyComponent.STATE.ATTACKING;
                        entity1.getComponent(AnimationComponent.class).animate(EnemyAnimations.AttackAnimation[(int) (Math.random() * 10) % 4], 1, 1f);
                        entity1.getComponent(StatusComponent.class).isAbleToAttack = false;
                        GameState.RenderBlood = true;
                        GameState.Colliding = true;
                        if (Settings.SoundEnabled) {
                            Assets.Sounds.get("hurt").play();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1) {
        if (colObj0.userData instanceof Entity && colObj1.userData instanceof Entity) {
            Entity entity0 = (Entity) colObj0.userData;
            Entity entity1 = (Entity) colObj1.userData;

            if (entity0.getComponent(EnemyComponent.class) != null && entity1.getComponent(PlayerComponent.class) != null) {
                GameState.Colliding = false;
            }
            else if(entity1.getComponent(EnemyComponent.class) != null && entity0.getComponent(PlayerComponent.class) != null) {
                GameState.Colliding = false;
            }
        }
    }
}