package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.util.Constants;

public class FallingFire extends Object3D {
    int state = 1;
    int count;
    SimpleVector dir;
    FallingFireThread a;
    GravityThread gravity;
    SimpleVector v;
    float angle;
    private Render game;

    public FallingFire(Render game, SimpleVector dir) {
        super(Primitives.getPlane(2, 2f));
        a = new FallingFireThread();
        translate(dir);
        setTexture("fuego1");
        setCollisionMode(COLLISION_CHECK_OTHERS | COLLISION_CHECK_SELF);
        FallingFireCollision fireCollision = new FallingFireCollision();
        addCollisionListener(fireCollision);
        gravity = new GravityThread(game, this);
        gravity.delay = 300;
        setLighting(LIGHTING_NO_LIGHTS);
        setBillboarding(true);
        setTransparency(20);
        this.game = game;
        this.dir = dir;
        game.world.addObject(this);

        strip();
        build();

        a.lifeTime = 5000000000L;
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
    }

    //m�todo de gravedad
    public void gravity() {
        try {
            translate(new SimpleVector(0, 1, 0));
        } catch (Exception e) {
        }
    }

    public class FallingFireCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            System.out.println("collision fire");
            if (arg0.getSource() == game.pirate) {
                if (game.pirate.state != Constants.PAIN && game.pirate.state != Constants.PAIN_BY_DEVIL)
                    game.pirate.damaged(FallingFire.this);
            }
            setVisibility(false);
            a.state = 3;
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public class FallingFireThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        public int state = 1;
        private long sleepTime;
        private long initTime;
        private long lifeTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 100;
        private long beforeTime;

        public FallingFireThread() {
            this.initTime = System.nanoTime();
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    try {
                        animate();
                    } catch (Exception e) {
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
                if (lifeTime != 0 && (this.beforeTime - this.initTime) > lifeTime)
                    state = STOPED;
                if (game.stop)
                    state = STOPED;
            }
            if (state == STOPED) {
                System.out.println("finalize thread");
                try {
                    FallingFire.this.setVisibility(false);
                    gravity.state = 3;
                    this.finalize();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
