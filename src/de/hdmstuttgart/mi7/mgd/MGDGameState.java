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
import de.hdmstuttgart.mi7.mgd.math.MathHelper;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector3;
import de.hdmstuttgart.mmi.mgd.R;

import static de.hdmstuttgart.mi7.mgd.math.MathHelper.randfloat;

public class MGDGameState implements GameState {
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
    private AABB bottomLineBox;
    private AABB gameBox;

    //GAMEOBJECTS
    private JetObject jetObject, tmpObject;
    private WeaponObject missileObject;

    private String[][] randomEnemyObjects = {{"cow.obj", "sphere.bmp",}, {"icosahedron.obj", "tree.png"}, {"teapot.obj", "road.png"}, {"box.obj", "box.png"}};

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    //CONTROL BOOLEANS
    private boolean pressedLeft , pressedRight;
    //BOXGENERATOR BOOLEANS
    private boolean atBottomA, atBottomB;

    private boolean yas = false, lock = false, clockLock = false, startGame = false;
    private boolean missileLock[];
    private int counter, missileCounter;
    private ArrayList<EnemyObject> boxArrayA, boxArrayB;
    private ArrayList<WeaponObject> missileArray;

    //TEST
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    float boxTimeB = 0;


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


        //CONTROLS
        pressedLeft = false;
        pressedRight = false;
        controlRightBox = new AABB(0, -40f,22f, 25f);
        controlLeftBox = new AABB(-22f, -40f,22f, 25f);
        topLeft = new AABB(-22f, 30f, 22f, 10f);
        topRight = new AABB(0, 30f, 22f, 10f);

        //INIT BOXES AND SET BOOL
        atBottomA = false;
        atBottomB = false;
        boxArrayA = boxDropper(10);
        boxArrayB = boxDropper(10);
        //SHOW INDEX OF LOWES BOX
//        for(EnemyObject o : boxArrayA){
//            System.out.println(o.getMatrix().m[13]);
//        }
//        System.out.println("Min: "+MathHelper.getMinValueIndex(boxArrayA));

        //HITBOXEN
        bottomLineBox = new AABB(-25, -41, 50, 0.01f);
        gameBox = new AABB(-25, -40, 50, 80);

        //TEXT
        matrixTest = new Matrix4x4().createTranslation(0, 0, 0);


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
                int i = MathHelper.randInt(0, (randomEnemyObjects.length-1));
                o.loadObject(randomEnemyObjects[i][0], randomEnemyObjects[i][1], graphicsDevice, context);
            }

            //LOAD BOX B ARRAY
            for(EnemyObject o : boxArrayB){
                int i = MathHelper.randInt(0, (randomEnemyObjects.length-1));
                o.loadObject(randomEnemyObjects[i][0], randomEnemyObjects[i][1], graphicsDevice, context);
            }

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        fontTest = graphicsDevice.createSpriteFont(null, 16);
        textTest = graphicsDevice.createTextBuffer(fontTest, 66);
        textTest.setText("Stahp!");

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
                            Vector3 screenTouchPosition = new Vector3((inputEvent.getValues()[0] / (game.getScreenWidth() / 2) - 1), -(inputEvent.getValues()[1] / (game.getScreenHeight() / 2) - 1), 0);

                            Vector3 worldTouchPosition = sceneCam.unproject(screenTouchPosition, 1);

                            Point touchPoint = new Point(worldTouchPosition.getX(), worldTouchPosition.getY());

                            //COORDINATES JET MATRIX
                            System.out.println("Jet X: " + jetObject.getMatrix().m[12] + " Jet Y: " + jetObject.getMatrix().m[13] + " Jet Z: " + jetObject.getMatrix().m[14]);
                            //COORDINATES MISSILE
                            System.out.println("Missile X: " + missileObject.getMatrix().m[12] + " Matrix Y: " + jetObject.getMatrix().m[13]+" Matrix Z: "+jetObject.getMatrix().m[14]);
                            //COORDINATES TOUCH
                            System.out.println("WorldTouch X: "+worldTouchPosition.getX()+" WorldTouch Y: "+worldTouchPosition.getY());

                            if(touchPoint.intersects(topRight)){
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                                yas = true;
                                startGame = true;
                            }

                            if(touchPoint.intersects(topLeft)){
                                if (mediaPlayer != null)
                                    mediaPlayer.release();
                                game.getGameStateManager().setGameState(new MGDMenuState());
                            }

                            if(touchPoint.intersects(controlLeftBox)){
                                System.out.println("Control left");
                                pressedLeft = true;
                            }

                            if(touchPoint.intersects(controlRightBox)){
                                System.out.println("Control right");
                                pressedRight = true;
                            }

                            if(touchPoint.intersects(jetObject.getHitBoxAABB())) {
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                            }

                            break;
                        case UP:
                            pressedLeft = false;
                            pressedRight = false;
                            break;
                    }
                    break;
            }
            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();
            //CONTROLS
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

            //DETECT COLLISION JET and BOXES
            for(EnemyObject o : boxArrayA){
                if(jetObject.getHitBoxAABB().intersects(o.getHitBoxAABB()) && o.isAlive()){
                    o.setAlive(false);
                    counter++;
                    textTest.setText("" + counter);
                    System.out.println("peng!!");
                }
            }

            for(EnemyObject o : boxArrayB){
                if(jetObject.getHitBoxAABB().intersects(o.getHitBoxAABB()) && o.isAlive()){
                    o.setAlive(false);
                    counter++;
                    textTest.setText("" + counter);
                    System.out.println("peng!!");
                }
            }

            //RESET ALIVE STATUS FOR BOXARRAY A
            for(EnemyObject o : boxArrayA){
                if(bottomLineBox.intersects(o.getHitBoxAABB()))
                    o.setAlive(true);
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
                    missileObject.setMatrix(new Matrix4x4());
                    lock = false;
                    boxTimeB = 0;
                }

//                if(!missileLock[missileCounter]){
//                    for(WeaponObject w : missileArray){
//                        w.getMatrix().m[12] = jetObject.getMatrix().m[12];
//                        missileLock[missileCounter] = true;
//                        //missileCounter++;
//                    }
//                }
//
//                if(missileCounter > missileArray.size()){
//                    missileCounter = 0;
//                }
//
//                if(missileLock[missileCounter]){
//                    for(WeaponObject w: missileArray){
//                        w.getMatrix().rotateY(deltaSeconds * 50);
//                        w.getMatrix().translate(0, 0.2f, 0);
//                    }
//                }


                //MISSILE SHOOOOOOOT
                missileObject.getMatrix().rotateY(deltaSeconds * 50);
                missileObject.getMatrix().translate(0, 0.2f, 0);


                //STAY IN GAMEBOX
                if(!jetObject.getHitBoxAABB().intersects(gameBox)){
                    System.out.println("come back inside!!!");
                }else{

                }


                //BOXES FROM TOP TO BOTTOM
                for(EnemyObject o : boxArrayA) {
                    o.getMatrix().translate(0, o.getSpeed(), 0);
                    o.getMatrix().rotateY(MathHelper.randfloat(0.02f, 0.8f));
                    o.updateHitBoxAABB();
                }

                for(EnemyObject o : boxArrayB) {
                    o.getMatrix().translate(0, o.getSpeed(), 0);
                    o.getMatrix().rotateY(MathHelper.randfloat(0.02f, 0.1f));
                    o.updateHitBoxAABB();
                }

                //GET LOWEST OBJECT FROM BOXARRAY A
                if(bottomLineBox.intersects(boxArrayA.get(MathHelper.getMinValueIndex(boxArrayA)).getHitBoxAABB())){
                    atBottomA = true;
                    //System.out.println("BOX A IS AT THE BOTTOM");
                }

                //GET LOWEST OBJECT FROM BOXARRAY B
                if(bottomLineBox.intersects(boxArrayB.get(MathHelper.getMinValueIndex(boxArrayB)).getHitBoxAABB())){
                    atBottomB = true;
                    //System.out.println("BOX B IS AT THE BOTTOM");
                }

                //TRIGGER OTHER ARRAY
                if(atBottomA && !atBottomB){
                    boxRandomizer(boxArrayB, 40, 120);
                    atBottomA = false;
                    //System.out.println("trigger boxArrayB");
                }

                //TRIGGER OTHER ARRAY
                if(atBottomB && !atBottomA){
                    boxRandomizer(boxArrayA, 40, 120);
                    atBottomB = false;
                    //System.out.println("trigger boxArrayA");
                }
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
            o.setMatrix(new Matrix4x4().createTranslation(randfloat(-25, 25), randfloat(minY, maxY), 0));
        }
    }

}
