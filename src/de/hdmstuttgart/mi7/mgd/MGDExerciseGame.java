package de.hdmstuttgart.mi7.mgd;

import java.io.IOException;

import java.lang.System;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.KeyEvent;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.game.GameState;
import de.hdmstuttgart.mi7.mgd.gameObject.EnemyObject;
import de.hdmstuttgart.mi7.mgd.gameObject.JetObject;
import de.hdmstuttgart.mi7.mgd.gameObject.WeaponObject;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.input.InputEvent;
import de.hdmstuttgart.mi7.mgd.input.InputSystem;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector3;
import de.hdmstuttgart.mmi.mgd.R;

import static de.hdmstuttgart.mi7.mgd.math.MathHelper.randfloat;

public class MGDExerciseGame implements GameState {
    private GraphicsDevice graphicsDevice;
    private Context context;
    Renderer renderer;


    private Camera sceneCam, hudCam;

	private Matrix4x4 matrixTest, projection, view;
    private SpriteFont fontTest;
    private TextBuffer textTest;
    //DECLARE CONTROLBOXES
    private AABB controlLeftBox, controlRightBox, topLeft,topRight;
    //OTHER BOXES
    private AABB testBox, bottomLineBox;

    private JetObject jetObject, tmpObject;
    private WeaponObject missileObject;

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    //CONTROL BOOLEANS
    private boolean pressedLeft = false, pressedRight = false;
    private boolean yas = false, lock = false, clockLock = false, startGame = false;
    private boolean missileLock[];
    private int counter, missileCounter;
    private ArrayList<EnemyObject> boxArrayA, boxArrayB;
    private ArrayList<WeaponObject> missileArray;

    //TEST
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    float boxTimeA = 1, boxTimeB = -2;


    @Override
	public void initialize(Game game) {
        if(context == null)
            context = game.getContext();

        //SCENECAM
        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 100f);
        //projection.setPerspectiveProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 1f);

        view = new Matrix4x4();
        view.translate(0, 0, -10);
        sceneCam = new Camera();
        sceneCam.setProjection(projection);
        sceneCam.setView(view);

        //HUDCAM
//        projection = new Matrix4x4();
//        projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
//        view = new Matrix4x4();
//        hudCam = new Camera();
//        hudCam.setProjection(projection);
//        hudCam.setView(view);

        //CONTROLS
        controlRightBox = new AABB(0, -40f,22f, 25f);
        controlLeftBox = new AABB(-22f, -40f,22f, 25f);
        topLeft = new AABB(-22f, 30, 22f, 10f);
        topRight = new AABB(0, 30, 22f, 10f);

        //HITBOXEN
        //testBox = new AABB(0, 0, 10, 10);
        bottomLineBox = new AABB(-25, -41, 50, 1);


        //GAMEOBJECTS
        jetObject = new JetObject(new Matrix4x4().createTranslation(0, -15f, 0), 5f, 5f);
        //jetObject.getMatrix().scale(0.7f);
        missileObject = new WeaponObject(new Matrix4x4().createTranslation(0, -15f, 0), 20f, 20f);

        //INITIALIZE MISSILE ARRAY AND MISSILELOCK
        missileArray = new ArrayList<>();
        missileLock = new boolean[4];
        for(int i = 0; i < missileLock.length; i++){
            missileArray.add(new WeaponObject(new Matrix4x4(), 20f, 20f));
            missileLock[i] = false;
        }

        tmpObject = new JetObject(new Matrix4x4(), 20f, 20f);
        tmpObject.getMatrix().translate(-10, 25,0);

        //RANDOM BOXES
        boxArrayA = boxDropper(1);
        boxArrayB = boxDropper(1);
        //executorService.scheduleAtFixedRate(boxRandomizer(boxArrayA), 0, 7, TimeUnit.SECONDS);

    }

	@Override
	public void loadContent(Game game) {
        if(context == null)
            context = game.getContext();
        if(graphicsDevice == null)
            graphicsDevice = game.getGraphicsDevice();


        try {
            //JET
            jetObject.loadObject("jetObject.obj", "jetTexture.png", graphicsDevice, context);
            //MISSILE
            missileObject.loadObject("box.obj", "box.png", graphicsDevice, context);
            //TMP
            tmpObject.loadObject("box.obj", "box.png", graphicsDevice, context);

            //LOAD MISSILE ARRAY
            for(WeaponObject o : missileArray){
                o.loadObject("box.obj", "box.png", graphicsDevice, context);
            }

            //LOAD BOX A ARRAY
            for(EnemyObject o : boxArrayA){
                o.loadObject("box.obj", "box.png", graphicsDevice, context);
            }

            //LOAD BOX B ARRAY
            for(EnemyObject o : boxArrayB){
                o.loadObject("box.obj", "box.png", graphicsDevice, context);
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
        mediaPlayer.setLooping(true);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(attributes).build();
        clickSound = soundPool.load(context, R.raw.click, 1);
	}

    @Override
    public void update(Game game, float deltaSeconds) {
        InputSystem inputSystem = game.getInputSystem();
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
                            //COORDINATES JET MATRIX
                            System.out.println("Matrix X: " + jetObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13] + " Matrix Z: " + jetObject.getMatrix().m[14]);
                            //COORDINATES JET HITBOX
//                            System.out.println("Circle X: " + jetObject.getHitBoxCircle().getPosition().getX() + " Circle Y: " + jetObject.getHitBoxCircle().getPosition().getY());
                            //COORDINATES MISSILE
                            System.out.println("Missile X: " + missileObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13]+" Matrix Z: "+jetObject.getMatrix().m[14]);

                            Vector3 screenTouchPosition = new Vector3((inputEvent.getValues()[0] / (game.getScreenWidth() / 2) - 1), -(inputEvent.getValues()[1] / (game.getScreenHeight() / 2) - 1), 0);

                            Vector3 worldTouchPosition = sceneCam.unproject(screenTouchPosition, 1);
                            //Vector3 worldTouchPosition = sceneCam.unproject(screenTouchPosition, 1);


                            Point touchPoint = new Point(worldTouchPosition.getX(), worldTouchPosition.getY());

                            System.out.println("WorldTouch X: "+worldTouchPosition.getX()+" WorldTouch Y: "+worldTouchPosition.getY());
                            System.out.println("Jet X: "+jetObject.getMatrix().m[12]+" Jet Y: "+jetObject.getMatrix().m[13]);


                            if(touchPoint.intersects(topRight)){
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                                counter++;
                                yas = true;
                                startGame = true;
                                textTest.setText("" + counter);
                                //COORDINATES JET MATRIX
                                System.out.println("Matrix X: " + jetObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13] + " Matrix Z: " + jetObject.getMatrix().m[14]);
                                //COORDINATES JET HITBOX
                                System.out.println("Circle X: " + jetObject.getHitBoxAABB().getPosition().getX() + " Circle Y: " + jetObject.getHitBoxAABB().getPosition().getY());
                                //COORDINATES MISSILE
                                System.out.println("Missile X: " + missileObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13]+" Matrix Z: "+jetObject.getMatrix().m[14]);

                            }

                            if(touchPoint.intersects(controlLeftBox)){
                                System.out.println("Control left");
                                pressedLeft = true;
                            }

                            if(touchPoint.intersects(controlRightBox)){
                                System.out.println("Control right");
                                pressedRight = true;
                            }

                            if(touchPoint.intersects(jetObject.getHitBoxAABB())){
                                System.out.println("topRight");
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                                jetObject.setControlSpeed(0.095f);
                                boxRandomizer(boxArrayB, 20, 50);
                            }

                            if(touchPoint.intersects(topLeft)){
                                if (mediaPlayer != null)
                                    mediaPlayer.release();
                                game.getGameStateManager().setGameState(new MGDMenuState());
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

            for(EnemyObject o : boxArrayA){
                if(jetObject.getHitBoxAABB().intersects(o.getHitBoxAABB()) && o.isAlive()){
                    o.setAlive(false);
                    System.out.println("peng!!");
                }

            }


            if(pressedLeft){
                jetObject.getMatrix().translate(-jetObject.getControlSpeed(), 0, 0);
                jetObject.getMatrix().rotateY(-0.1f);
                jetObject.updateHitBoxAABB();
            }


            if(pressedRight) {
                jetObject.getMatrix().translate(jetObject.getControlSpeed(), 0, 0);
                jetObject.getMatrix().rotateY(0.1f);
                jetObject.updateHitBoxAABB();
            }


            //IF HITBOX jetObject is pressed
            if (startGame) {
                if(!lock){
                    missileObject.getMatrix().m[12] = jetObject.getMatrix().m[12];
                    missileObject.getMatrix().translate(0, -15f, 0);
                    //LOCK X Position from Jet for shoot straight upwards
                    lock = true;
                }
                boxTimeB = boxTimeB + deltaSeconds/10;
                if(boxTimeB > 0.5){
                    System.out.println("reset clock B");
                    missileObject.setMatrix(new Matrix4x4());

                    lock = false;
                    boxTimeB = 0;
                }

                if(!missileLock[missileCounter]){
                    for(WeaponObject w : missileArray){
                        w.getMatrix().m[12] = jetObject.getMatrix().m[12];
                        missileLock[missileCounter] = true;
                        //missileCounter++;
                    }
                }

                if(missileCounter > missileArray.size()){
                    missileCounter = 0;
                }

                if(missileLock[missileCounter]){
                    for(WeaponObject w: missileArray){
                        w.getMatrix().rotateY(deltaSeconds * 50);
                        w.getMatrix().translate(0, 0.2f, 0);
                    }
                }


                //MISSILE SHOOOOOOOT
                missileObject.getMatrix().rotateY(deltaSeconds * 50);
                missileObject.getMatrix().translate(0, 0.2f, 0);


                for(EnemyObject o : boxArrayA) {
                    o.getMatrix().translate(0, o.getSpeed(), 0);
                    o.updateHitBoxAABB();
                }

                for(EnemyObject o : boxArrayB) {
                    o.getMatrix().translate(0, o.getSpeed(), 0);
                    o.updateHitBoxAABB();
                }

                boxTimeA = boxTimeA + deltaSeconds/10;
                if(boxTimeA > 1.7){
                    System.out.println("reset clock A");
                    boxRandomizer(boxArrayA, 20, 50);
                    boxTimeA = 0;
                }
                for(EnemyObject o : boxArrayA){
                    if(bottomLineBox.intersects(o.getHitBoxAABB()))
                        o.setAlive(true);
                }
//                boxTimeB = boxTimeB + deltaSeconds/10;
//                if(boxTimeB > 2){
//                    System.out.println("reset clock B");
//                    boxRandomizer(boxArrayB, 25, 50);
//                    boxTimeB = 0;
//                }
            }



        }
    }

	@Override
    public void draw(Game game, float deltaSeconds) {
        if(context == null)
            context = game.getContext();
        if(graphicsDevice == null)
            graphicsDevice = game.getGraphicsDevice();
        if(renderer == null)
            renderer = game.getRenderer();

        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        //Stuff fuer LVL
        graphicsDevice.setCamera(sceneCam);
        renderer.drawMesh(jetObject.getMesh(), jetObject.getMatrix(), jetObject.getMaterial());
        renderer.drawText(textTest, matrixTest);


        for(EnemyObject o : boxArrayA){
            renderer.drawMesh(o.getMesh(), o.getMatrix(), o.getMaterial());
        }

        for(EnemyObject o : boxArrayB){
            renderer.drawMesh(o.getMesh(), o.getMatrix(), o.getMaterial());
        }

        //RENDER IF HIT THE jetHitBox
        if(yas){
            renderer.drawMesh(missileObject.getMesh(), missileObject.getMatrix(), missileObject.getMaterial());
            for(WeaponObject w : missileArray){
                renderer.drawMesh(w.getMesh(), w.getMatrix(), w.getMaterial());
            }
        }

        //Stuff fue HUD
//        graphicsDevice.setCamera(hudCam);
//        renderer.drawText(textTest, matrixTest);

    }

	@Override
	public void resize(Game game, int width, int height) {
        float aspect = (float)width / (float)height;
        Matrix4x4 projection;

//        projection = new Matrix4x4();
//        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
//        hudCam.setProjection(projection);

        projection = new Matrix4x4();
//        projection.setPerspectiveProjection(-0.1f * aspect, 0.1f * aspect, -0.1f, 0.1f, 0.1f, 100.0f);
        projection.setOrhtogonalProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 100f);
        sceneCam.setProjection(projection);

        matrixTest.setIdentity();
        matrixTest.translate(-width / 2, height / 2 - 64, 0);

    }

    @Override
    public void resume(Game game) {

    }

    @Override
    public void pause(Game game) {
        if (mediaPlayer != null)
        	mediaPlayer.pause();
    }

    @Override
    public void shutdown(Game game) {

    }

    public ArrayList<EnemyObject> boxDropper(int amount){
        ArrayList<EnemyObject> a = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            EnemyObject o = new EnemyObject(new Matrix4x4().createTranslation(randfloat(-25, 25), randfloat(10, 50), 0), 2f, 2f);
            a.add(o);
        }
        return a;
    }

    public EnemyObject boxGenerator(){
        EnemyObject a = new EnemyObject(new Matrix4x4(), 20f, 20f);
        a.getMatrix().translate(randfloat(-10, 10), randfloat(25, 40), 0);
        return a;
    }

    public void boxRandomizer(ArrayList<EnemyObject> b, float minY, float maxY){
        for(EnemyObject o : b){
            o.setMatrix(new Matrix4x4().createTranslation(randfloat(-10, 10), randfloat(minY, maxY), 0));
        }
    }

}
