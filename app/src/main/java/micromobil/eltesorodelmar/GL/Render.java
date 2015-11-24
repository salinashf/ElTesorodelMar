package micromobil.eltesorodelmar.GL;


import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Vibrator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import micromobil.eltesorodelmar.CameraSensor;
import micromobil.eltesorodelmar.GameControls;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.activity.MainGame;
import micromobil.eltesorodelmar.object3D.Gull;
import micromobil.eltesorodelmar.object3D.Pirate;
import micromobil.eltesorodelmar.object3D.load.Castle;
import micromobil.eltesorodelmar.physic.Cinematics;
import micromobil.eltesorodelmar.sound.SoundManager;
import micromobil.eltesorodelmar.util.Constants;

public class Render extends GLSurfaceView implements Renderer {

    public static GameControls control = null;
    //elementos de la interfaz de juego
    private static Texture base = null;
    private static Texture pad = null;
    private static Texture pad2 = null;
    private static Texture life = null;
    private static Texture lifeBar = null;
    private static Texture logoBar = null;
    private static Texture doorB = null;
    private static Texture eye = null;
    private static Texture gun = null;
    private static Texture exit = null;
    private static Texture jump = null;
    private static Texture dead = null;
    private static Texture back = null;
    private static Texture again = null;
    public TextView menssageField;
    public ProgressBar bar;
    public int stage = Constants.STAGE_1; //escena actual
    public int stage1Step; //registro del avance en la escena 1
    public int stage6Step; //registro del avance en la escena 6
    public int stage7Step; //registro del avance en la escena 7
    public int stage9Step; //registro del avance en la escena 9
    public int stage11Step; //registro del avance en la escena 11
    public int crosses;
    public Context context;
    public boolean stop = false;
    public boolean paused = false;
    public boolean ready = false;
    public boolean loading = true;
    public long time;
    public Cinematics cinematics; //objecto donde est�n todas las cinem�ticas del juego
    public int dir; //direcci�n del joystick presionado
    //c�maras
    public Camera cam;
    public Camera camGun;
    public Camera camCine;
    public World world; //mundo
    public Light lamp; //luz
    public Castle castle; //objecto que contiene todos los elementos del castillo
    public Pirate pirate; //protagonista
    public Gull gull; //gaviota
    public int mode; //modo de juego
    public FrameBuffer fb;
    //vibraci�n y sensores de movimiento
    public Vibrator vibrator;
    public SensorManager sensor;
    //objecto que controla el modo pistola
    public CameraSensor camSensor;
    public SoundManager soundManager;
    public int WIDTH;
    public int HEIGHT;
    TextureManager tm;

    public Render(Context context) {
        super(context);
        //Config.maxPolysVisible = 5000;
        Config.farPlane = 1500;
        Config.glAvoidTextureCopies = false;
        //Config.collideOffset = 40;

        this.context = context;
        if (control == null) control = new GameControls();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        sensor = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        camSensor = new CameraSensor(this);
        sensor.unregisterListener(camSensor);
        cinematics = new Cinematics(this);
        time = System.nanoTime();
        soundManager = new SoundManager();
        soundManager.initSounds(context);
        /*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        System.out.println("Render Constructor");
    }

    @Override
    public synchronized void onDrawFrame(GL10 gl) {

        //System.out.println("pos: "+pirate.getTransformedCenter());
        //System.out.println("pirate state: "+pirate.state);

        //try {
        if (!stop) {
            if (paused) {
                System.out.println("paused!");
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            } else {
                if (mode == Constants.MODE_NORMAL) {// Modo normal
                    if (pirate.state == Constants.WALK && world.getCamera() == cam) {
                        movePirate(); // mueve al pirata y a la c�mara seg�n la direcci�n presionada
                    } else if (pirate.state == Constants.PUSH) {
                        pushBox();
                    }
                } else if (mode == Constants.MODE_GUN) {// Modo pistola
                    movePirateGun();
                }

                // Dibujar mundo
                drawBuffer();
            }
        } else {
            if (fb != null) {
                fb.dispose();
                fb = null;
            }
        }
        //} catch (Exception e) {
        //Logger.log("Drawing thread terminated!", Logger.MESSAGE);
        //}
    }

    public void drawBuffer() {

        fb.clear();
        world.compileAllObjects(fb);
        world.renderScene(fb);
        world.draw(fb);
        if (mode == Constants.MODE_DEATH) {
            fb.blit(back, 0, 0, 0, 0, 480, 320, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(dead, 0, 0, WIDTH / 2 - 256, 100, 512, 64, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(again, 0, 0, WIDTH / 2 - 128, 164, 256, 128, FrameBuffer.TRANSPARENT_BLITTING);
        } else if (mode == Constants.MODE_NORMAL) {
            fb.blit(lifeBar, 0, 0, 0, 3, 512, 128, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(life, 0, 0, 62, 20, pirate.damage, 16, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(logoBar, 0, 0, 10, 10, 128, 128, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(base, 0, 0, -2, 195, 120, 120, FrameBuffer.TRANSPARENT_BLITTING);
            if (!control.dragging) {
                fb.blit(pad, 0, 0, control.touchingPoint.x - 60, control.touchingPoint.y - 60, 120, 120, FrameBuffer.TRANSPARENT_BLITTING);
            } else {
                fb.blit(pad2, 0, 0, control.touchingPoint.x - 60, control.touchingPoint.y - 60, 120, 120, FrameBuffer.TRANSPARENT_BLITTING);
            }

            //dibujar s�mbolos para ejecutar acciones espec�ficas

            //puertas
            for (int i = 0; i < castle.doors.length; i++) {
                drawDoorActivate(i);
            }

            //switches
            for (int i = 0; i < castle.switches.length; i++) {
                drawSwitchActivate(i);
                drawSwitchCountdown(i);
            }

            fb.blit(eye, 0, 0, WIDTH - 70, 3, 64, 64, FrameBuffer.TRANSPARENT_BLITTING); //dibujar s�mbolo ojo
            fb.blit(gun, 0, 0, WIDTH - 140, 3, 64, 64, FrameBuffer.TRANSPARENT_BLITTING); //dibujar s�mbolo pistola
            fb.blit(jump, 0, 0, WIDTH - 70, 73, 64, 64, FrameBuffer.TRANSPARENT_BLITTING); //dibujar s�mbolo salto

        } else if (mode == Constants.MODE_GUN) {
            fb.blit(exit, 0, 0, 0, 0, 64, 64, FrameBuffer.TRANSPARENT_BLITTING); //dibujar s�mbolo de salida de modo pistola
        } else if (mode == Constants.MODE_CINEMATICS) {
            //ejecutar acciones de cinem�tica
            sceneCinematics();
        }
        fb.display();
    }

    public void drawDoorActivate(int id) {
        if (castle.doors[id] != null && castle.doors[id].count > 0) {
            fb.blit(doorB, 0, 0, WIDTH - 130, 280, 128, 32, FrameBuffer.TRANSPARENT_BLITTING);
            castle.doors[id].count--;
        }
    }

    public void drawSwitchActivate(int id) {
        if (castle.switches[id] != null && castle.switches[id].count > 0) {
            fb.blit(doorB, 0, 64, WIDTH - 130, 280, 128, 32, FrameBuffer.TRANSPARENT_BLITTING);
            castle.switches[id].count--;

        }
    }

    public void drawSwitchCountdown(int id) {
        if (castle.switches[id] != null) {
            if (castle.switches[id].flag) {
                if (castle.switches[id].countTime == 0) {
                    Cinematics.tb.blitText(fb, "Tiempo terminado!:", WIDTH - 225, 300);
                } else {
                    Cinematics.tb.blitText(fb, "Tiempo restante: " + String.valueOf(castle.switches[id].countTime), WIDTH - 225, 300, 475, 315);
                }
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        System.out.println("Render onSurfaceChanged");
        if (fb != null) {
            fb.dispose();
        }
        fb = new FrameBuffer(gl, w, h);
        WIDTH = w;
        HEIGHT = h;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("Render onSurfaceCreated");
        if (MainGame.master == null) {
            world = new World();
            Resources res = getResources();
            tm = TextureManager.getInstance();
            tm.flush();

            //a�adir texturas
            tm.addTexture("piratat", new Texture(res.openRawResource(R.raw.piratat)));
            tm.addTexture("espadat", new Texture(res.openRawResource(R.raw.espadat)));
            tm.addTexture("laddert", new Texture(res.openRawResource(R.raw.laddert)));
            tm.addTexture("crate_bm.bmp", new Texture(res.openRawResource(R.raw.crate_bm)));
            tm.addTexture("hawkt2", new Texture(res.openRawResource(R.raw.hawkt2)));
            tm.addTexture("brown.png", new Texture(res.openRawResource(R.raw.brown)));
            tm.addTexture("brown2.png", new Texture(res.openRawResource(R.raw.brown2)));
            tm.addTexture("flat_f01.png", new Texture(res.openRawResource(R.raw.flat_f01)));
            tm.addTexture("flat_f02.png", new Texture(res.openRawResource(R.raw.flat_f02)));
            tm.addTexture("flat_f03.png", new Texture(res.openRawResource(R.raw.flat_f03)));
            tm.addTexture("flat_f04.png", new Texture(res.openRawResource(R.raw.flat_f04)));
            tm.addTexture("flat_f05.png", new Texture(res.openRawResource(R.raw.flat_f05)));
            tm.addTexture("flat_fke.png", new Texture(res.openRawResource(R.raw.brown)));
            tm.addTexture("wker16.png", new Texture(res.openRawResource(R.raw.wker16)));
            tm.addTexture("wker2.png", new Texture(res.openRawResource(R.raw.wker2)));
            tm.addTexture("wker22.png", new Texture(res.openRawResource(R.raw.wker22)));
            tm.addTexture("wker26.png", new Texture(res.openRawResource(R.raw.wker26)));
            tm.addTexture("wker28.png", new Texture(res.openRawResource(R.raw.wker28)));
            tm.addTexture("wker29.png", new Texture(res.openRawResource(R.raw.wker29)));
            tm.addTexture("wker30.png", new Texture(res.openRawResource(R.raw.wker30)));
            tm.addTexture("wker4.png", new Texture(res.openRawResource(R.raw.wker4)));
            tm.addTexture("sdoor_pn.PNG", new Texture(res.openRawResource(R.raw.sdoor_pn)));
            tm.addTexture("sdoor2_p.PNG", new Texture(res.openRawResource(R.raw.sdoor2_p)));
            tm.addTexture("polvo1", new Texture(res.openRawResource(R.raw.polvo1)));
            tm.addTexture("polvo2", new Texture(res.openRawResource(R.raw.polvo2)));
            tm.addTexture("polvo3", new Texture(res.openRawResource(R.raw.polvo3)));
            tm.addTexture("polvo4", new Texture(res.openRawResource(R.raw.polvo4)));
            tm.addTexture("polvor1", new Texture(res.openRawResource(R.raw.polvor1)));
            tm.addTexture("polvor2", new Texture(res.openRawResource(R.raw.polvor2)));
            tm.addTexture("polvor3", new Texture(res.openRawResource(R.raw.polvor3)));
            tm.addTexture("polvor4", new Texture(res.openRawResource(R.raw.polvor4)));
            tm.addTexture("fuego1", new Texture(res.openRawResource(R.raw.fuego1), true));
            tm.addTexture("fuego2", new Texture(res.openRawResource(R.raw.fuego2), true));
            tm.addTexture("fuego3", new Texture(res.openRawResource(R.raw.fuego3), true));
            tm.addTexture("knight_text", new Texture(res.openRawResource(R.raw.knight_text)));
            tm.addTexture("head_text", new Texture(res.openRawResource(R.raw.head_text)));
            tm.addTexture("head_text2", new Texture(res.openRawResource(R.raw.head_text2)));
            tm.addTexture("luces3", new Texture(res.openRawResource(R.raw.luces3), true));
            tm.addTexture("luces4", new Texture(res.openRawResource(R.raw.luces4), true));
            tm.addTexture("luces5", new Texture(res.openRawResource(R.raw.luces5), true));
            tm.addTexture("vgafont", new Texture(res.openRawResource(R.raw.font3), true));
            tm.addTexture("switcht", new Texture(res.openRawResource(R.raw.switcht)));
            tm.addTexture("metal", new Texture(res.openRawResource(R.raw.metal)));
            tm.addTexture("black", new Texture(res.openRawResource(R.raw.black)));
            tm.addTexture("flat_cei.png", new Texture(res.openRawResource(R.raw.flat_cei)));
            tm.addTexture("flat_c01.png", new Texture(res.openRawResource(R.raw.flat_c01)));
            tm.addTexture("grimt", new Texture(res.openRawResource(R.raw.grimt)));
            tm.addTexture("alfom.jpg", new Texture(res.openRawResource(R.raw.alfom)));
            tm.addTexture("ront", new Texture(res.openRawResource(R.raw.ront2), true));
            tm.addTexture("devilt", new Texture(res.openRawResource(R.raw.devilt)));
            tm.addTexture("torchtex", new Texture(res.openRawResource(R.raw.torchtex2), true));
            tm.addTexture("bannertex", new Texture(res.openRawResource(R.raw.bannertex2), true));
            tm.addTexture("crosst", new Texture(res.openRawResource(R.raw.croos), true));

            world.setFogging(World.FOGGING_ENABLED);
            world.setFogParameters(100, 0, 0, 0);

            //a�adir luz
            lamp = new Light(world);
            lamp.setIntensity(200, 200, 200);
            lamp.setPosition(new SimpleVector(0, -5, 0));
            lamp.disable();

            //a�adir c�mara
            cam = new Camera();
            world.setCameraTo(cam);

            //crear c�mara de la pistola
            camGun = new Camera();

            //cargar pirata
            pirate = new Pirate(this);
            pirate.translate(0, -10, -56.0f);
            cam.setPosition(pirate.getTransformedCenter());
            cam.moveCamera(Camera.CAMERA_MOVEOUT, Constants.CAM_DIST);
            cam.lookAt(pirate.getTransformedCenter());

            //cargar gaviota
            gull = new Gull(this);
            gull.translate(0, 2, 0);

            //a�adir castillo
            castle = new Castle(this);

            //cargar elementos de la interfaz de juego
            if (base == null) base = new Texture(res.openRawResource(R.raw.base), true);
            if (pad == null) pad = new Texture(res.openRawResource(R.raw.pad), true);
            if (pad2 == null) pad2 = new Texture(res.openRawResource(R.raw.pad2), true);
            if (logoBar == null) logoBar = new Texture(res.openRawResource(R.raw.logo_bar), true);
            if (life == null) life = new Texture(res.openRawResource(R.raw.life2), true);
            if (lifeBar == null) lifeBar = new Texture(res.openRawResource(R.raw.life1), true);
            if (doorB == null) doorB = new Texture(res.openRawResource(R.raw.carteles), true);
            if (eye == null) eye = new Texture(res.openRawResource(R.raw.eye3), true);
            if (gun == null) gun = new Texture(res.openRawResource(R.raw.gun3), true);
            if (jump == null) jump = new Texture(res.openRawResource(R.raw.jump), true);
            if (exit == null) exit = new Texture(res.openRawResource(R.raw.exit), true);
            if (dead == null) dead = new Texture(res.openRawResource(R.raw.dead), true);
            if (back == null) back = new Texture(res.openRawResource(R.raw.back), true);
            if (again == null) again = new Texture(res.openRawResource(R.raw.again), true);

            tm.compress();

            //a�adir elementos al mundo
            world.buildAllObjects();

//			MainGame.mp = MediaPlayer.create(context, R.raw.threemetal);
//			MainGame.mp.setLooping(true);

            if (MainGame.master == null) {
                Logger.log("Saving master Activity!");
                MainGame.master = (MainGame) context;
            }
        }
        ready = true; //ha terminado la carga del render

    }

    //regresas al comienzo del juego
    public void reStart() {
        world.removeObject(castle.castle);
        castle.castle = null;
        if (castle.castleCache != null) {
            world.removeObject(castle.castleCache);
            castle.castleCache = null;
        }
        castle.removeObjectsFromStage(stage);
        castle.sceneTransport(Constants.STAGE_1);
        stage6Step = 0;
        stage7Step = 0;
        stage9Step = 0;
        stage11Step = 0;
        world.setCameraTo(cam);
        mode = Constants.MODE_NORMAL;
        pirate.state = Constants.STAND;

        //detener m�sicas
        if (Cinematics.battlesong != null && Cinematics.battlesong.isPlaying()) {
            Cinematics.battlesong.stop();
            Cinematics.battlesong.release();
        }

        //iniciar m�sica
        if (MainGame.mp != null && !MainGame.mp.isPlaying()) {
            MainGame.mp.start();
        }
    }

    //caso de uso: Mover pirata
    public void movePirate() {
        pirate.movePirate(dir);
    }

    //caso de uso: Mover caja
    public void pushBox() {
        pirate.box.pushBox(dir);
    }

    //caso de uso: Mover c�mara modo pistola
    public void movePirateGun() {
        pirate.movePirateGun();
    }

    //caso de uso: Iniciar cinem�tica escena
    public void sceneCinematics() {
        cinematics.sceneCinematics(fb, stage);
    }
}
