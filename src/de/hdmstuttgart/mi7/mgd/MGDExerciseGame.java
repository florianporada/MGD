package de.hdmstuttgart.mi7.mgd;

import android.content.Context;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.graphics.Camera;
import de.hdmstuttgart.mi7.mgd.graphics.Mesh;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.InputStream;
import java.io.IOException;
/**
 * Created by florianporada on 25.08.15.
 */
public class MGDExerciseGame extends Game {

    private Camera camera;
    private Mesh cube;
    private Matrix4x4 world;

    public MGDExerciseGame(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
        view.translate(0, 0, -5);

        camera = new Camera();
        camera.setProjection(projection);
        camera.setView(view);

        try {
            InputStream stream = context.getAssets().open("cube.obj");
            cube = Mesh.loadFromOBJ(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        world = new Matrix4x4();
    }

    @Override
    public void update(float deltaSeconds) {
        world.rotateY(deltaSeconds * 45);
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.75f, 1.0f);
        graphicsDevice.setCamera(camera);

        renderer.drawMesh(cube, world);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;

        Matrix4x4 projection = new Matrix4x4();
        projection.setPerspectiveProjection(-aspect * 0.1f, aspect * 0.1f, -0.1f, 0.1f, 0.1f, 100f);

        camera.setProjection(projection);
    }
}
