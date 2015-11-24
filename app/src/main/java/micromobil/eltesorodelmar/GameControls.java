package micromobil.eltesorodelmar;

import android.graphics.Point;
import android.view.MotionEvent;

public class GameControls {

    public float initx = 58;
    public float inity = 255;
    public Point touchingPoint = new Point(58, 255);
    public Boolean dragging = false;

    private MotionEvent lastEvent;

    public void update(MotionEvent event) {
        if (event != null)
            if (event == null && lastEvent == null) {
                return;
            } else if (event == null && lastEvent != null) {
                event = lastEvent;
            } else {
                lastEvent = event;
            }
        //drag drop
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getX() < 130 && event.getX() >= 0 && event.getY() < 330 && event.getY() > 190) {
                touchingPoint.x = (int) event.getX();
                touchingPoint.y = (int) event.getY();
                dragging = true;
            } else {
                dragging = false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            dragging = false;
        }

        double angle = Math.atan2(touchingPoint.y - inity, touchingPoint.x - initx) / (Math.PI / 180);

        if (dragging) {
            double d = distance(touchingPoint.x, (int) initx, touchingPoint.y, (int) inity);
            double r = d / 40;
            if (r > 1) r = 1;
            touchingPoint.y = (int) (inity + (Math.sin(angle * (Math.PI / 180)) * 40 * r));
            touchingPoint.x = (int) (initx + (Math.cos(angle * (Math.PI / 180)) * 40 * r));


        } else if (!dragging) {
            // Snap back to center when the joystick is released
            touchingPoint.x = (int) initx;
            touchingPoint.y = (int) inity;
        }
    }

    public double distance(int x, int x1, int y, int y1) {
        double r = ((x1 - x) * (x1 - x)) + ((y1 - y) * (y1 - y));
        return (Math.sqrt(r));
    }
}
