package com.project.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.project.components.*;
import com.project.game.Assets;
import com.project.game.Core;
import com.project.managers.EnemyAnimations;
import com.project.managers.Settings;
import com.project.states.GameState;
import com.project.ui.GameUI;
import com.project.ui.widgets.ControllerWidget;
import com.project.worlds.GameWorld;

public class PlayerSystem extends EntitySystem implements EntityListener {

    private final GameWorld gameWorld;
    private final GameUI gameUI;
    private final ClosestRayResultCallback rayTestCB;
    private final Camera camera;

    private final Vector3 tmp = new Vector3();
    private final Vector3 rayFrom = new Vector3();
    private final Vector3 rayTo = new Vector3();

    private Entity player;
    public Entity gun;

    private PlayerComponent playerComponent;
    private CharacterComponent characterComponent;
    private ModelComponent modelComponent;

    private int stepCounter = 0;

    public PlayerSystem(Camera camera, GameWorld gameWorld, GameUI gameUI) {
        this.camera = camera;
        this.gameWorld = gameWorld;
        this.gameUI = gameUI;
        GameState.Colliding = false;
        rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(Family.all(PlayerComponent.class).get(), this);
    }

    @Override
    public void update(float deltaTime) {
        if(GameState.StartDeadCounter) {
            GameState.DeadPlayerCounterScreen += deltaTime;
        }

        checkGameOver();
        if(player == null) return;

        updateMovement(deltaTime);
        updateStatus();
    }

    private void checkGameOver() {
        if(playerComponent.health <= 0 && !Settings.Paused) {
            Settings.Paused = true;
            gameUI.gameOverWidget.gameOver();
            GameUI.reloadButton.setVisible(false);
            GameUI.shootButton.setVisible(false);
            GameUI.jumpButton.setVisible(false);
            GameUI.menuButton.setVisible(false);
            GameState.StartDeadCounter = true;
        }
    }

    private void updateMovement(float delta) {
        float deltaX, deltaY;
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            deltaX = -ControllerWidget.getWatchVector().x * 10 * Settings.MouseSensitivity;
            deltaY = ControllerWidget.getWatchVector().y  * 10 * Settings.MouseSensitivity;
        } else {
             deltaX = -Gdx.input.getDeltaX() * Settings.MouseSensitivity;
             deltaY = -Gdx.input.getDeltaY() * Settings.MouseSensitivity;
        }

        tmp.set(0, 0, 0);
        camera.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);
        tmp.set(0, 0, 0);

        characterComponent.characterDirection.set(-1, 0, 0).rot(modelComponent.instance.transform).nor();
        characterComponent.walkDirection.set(0, 0, 0);

        Vector3 prePosition = player.getComponent(ModelComponent.class).instance.transform.getTranslation(new Vector3());
        stepCounter ++;
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            if(ControllerWidget.getMovementVector().y > 0) {
                characterComponent.walkDirection.add(camera.direction);
                if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                    Assets.Sounds.get("footStep").play(0.05f);
            }
            if(ControllerWidget.getMovementVector().y < 0) {
                characterComponent.walkDirection.sub(camera.direction);
                if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                    Assets.Sounds.get("footStep").play(0.05f);
            }
            if(ControllerWidget.getMovementVector().x < 0) {
                tmp.set(camera.direction).crs(camera.up).scl(-1);
                if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                    Assets.Sounds.get("footStep").play(0.05f);
            }
            if(ControllerWidget.getMovementVector().x > 0) {
                tmp.set(camera.direction).crs(camera.up).scl(1);
                if (prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                    Assets.Sounds.get("footStep").play(0.05f);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterComponent.walkDirection.add(camera.direction);
            if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                Assets.Sounds.get("footStep").play(0.05f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterComponent.walkDirection.sub(camera.direction);
            if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                Assets.Sounds.get("footStep").play(0.05f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            tmp.set(camera.direction).crs(camera.up).scl(-1);
            if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                Assets.Sounds.get("footStep").play(0.05f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            tmp.set(camera.direction).crs(camera.up).scl(1);
            if(prePosition.y < 6 && stepCounter % 40 == 0 && Settings.SoundEnabled)
                Assets.Sounds.get("footStep").play(0.05f);
        }

        if(GameUI.Reload && !PlayerComponent.IsReloading && PlayerComponent.ShootAmmo < 15 && PlayerComponent.TotalAmmo > 0) {
            player.getComponent(PlayerComponent.class).reloadingTime = 0;
            PlayerComponent.IsReloading = true;
            PlayerComponent.TotalAmmo -= 15;
            if(PlayerComponent.TotalAmmo < 0) {
                PlayerComponent.Rest = 15 - Math.abs(PlayerComponent.TotalAmmo);
                PlayerComponent.TotalAmmo = 0;
            }
            if(Settings.SoundEnabled) {
                Assets.Sounds.get("gunReload").play(1.5f);
            }
            gun.getComponent(AnimationComponent.class).animate("P90Armature|Reload", 1, 0.15f);
            GameUI.Reload = false;
        }
        else if(PlayerComponent.TotalAmmo <= 0 && PlayerComponent.ShootAmmo <= 0) {
            PlayerComponent.IsAmmoEmpty = true;
        }
        else if(PlayerComponent.TotalAmmo > 0) {
            PlayerComponent.IsAmmoEmpty = false;
        }

        if(stepCounter > 1000000) stepCounter = 0;

        characterComponent.walkDirection.add(tmp);
        characterComponent.walkDirection.scl(GameState.PlayerSpeed);
        characterComponent.characterController.setWalkDirection(characterComponent.walkDirection);

        Matrix4 ghost = new Matrix4();
        Vector3 translation = new Vector3();

        characterComponent.ghostObject.getWorldTransform(ghost);
        ghost.getTranslation(translation);

        modelComponent.instance.transform.set(translation.x, translation.y, translation.z, camera.direction.x, camera.direction.y, camera.direction.z, 0);
        camera.position.set(translation.x, translation.y, translation.z);
        camera.update(true);

        if (GameUI.Shoot && !PlayerComponent.IsReloading && PlayerComponent.ShootAmmo > 0) {
            fire();
            updateAmmo();
            GameUI.Shoot = false;
        }
        else if (player.getComponent(PlayerComponent.class).reloadingTime < 2.5f && PlayerComponent.IsReloading) {
            player.getComponent(PlayerComponent.class).reloadingTime += delta;
            if (player.getComponent(PlayerComponent.class).reloadingTime >= 2.5f) {
                PlayerComponent.DoneReloading = true;
            }
        }
        else if(PlayerComponent.DoneReloading) {
            PlayerComponent.DoneReloading = false;
            if(PlayerComponent.Rest != 0) {
                PlayerComponent.ShootAmmo = PlayerComponent.Rest;
                PlayerComponent.Rest = 0;
            }
            else {
                PlayerComponent.ShootAmmo = 15;
            }
            PlayerComponent.IsReloading = false;
        }
        Vector3 position = player.getComponent(ModelComponent.class).instance.transform.getTranslation(new Vector3());
        if(position.y < 6) {
            player.getComponent(PlayerComponent.class).jumpable = true;
        }
        else {
            player.getComponent(PlayerComponent.class).jumpable = false;
        }
        if(GameUI.Jump && player.getComponent(PlayerComponent.class).jumpable) {
            characterComponent.characterController.jump(new Vector3(0, 15, 0));
            GameUI.Jump = false;
        }
        if(player.getComponent(PlayerComponent.class).health <= 0) {
            PlayerComponent.IsDead = true;
        }
    }

    private void updateAmmo() {
        PlayerComponent.ShootAmmo -= 1;
    }

    private void updateStatus() {
        gameUI.healthWidget.setValue(playerComponent.health);
    }

    private void fire() {
        Ray ray = camera.getPickRay((float) Core.SCREEN_WIDTH / 2, (float) Core.SCREEN_HEIGHT / 2);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(500f).add(rayFrom);

        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);

        gameWorld.getBulletSystem().collisionWorld.rayTest(rayFrom, rayTo, rayTestCB);

        if (rayTestCB.hasHit()) {
            btCollisionObject obj = rayTestCB.getCollisionObject();
            if (((Entity) obj.userData).getComponent(EnemyComponent.class) != null) {
                ((Entity) obj.userData).getComponent(EnemyComponent.class).health -= player.getComponent(PlayerComponent.class).damage;
                if (((Entity) obj.userData).getComponent(EnemyComponent.class).health <= 0 && ((Entity) obj.userData).getComponent(StatusComponent.class).alive) {
                    ((Entity) obj.userData).getComponent(StatusComponent.class).setAlive(false);
                    PlayerComponent.Score += 100;
                    PlayerComponent.KillCounter += 1;
                    if (PlayerComponent.KillCounter % 5 == 0) {
                        GameState.EnemySpawnNumber += 1;
                    }
                    gameWorld.getBulletSystem().removeBody(((Entity) obj.userData));
                }
                else {
                    if (((Entity) obj.userData).getComponent(StatusComponent.class).alive) {
                        ((Entity) obj.userData).getComponent(AnimationComponent.class).animate(EnemyAnimations.HurtAnimation[(int) (Math.random() * 10) % 5], 1, 1);
                        ((Entity) obj.userData).getComponent(EnemyComponent.class).state = EnemyComponent.STATE.HURT;
                    }
                }
            }
        }
        gun.getComponent(AnimationComponent.class).animate("P90Armature|Fire", 1, 0.5f);
        if (Settings.SoundEnabled) {
            Assets.Sounds.get("gunShot").play(.1f);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        player = entity;
        playerComponent = player.getComponent(PlayerComponent.class);
        characterComponent = player.getComponent(CharacterComponent.class);
        modelComponent = player.getComponent(ModelComponent.class);
    }

    @Override
    public void entityRemoved(Entity entity) { }
}
