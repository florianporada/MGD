package de.hdmstuttgart.mi7.mgd.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.graphics.GraphicsDevice;
import de.hdmstuttgart.mi7.mgd.graphics.Renderer;
import de.hdmstuttgart.mi7.mgd.input.InputSystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by florianporada on 25.08.15.
 */
public abstract class Game implements GLSurfaceView.Renderer {
    private boolean initialized;
    private long lastTime;

    protected Context context;
    protected GraphicsDevice graphicsDevice;
    protected Renderer renderer;
    protected InputSystem inputSystem;
    protected View view;


    public Game(View view) {
        this.view = view;
        this.context = view.getContext();

        inputSystem = new InputSystem(view);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        lastTime = System.currentTimeMillis();

        if (!initialized) {
            graphicsDevice = new GraphicsDevice();
            graphicsDevice.onSurfaceCreated(gl);

            renderer = new Renderer(graphicsDevice);

            initialize();
            initialized = true;

            loadContent();
        } else {
            graphicsDevice.onSurfaceCreated(gl);
            loadContent();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        graphicsDevice.resize(width, height);
        resize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long currTime = System.currentTimeMillis();
        float deltaSeconds = (currTime - lastTime) / 1000.0f;

        update(deltaSeconds);
        draw(deltaSeconds);

        lastTime = currTime;
    }

    public abstract void initialize();
    public abstract void update(float deltaSeconds);
    public abstract void draw(float deltaSeconds);
    public abstract void resize(int width, int height);
    public abstract void loadContent();
}
