package de.hdmstuttgart.mi7.mgd;

import de.hdmstuttgart.mi7.mgd.game.Game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by florianporada on 25.08.15.
 */
public class MGDExerciseGame extends Game {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        if(System.currentTimeMillis() % 2 == 0){
            gl.glClearColor(1.9f, 1.5f, 2.2f, 0.3f);
        }else{
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(float deltaSeconds) {

    }

    @Override
    public void draw(float deltaSeconds) {

    }

    @Override
    public void resize(int width, int height) {

    }
}
