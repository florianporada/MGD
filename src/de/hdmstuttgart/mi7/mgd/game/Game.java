package de.hdmstuttgart.mi7.mgd.game;

import android.opengl.GLSurfaceView;

/**
 * Created by florianporada on 25.08.15.
 */
public abstract class Game implements GLSurfaceView.Renderer {
    public abstract void initialize();

    public abstract void update(float deltaSeconds);

    public abstract void draw(float deltaSeconds);

    public abstract void resize(int width, int height);

}
