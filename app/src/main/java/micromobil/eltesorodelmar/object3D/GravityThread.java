package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.Object3D;

import micromobil.eltesorodelmar.GL.Render;

public class GravityThread extends Thread {
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;
    public final static int STOPED = 3;
    //amount of time to sleep for (in milliseconds)
    public long delay = 100;
    public int state = 1;
    Render game;
    Object3D obj;
    private long sleepTime;
    private long beforeTime;

    public GravityThread(Render game, Object3D obj) {
        this.game = game;
        this.obj = obj;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (state == RUNNING) {
            beforeTime = System.nanoTime();
            synchronized (game) {
                if (obj instanceof Pirate) {
                    ((Pirate) obj).gravity();
                    ((Pirate) obj).cameraGravity();
                } else if (obj instanceof Knight) {
                    ((Knight) obj).gravity();
                } else if (obj instanceof FallingFire) {
                    ((FallingFire) obj).gravity();
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
