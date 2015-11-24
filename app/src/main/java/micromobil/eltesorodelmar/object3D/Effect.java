package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.util.Constants;

public class Effect extends Object3D {
    public SimpleVector dir;
    public AnimateEffect a;
    public SimpleVector v;
    int state = 1;
    int count;
    int type;
    float angle;
    private Render game;

    public Effect(Render game, SimpleVector dir, int type) {
        super(Primitives.getPlane(2, 2f));
        a = new AnimateEffect();
        if (type == Constants.FIRE) {
            translate(dir);
            setTexture("fuego1");
            setCollisionMode(COLLISION_CHECK_OTHERS | COLLISION_CHECK_SELF);
            FireCollision fireCollision = new FireCollision();
            addCollisionListener(fireCollision);
        } else if (type == Constants.DUST) {
            translate(dir);
            setTexture("polvo1");
        } else if (type == Constants.DUSTR) {
            translate(dir);
            setTexture("polvor1");
        } else if (type == Constants.PARTICLES) {
            this.angle = (int) (Math.random() * 180);
            a.delay = 500;
            a.lifeTime = 20000000000L;
            scale(0.2f);
            v = getTransformedCenter();
            translate(dir);
            setTexture("luces5");
            setAdditionalColor(new RGBColor(1, 1, 1));
            setTransparency(TRANSPARENCY_MODE_ADD);
            //setLighting(Object3D.LIGHTING_NO_LIGHTS);
        }
        setLighting(LIGHTING_NO_LIGHTS);
        setBillboarding(true);
        setTransparency(20);
        this.type = type;
        this.game = game;
        this.dir = dir;
        game.world.addObject(this);

        strip();
        build();

        a.start();
    }

    public void animate() {
        if (type == Constants.FIRE) {
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
        } else if (type == Constants.DUST) {
            switch (state) {
                case 1: {
                    setTexture("polvo1");
                    state++;
                    break;
                }
                case 2: {
                    setTexture("polvo2");
                    state++;
                    break;
                }
                case 3: {
                    setTexture("polvo3");
                    state++;
                    break;
                }
                case 4: {
                    setTexture("polvo4");
                    state++;
                    break;
                }
                case 5: {
                    setVisibility(false);
                    a.state = 3;
                    break;
                }
            }
        } else if (type == Constants.DUSTR) {
            switch (state) {
                case 1: {
                    setTexture("polvor1");
                    state++;
                    break;
                }
                case 2: {
                    setTexture("polvor2");
                    state++;
                    break;
                }
                case 3: {
                    setTexture("polvor3");
                    state++;
                    break;
                }
                case 4: {
                    setTexture("polvor4");
                    state++;
                    break;
                }
                case 5: {
                    setVisibility(false);
                    a.state = 3;
                    break;
                }
            }
        } else if (type == Constants.PARTICLES) {
            if (angle < 180) {
                SimpleVector v = new SimpleVector((float) 2 * (Math.sin(angle)), -Math.random() / 5, (float) 2 * (Math.cos(angle)));
                translate(v);
                angle++;
            } else {
                angle = -180;
            }
            int i = (int) (Math.random() * 3);
            if (i == 0) {
                setTexture("luces4");
            } else if (i == 1) {
                setTexture("luces3");
            } else if (i == 2) {
                setTexture("luces5");
            }
        }
    }

    public class FireCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0.getSource() == game.pirate) {
                if (game.pirate.state != Constants.PAIN)
                    game.pirate.damaged(Effect.this);
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

    public class AnimateEffect extends Thread {
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

        public AnimateEffect() {
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
                    Effect.this.setVisibility(false);
                    this.finalize();
                    //game.world.removeObject(Effect.this);
                    Thread.sleep(100);
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
