package de.hdmstuttgart.mi7.mgd.graphics;

import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by florianporada on 25.08.15.
 */
public class GraphicsDevice {

    private GL10 gl;

    public void onSurfaceCreated(GL10 gl){
        this.gl = gl;
    }

    public void clear(float red, float green, float blue, float alpha){
        gl.glClearColor(red, green, blue, alpha);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void clear(float red, float green, float blue){
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void resize(int width, int height){
        gl.glViewport(0, 0, width, height);
    }

    public void setCamera(Camera camera){
        Matrix4x4 viewProjection = Matrix4x4.multiply(camera.getProjection(), camera.getView());

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(viewProjection.m, 0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }
}
