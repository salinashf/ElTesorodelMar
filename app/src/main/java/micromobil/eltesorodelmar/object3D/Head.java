package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;

public class Head extends Object3D {

    public static final int OFF = 0;
    public static final int ON = 1;
    public HeadThread a;
    int state = OFF;
    Object3D target;
    Render game;
    SimpleVector headOrientation;

    public Head(Render game, Object3D target) {
        super(Loader.loadMD2(game.getResources().openRawResource(R.raw.head), 0.3f));
        setTexture("head_text");
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        this.target = target;
        this.game = game;
        strip();
        build();
        headOrientation = new SimpleVector();
        a = new HeadThread();
    }

    public Head(Render game, Object3D target, Head clone) {
        super(clone, true);
        setTexture("head_text");
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        this.target = target;

        strip();
        build();
    }

    public void stateChange() {
        System.out.println("stateChange");
        if (state == OFF) {
            state = ON;
            if (target instanceof Bridge) {
                ((Bridge) target).activate();
            } else if (target instanceof Door) {
                ((Door) target).activate();
                ((Door) target).setDoorCam(1);
            }
            setTexture("head_text2");
        }
        try {
            if (!a.isAlive()) {
                a.start();
            }
        } catch (Exception e) {
        }
    }

    public void animate() {
        headOrientation.set(getTransformedCenter().calcSub(game.pirate.getTransformedCenter()));
        headOrientation.rotateY(-(float) Math.PI / 2);
        this.setOrientation(new SimpleVector(headOrientation.x, 0, headOrientation.z), new SimpleVector(0, 1, 0));
    }

    public class HeadThread extends Thread {
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
