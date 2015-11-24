package micromobil.eltesorodelmar.object3D;


import com.threed.jpct.Camera;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Bridge extends Object3D {

    Render game;
    BridgeThread a;
    int state;
    Camera bridgeCam;

    public Bridge(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.bridgeser)));
        a = new BridgeThread();
        if (game.stage6Step < 2) { //nunca se ha levantado el puente antes
            translate(0, 50, 0);
        }
        state = Constants.DOOR_STAND;
        setCulling(false);
        setCollisionMode(COLLISION_CHECK_OTHERS);
        this.game = game;

        strip();
        build();
    }

    public void activate() {
        if (game.stage6Step < 2) {
            game.stage6Step++;
            state = Constants.DOOR_OPEN;
            setBridgeCam();
            if (!a.isAlive()) {
                a.start();
            }
        }
    }

    public void setBridgeCam() {
        bridgeCam = new Camera();
        bridgeCam.setPositionToCenter(this);
        game.world.setCameraTo(bridgeCam);
        game.world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEUP, Constants.CAM_ELLIPSOID, 60, Constants.RECURSION);
        game.world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEOUT, Constants.CAM_ELLIPSOID, 10, Constants.RECURSION);
        bridgeCam.lookAt(this.getTransformedCenter());
    }

    public class BridgeThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        int counter;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50;
        private long beforeTime;
        private int state = 1;

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    if (Bridge.this.state == Constants.DOOR_OPEN) {
                        if (counter < 50) {
                            Bridge.this.translate(0, -1, 0);
                        } else {
                            Bridge.this.state = Constants.DOOR_STAND2;
                            state = STOPED;
                        }
                        counter++;
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
                if (counter >= 50) {
                    if (game.mode == Constants.MODE_NORMAL) {
                        game.world.setCameraTo(game.cam);
                    } else if (game.mode == Constants.MODE_GUN) {
                        game.world.setCameraTo(game.camGun);
                    }
                    state = STOPED;
                }
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
