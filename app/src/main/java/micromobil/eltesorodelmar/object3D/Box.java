package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Box extends Object3D {
    int state;
    float ind;
    int count;
    int face;
    BoxThread b;
    private Render game;

    public Box(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.boxser)));
        setTexture("crate_bm.bmp");
        System.out.println("caja");
        BoxCollision boxCollision = new BoxCollision();
        setCollisionMode(COLLISION_CHECK_OTHERS | COLLISION_CHECK_SELF);
        addCollisionListener(boxCollision);
        this.game = game;

        strip();
        build();
    }

    public Box(Render game, boolean aux) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.cajaser)));
        b = new BoxThread(this);
        state = Constants.BOX_MOVE;
        setTexture("crate_bm.bmp");
        System.out.println("caja2");
        BoxCollision2 boxCollision = new BoxCollision2();
        setCollisionMode(COLLISION_CHECK_OTHERS);
        addCollisionListener(boxCollision);
        this.game = game;
        b.start();
    }

    public void animate() {
        switch (state) {
            case Constants.BOX_MOVE: {
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

    public void pushBox(int dir) {

        SimpleVector moveRes = new SimpleVector(0, 0, 0);

        switch (dir) {
            case Constants.W:
                //Mover izquierda
                moveRes = new SimpleVector(game.cam.getDirection().x
                        * Constants.MOV_SPEED, 0, game.cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.rotateY((float) Math.PI / 2);
                moveRes = game.pirate.checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION);
                game.pirate.translate(moveRes);
                game.world.checkCameraCollisionEllipsoid(moveRes, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                SimpleVector oriW = new SimpleVector(game.cam.getDirection().x * Constants.MOV_SPEED, 0, game.cam.getDirection().z * Constants.MOV_SPEED);
                oriW.rotateY((float) Math.PI / 2);
                game.pirate.setOrientation(oriW, new SimpleVector(0, 1, 0));
                break;
            case Constants.E:
                //Mover derecha
                moveRes = new SimpleVector(game.cam.getDirection().x
                        * Constants.MOV_SPEED, 0, game.cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes.rotateY(-(float) Math.PI / 2);
                moveRes = game.pirate.checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION);
                game.pirate.translate(moveRes);
                game.world.checkCameraCollisionEllipsoid(moveRes, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                SimpleVector oriE = new SimpleVector(game.cam.getDirection().x * Constants.MOV_SPEED, 0, game.cam.getDirection().z * Constants.MOV_SPEED);
                oriE.rotateY(-(float) Math.PI / 2);
                game.pirate.setOrientation(oriE, new SimpleVector(0, 1, 0));
                break;
            case Constants.N:
                //Mover adelante
                moveRes = new SimpleVector(game.cam.getDirection().x
                        * Constants.MOV_SPEED, 0, game.cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes = game.pirate.checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION);
                game.pirate.translate(moveRes);
                game.world.checkCameraCollisionEllipsoid(moveRes, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                SimpleVector oriN = new SimpleVector(game.cam.getDirection().x * Constants.MOV_SPEED, 0, game.cam.getDirection().z * Constants.MOV_SPEED);
                game.pirate.setOrientation(oriN, new SimpleVector(0, 1, 0));
                break;
            case Constants.S:
                //Mover atr�s
                moveRes = new SimpleVector(-game.cam.getDirection().x
                        * Constants.MOV_SPEED, 0, -game.cam.getDirection().z
                        * Constants.MOV_SPEED);
                moveRes = game.pirate.checkForCollisionEllipsoid(moveRes, Constants.ELLIPSOID, Constants.RECURSION);
                game.pirate.translate(moveRes);
                game.world.checkCameraCollisionEllipsoid(moveRes, Constants.CAM_ELLIPSOID, 1, Constants.RECURSION);
                SimpleVector oriS = new SimpleVector(-game.cam.getDirection().x * Constants.MOV_SPEED, 0, -game.cam.getDirection().z * Constants.MOV_SPEED);
                game.pirate.setOrientation(oriS, new SimpleVector(0, 1, 0));
                break;
        }
        //Movimiento de la c�mara
        //game.cam.lookAt(getTransformedCenter());
    }

    public class BoxCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                if (arg0.getSource() == game.pirate) {
                    game.pirate.state = Constants.PUSH;
                    game.pirate.box = Box.this;
                    //Mover caja
                    boolean[] poligons = new boolean[8];
                    int[] poligonsID = arg0.getPolygonIDs();
                    for (int i = 0; i < poligonsID.length; i++) {
                        switch (poligonsID[i]) {
                            case 0:
                                poligons[0] = true;
                                break;
                            case 1:
                                poligons[1] = true;
                                break;
                            case 2:
                                poligons[2] = true;
                                break;
                            case 3:
                                poligons[3] = true;
                                break;
                            case 6:
                                poligons[4] = true;
                                break;
                            case 7:
                                poligons[5] = true;
                                break;
                            case 10:
                                poligons[6] = true;
                                break;
                            case 11:
                                poligons[7] = true;
                                break;
                        }
                    }
                    SimpleVector moveRes = new SimpleVector();
                    //South
                    if ((poligons[0] == true || poligons[1] == true) && poligons[2] == false && poligons[3] == false &&
                            poligons[4] == false && poligons[5] == false && poligons[6] == false && poligons[7] == false) {
                        SimpleVector s = new SimpleVector(0, 0, 0.5);
                        moveRes = game.pirate.box.checkForCollisionEllipsoid(s, Constants.BOX_ELLIPSOID, Constants.RECURSION);
                        translate(moveRes);
                    }
                    //East
                    if ((poligons[2] == true || poligons[3] == true) && poligons[0] == false && poligons[1] == false &&
                            poligons[4] == false && poligons[5] == false && poligons[6] == false && poligons[7] == false) {
                        SimpleVector e = new SimpleVector(0, 0, 0.5);
                        e.rotateY((float) Math.PI / 2);
                        moveRes = game.pirate.box.checkForCollisionEllipsoid(e, Constants.BOX_ELLIPSOID, Constants.RECURSION);
                        translate(moveRes);
                    }
                    //West
                    if ((poligons[4] == true || poligons[5] == true) && poligons[0] == false && poligons[1] == false &&
                            poligons[2] == false && poligons[3] == false && poligons[6] == false && poligons[7] == false) {
                        SimpleVector w = new SimpleVector(0, 0, 0.5);
                        w.rotateY(-(float) Math.PI / 2);
                        moveRes = game.pirate.box.checkForCollisionEllipsoid(w, Constants.BOX_ELLIPSOID, Constants.RECURSION);
                        translate(moveRes);
                    }
                    if ((poligons[6] == true || poligons[7] == true) && poligons[0] == false && poligons[1] == false &&
                            poligons[2] == false && poligons[3] == false && poligons[4] == false && poligons[5] == false) {
                        SimpleVector n = new SimpleVector(0, 0, 0.5);
                        n.rotateY((float) Math.PI);
                        moveRes = game.pirate.box.checkForCollisionEllipsoid(n, Constants.BOX_ELLIPSOID, Constants.RECURSION);
                        translate(moveRes);
                    }
                }

            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            return true;
        }
    }

    public class BoxCollision2 implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            System.out.println("colisi�n");
            if (arg0.getSource() == game.pirate) {
                if (game.pirate.state != Constants.PAIN && game.pirate.state != Constants.DEATH) {
                    game.pirate.damaged(Box.this);
                }
            }
        }


        @Override
        public boolean requiresPolygonIDs() {
            return false;
        }
    }

    public class BoxThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Box box;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50;
        private long beforeTime;
        private int state = 1;

        public BoxThread(Box box) {
            this.box = box;
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    box.animate();
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
