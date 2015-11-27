package micromobil.eltesorodelmar.activity.helper;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by Henry on 25/11/2015.
 */
public class CastleActivity    extends Activity{
    public static final String LOG_APP ="ElTesosoDelMar" ;
    private CastleRender glSurfaceView ;
    GLSurfaceView vista  ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vista = new GLSurfaceView(getApplication());


        vista.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        glSurfaceView = new CastleRender(getResources()  , getBaseContext());
        vista.setRenderer(glSurfaceView);
        setContentView(vista);
    }


}
