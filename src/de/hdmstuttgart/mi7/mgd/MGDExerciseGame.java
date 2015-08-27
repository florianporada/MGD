package de.hdmstuttgart.mi7.mgd;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.input.InputEvent;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector3;

public class MGDExerciseGame extends Game {

	private Camera sceneCam, hudCam;
	private Mesh meshJet, meshBox;
	private Texture textureJet, textureBox;
	private Material materialJet, materialBox;
	private Matrix4x4 matrixJet, matrixBox, matrixTest, matrixTitle;
    private SpriteFont fontTest, fontTitle;
    private TextBuffer textTest, textTitle;
    private AABB aabbTest;

    private boolean pressed = false, yas = false;
    private int counter;

	public MGDExerciseGame(View view) {
		super(view);
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
        view.translate(0, 0, -5);
        sceneCam = new Camera();
        sceneCam.setProjection(projection);
        sceneCam.setView(view);

        materialJet = new Material();
        matrixJet = new Matrix4x4();
        matrixJet.scale(0.2f);

        materialBox = new Material();
        matrixBox = new Matrix4x4();
        matrixBox.translate(20f, -200f, 0);
    }

	@Override
	public void loadContent() {

		try {
			InputStream stream;

            //JET
            stream = context.getAssets().open("jet.obj");
            meshJet = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("jet.png");
            textureJet = graphicsDevice.createTexture(stream);
            materialJet.setTexture(textureJet);
            matrixJet.translate(0, 0, 0);

            //BOX
            stream = context.getAssets().open("box.obj");
            meshBox = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("road.png");
            textureBox = graphicsDevice.createTexture(stream);
            materialBox.setTexture(textureBox);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        fontTest = graphicsDevice.createSpriteFont(Typeface.DEFAULT, 64);
        textTest = graphicsDevice.createTextBuffer(fontTest, 16);
        textTest.setText("Stahp!");

        matrixTest = Matrix4x4.createTranslation(0, 0, 0);
        aabbTest = new AABB(0, 0, 400, 400);
	}

    @Override
    public void update(float deltaSeconds) {
        InputEvent inputEvent = inputSystem.peekEvent();
        while (inputEvent != null) {

            switch (inputEvent.getInputDevice()) {
                case KEYBOARD:
                    switch (inputEvent.getInputAction()) {
                        case DOWN:
                            switch (inputEvent.getKeycode()) {
                                case KeyEvent.KEYCODE_MENU:
                                    System.out.println("key pressed");
                                    break;
                            }
                            break;
                    }
                    break;
                case TOUCHSCREEN:
                    switch (inputEvent.getInputAction()) {
                        case DOWN:
                            //Get Y
                            System.out.println(inputEvent.getValues()[0]);
                            //GET X
                            System.out.println(inputEvent.getValues()[1]);
                            pressed = true;


                            Vector3 screenTouchPosition = new Vector3(
                                    (inputEvent.getValues()[0] / (screenWidth / 2) - 1),
                                    -(inputEvent.getValues()[1] / (screenHeight / 2) - 1),
                                    0);

                            Vector3 worldTouchPosition = hudCam.unproject(screenTouchPosition, 1);

                            Point touchPoint = new Point(
                                    worldTouchPosition.getX(),
                                    worldTouchPosition.getY());

                                if (touchPoint.intersects(aabbTest)){
                                    counter++;
                                    yas = true;
                                    textTest.setText("asdf"+counter);

                                    System.out.println(aabbTest.getPosition());
                                }
                            break;
                        case UP:
                            pressed = false;
                            yas = false;
                            break;
                    }
                    break;
            }
            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();
        }
        if(pressed){
            matrixJet.rotateY(deltaSeconds * 25);
            matrixJet.translate(0, deltaSeconds * 0.8f, 0);
        }
    }

	@Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        //Stuff fuer LVL
        graphicsDevice.setCamera(sceneCam);
        renderer.drawMesh(meshJet, matrixJet, materialJet);
        renderer.drawMesh(meshBox, matrixBox, materialBox);

        if(yas){
            renderer.drawText(textTest, matrixJet);
        }



        //Stuff fue HUD
        graphicsDevice.setCamera(hudCam);
        renderer.drawText(textTest, matrixTest);

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

        matrixTest.setIdentity();
        //matrixTest.translate(-width / 2, height / 2 - 64, 0);

    }

}
