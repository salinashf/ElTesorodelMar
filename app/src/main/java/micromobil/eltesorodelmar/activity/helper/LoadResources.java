package micromobil.eltesorodelmar.activity.helper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import micromobil.eltesorodelmar.R;

/**
 * @author Henry
 *         esta es una  pantalla que carga recursos externos para el juego S
 *         this is a screen that loads external resources for the  game
 */
public class LoadResources extends Activity {
    public static boolean nextActivity = false;
    public static MediaPlayer titlesong;
    private LinearLayout layout;
    private TextView text;

    public void onCreate(Bundle savedInstanceState) {
        System.out.println("nextActivity: " + nextActivity);

        super.onCreate(savedInstanceState);

        if (!nextActivity) {
            setContentView(R.layout.main);
            //fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            text = (TextView) findViewById(R.id.text);
            text.setTypeface(Typeface.SERIF, Typeface.BOLD);
            text.setTextColor(Color.BLACK);
            text.setTextSize(16f);
            text.setShadowLayer(2f, 1.2f, 1.2f, Color.DKGRAY);
            layout = (LinearLayout) findViewById(R.id.LinearLayout01);
            layout.setBackgroundDrawable(getResources().getDrawable(R.raw.screen2));
            //Intent intent = new Intent(Main.this, ActivityRender.class);
            //startActivity(intent);

            if (titlesong == null) {
                titlesong = MediaPlayer.create(this, R.raw.main2);
                titlesong.setLooping(true);
            } else {
                titlesong.start();
            }
        } else {
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
            finish();
        }
        return true;
    }

    @Override
    public void onStop() {
        System.out.println("Main onStop");
        super.onStop();
    }

    public void onResume() {
        System.out.println("Main onResume");
        super.onResume();
        if (nextActivity) {
            finish();
        }
    }

    public void onPause() {
        System.out.println("Main onPause");
        super.onPause();
    }

    public void onDestroy() {
        System.out.println("Main onDestroy");
        nextActivity = false;
        titlesong = null;
        super.onDestroy();
    }
}
