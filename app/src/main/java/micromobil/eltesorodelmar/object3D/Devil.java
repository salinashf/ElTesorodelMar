package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.Camera;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Devil extends Object3D {

    public DevilThread a;
    public DevilThreadMode a2;
    public GravityThread gravity;
    public int mode = 2; //0: normal, 1: estatua lluvia de fuego, 2: estatua
    int state;
    float ind;
    int count = 0;
    int damage;
    SimpleVector moveDevil;
    private Render game;

    public Devil(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.devilser)));
        a = new DevilThread();
        a2 = new DevilThreadMode();
        gravity = new GravityThread(game, this);
        setTexture("devilt");
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS | Object3D.COLLISION_CHECK_SELF);
        DevilCollision devilCollision = new DevilCollision();
        addCollisionListener(devilCollision);
        this.game = game;
        state = Constants.STAND;
        moveDevil = new SimpleVector();
        this.damage = Constants.DEVIL_DAMAGE;

        build();
        strip();

        gravity.start();
    }

    public void animate() {
        switch (state) {
            case Constants.STAND: {
                ind += 0.025f;
                if (ind > 1) {
                    ind = 0;
                }
                animate(ind, 5);
                break;
            }
            case Constants.WALK: {
                ind += 0.06;
                if (ind > 1) {
                    ind = 0;
                }
                animate(ind, 1);
                break;
            }
            case Constants.ATK1: {
                ind += 0.0625f;
                if (ind > 1) {
                    state = Constants.STAND;
                    ind = 0;
                }
                SimpleVector attack = this.getRotationMatrix().getZAxis();
                attack.scalarMul(0.25f);
                attack = checkForCollisionEllipsoid(attack, Constants.ELLIPSOID, Constants.RECURSION);
                this.translate(attack.x, 0, attack.z);
                animate(ind, 2);
                break;
            }
            case Constants.PAIN: {
                ind += 0.04166666f;
                if (ind > 0.25) {
                    state = Constants.STAND;
                    ind = 0;
                }
                SimpleVector lookToSource = game.pirate.getTransformedCenter().calcSub(getTransformedCenter()).normalize();
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                setOrientation(lookToSource, new SimpleVector(0, 1, 0));
                lookToSource.rotateY((float) Math.PI);
                lookToSource.scalarMul(0.5f);
                lookToSource.set(checkForCollisionEllipsoid(lookToSource, Constants.ELLIPSOID, Constants.RECURSION));
                lookToSource.set(lookToSource.x, 0, lookToSource.z);
                this.translate(lookToSource);
                animate(ind, 3);
                break;
            }
            case Constants.DEATH: {
                setCollisionMode(Object3D.COLLISION_CHECK_NONE);
                gravity.state = 3;
                ind += 0.0166666f;
                if (ind > 0.8) {
                    System.out.println("death");
                    ind = 0.8f;
                    a.state = 3;
                    a2.state = 3;
                    game.mode = Constants.MODE_CINEMATICS;
                    game.pirate.state = Constants.STAND;
                    Camera sCam = new Camera();
                    sCam.setPositionToCenter(game.pirate);
                    SimpleVector sv = game.pirate.getRotationMatrix().getZAxis();
                    sv.rotateY((float) Math.PI / 2);
                    sCam.moveCamera(sv, 5);
                    sCam.lookAt(getTransformedCenter());
                    game.world.setCameraTo(sCam);
                }
                animate(ind, 4);
                break;
            }
        }
    }

    //m�todo de gravedad
    public void gravity() {
        try {
            SimpleVector gravityVector = checkForCollisionEllipsoid(new SimpleVector(0, 1, 0), Constants.KNIGHT_ELLIPSOID, Constants.RECURSION);
            translate(gravityVector);
        } catch (Exception e) {
            Logger.log("Devil gravity fails!", Logger.MESSAGE);
        }
    }

    public void moveDevil() {
        if (game.pirate.state == Constants.DEATH) {
            state = Constants.STAND;
        } else if (game.pirate.state == Constants.PAIN_BY_DEVIL) {
            SimpleVector lookToPirate = game.pirate.getTransformedCenter().calcSub(getTransformedCenter());
            lookToPirate.set(lookToPirate.x, 0, lookToPirate.z);
            setOrientation(lookToPirate, new SimpleVector(0, 1, 0));
            moveDevil.set(lookToPirate.normalize().x * Constants.MOV_SPEED_KNIGHT, 0, lookToPirate.normalize().z * Constants.MOV_SPEED_KNIGHT);
            moveDevil.rotateY((float) Math.PI);
            moveDevil = checkForCollisionEllipsoid(moveDevil, Constants.KNIGHT_ELLIPSOID, Constants.RECURSION);
            translate(moveDevil.x, 0, moveDevil.z);
            state = Constants.WALK;
        } else {
            if (state == Constants.STAND || state == Constants.WALK) {
                SimpleVector lookToPirate = game.pirate.getTransformedCenter().calcSub(getTransformedCenter());
                lookToPirate.set(lookToPirate.x, 0, lookToPirate.z);
                setOrientation(lookToPirate, new SimpleVector(0, 1, 0));
                if (lookToPirate.length() < Constants.ENEMY_DIST_MAX) {
                    //Cerca del pirata, iniciar movimientos de combate
                    ind = 0;
                    state = Constants.ATK1;
                } else {
                    //Lejos del pirata, as� que se acerca
                    moveDevil.set(lookToPirate.normalize().x * Constants.MOV_SPEED_KNIGHT, 0, lookToPirate.normalize().z * Constants.MOV_SPEED_KNIGHT);
                    moveDevil = checkForCollisionEllipsoid(moveDevil, Constants.KNIGHT_ELLIPSOID, Constants.RECURSION);
                    translate(moveDevil.x, 0, moveDevil.z);
                    state = Constants.WALK;
                }
            }
        }
    }

    public void moveDevil2() {
        SimpleVector dir = game.pirate.getTransformedCenter();
        dir.y -= 8;
        FallingFire fire = new FallingFire(game, dir);
        fire.gravity.start();
    }

    public void damaged() {
        ind = 0;
        state = Constants.PAIN;
        if (damage >= 1) {
            damage--;
        } else {
            state = Constants.DEATH;
        }
    }

    public class DevilCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                Object3D obj = arg0.getSource();
                if (obj == game.pirate) {
                    if (mode == 2) {
                        game.mode = Constants.MODE_CINEMATICS;
                    } else if (game.pirate.state != Constants.DEATH && game.pirate.state != Constants.PAIN_BY_DEVIL && game.pirate.state != Constants.PAIN) {
                        if (mode == 0) {
                            //pirata golpea al caballero
                            if (game.pirate.state == Constants.ATK1 && (state != Constants.ATK1 || (state == Constants.ATK1 && (ind < 0.3 || ind > 0.6)))) {
                                damaged();
                                //caballero golpea al pirata
                            } else {
                                state = Constants.WALK;
                                game.pirate.damaged(Devil.this);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public class DevilThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        public int state = 1;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 100;
        private long beforeTime;

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (Devil.this) {
                    animate();
                    if (mode == 0) {
                        moveDevil();
                    } else if (mode == 1) {
                        moveDevil2();
                    }
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

    public class DevilThreadMode extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        public int state = 1;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 20000;
        private long beforeTime;

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (Devil.this) {
                    if (game.pirate.state != Constants.DEATH && Devil.this.state != Constants.DEATH) {
                        if (mode == 0) {
                            mode = 1;
                            Devil.this.state = Constants.STAND;
                            a.delay = 2000;
                        } else if (mode == 1) {
                            mode = 0;
                            a.delay = 100;
                        }
                    }
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
