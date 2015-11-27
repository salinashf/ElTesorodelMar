package micromobil.eltesorodelmar.GL;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.activity.helper.LoadResources;

public class RenderIntro extends GLSurfaceView implements Renderer {
    public boolean stop = false;
    public boolean paused = false;
    public boolean loadingReady = false;
    public long time;
    public boolean start;
    public Camera cam;
    public World world;
    public Object3D castle;
    public FrameBuffer fb;
    public ProgressDialog dialog;
    float angle = -180;
    boolean ciclo;
    int l = -10;
    Texture logo;
    Texture letras;
    Context context;
    int WIDTH;
    int HEIGHT;
    private Light lamp;


    public RenderIntro(Context context) {
        super(context);
        //Config.maxPolysVisible = 5000;
        Config.farPlane = 1500;
        //Config.collideOffset = 40;
        time = System.nanoTime();
        ciclo = false;
        start = false;
        dialog = new ProgressDialog(context);
        System.out.println("constructor");
        this.context = context;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!stop) {
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
            lamp.rotate(new SimpleVector(0, 0.05f, 0), castle.getTransformedCenter());
        }
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
        world = new World();
        Resources res = getResources();
        TextureManager tm = TextureManager.getInstance();

        //a�adir texturas
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

        //cargar castillo
        castle = Object3D.mergeAll(Loader.load3DS(res.openRawResource(R.raw.skpfile), 1.5f));
        castle.translate(-70, -15, -12);
        castle.rotateX(-(float) Math.PI / 2);
        castle.setCulling(false);

        //a�adir luz
        lamp = new Light(world);
        SimpleVector sv = new SimpleVector();
        sv.set(castle.getTransformedCenter());
        sv.y -= 300;
        sv.x -= 100;
        sv.z += 200;
        lamp.setPosition(sv);

        //a�adir c�mara
        cam = world.getCamera();
        SimpleVector moveCam = new SimpleVector(0, 0, 0);
        cam.moveCamera(moveCam, 0);

        System.out.println("onSurfaceCreated");

        //a�adir elementos al mundo
        world.addObject(castle);
        world.buildAllObjects();
        dialog.dismiss();

        if (LoadResources.titlesong != null) LoadResources.titlesong.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //holder.getSurface().freeze();
        holder = null;
        RenderIntro.this.destroyDrawingCache();
        System.out.println("surfaceDestroyed");
        ((Activity) context).finish();
    }

}
