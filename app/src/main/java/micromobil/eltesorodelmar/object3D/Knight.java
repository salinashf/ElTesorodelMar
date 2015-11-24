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

public class Knight extends Object3D {

    public int state;
    public float ind;
    public int count = 0;
    public KnightThread a;
    public GravityThread gravity;
    int damage;
    SimpleVector moveKnight;
    private Render game;

    public Knight(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.knightser)));
        a = new KnightThread();
        gravity = new GravityThread(game, this);
        setTexture("knight_text");
        setCulling(true);
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS | Object3D.COLLISION_CHECK_SELF);
        KnightCollision knightCollision = new KnightCollision();
        addCollisionListener(knightCollision);
        game.world.addObject(this);
        this.game = game;
        this.damage = Constants.KNIGHT_DAMAGE;
        moveKnight = new SimpleVector();

        strip();
        build();

        a.start();
        gravity.start();
        state = 0;
        state = Constants.STAND;
    }

    public void animate() {
        switch (state) {
            case Constants.STAND: {
                ind += 0.025f;
                if (ind > 1) {
                    ind = 0;
                }
                animate(ind, 1);
                break;
            }
            case Constants.WALK: {
                ind += 0.06;
                if (ind > 1) {
                    ind = 0;
                }
                animate(ind, 2);
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
                animate(ind, 3);
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
                animate(ind, 4);
                break;
            }
            case Constants.DEATH: {
                setCollisionMode(Object3D.COLLISION_CHECK_NONE);
                gravity.state = 3;
                ind += 0.0166666f;
                if (ind > 0.8) {
                    System.out.println("death");
                    ind = 0.8f;
                    gravity.state = 3;
                    a.state = 3;
                    game.mode = Constants.MODE_CINEMATICS;
                    game.pirate.state = Constants.STAND;
                    Camera sCam = new Camera();
                    sCam.setPositionToCenter(game.pirate);
                    SimpleVector sv = game.pirate.getRotationMatrix().getZAxis();
                    sv.rotateY((float) Math.PI / 2);
                    sCam.moveCamera(sv, 5);
                    sCam.lookAt(Knight.this.getTransformedCenter());
                    game.world.setCameraTo(sCam);
                }
                animate(ind, 5);
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
            Logger.log("Knight gravity fails!", Logger.MESSAGE);
        }
    }

    public void moveKnight() {
        if (game.pirate.state == Constants.DEATH) {
            state = Constants.STAND;
        } else if (game.pirate.state == Constants.PAIN_BY_KNIGHT) {
            SimpleVector lookToPirate = game.pirate.getTransformedCenter().calcSub(getTransformedCenter());
            lookToPirate.set(lookToPirate.x, 0, lookToPirate.z);
            setOrientation(lookToPirate, new SimpleVector(0, 1, 0));
            moveKnight.set(lookToPirate.normalize().x * Constants.MOV_SPEED_KNIGHT, 0, lookToPirate.normalize().z * Constants.MOV_SPEED_KNIGHT);
            moveKnight.rotateY((float) Math.PI);
            moveKnight = checkForCollisionEllipsoid(moveKnight, Constants.KNIGHT_ELLIPSOID, Constants.RECURSION);
            translate(moveKnight);
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
                    moveKnight.set(lookToPirate.normalize().x * Constants.MOV_SPEED_KNIGHT, 0, lookToPirate.normalize().z * Constants.MOV_SPEED_KNIGHT);
                    moveKnight = checkForCollisionEllipsoid(moveKnight, Constants.KNIGHT_ELLIPSOID, Constants.RECURSION);
                    translate(moveKnight.x, 0, moveKnight.z);
                    state = Constants.WALK;
                }
            }
        }
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

    public class KnightCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                Object3D obj = arg0.getSource();
                if (obj == game.pirate && game.pirate.state != Constants.DEATH && game.pirate.state != Constants.PAIN && game.pirate.state != Constants.PAIN_BY_KNIGHT) {
                    //pirata golpea al caballero
                    if (game.pirate.state == Constants.ATK1 && (state != Constants.ATK1 || (state == Constants.ATK1 && (ind < 0.3 || ind > 0.6)))) {
                        damaged();
                        //caballero golpea al pirata
                    } else {
                        state = Constants.WALK;
                        game.pirate.damaged(Knight.this);
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

    public class KnightThread extends Thread {
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
                synchronized (game) {
                    animate();
                    moveKnight();
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
