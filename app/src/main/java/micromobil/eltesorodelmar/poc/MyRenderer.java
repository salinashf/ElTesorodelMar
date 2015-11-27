package micromobil.eltesorodelmar.poc;

/**
 * Created by Henry on 25/11/2015.
 */


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
public class MyRenderer implements GLSurfaceView.Renderer {


    Triangle triangle;


    private final float[] projectionMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];


    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        triangle = new Triangle();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);


        float ratio = (float) width / height;


        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }


    @Override
    public void onDrawFrame(GL10 gl) {


        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mVMatrix, 0);


        // Create a rotation transformation for the triangle
        // Create a rotation for the triangle
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);


        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);


        triangle.draw(mMVPMatrix);
    }
}