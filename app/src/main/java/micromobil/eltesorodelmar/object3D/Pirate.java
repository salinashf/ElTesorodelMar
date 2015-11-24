package micromobil.eltesorodelmar.object3D;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Pirate extends Object3D {

    public Object3D sword;
    public SimpleVector moveRes;
    public SimpleVector moveRotate;
    public SimpleVector g;
    public SimpleVector gravityVector;
    public SimpleVector jumpDir;
    public SimpleVector attackDir;
    public SimpleVector shootDir;
    public int state;
    public float ind;
    public int count = 0;
    public PirateThread a;
    public GravityThread gravity;
    public int gravityDamage;
    public Camera cam;
    public Camera fallCam;
    public Box box;
    public int damage;
    public boolean onGround; //especifica si el pirata esto en el suelo o en el aire
    private Render game;


    public Pirate(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.pirataser)));
        game.soundManager.addSound(Constants.PAIN, R.raw.sdamaged);
        game.soundManager.addSound(Constants.DEATH, R.raw.sdeath);
        game.soundManager.addSound(Constants.ATK1, R.raw.satk);
        game.soundManager.addSound(Constants.SHOOT, R.raw.sgun);
        a = new PirateThread(this);
        gravity = new GravityThread(game, this);
        setTexture("piratat");
        setCulling(false);
        setCollisionMode(Object3D.COLLISION_CHECK_SELF);
        setEllipsoidMode(Object3D.ELLIPSOID_TRANSFORMED);
        setCollisionOptimization(Object3D.COLLISION_DETECTION_OPTIMIZED);
        // cargar espada
        sword = Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.espadaser));
        sword.setTexture("espadat");
        sword.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
        sword.setEllipsoidMode(Object3D.ELLIPSOID_TRANSFORMED);
        // asocia protagonista y la espada
        addChild(sword);
        this.game = game;
        state = Constants.STAND;

        strip();
        sword.strip();
        build();
        sword.build();

        game.world.addObject(this);
        game.world.addObject(sword);

        moveRes = new SimpleVector();
        moveRotate = new SimpleVector();
        g = new SimpleVector();
        gravityVector = new SimpleVector();
        jumpDir = new SimpleVector();
        attackDir = new SimpleVector();
        shootDir = new SimpleVector();

        a.start();
        gravity.start();
    }

    public void animate() {
        // System.out.println(state);
        switch (state) {
            case Constants.STAND: {
                ind += 0.01f;
                if (ind > 1) {
                    ind -= 1;
                }
                animate(ind, 7);
                sword.animate(ind, 7);
                break;
            }
            case Constants.WALK: {
                ind += 0.1f;
                if (ind > 1) {
                    ind = 0;
                }
                animate(ind, 1);
                sword.animate(ind, 1);
                break;
            }
            case Constants.ATK1: {
                ind += 0.25;
                sword.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
                if (ind > 1) {
                    sword.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
                    state = Constants.STAND;
                    ind = 0;
                }
                SimpleVector attack = this.getRotationMatrix().getZAxis();
                attack.scalarMul(0.25f);
                attack = checkForCollisionEllipsoid(attack, Constants.ELLIPSOID, Constants.RECURSION);
                this.translate(attack);
                animate(ind, 5);
                sword.animate(ind, 5);
                break;
            }
            case Constants.JUMP: {
                ind += 0.0285;
                jumpDir = getRotationMatrix().getZAxis();
                if (ind >= 0.5) {
                    state = Constants.STAND;
                    ind = 0;
                } else if (ind < 0.25) {
                    jumpDir.set(jumpDir.x
                            * Constants.JUMP_LENGTH, -Constants.JUMP_HEIGHT, jumpDir.z
                            * Constants.JUMP_LENGTH);
                    jumpDir.set(checkForCollisionEllipsoid(jumpDir, Constants.ELLIPSOID, Constants.RECURSION));
                    translate(jumpDir);
                    if (game.cam.getPosition().distance(getTransformedCenter()) >= Constants.CAM_DIST) {
                        game.world.checkCameraCollisionEllipsoid(jumpDir,
                                Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                    }
                } else if (ind >= 0.25 && ind < 0.5) {
                    jumpDir.set(jumpDir.x
                            * Constants.JUMP_LENGTH, Constants.JUMP_HEIGHT, jumpDir.z
                            * Constants.JUMP_LENGTH);
                    jumpDir.set(checkForCollisionEllipsoid(jumpDir, Constants.ELLIPSOID, Constants.RECURSION));
                    translate(jumpDir);
                    if (game.cam.getPosition().distance(getTransformedCenter()) >= Constants.CAM_DIST) {
                        game.world.checkCameraCollisionEllipsoid(jumpDir,
                                Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                    }
                }
                animate(ind, 2);
                sword.animate(ind, 2);
                break;
            }
            case Constants.FALL: {
                ind += 0.066666f;
                if (ind > 1) {
                    ind -= 1;
                }
                animate(ind, 4);
                sword.setVisibility(false);
                break;
            }
            case Constants.PRECLIMB: {
                ind += 0.0166666f;
                if (ind > 0.6) {
                    ind = 0;
                }
                animate(ind, 6);
                sword.animate(ind, 6);
                break;
            }
            case Constants.PUSH: {
                System.out.println("push2");
                ind += 0.0166666f;
                if (ind > 1) {
                    ind = -1;
                }

                animate(ind, 8);
                sword.animate(ind, 8);
                break;
            }
            case Constants.DEATH: {
                ind += 0.0166666f;
                if (ind > 0.8) {
                    ind = 0.8f;
                }
                animate(ind, 3);
                sword.animate(ind, 3);
                break;
            }
            case Constants.PAIN: {
                ind += 0.04166666f;
                if (ind > 0.33333) {
                    state = Constants.STAND;
                    ind = 0;
                }
                animate(ind, 9);
                sword.animate(ind, 9);
                break;
            }
            case Constants.PAIN_BY_KNIGHT: {
                ind += 0.04166666f;
                if (ind > 0.33333) {
                    state = Constants.STAND;
                    ind = 0;
                }
                SimpleVector lookToSource = game.castle.knight.getTransformedCenter().calcSub(getTransformedCenter()).normalize();
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                setOrientation(lookToSource, new SimpleVector(0, 1, 0));
                lookToSource.rotateY((float) Math.PI);
                lookToSource.scalarMul(0.5f);
                lookToSource.set(checkForCollisionEllipsoid(lookToSource, Constants.ELLIPSOID, Constants.RECURSION));
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                this.translate(lookToSource);
                game.world.checkCameraCollisionEllipsoid(lookToSource, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                animate(ind, 9);
                sword.animate(ind, 9);
                break;
            }
            case Constants.PAIN_BY_DEVIL: {
                ind += 0.04166666f;
                if (ind > 0.33333) {
                    state = Constants.STAND;
                    ind = 0;
                }
                SimpleVector lookToSource = game.castle.devil.getTransformedCenter().calcSub(getTransformedCenter()).normalize();
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                setOrientation(lookToSource, new SimpleVector(0, 1, 0));
                lookToSource.rotateY((float) Math.PI);
                lookToSource.scalarMul(0.5f);
                lookToSource.set(checkForCollisionEllipsoid(lookToSource, Constants.ELLIPSOID, Constants.RECURSION));
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                this.translate(lookToSource);
                game.world.checkCameraCollisionEllipsoid(lookToSource, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                animate(ind, 9);
                sword.animate(ind, 9);
                break;
            }
        }
    }

    public void movePirate(int dir) {

        moveRes.set(0, 0, 0);
        moveRotate.set(0, 0, 0);
        cam = game.cam;

        switch (dir) {
            case Constants.NW:
                //Mover adelante
                moveRes.set(cam.getDirection().x
                        * Constants.MOV_SPEED, 0, cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) >= Constants.CAM_DIST) {
                    game.cam.moveCamera(moveRes, 1);
                }
                //Mover izquierda
                moveRes.set(cam.getDirection());
                moveRes.rotateY((float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(game.cam.getDirection().x, 0, cam.getDirection().z);
                moveRotate.rotateY((float) Math.PI / 4);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.SW:
                //Mover atras
                moveRes.set(-cam.getDirection().x
                        * Constants.MOV_SPEED, 0, -cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) <= Constants.CAM_DIST) {
                    game.world.checkCameraCollisionEllipsoid(moveRes,
                            Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                }
                //Mover izquierda
                moveRes.set(cam.getDirection());
                moveRes.rotateY((float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(-cam.getDirection().x, 0, -cam.getDirection().z);
                moveRotate.rotateY(-(float) Math.PI / 4);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.W:
                //Mover izquierda
                moveRes.set(cam.getDirection());
                moveRes.rotateY((float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(cam.getDirection().x, 0, cam.getDirection().z);
                moveRotate.rotateY((float) Math.PI / 2);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.NE:
                //Mover adelante
                moveRes.set(cam.getDirection().x
                        * Constants.MOV_SPEED, 0, cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) >= Constants.CAM_DIST) {
                    game.cam.moveCamera(moveRes, 1);
                }
                //Mover derecha
                moveRes.set(cam.getDirection());
                moveRes.rotateY(-(float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(cam.getDirection().x, 0, cam.getDirection().z);
                moveRotate.rotateY(-(float) Math.PI / 4);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.SE:
                //Mover atr�s
                moveRes.set(-cam.getDirection().x
                        * Constants.MOV_SPEED, 0, -cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) <= Constants.CAM_DIST) {
                    game.world.checkCameraCollisionEllipsoid(moveRes,
                            Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                }
                //Mover derecha
                moveRes.set(cam.getDirection());
                moveRes.rotateY(-(float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(-cam.getDirection().x, 0, -cam.getDirection().z);
                moveRotate.rotateY((float) Math.PI / 4);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.E:
                //Mover derecha
                moveRes.set(cam.getDirection());
                moveRes.rotateY(-(float) Math.PI / 2);
                moveRes.set(moveRes.x * Constants.MOV_SPEED, 0,
                        moveRes.z * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                moveRotate.set(cam.getDirection().x, 0, cam.getDirection().z);
                moveRotate.rotateY(-(float) Math.PI / 2);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.N:
                //Mover adelante
                moveRes.set(cam.getDirection().x
                        * Constants.MOV_SPEED, 0, cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) >= Constants.CAM_DIST) {
                    game.cam.moveCamera(moveRes, 1);
                }
                moveRotate.set(cam.getDirection().x, 0, cam.getDirection().z);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
            case Constants.S:
                //Mover atr�s
                moveRes.set(-cam.getDirection().x
                        * Constants.MOV_SPEED, 0, -cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.set(checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION));
                translate(moveRes);
                if (cam.getPosition().distance(getTransformedCenter()) <= Constants.CAM_DIST) {
                    game.world.checkCameraCollisionEllipsoid(moveRes,
                            Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                }
                moveRotate.set(cam.getDirection().x, 0, cam.getDirection().z);
                moveRotate.rotateY((float) Math.PI);
                setOrientation(moveRotate, new SimpleVector(0, 1, 0));
                break;
        }
        //Movimiento de la c�mara
        cam.lookAt(getTransformedCenter());
    }

    public void movePirateGun() {
        game.camSensor.moveCameraGun();
        game.camGun.setPositionToCenter(game.pirate);
    }

    //m�todo de gravedad
    public void gravity() {
        try {
            if (state != Constants.JUMP) {
                g.set(0, 1, 0);
                gravityVector.set(checkForCollisionEllipsoid(g, Constants.ELLIPSOID, Constants.RECURSION));
                translate(gravityVector);
                if (g.equals(gravityVector)) {
                    onGround = false;
                    gravityDamage++;
                    if (gravityDamage >= 20 && gravityDamage < 40) {
                        state = Constants.FALL;
                    } else if (gravityDamage >= 40) {
                        fallToTheAbyss();
                        damage += 20;
                        gravityDamage = 0;
                    }
                } else {
                    if (state == Constants.FALL) {
                        state = Constants.STAND;
                    }
                    onGround = true;
                    gravityDamage = 0;
                }
            }
        } catch (Exception e) {
            Logger.log("Gravity fails!", Logger.MESSAGE);
        }
    }

    //has caido en un abismo y regresas al comienzo del juego
    public void fallToTheAbyss() {
        System.out.println("stage: " + game.stage);
        game.world.setCameraTo(game.cam);
        game.mode = Constants.MODE_NORMAL;
        state = Constants.STAND;
        this.setVisibility(true);
        sword.setVisibility(true);
        this.clearTranslation();
        if (game.stage == Constants.STAGE_3) {
            this.translate(-90.1f, -18.5f, 221.8f);
        } else if (game.stage == Constants.STAGE_6 || game.stage == Constants.STAGE_7) { //se aplica al stage 7 porque hay un peque�o porcentaje que se est� en el stage 7 cuando el pirata caiga
            this.translate(-106.0f, -18.5f, 549.8f);
        }
        game.cam.setPosition(this.getTransformedCenter());
        this.moveCameraBehind();
        game.vibrator.vibrate(300);
    }

    public void damaged(Object3D source) {
        if (state != Constants.DEATH) {
            ind = 0;
            if (source instanceof Knight) {
                damage += 15;
                state = Constants.PAIN_BY_KNIGHT;
            } else if (source instanceof Devil) {
                damage += 15;
                state = Constants.PAIN_BY_DEVIL;
            } else if (source instanceof FlyingFire) {
                damage += 5;
                state = Constants.PAIN;
            } else if (source instanceof Guillotine) {
                damage += 10;
                state = Constants.PAIN;
            } else if (source instanceof Saw) {
                damage += 10;
                state = Constants.PAIN;
            } else if (source instanceof FallingFire) {
                damage += 5;
                state = Constants.PAIN;
            }
            game.vibrator.vibrate(300);
            if (damage >= 250) {
                game.soundManager.playSound(Constants.DEATH);
                game.mode = Constants.MODE_DEATH;
                state = Constants.DEATH;
            } else {
                game.soundManager.playSound(Constants.PAIN);
            }
        }
    }

    public void cameraGravity() {
        game.cam.setPosition(game.cam.getPosition().x, getTransformedCenter().y - 5, game.cam.getPosition().z);
    }

    //Disparar en modo pistola
    public void shoot(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            game.vibrator.vibrate(50);
            game.soundManager.playSound(Constants.SHOOT);
            shootDir.set(Interact2D.reproject2D3DWS(game.camGun, game.fb, (int) event.getX(), (int) event.getY()));
            Object target = game.world.calcMinDistanceAndObject3D(game.camGun.getPosition(), shootDir, 10000)[1];
            //disparar al caballero
            if (target instanceof Knight) {
                ((Knight) target).damaged();
                //disparar al devil
            } else if (target instanceof Devil) {
                ((Devil) target).damaged();
                //disparar al fuego
            } else if (target instanceof FlyingFire) {
                ((FlyingFire) target).setVisibility(false);
                ((FlyingFire) target).a.state = 3;
                //disparar a la cabeza del stage 6
            } else if (target instanceof Head) {
                ((Head) target).stateChange();
            }
        }
    }

    //Activar modo pistola
    public void activateGunMode() {
        game.mode = Constants.MODE_GUN;
        game.camGun.setPositionToCenter(this);
        game.camGun.setOrientation(getRotationMatrix().getZAxis(), new SimpleVector(0, 1, 0));
        game.world.setCameraTo(game.camGun);
        setVisibility(false);
        sword.setVisibility(false);

        game.sensor.registerListener(game.camSensor,
                game.sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        game.sensor.registerListener(game.camSensor,
                game.sensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    //Desactivar modo pistola
    public void deactivateGunMode() {
        game.mode = Constants.MODE_NORMAL;
        game.world.setCameraTo(game.cam);
        setVisibility(true);
        sword.setVisibility(true);

        game.sensor.unregisterListener(game.camSensor);
    }

    //Mover la c�mara detr�s del pirata
    public void moveCameraBehind() {
        SimpleVector m = getRotationMatrix().getZAxis();
        m.rotateY((float) Math.PI);
        game.cam.setPosition(getTransformedCenter().x, game.cam.getPosition().y, getTransformedCenter().z);
        game.world.checkCameraCollisionEllipsoid(m, Constants.CAM_ELLIPSOID, Constants.CAM_DIST, Constants.RECURSION);
        game.cam.lookAt(getTransformedCenter());
    }

    //Atacar si es que toc� a alg�n enemigo
    public void attack(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            attackDir.set(Interact2D.reproject2D3DWS(game.cam, game.fb, (int) event.getX(), (int) event.getY()));
            Object target = game.world.calcMinDistanceAndObject3D(game.cam.getPosition(), attackDir, 10000)[1];
            if (((target instanceof Knight || target instanceof Devil)
                    && state != Constants.ATK1 && state != Constants.PAIN && state != Constants.PAIN_BY_KNIGHT && state != Constants.PAIN_BY_DEVIL)
                    || target instanceof FlyingFire) {
                // Cambia la orientaci�n del pirata
                SimpleVector orientation = ((Object3D) target)
                        .getTransformedCenter().calcSub(getTransformedCenter());
                setOrientation(
                        new SimpleVector(orientation.x, 0, orientation.z),
                        new SimpleVector(0, 1, 0));
                ind = 0;
                state = Constants.ATK1;
                game.soundManager.playSound(Constants.ATK1);
            }
        }
    }

    //Saltar
    public void jump() {
        if (onGround == true) {
            ind = 0;
            state = Constants.JUMP;
        }
    }

    public class PirateThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Pirate pirate;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 100;
        private long beforeTime;
        private int state = 1;

        public PirateThread(Pirate pirate) {
            this.pirate = pirate;
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    pirate.animate();
                }

                this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);
                try {
                    //actual sleep code
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException ex) {
                }
                if (game.stop)
                    state = STOPED;
            }
            if (state == STOPED) {
                System.out.println("finalize thread");
                try {
                    this.finalize();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
