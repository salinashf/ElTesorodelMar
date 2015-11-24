package micromobil.eltesorodelmar.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;

import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.activity.helper.Story;


public class IntroView extends View {
    int state;
    Bitmap pad;
    Bitmap sreen;
    Story intro;
    private Matrix n;

    public IntroView(Context context, Story intro) {
        super(context);
        this.intro = intro;
        this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        // TODO Auto-generated constructor stub
        pad = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.control));
        sreen = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen1));
        n = new Matrix();


    }

    protected void onDraw(Canvas c) {
        int w = c.getWidth();
        int h = c.getHeight();
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(18);
        p.setTypeface(Typeface.SERIF);
        p.setTextAlign(Paint.Align.CENTER);
        p.setShadowLayer(5f, 1.2f, 1.2f, Color.BLACK);
        switch (intro.level) {
            case 0: {
                c.drawText("Presione la pantalla para continuar", w / 2 - 10, 60, p);
                c.drawText("Durante la introducci�n", w / 2 - 10, 80, p);
                break;
            }
            case 1: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen1)), n, p);
                c.drawText("Hace incontables siglos una leyenda seduce", w / 2 - 10, 60, p);
                c.drawText("a reyes y cazadores de tesoros. Historias", w / 2 - 10, 80, p);
                c.drawText("sobre una isla errante llena de los m�s", w / 2 - 10, 100, p);
                c.drawText("maravillosos tesoros de ayer y hoy. ", w / 2 - 10, 120, p);
                break;
            }
            case 2: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen2)), n, p);
                c.drawText("Antiguos documentos se�alan que la isla de", w / 2 - 10, 60, p);
                c.drawText("verdad existe y aparece s�lo una vez cada", w / 2 - 10, 80, p);
                c.drawText("era, en ella existe un extra�o castillo", w / 2 - 10, 100, p);
                c.drawText("cuyo interior est� adornado con tesoros de", w / 2 - 10, 120, p);
                c.drawText("distintas culturas y �pocas de la historia", w / 2 - 10, 140, p);
                c.drawText("de la humanidad.", w / 2 - 10, 160, p);
                break;
            }
            case 3: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen3)), n, p);
                c.drawText("Sin embargo no es posible acceder a sus", w / 2 - 10, 60, p);
                c.drawText("mayores secretos puesto que el castillo", w / 2 - 10, 80, p);
                c.drawText("est� repleto de trampas contra intrusos.", w / 2 - 10, 100, p);
                c.drawText(" 1719 � Era dorada de la pirater�a ", w / 2 - 10, 140, p);
                break;
            }
            case 4: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen4)), n, p);
                c.drawText("Un capit�n surca los mares en busca de", w / 2 - 10, 60, p);
                c.drawText("tesoros y sue�os en los oc�anos. Sin", w / 2 - 10, 80, p);
                c.drawText("embargo, la mala fama de este capit�n hizo", w / 2 - 10, 100, p);
                c.drawText("que su tripulaci�n desatara una rebeli�n", w / 2 - 10, 120, p);
                c.drawText("contra �l.  Fue arrojado a su suerte en la", w / 2 - 10, 140, p);
                c.drawText("soledad del Atl�ntico Sur.", w / 2 - 10, 160, p);
                break;
            }
            case 5: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen6)), n, p);
                c.drawText("Este despierta aturdido en una isla ... ", w / 2 - 10, 60, p);
                c.drawText("��Milagro divino!� piensa �l.", w / 2 - 10, 80, p);
                c.drawText("Camina unos metros y encuentra decenas de", w / 2 - 10, 100, p);
                c.drawText("viejos esqueletos humanos desparramados en", w / 2 - 10, 120, p);
                c.drawText("la extensa vegetaci�n all� presente.", w / 2 - 10, 140, p);
                break;
            }
            case 6: {
                c.drawBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.screen7)), n, p);
                c.drawText("Armado con su espada recorre otros metros", w / 2 - 10, 60, p);
                c.drawText("m�s, con mucha precauci�n ...", w / 2 - 10, 80, p);
                c.drawText("��matar o morir!�.", w / 2 - 10, 100, p);
                c.drawText("Pero ante sus ojos apareci� algo que le", w / 2 - 10, 120, p);
                c.drawText("hizo temblar. Un enorme y extra�o castillo", w / 2 - 10, 140, p);
                c.drawText("apareci� de entre los �rboles� ", w / 2 - 10, 160, p);
                c.drawText("�la leyenda es real! ", w / 2 - 10, 200, p);
                break;
            }

        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            intro.level++;
        AlphaAnimation _aAnim = new AlphaAnimation(0, 1);
        _aAnim.setDuration(3000);
        this.startAnimation(_aAnim);
        this.invalidate(0, 0, 0, 150);
        return true;
    }

}
