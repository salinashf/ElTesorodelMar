package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

import java.util.Random;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.util.Constants;

public class FlyingFire extends Object3D {

    public SimpleVector dir;
    public SimpleVector movement;
    public FlyingFireThread a;
    int flyingFireHeight;
    int flyingFireRadius;
    int flyingFireSpeed;
    int flyingFireDirection;
    int flyingFireStage;
    float targetDistance;
    float angle;
    int state = 1;
    private Render game;

    public FlyingFire(Render game, SimpleVector dir, int stage) {
        super(Primitives.getPlane(2, 2f));
        a = new FlyingFireThread();
        translate(dir);
        setTexture("fuego1");
        setLighting(LIGHTING_NO_LIGHTS);
        setCollisionMode(COLLISION_CHECK_OTHERS);
        FlyingFireCollision flyingFireCollision = new FlyingFireCollision();
        addCollisionListener(flyingFireCollision);

        Random random = new Random();
        flyingFireHeight = random.nextInt(50);
        flyingFireRadius = random.nextInt(50);
        flyingFireSpeed = random.nextInt(2);
        flyingFireDirection = random.nextInt(2);

        setBillboarding(true);
        setTransparency(20);
        this.game = game;
        this.dir = dir;
        flyingFireStage = stage;
        game.world.addObject(this);
        movement = new SimpleVector();

        strip();
        build();

        a.start();
    }

    public void animate() {
        switch (state) {
            case 1: {
                setTexture("fuego1");
                state++;
                break;
            }
            case 2: {
                setTexture("fuego2");
                state++;
                break;
            }
            case 3: {
                setTexture("fuego3");
                state++;
                break;
            }
            case 4: {
                setTexture("fuego2");
                state++;
                break;
            }
            case 5: {
                setTexture("fuego1");
                state = 2;
                break;
            }
        }
        if (angle < 180) {
            this.clearTranslation();
            this.translate(dir);
            if (flyingFireDirection == 1) {
                movement.set((float) (Math.sin(Math.toRadians(angle)) * flyingFireRadius), (float) (Math.sin(Math.toRadians(angle)) * flyingFireHeight), (float) (Math.cos(Math.toRadians(angle))) * flyingFireRadius);
            } else {
                movement.set((float) (Math.sin(Math.toRadians(angle)) * flyingFireRadius), (float) -(Math.sin(Math.toRadians(angle)) * flyingFireHeight), -(float) (Math.cos(Math.toRadians(angle))) * flyingFireRadius);
            }
            translate(movement);
            movement.set(game.pirate.getTransformedCenter().calcSub(getTransformedCenter()).normalize());
            movement.scalarMul(targetDistance);
            translate(movement);
            targetDistance += 0.05;
            angle += 0.5;
        } else {
            angle = -180;
        }
    }

    public class FlyingFireCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0.getSource() instanceof Pirate) {
                if (game.pirate.state != Constants.ATK1) {
                    if (game.pirate.state != Constants.PAIN) {
                        game.pirate.damaged(FlyingFire.this);
                    }
                }
                FlyingFire.this.setVisibility(false);
                a.state = 3;
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public class FlyingFireThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        public int state = 1;
        private long sleepTime;
        private long initTime;
        private long lifeTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50 * (flyingFireSpeed + 1);
        private long beforeTime;

        @Override
        public synchronized void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                FlyingFire.this.animate();

                this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);
                try {
                    //actual sleep code
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException ex) {
                }

                if (lifeTime != 0 && (this.beforeTime - this.initTime) > lifeTime)
                    state = STOPED;
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
