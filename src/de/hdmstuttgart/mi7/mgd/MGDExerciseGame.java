package de.hdmstuttgart.mi7.mgd;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Typeface;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

public class MGDExerciseGame extends Game {

	private Camera sceneCam, hudCam;
	private Mesh meshJet;
	private Texture texJet;
	private Material matJet;
	private Matrix4x4 worldJet;
    private SpriteFont font;
    private TextBuffer text;
    private Matrix4x4 hudText;

	public MGDExerciseGame(Context context) {
		super(context);
	}

	@Override
	public void initialize() {
		Matrix4x4 projection = new Matrix4x4();
		Matrix4x4 view = new Matrix4x4();

//		projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
//		view.translate(0, 0, -5);

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        hudCam = new Camera();
        hudCam.setProjection(projection);
        hudCam.setView(view);

        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 16.0f);
        view = new Matrix4x4();
        view.translate(0, 0, -5 );
        sceneCam = new Camera();
        sceneCam.setProjection(projection);
        sceneCam.setView(view);

        matJet = new Material();
        worldJet = new Matrix4x4();
        worldJet.scale(0.2f);

        hudText = new Matrix4x4();
        hudText.scale(2.3f);
        hudText.translate(300f, -900f, 0);

    }

	@Override
	public void loadContent() {
        font = graphicsDevice.createSpriteFont(Typeface.DEFAULT, 16);
        text = graphicsDevice.createTextBuffer(font, 128);
        text.setText("Stahp!");

		try {
			InputStream stream;

            stream = context.getAssets().open("jet.obj");
            meshJet = Mesh.loadFromOBJ(stream);
            stream = context.getAssets().open("jet.png");
            texJet = graphicsDevice.createTexture(stream);
            matJet.setTexture(texJet);
            worldJet.translate(0, 0, 0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    public void update(float deltaSeconds) {
        worldJet.rotateY(deltaSeconds * 25);
        worldJet.translate(0, deltaSeconds * 0.2f , 0);
        renderer.drawText(text, hudText);
    }

	@Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        //Stuff fuer LVL
        graphicsDevice.setCamera(sceneCam);
        renderer.drawMesh(meshJet, worldJet, matJet);
        renderer.drawText(text, worldJet);

        //Stuff fue HUD
        graphicsDevice.setCamera(hudCam);
        renderer.drawText(text, hudText);

    }

	@Override
	public void resize(int width, int height) {
        float aspect = (float)width / (float)height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        hudCam.setProjection(projection);

        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f * aspect, 0.1f * aspect, -0.1f, 0.1f, 0.1f, 100.0f);
        sceneCam.setProjection(projection);

        hudText.translate(-width / 2, height / 2 - 16, 0);

    }

}
