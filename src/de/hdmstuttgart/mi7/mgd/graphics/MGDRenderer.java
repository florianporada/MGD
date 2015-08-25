package de.hdmstuttgart.mi7.mgd.graphics;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by florianporada on 25.08.15.
 */
public class MGDRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        if(System.currentTimeMillis() % 2 == 0){
            gl.glClearColor(1.9f, 1.5f, 2.2f, 0.3f);
        }else{
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
