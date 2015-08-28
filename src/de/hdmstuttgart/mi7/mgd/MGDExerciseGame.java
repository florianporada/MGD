package de.hdmstuttgart.mi7.mgd;

import java.io.IOException;
import java.io.InputStream;

import java.lang.System;
import java.util.ArrayList;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Circle;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.gameObject.GameObject;
import de.hdmstuttgart.mi7.mgd.gameObject.JetObject;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.input.InputEvent;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector2;
import de.hdmstuttgart.mi7.mgd.math.Vector3;
import de.hdmstuttgart.mmi.mgd.R;

import static de.hdmstuttgart.mi7.mgd.math.MathHelper.randfloat;

public class MGDExerciseGame extends Game {

	private Camera sceneCam, hudCam;

	private Matrix4x4 matrixTest;
    private SpriteFont fontTest;
    private TextBuffer textTest;
    private AABB controlBoxLeft, controlBoxRight;
    private JetObject jetObject, missileObject, tmpObject;

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    private boolean pressedLeft = false, pressedRight = false, yas = false, lock = false;
    private int counter;
    private ArrayList<JetObject> boxArray;

	public MGDExerciseGame(View view) {
		super(view);
	}

	@Override
	public void initialize() {
		Matrix4x4 projection = new Matrix4x4();
		Matrix4x4 view = new Matrix4x4();

//		projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
//		view.translate(0, 0, -5);

        //HUDCAM
        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        hudCam = new Camera();
        hudCam.setProjection(projection);
        hudCam.setView(view);

        //SCENECAM
        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 16.0f);
        view = new Matrix4x4();
        view.translate(0, 0, -25);
        sceneCam = new Camera();
        sceneCam.setProjection(projection);
        sceneCam.setView(view);

        //CONTROLS
        controlBoxLeft = new AABB(-500, -900, 500, 500);
        controlBoxRight = new AABB(0, -900, 500, 500);

        //GAMEOBJECTS
        jetObject = new JetObject(new Matrix4x4());
        jetObject.getMatrix().translate(0, -15f, 0);
        jetObject.getMatrix().scale(0.7f);
        missileObject = new JetObject(new Matrix4x4(), new AABB(0,0, 400, 400));
        missileObject.getMatrix().translate(0, -15f, 0);

        tmpObject = new JetObject(new Matrix4x4(), new AABB(0,0, 400, 400));
        tmpObject.getMatrix().translate(-10, 25,0);

        //RANDOM BOXES
        boxArray = boxDropper(10);
    }

	@Override
	public void loadContent() {

		try {
            //JET
            jetObject.loadObject("jetObject.obj", "jetTexture.png", graphicsDevice, view);
            //MISSILE
            missileObject.loadObject("box.obj", "box.png", graphicsDevice, view);
            //TMP
            tmpObject.loadObject("box.obj", "box.png", graphicsDevice, view);

            for(JetObject o : boxArray){
                o.loadObject("box.obj", "box.png", graphicsDevice, view);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        fontTest = graphicsDevice.createSpriteFont(Typeface.DEFAULT, 64);
        textTest = graphicsDevice.createTextBuffer(fontTest, 16);
        textTest.setText("Stahp!");

        matrixTest = Matrix4x4.createTranslation(0, 0, 0);

        //LOAD MEDIAPLAYER
        while (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_loop);
        }
        mediaPlayer.start();

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        clickSound = soundPool.load(context, R.raw.click, 1);
	}

    @Override
    public void update(float deltaSeconds) {
        jetObject.updateHitBoxCircle();
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
                            //System.out.println(inputEvent.getValues()[0]);

                            //COORDINATES JET MATRIX
                            System.out.println("Matrix X: " + jetObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13] + " Matrix Z: " + jetObject.getMatrix().m[14]);
                            //COORDINATES JET HITBOX
                            System.out.println("Circle X: " + jetObject.getHitBoxCircle().getPosition().getX() + " Circle Y: " + jetObject.getHitBoxCircle().getPosition().getY());
                            //COORDINATES MISSILE
                            System.out.println("Missile X: " + missileObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13]+" Matrix Z: "+jetObject.getMatrix().m[14]);

                            Vector3 screenTouchPosition = new Vector3((inputEvent.getValues()[0] / (screenWidth / 2) - 1), -(inputEvent.getValues()[1] / (screenHeight / 2) - 1), 0);

                            Vector3 worldTouchPosition = hudCam.unproject(screenTouchPosition, 1);

                            Point touchPoint = new Point(worldTouchPosition.getX(), worldTouchPosition.getY());

                            if(touchPoint.intersects(jetObject.getHitBoxCircle())){
                                counter++;
                                yas = true;
                                textTest.setText("" + counter);
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                            }

                            if(touchPoint.intersects(controlBoxLeft)){
                                System.out.println("hitbox left");
                                pressedLeft = true;
                            }

                            if(touchPoint.intersects(controlBoxRight)){
                                System.out.println("hitbox right");
                                pressedRight = true;
                            }

                            break;
                        case UP:
                            pressedLeft = false;
                            pressedRight = false;
                            //yas = false;
                            break;
                    }
                    break;
            }
            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();

//            for(JetObject o : boxArray){
//                if(jetObject.getHitBoxCircle().intersects(o.getHitBoxAABB()))
//                    System.out.println("peng");
//            }


            if(pressedLeft){
                jetObject.getMatrix().translate(-deltaSeconds * 10f, 0, 0);
                jetObject.updateHitBoxCircle();
            }

            if(pressedRight) {
                jetObject.getMatrix().translate(deltaSeconds * 10f, 0, 0);
                jetObject.updateHitBoxCircle();
            }

            if (yas) {
                if(!lock){
                    //LOCK X Position from Jet for shoot straight upwards
                    lock = true;
                    missileObject.getMatrix().m[12] = jetObject.getMatrix().m[12];
                }
                //MISSILE SHOOOOOOOT
                missileObject.getMatrix().rotateY(deltaSeconds * 50);
                missileObject.getMatrix().translate(0, 0.2f, 0);

                for(JetObject o : boxArray){
                    o.getMatrix().translate(0, -0.1f, 0);
                }

            }
        }
    }

	@Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        //Stuff fuer LVL
        graphicsDevice.setCamera(sceneCam);
        renderer.drawMesh(jetObject.getMesh(), jetObject.getMatrix(), jetObject.getMaterial());
        //RENDER IF HIT THE jetHitBox
        if(yas){
            renderer.drawMesh(missileObject.getMesh(), missileObject.getMatrix(), missileObject.getMaterial());
            for(JetObject o : boxArray){
                renderer.drawMesh(o.getMesh(), o.getMatrix(), o.getMaterial());
            }
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
        matrixTest.translate(-width / 2, height / 2 - 64, 0);

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        if (mediaPlayer != null)
        	mediaPlayer.pause();
    }

    public ArrayList<JetObject> boxDropper(int amount){
        ArrayList<JetObject> a = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            JetObject o = new JetObject(new Matrix4x4(), new AABB(0,0, 20, 20));
            o.getMatrix().translate(randfloat(-10, 10), randfloat(25, 500), 0);
            a.add(o);
        }
        return a;
    }

}
