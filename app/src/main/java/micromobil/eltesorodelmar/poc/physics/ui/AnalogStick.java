package micromobil.eltesorodelmar.poc.physics.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import micromobil.eltesorodelmar.poc.physics.MyEvent;

public class AnalogStick extends View implements OnTouchListener{
	private Canvas canvas;
	private Paint paint;
	
	private float analogStickX, analogStickY;
	
	public AnalogStick(Context context) {
		super(context);
		
		this.paint = new Paint();
		setOnTouchListener(this);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if(this.canvas == null) this.canvas = canvas;
		
		paint.setColor(Color.rgb(100, 100, 100));
		paint.setAlpha(150);
		
		float centerX = canvas.getWidth() / 2;
		float centerY = canvas.getHeight() / 2;
		
		if(analogStickX == 0.0f && analogStickY == 0.0f){
			analogStickX = centerX;
			analogStickY = centerY;
		}
		
		// 背景を描画
		canvas.drawCircle(centerX, centerY, centerX * 0.9f, paint);
		
		paint.setColor(Color.BLACK);
		
		// アナログスティック本体を描画
		canvas.drawCircle(analogStickX, analogStickY, centerX * 0.675f, paint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		MyEvent myEvent = MyEvent.getInstance();
		myEvent.onAnalogStick(canvas, e, this);
		
		invalidate();
		
		return true;
	}
	
	public void setPosition(float analogStickX, float analogStickY){
		this.analogStickX = analogStickX;
		this.analogStickY = analogStickY;
	}

}
