package micromobil.eltesorodelmar.activity;


import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.threed.jpct.Logger;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.activity.helper.LoadResources;
import micromobil.eltesorodelmar.util.Constants;

public class MainGame extends Activity {
    public static MainGame master = null;
    public static MediaPlayer mp;
    private static Render renderer = null;
    public Point touchingPoint = new Point(58, 255);
    private GLSurfaceView mGLView;
    private boolean dragging = false;
    //public  MediaPlayer titlesong;

    protected void onCreate(Bundle savedInstanceState) {

        if (master != null) {
            copy(master);
        }

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(getApplication());

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        if (renderer == null) renderer = new Render(this);
        mGLView.setRenderer(renderer);
        setContentView(mGLView);

//		if(titlesong == null){
//			titlesong = MediaPlayer.create(this, R.raw.main2);
//			titlesong.setLooping(true);
//		}else{
//			titlesong.start();
//		}
//		
//		if(titlesong.isPlaying()){
//			titlesong.stop();
//			titlesong.release();
//		}
    }

    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        renderer.paused = true;
        super.onPause();
        mGLView.onPause();
//		if(titlesong.isPlaying()){
//			titlesong.pause();
//		}

        renderer.sensor.unregisterListener(renderer.camSensor);
        System.out.println("MainGame onPause");
    }

    @Override
    protected void onResume() {
        renderer.paused = false;
        if (renderer.mode == Constants.MODE_GUN) {
            renderer.sensor.registerListener(renderer.camSensor,
                    renderer.sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
            renderer.sensor.registerListener(renderer.camSensor,
                    renderer.sensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_GAME);
        }

        super.onResume();
        mGLView.onResume();

        System.out.println("MainGame onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
//		if(titlesong.isPlaying()){
//			titlesong.stop();
//		}
        System.out.println("MainGame onStop");
    }

    @Override
    public void onDestroy() {
        System.out.println("MainGame onDestroy");
        mp = null;
        super.onDestroy();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoadResources.nextActivity = true;
            finish();
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (renderer.ready && !renderer.paused && !renderer.stop) { //debe haber dejado de cargar, no estar pausado y no haber terminado
            if (renderer.mode == Constants.MODE_NORMAL) {
                renderer.control.update(event);

                //Posicionar c�mara detr�s del pirata
                if (event.getX() < renderer.WIDTH && event.getX() >= renderer.WIDTH - 70 && event.getY() < 55 && event.getY() >= 0) {
                    if (renderer.world.getCamera() == renderer.cam) {
                        moveCameraBehind();
                    }
                    //Activar modo pistola
                } else if (event.getX() < renderer.WIDTH - 70 && event.getX() >= renderer.WIDTH - 140 && event.getY() < 65 && event.getY() >= 0) {
                    activateGunMode();
                    //Saltar
                } else if (event.getX() < renderer.WIDTH && event.getX() >= renderer.WIDTH - 70 && event.getY() < 130 && event.getY() >= 65) {
                    if (renderer.pirate.state != Constants.JUMP) {
                        jump();
                    }
                    //Tocar a alg�n enemigo
                } else if (renderer.pirate.state != Constants.ATK1) {
                    if (!(event.getX() < 130 && event.getX() >= 0 && event.getY() < 330 && event.getY() > 190)) {
                        attack(event);
                    }
                }

                //Movimiento
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getX() < 130 && event.getX() >= 0 && event.getY() < 330 && event.getY() > 190) {
                        touchingPoint.x = (int) event.getX();
                        touchingPoint.y = (int) event.getY();
                        dragging = true;
                    } else {
                        dragging = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    dragging = false;
                }
                if (dragging) {
                    if (renderer.pirate.state != Constants.FALL && renderer.pirate.state != Constants.DEATH && renderer.pirate.state != Constants.PAIN && renderer.pirate.state != Constants.PAIN_BY_KNIGHT && renderer.pirate.state != Constants.PAIN_BY_DEVIL && renderer.pirate.state != Constants.JUMP)
                        renderer.pirate.state = Constants.WALK;
                    if (touchingPoint.x < 40) {
                        if (touchingPoint.y >= 200 && touchingPoint.y < 240) { //NW
                            renderer.dir = Constants.NW;
                        } else if (touchingPoint.y >= 240 && touchingPoint.y < 280) { //W
                            renderer.dir = Constants.W;
                        } else if (touchingPoint.y >= 280 && touchingPoint.y < 320) { //SW
                            renderer.dir = Constants.SW;
                        }
                    } else if (touchingPoint.x >= 40 && touchingPoint.x < 80) {
                        if (touchingPoint.y >= 200 && touchingPoint.y < 240) { //N
                            renderer.dir = Constants.N;
                        } else if (touchingPoint.y >= 280 && touchingPoint.y < 320) { //S
                            renderer.dir = Constants.S;
                        }
                    } else if (touchingPoint.x >= 80 && touchingPoint.x < 120) {
                        if (touchingPoint.y >= 200 && touchingPoint.y < 240) { //NE
                            renderer.dir = Constants.NE;
                        } else if (touchingPoint.y >= 240 && touchingPoint.y < 280) { //E
                            renderer.dir = Constants.E;
                        } else if (touchingPoint.y >= 280 && touchingPoint.y < 320) { //SE
                            renderer.dir = Constants.SE;
                        }
                    }

                } else if (!dragging) {
                    activateAction(event);

                }
            } else if (renderer.mode == Constants.MODE_GUN) {
                //Salir del modo pistola
                if (event.getX() < 64 && event.getX() >= 0 && event.getY() < 64 && event.getY() >= 0) {
                    deactivateGunMode();
                } else {
                    //Disparar
                    shoot(event);
                }
            } else if (renderer.mode == Constants.MODE_CINEMATICS) {
                //Si el di�logo se escribi� completo en la pantalla, escribir el siguiente
                if (renderer.cinematics.ready) {
                    renderer.cinematics.ready = false;
                    renderer.cinematics.index++;
                    renderer.cinematics.sceneBuffer = new StringBuffer();
                }
            } else if (renderer.mode == Constants.MODE_DEATH) {
                if (event.getX() >= renderer.WIDTH / 2 - 128 && event.getX() < renderer.WIDTH / 2 + 128 && event.getY() < renderer.HEIGHT && event.getY() >= 164) {
                    renderer.reStart();
                }
            }
        }
        try {
            Thread.sleep(50);
        } catch (Exception e) {
        }
        return true;
    }

    //caso de uso: Saltar
    public void jump() {
        renderer.pirate.jump();
    }

    //caso de uso: Atacar
    public void attack(MotionEvent event) {
        renderer.pirate.attack(event);
    }

    //caso de uso: Disparar
    public void shoot(MotionEvent event) {
        renderer.pirate.shoot(event);
    }

    //caso de uso: Mover c�mara detr�s del pirata
    public void moveCameraBehind() {
        renderer.pirate.moveCameraBehind();
    }

    //caso de uso: Activar modo pistola
    public void activateGunMode() {
        renderer.pirate.activateGunMode();
    }

    //caso de uso: Volver al modo normal
    public void deactivateGunMode() {
        renderer.pirate.deactivateGunMode();
    }

    //caso de uso: Mover control en pantalla
    public void controlUpdate(MotionEvent event) {
        renderer.control.update(event);
    }

    //caso de uso: Activar acci�n
    public void activateAction(MotionEvent event) {
        int state = 0;
        for (int i = 0; i < renderer.castle.doors.length; i++) {
            if (renderer.castle.doors[i] != null && renderer.castle.doors[i].count > 0 && event.getY() > 280 && event.getX() > renderer.WIDTH - 130) {
                doorActivate(i);
            }
        }
        for (int i = 0; i < renderer.castle.switches.length; i++) {
            if (renderer.castle.switches[i] != null && renderer.castle.switches[i].count > 0 && event.getY() > 280 && event.getX() > renderer.WIDTH - 130) {
                switchActivate(i);
            }
        }
        //if(renderer.castle.box != null && renderer.castle.box.count>0 && event.getY()>280 && event.getX()>400) state = 2;
        if (renderer.pirate.state == Constants.ATK1 || renderer.pirate.state == Constants.DEATH || renderer.pirate.state == Constants.PAIN || renderer.pirate.state == Constants.PAIN_BY_KNIGHT || renderer.pirate.state == Constants.PAIN_BY_DEVIL || renderer.pirate.state == Constants.JUMP)
            state = -1;

        switch (state) {
            case 0: {
                renderer.pirate.state = Constants.STAND;
                renderer.dir = Constants.NONE;
                break;
            }
            case 2: {
                //renderer.pirate.state = Constants.PUSH;
                break;
            }
        }
    }

    //caso de uso: Activar switch
    public void switchActivate(int id) {
        renderer.castle.switches[id].activate();
    }

    //caso de uso: Activar puerta
    public void doorActivate(int id) {
        renderer.castle.doors[id].activate();
    }

    public void credits() {
//		Intent intent = new Intent(MainGame.this, Credits.class);
//		startActivity(intent);
        finish();
    }
}
