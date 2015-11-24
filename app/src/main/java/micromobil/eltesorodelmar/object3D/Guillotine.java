package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Guillotine extends Object3D {
    int state;
    float ind;
    GuillotineThread a;
    private Render game;

    public Guillotine(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.guillotina)));
        a = new GuillotineThread(this);
        state = Constants.GUILLOTINE_MOVE;
        setTexture("metal");
        setCulling(false);
        GuillotineCollision guillotineCollision = new GuillotineCollision();
        setCollisionMode(COLLISION_CHECK_OTHERS);
        addCollisionListener(guillotineCollision);
        this.game = game;

        strip();
        build();

        a.start();
    }

    public void animate() {
        switch (state) {
            case Constants.GUILLOTINE_MOVE: {
                ind += 0.009f;
                if (ind > 1) {
                    ind = 0f;
                } else {
                    animate(ind, 1);
                }
                break;
            }
        }
    }

    public class GuillotineCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                if (arg0.getSource() == null) {
                    setCollisionMode(COLLISION_CHECK_NONE);
                } else {
                    setCollisionMode(COLLISION_CHECK_OTHERS);
                }
                if (game.pirate.state != Constants.PAIN)
                    game.pirate.damaged(Guillotine.this);
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return true;
        }
    }

    public class GuillotineThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Guillotine s;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50;
        private long beforeTime;
        private int state = 1;

        public GuillotineThread(Guillotine s) {
            this.s = s;
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    s.animate();
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
