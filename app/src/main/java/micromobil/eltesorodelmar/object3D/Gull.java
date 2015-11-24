package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.Camera;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Gull extends Object3D {
    float ind;
    GullThread a;
    private Render game;

    public Gull(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.gaviotaser)));
        a = new GullThread(this);
        setTexture("hawkt2");
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        setEllipsoidMode(Object3D.ELLIPSOID_TRANSFORMED);
        GullCollision gullCollision = new GullCollision();
        addCollisionListener(gullCollision);
        game.world.addObject(this);
        this.game = game;

        strip();
        build();

        a.start();
    }

    public void animate() {
        ind += 0.0166666f;
        if (ind > 1) {
            ind = 0;
        }
        animate(ind, 1);
    }

    public class GullCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            game.mode = Constants.MODE_CINEMATICS;
            game.pirate.state = Constants.STAND;
            Camera sCam = new Camera();
            sCam.setPositionToCenter(game.pirate);
            SimpleVector sv = game.pirate.getRotationMatrix().getZAxis();
            sv.rotateY((float) Math.PI / 2);
            sCam.moveCamera(sv, 5);
            sCam.lookAt(Gull.this.getTransformedCenter());
            game.world.setCameraTo(sCam);
            Gull.this.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
            Gull.this.align(game.pirate);
            Gull.this.rotateY((float) Math.PI);
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public class GullThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Gull gull;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50;
        private long beforeTime;
        private int state = 1;

        public GullThread(Gull gull) {
            this.gull = gull;
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    gull.animate();
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
