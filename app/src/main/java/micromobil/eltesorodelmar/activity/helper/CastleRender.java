package micromobil.eltesorodelmar.activity.helper;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;

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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import micromobil.eltesorodelmar.R;


/**
 * Created by Henry on 25/11/2015.
 */
public class CastleRender implements GLSurfaceView.Renderer {

    private boolean stop = false;
    private boolean paused = false;
    private boolean loadingReady = false;
    private long time;
    private boolean start;
    private float angle = -180;
    private boolean ciclo;
    private int l = -10;
    private int WIDTH;
    private int HEIGHT;
    private Object3D castle;
    private Texture logo;
    private Texture letras;
    private World world = null;
    private FrameBuffer fb = null;
    private Light sun = null;
    private Object3D esfera = null;
    private Camera cam;
    private RGBColor back = new RGBColor(0, 0, 0);
    private Resources res;

    public CastleRender(Resources resources, Context _contex) {
        world = new World();
        this.res = resources;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
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
        cam.moveCamera(v, 0.5f);
        cam.lookAt(castle.getTransformedCenter());
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
        cam = world.getCamera();
        SimpleVector moveCam = new SimpleVector(0, 0, 0);
        cam.moveCamera(moveCam, 0);
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
