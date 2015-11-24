package micromobil.eltesorodelmar.object3D;


import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Switch extends Object3D {
    public int state;
    public float ind;
    public int count;
    public SwitchThread a;
    public Object3D target;
    public boolean flag;
    public int countTime = 200;
    public Object3D sw;
    private Render game;

    public Switch(Render game, Object3D target) {
        super(Primitives.getCube(2f));
        sw = Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.switchser));
        sw.rotateY((float) Math.PI / 4);
        rotateY(-(float) Math.PI / 4);
        setLighting(LIGHTING_NO_LIGHTS);
        translate(0, -1, 0);
        state = Constants.DOOR_STAND;
        setTexture("black");
        setTransparency(20);
        sw.setTexture("switcht");
        SwitchCollision switchCollision = new SwitchCollision();
        setCollisionMode(COLLISION_CHECK_OTHERS);
        addCollisionListener(switchCollision);
        addChild(sw);
        this.game = game;
        this.target = target;

        strip();
        build();

    }

    public void animate() {
        switch (state) {
            case Constants.DOOR_STAND: {
                sw.animate(0, 1);
                break;
            }
            case Constants.DOOR_OPEN: {
                ind += 0.016f;
                if (ind > 0.8) {
                    ind = 0;
                    state = Constants.DOOR_STAND2;
                    a.state = SwitchThread.STOPED;
                    //activa animaci�n de objetivo
                    if (target != null && target instanceof Door) {
                        ((Door) target).activate();
                        if (!((Door) target).a.isAlive())
                            ((Door) target).a.start(); //anima la puerta
                        ((Door) target).setDoorCam(0); //ubica y cambia la c�mara del mundo a la c�mara de la puerta
                    }
                    if (game.stage == 1) {
                        game.stage1Step = 3;
                    }
                } else {
                    sw.animate(ind, 1);
                }
                break;
            }
            case Constants.DOOR_STAND2: {
                sw.animate(0.8f, 1);
                break;
            }
        }
    }

    public void activate() {
        state = Constants.DOOR_OPEN;
        a = new SwitchThread(this);
        a.start();
    }

    public class SwitchCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            System.out.println("colision");
            if (arg0 != null && arg0.getSource() == game.pirate) {
                if (((Door) target).state != Constants.DOOR_STAND2) { //s�lo se puede activar el switch cuando la puerta no esta abierta
                    if (game.stage == Constants.STAGE_1) {
                        if (game.stage1Step > 0 && Switch.this.state == Constants.DOOR_STAND) {
                            if (count <= 0) {
                                count = Constants.COUNT;
                            }
                        }
                    } else {
                        if (count <= 0) {
                            count = Constants.COUNT;
                        }
                    }
                }
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return true;
        }
    }

    public class SwitchThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        Switch s;
        long initTime;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 50;
        private long beforeTime;
        private int state = 1;

        public SwitchThread(Switch s) {
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
                    if (((Door) s.target).type == 1 && s.state == Constants.DOOR_STAND2 && game.stage == Constants.STAGE_4) {
                        System.out.println("contador: " + countTime + " flag: " + flag);
                        if (!flag) {
                            initTime = System.currentTimeMillis();
                            flag = true;
                        } else if (System.currentTimeMillis() - initTime > 1000) {
                            initTime = System.currentTimeMillis();
                            if (countTime != 0) {
                                countTime -= 1;
                            } else {
                                flag = false;
                                countTime = 200;
                                s.state = Constants.DOOR_STAND;
                                ((Door) s.target).deactivate();
                            }
                        }
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
            } else if (state == PAUSED) {
                System.out.println("pause thread");
                try {
                    this.suspend();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
