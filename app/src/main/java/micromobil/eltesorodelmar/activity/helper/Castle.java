package micromobil.eltesorodelmar.activity.helper;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import micromobil.eltesorodelmar.GL.RenderIntro;

/**
 * @author Henry
 *         Clase que describe una introcudcion de las clases un mapa a donde esta el catillo
 *         dibuja un render de  donde esta el mapa
 */
public class Castle extends Activity  {
    public static final String LOG_APP ="ElTesosoDelMar" ;
    private GLSurfaceView mGLView;
    private RenderIntro renderer = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(getApplication());

        // fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        renderer = new RenderIntro(this);
        mGLView.setRenderer(renderer);
        setContentView(mGLView);

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (renderer.start) {
            renderer.surfaceDestroyed(renderer.getHolder());
            System.out.println("ActivityRender onTouchEvent finish");
//			Intent intent = new Intent(ActivityRender.this, Intro.class);
//			startActivity(intent);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoadResources.nextActivity = true;
            System.out.println("nextActivity: " + LoadResources.nextActivity);
            finish();
        }
        return true;
    }
    @Override
    public void onStop() {
        Log.d(LOG_APP, "ActivityRender onStop");
        finish();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_APP, "Castle onDestroy") ;
        //super.onStop();
    }
}
