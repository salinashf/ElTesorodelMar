package micromobil.eltesorodelmar.poc;

import android.app.Activity;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import micromobil.eltesorodelmar.R;

public class EsferitaActivity extends Activity {


    private World world = null;
    private FrameBuffer fb = null;
    private Light sun = null;
    private Object3D esfera = null;
    private Camera cam;
    private RGBColor back = new RGBColor(0, 0, 0);
    private Resources res;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView vista = new GLSurfaceView(getApplication());

        vista.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        res = getResources();
        vista.setRenderer(new OpenGLRenderer());//new CastleRender(getResources()  , getBaseContext())
        setContentView(vista);
        world = new World();


    }
    class OpenGLRenderer implements GLSurfaceView.Renderer{

        public boolean stop = false;
        public boolean paused = false;
        public boolean loadingReady = false;
        public long time;
        public boolean start;
        float angle = -180;
        boolean ciclo;
        int l = -10;
        public int WIDTH;
        public int HEIGHT;


        public Object3D castle;
        Texture logo;
        Texture letras;


        public OpenGLRenderer(){

        }
        @Override
        public void onDrawFrame(GL10 gl) {
            /*
            fb.clear(back);
            world.renderScene(fb);
            world.draw(fb);
            fb.display();
            */

            loadingReady = true;
            if (angle < 180 && !ciclo) {
                angle += 0.005f;
            } else if (angle > 179) {
                ciclo = true;
            } else if (ciclo) {
                angle -= 0.005f;
            } else if (angle < -180) {
                ciclo = false;
            }
            if ((int) angle == -178) {
                start = true;
                l = 10;
            }
            SimpleVector v = new SimpleVector((float) 0.1 * (Math.sin(angle)), 0, (float) 0.1 * (Math.cos(angle)));
            //System.out.println(angle);
            cam.moveCamera(v, 0.5f);
            cam.lookAt(castle.getTransformedCenter());
            //Dibujar mundo
            drawBuffer();
            sun.rotate(new SimpleVector(0, 0.05f, 0), castle.getTransformedCenter());
            MemoryHelper.compact();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            if (fb != null) {
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);
            this.WIDTH = w;
            this.HEIGHT = h;

/*
            if(fb != null){
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);
            world = new World();
            world.setAmbientLight(20,20,20);
            sun = new Light(world);
            sun.setIntensity(250, 250, 250);
            esfera = Primitives.getSphere(10, 1);
            esfera.calcTextureWrapSpherical();
            esfera.strip();
            esfera.build();
            world.addObject(esfera);
            cam = world.getCamera();
            cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
            cam.lookAt(esfera.getTransformedCenter());
            SimpleVector sv = new SimpleVector();
            sv.set(esfera.getTransformedCenter());
            sv.y -= 100;
            sv.z -= 100;
            sun.setPosition(sv);
*/


        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {


            TextureManager.getInstance().flush();
            TextureManager tm = TextureManager.getInstance();
            tm.addTexture("_Roofing.jpg", new Texture(res.openRawResource(R.raw.roofing)));
            tm.addTexture("_Stone_C.jpg", new Texture(res.openRawResource(R.raw.stone_c)));
            tm.addTexture("_Stone_S.jpg", new Texture(res.openRawResource(R.raw.stone_s)));
            tm.addTexture("_Wood_Ba.jpg", new Texture(res.openRawResource(R.raw.wood_ba)));
            tm.addTexture("Above_01.jpg", new Texture(res.openRawResource(R.raw.above_01)));
            tm.addTexture("Above_02.jpg", new Texture(res.openRawResource(R.raw.above_02)));
            tm.addTexture("Above_Ma.jpg", new Texture(res.openRawResource(R.raw.above_ma)));
            tm.addTexture("Concrete.jpg", new Texture(res.openRawResource(R.raw.concrete)));
            tm.addTexture("Stone_Br.jpg", new Texture(res.openRawResource(R.raw.stone_br)));
            tm.addTexture("Stone_Co.jpg", new Texture(res.openRawResource(R.raw.stone_co)));
            tm.addTexture("Stone_Ma.jpg", new Texture(res.openRawResource(R.raw.stone_ma)));
            tm.addTexture("sky_jpg.jpg", new Texture(res.openRawResource(R.raw.sky_jpg)));
            tm.addTexture("Water_Po.jpg", new Texture(res.openRawResource(R.raw.water_deep)));
            tm.addTexture("Fencing_.png", new Texture(res.openRawResource(R.raw.fencing_)));
            logo = new Texture(res.openRawResource(R.raw.logo), true);
            letras = new Texture(res.openRawResource(R.drawable.letrascastillo), true);

            castle = Object3D.mergeAll(Loader.load3DS(res.openRawResource(R.raw.skpfile), 1.5f));
            castle.translate(-70, -15, -12);
            castle.rotateX(-(float) Math.PI / 2);
            castle.setCulling(false);

            sun = new Light(world);
            SimpleVector svCastle = new SimpleVector();
            svCastle.set(castle.getTransformedCenter());
            svCastle.y -= 300;
            svCastle.x -= 100;
            svCastle.z += 200;
            sun.setPosition(svCastle);

            //a�adir c�mara
            cam = world.getCamera();
            SimpleVector moveCam = new SimpleVector(0, 0, 0);
            cam.moveCamera(moveCam, 0);

            //a�adir elementos al mundo
            world.addObject(castle);
            world.buildAllObjects();
        }


        public void drawBuffer() {
            if (l == -20)
                l = 20;
            fb.clear();
            world.renderScene(fb);
            world.draw(fb);
            if (start) {
                fb.blit(logo, 0, 0, WIDTH / 2 - 245, 0, 512, 256, FrameBuffer.TRANSPARENT_BLITTING);
                if (l > 0) {
                    fb.blit(letras, 0, 0, WIDTH / 2 - 128, HEIGHT - 100, 256, 64, FrameBuffer.TRANSPARENT_BLITTING);
                }
                l--;
            }
            fb.display();
        }

    }


}