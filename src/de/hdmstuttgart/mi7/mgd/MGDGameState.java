package de.hdmstuttgart.mi7.mgd;

import java.io.IOException;

import java.lang.System;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.KeyEvent;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.Filestuff;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.game.GameState;
import de.hdmstuttgart.mi7.mgd.gameObject.EnemyObject;
import de.hdmstuttgart.mi7.mgd.gameObject.JetObject;
import de.hdmstuttgart.mi7.mgd.gameObject.PowerUpObject;
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
    private Filestuff fs;


    private Camera sceneCam, hudCam;

	private Matrix4x4 projection, view;
    //DECLARE TEXT
    private Matrix4x4 matrixHitCount, matrixKillCount, matrixLevelCount, matrixGameOver, matrixStartGame, matrixBack;
    private SpriteFont fontHitCount, fontKillCount, fontLevelCount, fontGameOver, fontStartGame, fontBack;
    private TextBuffer textHitCount, textKillCount, textLevelCount, textGameOver, textStartGame, textBack;
    //DECLARE CONTROLBOXES
    private AABB controlLeftBox, controlRightBox, topLeft, startGameBox;
    //OTHER BOXES
    private AABB bottomLineBox, topLineBox;

    //GAMEOBJECTS
    private PowerUpObject powerUpObject;
    private JetObject jetObject;
    private WeaponObject missileObject;

    private String[][] randomEnemyObjects = {{"cow.obj", "road.png",}, {"sphere.obj", "road.png"}, {"teapot.obj", "road.png"}, {"cube.obj", "road.png"}};

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound, duckSound1, duckSound2, noSound, fartSound;

    //CONTROL BOOLEANS
    private boolean pressedLeft , pressedRight;
    //BOXGENERATOR BOOLEANS
    private boolean atBottomA, atBottomB;

    private boolean startGame, gameOver;
    //COUNTER
    private int hitCounter, killCounter, levelCounter;
    //ENEMYOBJECTS
    private ArrayList<EnemyObject> boxArrayA, boxArrayB;

    //LVL DIFFICULTY
    float lvlTime, gameOverTime,powerUpTime;
    int boxCount;

    //TEST
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
	public void initialize(Game game) {
        if(context == null)
            context = game.getContext();

        float width = game.getScreenWidth();
        float height = game.getScreenHeight();

        fs = new Filestuff(game);

        //SCENECAM
        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 100f);
        //projection.setPerspectiveProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 1f);

        view = new Matrix4x4();
        view.translate(0, 0, -10);
        sceneCam = new Camera();
        sceneCam.setProjection(projection);
        sceneCam.setView(view);


        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        view = new Matrix4x4();
        hudCam = new Camera();
        hudCam.setProjection(projection);
        hudCam.setView(view);

        //COUNTER
        hitCounter = 10;
        levelCounter = 0;
        killCounter = 0;

        //BOOLS
        startGame = false;
        gameOver = false;

        //INTS
        lvlTime = 0;
        gameOverTime = 0;
        boxCount = 15;


        //CONTROLS
        pressedLeft = false;
        pressedRight = false;
        controlRightBox = new AABB(0, -40f,22f, 20f);
        controlLeftBox = new AABB(-22f, -40f,22f, 20f);
        topLeft = new AABB(-22f, 30f, 22f, 10f);
        startGameBox = new AABB(0, 0, 22f, 10f);

        //INIT BOXES AND SET BOOL
        atBottomA = false;
        atBottomB = false;
        boxArrayA = boxDropper(boxCount, 40, 120);
        //SET FIRST INITIALISATION VERY HEIGH
        boxArrayB = boxDropper(boxCount, 200, 200);

        //HITBOXEN
        bottomLineBox = new AABB(-25, -41, 50, 0.01f);
        topLineBox = new AABB(-25, 41, 50, 0.01f);

        //TEXT
        matrixHitCount = new Matrix4x4(Matrix4x4.createTranslation(-500, 700, 0));
        matrixKillCount = new Matrix4x4(Matrix4x4.createTranslation(-500, 650, 0));
        matrixLevelCount = new Matrix4x4(Matrix4x4.createTranslation(-500, 600, 0));
        matrixGameOver = new Matrix4x4(Matrix4x4.createTranslation(-275, 0, 0));
        matrixStartGame = new Matrix4x4(Matrix4x4.createTranslation(-275, 0, 0));
        matrixBack = new Matrix4x4(Matrix4x4.createTranslation(200, 700, 0));




        //GAMEOBJECTS
        powerUpObject = new PowerUpObject(new Matrix4x4(Matrix4x4.createTranslation(0,35,0)),2f,2f);
        jetObject = new JetObject(new Matrix4x4(Matrix4x4.createTranslation(0, -15f, 0)), 5f, 5f);
        jetObject.getMatrix().scale(0.7f);

        missileObject = new WeaponObject(new Matrix4x4(Matrix4x4.createTranslation(0, -15f, 0)), 2f, 2f);

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
<<<<<<< HEAD
            missileObject.loadObject("icosahedron.obj", "blank.png", graphicsDevice, context);
=======
            missileObject.loadObject("box.obj", "box.png", graphicsDevice, context);
            //LOAD POWERUP
            powerUpObject.loadObject("box.obj","box.png", graphicsDevice,context);
>>>>>>> 255c09b974fd06b8bc1ce8f852152a34d93a13b4
            
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

        missileObject.setAlive(false);

        fontHitCount = graphicsDevice.createSpriteFont(null, 64);
        textHitCount = graphicsDevice.createTextBuffer(fontHitCount, 16);
        textHitCount.setText("Life: "+hitCounter);

        fontKillCount = graphicsDevice.createSpriteFont(null, 64);
        textKillCount = graphicsDevice.createTextBuffer(fontKillCount, 16);
        textKillCount.setText("Kills: "+killCounter);

        fontLevelCount = graphicsDevice.createSpriteFont(null, 64);
        textLevelCount = graphicsDevice.createTextBuffer(fontLevelCount, 16);
        textLevelCount.setText("Level: "+levelCounter);

        fontGameOver = graphicsDevice.createSpriteFont(null, 120);
        textGameOver = graphicsDevice.createTextBuffer(fontGameOver, 16);
        textGameOver.setText("Game Over!");

        fontStartGame = graphicsDevice.createSpriteFont(null, 120);
        textStartGame = graphicsDevice.createTextBuffer(fontStartGame, 16);
        textStartGame.setText("Start!");

        //LOAD MEDIAPLAYER
        while (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_loop1);
        }


        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(attributes).build();
        clickSound = soundPool.load(context, R.raw.click, 1);
        duckSound1 = soundPool.load(context, R.raw.duck1, 1);
        duckSound2 = soundPool.load(context, R.raw.duck2, 1);
        noSound = soundPool.load(context, R.raw.no, 1);
        fartSound = soundPool.load(context, R.raw.fart, 1);

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

                            if(touchPoint.intersects(startGameBox)){
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
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
                                pressedRight = false;
                            }

                            if(touchPoint.intersects(controlRightBox)){
                                System.out.println("Control right");
                                pressedRight = true;
                                pressedLeft = false;
                            }

                            if(touchPoint.intersects(jetObject.getHitBoxAABB())) {
                                if(!missileObject.isAlive()){
                                    if (soundPool != null)
                                        soundPool.play(duckSound2, 1, 1, 0, 0, 1);
                                }
                                if(missileObject.isAlive()){
                                    if (soundPool != null)
                                        soundPool.play(noSound, 1, 1, 0, 0, 1);
                                }
                                missileObject.setAlive(true);
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


            if (startGame) {
            //######################## START startGame BLOCK ##############################
                //START MEDIA PLAYER
                mediaPlayer.start();
                mediaPlayer.setLooping(true);

                //CONTROLS
                if(pressedLeft && !pressedRight){
                    if(jetObject.getMatrix().m[12] > -22f){
                        jetObject.getMatrix().translate(-jetObject.getControlSpeed(), 0, 0);
                        jetObject.getMatrix().rotateY(-0.1f);
                        jetObject.updateHitBoxAABB();
                    }
                    //SET MISIILEPOSITION IF IT IS NOT ALIVE
                    if(!missileObject.isAlive()){
                        missileObject.setMatrix(new Matrix4x4(jetObject.getMatrix()));
                    }
                }

                if(pressedRight && !pressedLeft) {
                    if(jetObject.getMatrix().m[12] < 22f){
                        jetObject.getMatrix().translate(jetObject.getControlSpeed(), 0, 0);
                        jetObject.getMatrix().rotateY(0.1f);
                        jetObject.updateHitBoxAABB();
                    }
                    //SET MISIILEPOSITION IF IT IS NOT ALIVE
                    if(!missileObject.isAlive()) {
                        missileObject.setMatrix(new Matrix4x4(jetObject.getMatrix()));
                    }
                }

                for(EnemyObject o : boxArrayA){
                    enemyHitAction(o);
                }

                for(EnemyObject o : boxArrayB){
                    enemyHitAction(o);
                }

                if(jetObject.getHitBoxAABB().intersects(powerUpObject.getHitBoxAABB()) || missileObject.getHitBoxAABB().intersects(powerUpObject.getHitBoxAABB()) && missileObject.isAlive())
                {
                    powerUpAction(deltaSeconds);
                }

                if(topLineBox.intersects(missileObject.getHitBoxAABB()))
                {
                    missileObject.setAlive(false);
                    missileObject.setMatrix(new Matrix4x4(jetObject.getMatrix()));
                    System.out.println("missile weg");

                }

                //MISSILE SHOOOOOOOT
                if(missileObject.isAlive()){
                    missileObject.getMatrix().rotateY(deltaSeconds * 50);
                    missileObject.getMatrix().translate(0, missileObject.getMissileSpeed(), 0);
                }
                missileObject.updateHitBoxAABB();


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

                lvlTime += deltaSeconds;
                if(lvlTime > 80){
                    System.out.println("lvl up!");
                    for(EnemyObject o : boxArrayA){
                        o.setSpeed(o.getSpeed()-0.019f);
                    }
                    for(EnemyObject o : boxArrayB){
                        o.setSpeed(o.getSpeed()-0.019f);
                    }
                    levelCounter += 1;
                    textLevelCount.setText("Level: "+levelCounter);
                    lvlTime = 0;
                }
            //######################## END startGame BLOCK ##############################
            }
            if(hitCounter == 0 && !gameOver){
                if(jetObject.isAlive()){
                    jetObject.setAlive(false);
                    if(soundPool != null)
                        soundPool.play(fartSound, 1, 1, 0, 0, 1);
                }
                gameOverTime += deltaSeconds;
                startGame = false;
                if(gameOverTime > 5) {
                    gameOver = true;
                    fs.setScore(3,10);
                    gameOver(game);
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


        //Stuff for LVL
        graphicsDevice.setCamera(sceneCam);
        if(startGame){
            //RENDER JET
            renderer.drawMesh(jetObject.getMesh(), jetObject.getMatrix(), jetObject.getMaterial());

            for(EnemyObject o : boxArrayA){
                renderer.drawMesh(o.getMesh(), o.getMatrix(), o.getMaterial());
            }

            for(EnemyObject o : boxArrayB){
                renderer.drawMesh(o.getMesh(), o.getMatrix(), o.getMaterial());
            }

            //RENDER IF HIT THE jetHitBox
            if(missileObject.isAlive())
                renderer.drawMesh(missileObject.getMesh(), missileObject.getMatrix(), missileObject.getMaterial());

            //Stuff for HUD
            graphicsDevice.setCamera(hudCam);
            renderer.drawText(textHitCount, matrixHitCount);
            renderer.drawText(textKillCount, matrixKillCount);
            renderer.drawText(textLevelCount, matrixLevelCount);
        }
        graphicsDevice.setCamera(hudCam);
        if(!jetObject.isAlive() && !startGame){
            renderer.drawText(textGameOver, matrixGameOver);
        }
    }

	@Override
	public void resize(Game game, int width, int height) {
//        float aspect = (float)width / (float)height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        hudCam.setProjection(projection);

        projection = new Matrix4x4();
//        projection.setPerspectiveProjection(-0.1f * aspect, 0.1f * aspect, -0.1f, 0.1f, 0.1f, 100.0f);
        projection.setOrhtogonalProjection(-22.5f, 22.5f, -40f, 40f, 0.1f, 100f);
        sceneCam.setProjection(projection);
        System.out.println("check");

        //matrixHitCount.setIdentity();
        //matrixHitCount.translate(-width / 2, height / 2 - 64, 0);

    }

    @Override
    public void resume(Game game) {
        startGame = true;
    }

    @Override
    public void pause(Game game) {
        if (mediaPlayer != null)
        	mediaPlayer.pause();
        startGame = false;
    }

    @Override
    public void shutdown(Game game) {

    }

    public ArrayList<EnemyObject> boxDropper(int amount, float start, float end){
        ArrayList<EnemyObject> a = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            EnemyObject o = new EnemyObject(new Matrix4x4(Matrix4x4.createTranslation(randfloat(-25, 25), randfloat(start, end), 0)), 2f, 2f);
            a.add(o);
        }
        return a;
    }

    public void boxRandomizer(ArrayList<EnemyObject> b, float minY, float maxY){
        for(EnemyObject o : b){
            o.setMatrix(new Matrix4x4(Matrix4x4.createTranslation(randfloat(-25, 25), randfloat(minY, maxY), 0)));
        }
    }

    public void jetHitAction(EnemyObject o){
        o.setAlive(false);
        if (soundPool != null)
            soundPool.play(duckSound1, 1, 1, 0, 0, 1);
        hitCounter--;
        textHitCount.setText("Hits: " + hitCounter);
        System.out.println("peng!!");
    }

    public void missileHitAction(EnemyObject o){
        o.setAlive(false);
        missileObject.setAlive(false);
        missileObject.setMatrix(new Matrix4x4(jetObject.getMatrix()));
        killCounter++;
        textKillCount.setText("Kills: " + killCounter);
        if (soundPool != null)
            soundPool.play(fartSound, 1, 1, 0, 0, 1);
        o.getMatrix().scale(0.0000000001f);
    }

    public void enemyHitAction(EnemyObject o){
        //DETECT COLLISION JET and BOXES
        if (jetObject.getHitBoxAABB().intersects(o.getHitBoxAABB()) && o.isAlive()){
            jetHitAction(o);
        }
        //DETECT COLLISION MISSILE and BOXES
        if(missileObject.getHitBoxAABB().intersects(o.getHitBoxAABB()) && missileObject.isAlive() && o.isAlive()){
            missileHitAction(o);
        }
        //RESET ALIVE STATUS FOR BOXARRAY B
        if(bottomLineBox.intersects(o.getHitBoxAABB()))
            o.setAlive(true);

        //MOVE BOXES DOWN
        o.getMatrix().translate(0, o.getSpeed(), 0);
        o.getMatrix().rotateY(MathHelper.randfloat(0.02f, 0.8f));
        o.updateHitBoxAABB();
    }

    public void gameOver(Game game){
        mediaPlayer.stop();
        mediaPlayer.release();
        game.getGameStateManager().setGameState(new MGDHighscoreState());
    }
    public void powerUpAction(float deltaseconds) {
        powerUpTime += deltaseconds;


        switch (powerUpObject.getPowerup()) {
            case 0:
                for (EnemyObject b : boxArrayA) {
                    b.setSpeed(b.getSpeed() - powerUpObject.getBoxSpeed());
                }
                for (EnemyObject b : boxArrayB) {
                    b.setSpeed(b.getSpeed() - powerUpObject.getBoxSpeed());
                }
                break;
            case 1:
                jetObject.setControlSpeed(jetObject.getControlSpeed() + powerUpObject.getControlSpeed());
                break;
            case 2:
                missileObject.setMissileSpeed(missileObject.getMissileSpeed() + powerUpObject.getShootSpeed());
                break;
        }
        if (powerUpTime > 20) {

            switch (powerUpObject.getPowerup()) {
                case 0:
                    for (EnemyObject b : boxArrayA) {
                        b.setSpeed(b.getSpeed() + powerUpObject.getBoxSpeed());
                    }
                    for (EnemyObject b : boxArrayB) {
                        b.setSpeed(b.getSpeed() + powerUpObject.getBoxSpeed());
                    }
                    break;
                case 1:
                    jetObject.setControlSpeed(jetObject.getControlSpeed() - powerUpObject.getControlSpeed());
                    break;
                case 2:
                    missileObject.setMissileSpeed(missileObject.getMissileSpeed() - powerUpObject.getShootSpeed());
                    break;
            }
        }
    }
}

