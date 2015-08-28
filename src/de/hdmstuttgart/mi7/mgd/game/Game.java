package de.hdmstuttgart.mi7.mgd.game;

import android.app.Activity;
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
public class Game implements GLSurfaceView.Renderer {
    private boolean initialized;
    private long lastTime;

    protected Context context;
    protected GraphicsDevice graphicsDevice;
    protected Renderer renderer;
    protected InputSystem inputSystem;
    protected View view;
    protected int screenWidth;
    protected int screenHeight;

    protected GameStateManager gameStateManager;


    public Game(View view) {
        this.view = view;
        this.context = view.getContext();
        this.screenWidth = 1;
        this.screenHeight = 1;
        inputSystem = new InputSystem(view);
        this.inputSystem = new InputSystem(view);
        this.gameStateManager = new GameStateManager(this);
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
        screenWidth = width;
        screenHeight = height;
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

    public Context getContext(){
        return context;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public View getView() {
        return view;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void finish() {
        view.post(new Runnable() {
            public void run() {
                ((Activity)context).finish();
            }
        });
    }

    public void initialize() {
    }

    public void loadContent() {
        gameStateManager.loadContent();
    }

    public void update(float deltaSeconds) {
        gameStateManager.update(deltaSeconds);
    }

    public void draw(float deltaSeconds) {
        gameStateManager.draw(deltaSeconds);
    }

    public void resize(int width, int height) {
        gameStateManager.resize(width, height);
    }

    public void pause() {
        gameStateManager.pause();
    }

    public void resume() {
        gameStateManager.resume();
    }
}
