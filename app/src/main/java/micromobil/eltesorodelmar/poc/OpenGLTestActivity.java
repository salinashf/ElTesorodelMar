package micromobil.eltesorodelmar.poc;
/**
 * Created by Henry on 25/11/2015.
 */
import android.app.Activity;
import android.os.Bundle;
public class OpenGLTestActivity extends Activity {
    private MyGLSurfaceView glSurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new MyGLSurfaceView(this);
        setContentView(glSurfaceView);
    }
}