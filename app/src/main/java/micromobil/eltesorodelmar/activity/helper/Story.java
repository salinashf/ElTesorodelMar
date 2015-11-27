package micromobil.eltesorodelmar.activity.helper;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.view.IntroView;


/**
 * @author Henry
 *         Describe las historia del juego con peque�a biografia del la trama
 */
public class Story extends Activity {
    /**
     * Called when the activity is first created.
     */
    public int level;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.addContentView(new IntroView(this, this), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.addContentView(new layout2(this), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.addContentView(new layout(this), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoadResources.nextActivity = true;
            finish();
        }
        return true;
    }

    public void onPause() {
        super.onPause();
        System.out.println("Intro onPause");
        finish();
    }

    public void onStop() {
        System.out.println("Intro onStop");
        finish();
        super.onStop();
    }

    public void onDestroy() {
        System.out.println("Intro onDestroy");
//		Intent intent3 = new Intent(Intro.this, MainGame.class);
//		startActivity(intent3);
        System.out.println("finish case 7");
        super.onDestroy();
        //super.onStop();
    }

    public class layout extends View {
        int c1 = Color.argb(255, 127, 82, 18);
        int c2 = Color.argb(255, 170, 119, 47);
        int c3 = Color.argb(255, 193, 151, 91);
        int c4 = Color.argb(255, 213, 178, 128);
        int c5 = Color.argb(255, 231, 206, 170);
        int c6 = Color.argb(255, 246, 233, 215);
        int c7 = Color.argb(255, 255, 255, 255);

        public layout(Context context) {
            super(context);
            level = 1;
            // TODO Auto-generated constructor stub
        }

        protected void onDraw(Canvas c) {
            Paint p = new Paint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println(level);
            switch (level) {
                case 1: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    break;
                }
                case 2: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    p.setColor(c2);
                    c.drawCircle(40, 300, 10, p);
                    break;
                }
                case 3: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    p.setColor(c2);
                    c.drawCircle(40, 300, 10, p);
                    p.setColor(c3);
                    c.drawCircle(70, 300, 10, p);
                    break;
                }
                case 4: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    p.setColor(c2);
                    c.drawCircle(40, 300, 10, p);
                    p.setColor(c3);
                    c.drawCircle(70, 300, 10, p);
                    p.setColor(c4);
                    c.drawCircle(100, 300, 10, p);
                    break;
                }
                case 5: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    p.setColor(c2);
                    c.drawCircle(40, 300, 10, p);
                    p.setColor(c3);
                    c.drawCircle(70, 300, 10, p);
                    p.setColor(c4);
                    c.drawCircle(100, 300, 10, p);
                    p.setColor(c5);
                    c.drawCircle(130, 300, 10, p);
                    break;
                }
                case 6: {
                    p.setColor(c1);
                    c.drawCircle(10, 300, 10, p);
                    p.setColor(c2);
                    c.drawCircle(40, 300, 10, p);
                    p.setColor(c3);
                    c.drawCircle(70, 300, 10, p);
                    p.setColor(c4);
                    c.drawCircle(100, 300, 10, p);
                    p.setColor(c5);
                    c.drawCircle(130, 300, 10, p);
                    p.setColor(c6);
                    c.drawCircle(160, 300, 10, p);
                    break;
                }
                default: {
                    finish();
                    break;
                }
            }
        }
    }

    public class layout2 extends View {
        Matrix m;
        Bitmap b;

        public layout2(Context context) {
            super(context);
            level = 0;
            m = new Matrix();
            m.preTranslate(330, 170);
            b = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.control));
            // TODO Auto-generated constructor stub
        }

        protected void onDraw(Canvas c) {
            Paint p = new Paint();
            m.postRotate(5, 405, 245);
            // m.preTranslate(100, 100);
            c.drawBitmap(b, m, p);

        }
    }
}