package micromobil.eltesorodelmar;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.threed.jpct.SimpleVector;
import micromobil.eltesorodelmar.GL.Render;

public class CameraSensor implements SensorEventListener {
    Render game;
    private float[] rotationMatrix = new float[9];
    private float[] accelGData = new float[3];
    private float[] bufferedAccelGData = new float[3];
    private float[] magnetData = new float[3];
    private float[] bufferedMagnetData = new float[3];
    private float[] resultingAngles = new float[3];

    public CameraSensor(Render game) {
        this.game = game;
    }

    public void moveCameraGun() {
        game.camGun.getBack().setIdentity();
        game.camGun.setOrientation(new SimpleVector(0, 0, 1), new SimpleVector(0, -1, 0));
        //game.camGun.rotateCameraX((float) Math.PI / 2);

        System.out.println("resultingAngle: " + resultingAngles[0] + ", " + resultingAngles[1] + ", " + resultingAngles[2]);

		/*game.camGun.rotateCameraZ(-resultingAngles[0]);
		game.camGun.rotateCameraX(resultingAngles[2]);
		game.camGun.rotateCameraY(resultingAngles[1]);*/

        game.camGun.rotateCameraY(resultingAngles[1]);
        game.camGun.rotateCameraX(resultingAngles[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        loadNewSensorData(event);

        rootMeanSquareBuffer(bufferedAccelGData, accelGData);
        rootMeanSquareBuffer(bufferedMagnetData, magnetData);
        SensorManager.getRotationMatrix(rotationMatrix, null,
                bufferedAccelGData, bufferedMagnetData);
        final float[] anglesInRadians = new float[3];
        SensorManager.getOrientation(rotationMatrix, anglesInRadians);
        // TODO check for landscape mode
        resultingAngles[0] = anglesInRadians[0]; // * rad2deg;
        resultingAngles[1] = anglesInRadians[1]; // * rad2deg;
        resultingAngles[2] = anglesInRadians[2]; // * -rad2deg;
    }

    private void rootMeanSquareBuffer(float[] target, float[] values) {

        final float amplification = 200.0f;
        float buffer = 20.0f;

        target[0] += amplification;
        target[1] += amplification;
        target[2] += amplification;
        values[0] += amplification;
        values[1] += amplification;
        values[2] += amplification;

        target[0] = (float) (Math
                .sqrt((target[0] * target[0] * buffer + values[0] * values[0])
                        / (1 + buffer)));
        target[1] = (float) (Math
                .sqrt((target[1] * target[1] * buffer + values[1] * values[1])
                        / (1 + buffer)));
        target[2] = (float) (Math
                .sqrt((target[2] * target[2] * buffer + values[2] * values[2])
                        / (1 + buffer)));

        target[0] -= amplification;
        target[1] -= amplification;
        target[2] -= amplification;
        values[0] -= amplification;
        values[1] -= amplification;
        values[2] -= amplification;
    }

    private void loadNewSensorData(SensorEvent event) {
        final int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            accelGData = event.values.clone();
        }
        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetData = event.values.clone();
        }
    }
}
