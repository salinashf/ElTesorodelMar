package micromobil.eltesorodelmar.activity.helper;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import micromobil.eltesorodelmar.R;

public class Credits extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.credits);
        LinearLayout l = (LinearLayout) findViewById(R.id.LinearLayout02);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 50, -750);
        animation.setDuration(20000);
        l.startAnimation(animation);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoadResources.nextActivity = true;
            finish();
        }
        return true;
    }

    @Override
    public void onStop() {
        System.out.println("CreditsActivity onStop");
        finish();
        super.onStop();
    }

    public void onDestroy() {
        System.out.println("CreditsActivity onDestroy");
        super.onStop();
    }
}
