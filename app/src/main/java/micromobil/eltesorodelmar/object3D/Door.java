package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.Camera;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Door extends Object3D {
    /**
     *
     */
    private static final long serialVersionUID = 5603794635085975599L;
    public int state;
    public float ind;
    public int count;
    DoorThread a;
    Camera doorCam;
    int type; //type 0: se puede abrir, type 1: no se puede abrir
    private Render game;

    public Door(Render game, int type) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.doorser)));
        game.soundManager.addSound(Constants.DOOR_OPEN, R.raw.sdoor);
        rotateY((float) Math.PI);
        translate(-2f, 0, 0);
        DoorCollision doorCollision = new DoorCollision();
        addCollisionListener(doorCollision);
        state = Constants.DOOR_STAND;
        setTexture("sdoor_pn.PNG");
        setCollisionMode(COLLISION_CHECK_OTHERS);
        this.game = game;
        this.type = type;

        strip();
        build();
    }


    public void animate() {
        switch (state) {
            case Constants.DOOR_STAND: {
                animate(0, 1);
                break;
            }
            case Constants.DOOR_OPEN: {
                ind += 0.016f;
                if (ind > 0.8) {
                    ind = 0;
                    state = Constants.DOOR_STAND2;
                    a.state = DoorThread.STOPED;
                    if (type == 1) {
                        if (game.mode == Constants.MODE_NORMAL) {
                            game.world.setCameraTo(game.cam);
                        } else if (game.mode == Constants.MODE_GUN) {
                            game.world.setCameraTo(game.camGun);
                        }
                    }
                } else {
                    animate(ind, 1);
                }
                break;
            }
            case Constants.DOOR_CLOSE: {
                ind += 0.016f;
                if (ind > 0.8) {
                    ind = 0;
                    state = Constants.DOOR_STAND;
                    a.state = DoorThread.STOPED;
                    if (type == 1) {
                        if (game.mode == Constants.MODE_NORMAL) {
                            game.world.setCameraTo(game.cam);
                        } else if (game.mode == Constants.MODE_GUN) {
                            game.world.setCameraTo(game.camGun);
                        }
                    }
                } else {
                    animate(ind, 2);
                }
                break;
            }
            case Constants.DOOR_STAND2: {
                animate(0, 2);
                break;
            }
        }
    }

    public void setDoorCam(int type) { //0: OUT, 1: IN
        doorCam = new Camera();
        doorCam.setPositionToCenter(this);
        game.world.setCameraTo(doorCam);
        if (type == 0) {
            game.world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEOUT, Constants.CAM_ELLIPSOID, 30, Constants.RECURSION);
        } else {
            game.world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEIN, Constants.CAM_ELLIPSOID, 30, Constants.RECURSION);
        }
        doorCam.moveCamera(Camera.CAMERA_MOVEUP, 5);
        doorCam.lookAt(this.getTransformedCenter());
    }

    public void activate() {
        game.soundManager.playSound(Constants.DOOR_OPEN);
        state = Constants.DOOR_OPEN;
        a = new DoorThread(this);
        a.start();
    }

    public void deactivate() {
        game.soundManager.playSound(Constants.DOOR_OPEN);
        state = Constants.DOOR_CLOSE;
        a = new DoorThread(this);
        a.start();
    }

    public class DoorThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Door door;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 100;
        private long beforeTime;
        private int state = 1;

        public DoorThread(Door door) {
            this.door = door;
        }

        @Override
        public void run() {
            System.out.println("run");
            while (state == RUNNING || state == PAUSED) {
                if (state == RUNNING) {
                    beforeTime = System.nanoTime();
                    synchronized (game) {
                        door.animate();
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

    public class DoorCollision implements CollisionListener {
        /**
         *
         */
        private static final long serialVersionUID = 6525164861184209803L;

        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null && arg0.getSource() == game.pirate) {
                if (type == 1) {
                    if (game.stage == Constants.STAGE_1 && game.stage1Step == 1 && Door.this.state == Constants.DOOR_STAND) {
                        game.mode = Constants.MODE_CINEMATICS;
                        game.pirate.state = Constants.STAND;
                    }
                } else if (type == 0 && Door.this.state == Constants.DOOR_STAND) {
                    if (count <= 0) {
                        count = Constants.COUNT;
                    }
                }
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            return false;
        }
    }
}
