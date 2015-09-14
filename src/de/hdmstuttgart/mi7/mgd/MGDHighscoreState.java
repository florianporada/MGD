package de.hdmstuttgart.mi7.mgd;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.FileWriter;
import de.hdmstuttgart.mi7.mgd.game.Filestuff;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.game.GameState;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.input.InputEvent;
import de.hdmstuttgart.mi7.mgd.input.InputSystem;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector3;
import de.hdmstuttgart.mmi.mgd.R;


/**
 * Created by christophkramer on 09.09.15.
 */
public class MGDHighscoreState implements GameState {


    private Camera menuCam;
    private Filestuff fs;
    private FileWriter fileWriter;

    private SpriteFont fontTitle;
    private TextBuffer textTitle;
    private Matrix4x4 matTitle;
    private Typeface type = Typeface.create("Copperplate Gothic", 3);
    private SpriteFont fontMenu;
    private TextBuffer[] textMenu, textMenu2;
    private Matrix4x4 projection, view;
    private Matrix4x4[] matMenu, matMenu2;
    private AABB[] aabbMenu, aabbMenu2;

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    public void initialize(Game game) {

        fs = new Filestuff(game);
        fileWriter = new FileWriter(game);


        float width = game.getScreenWidth();
        float height = game.getScreenHeight();

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        //projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        menuCam = new Camera();
        menuCam.setProjection(projection);
        menuCam.setView(view);

        matTitle = Matrix4x4.createTranslation(-width / 2, height / 2 - 64, 0);

    }

    public void loadContent(Game game) {
        String[][] score2 = fileWriter.readFromFile();
        GraphicsDevice graphicsDevice = game.getGraphicsDevice();

        System.out.println("myText: "+fileWriter.readFromFile());

        fontTitle = graphicsDevice.createSpriteFont(type, 96);
        textTitle = graphicsDevice.createTextBuffer(fontTitle, 20);
        fontMenu = graphicsDevice.createSpriteFont(null, 32);
        textMenu = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontTitle, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontMenu, 20),
                graphicsDevice.createTextBuffer(fontTitle, 20)
        };
        textMenu[0].setText("Last Score");
        textMenu2 = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontTitle, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontTitle, 30)
        };
        textMenu2[0].setText("");
        matMenu = new Matrix4x4[]{
                Matrix4x4.createTranslation(-192,  640, 0),
                Matrix4x4.createTranslation(-400,  512, 0),
                Matrix4x4.createTranslation(-400,  432, 0),
                Matrix4x4.createTranslation(-400,  352, 0),
                Matrix4x4.createTranslation(-400,  272, 0),
                Matrix4x4.createTranslation(-400,  192, 0),
                Matrix4x4.createTranslation(-400,  112, 0),
                Matrix4x4.createTranslation(-400,   32, 0),
                Matrix4x4.createTranslation(-400,  -48, 0),
                Matrix4x4.createTranslation(-400, -128, 0),
                Matrix4x4.createTranslation(-400, -208, 0),
                Matrix4x4.createTranslation( -80, -400, 0)
        };
        matMenu2 = new Matrix4x4[]{
                Matrix4x4.createTranslation(-50,  640, 0),
                Matrix4x4.createTranslation(-50,  512, 0),
                Matrix4x4.createTranslation(-50,  432, 0),
                Matrix4x4.createTranslation(-50,  352, 0),
                Matrix4x4.createTranslation(-50,  272, 0),
                Matrix4x4.createTranslation(-50,  192, 0),
                Matrix4x4.createTranslation(-50,  112, 0),
                Matrix4x4.createTranslation(-50,   32, 0),
                Matrix4x4.createTranslation(-50,  -48, 0),
                Matrix4x4.createTranslation(-50, -128, 0),
                Matrix4x4.createTranslation(-50, -208, 0),
                Matrix4x4.createTranslation(-80,  -400, 0)
        };
        aabbMenu = new AABB[]{
                new AABB(-400,  640, 120, 96),
                new AABB(-400,  512, 120, 96),
                new AABB(-400,  432, 120, 96),
                new AABB(-400,  352, 120, 96),
                new AABB(-400,  272, 120, 96),
                new AABB(-400,  192, 120, 96),
                new AABB(-400,  112, 120, 96),
                new AABB(-400,   32, 120, 96),
                new AABB(-400,  -48, 120, 96),
                new AABB(-400, -128, 120, 96),
                new AABB(-400, -208, 120, 96),
                new AABB(-80,  -400, 120, 96)
        };
        aabbMenu2 = new AABB[]{
                new AABB(-50,  640, 120, 96),
                new AABB(-50,  512, 120, 96),
                new AABB(-50,  432, 120, 96),
                new AABB(-50,  352, 120, 96),
                new AABB(-50,  272, 120, 96),
                new AABB(-50,  192, 120, 96),
                new AABB(-50,  112, 120, 96),
                new AABB(-50,   32, 120, 96),
                new AABB(-50,  -48, 120, 96),
                new AABB(-50, -128, 120, 96),
                new AABB(-50, -208, 120, 96),
                new AABB(-80, -400, 120, 96)
        };

        fontMenu = graphicsDevice.createSpriteFont(null, 64);

        for (int i=1;i<11;i++){

            String name = i+". "+score2[(i-1)][0]+" :";
            String points = score2[(i-1)][1]+" Pionts, LvL: "+score2[(i-1)][2];

            textMenu[i].setText(name);
            textMenu2[i].setText(points);
        }
        textMenu[0].setText("Highscore");
        textMenu[11].setText("back");


        Context context = game.getContext();

        //LOAD MEDIAPLAYER
        while (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.main2);
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

    public void update(Game game, float deltaSeconds) {
        InputSystem inputSystem = game.getInputSystem();
        int screenWidth = game.getScreenWidth();
        int screenHeight = game.getScreenHeight();

        InputEvent inputEvent = inputSystem.peekEvent();
        while (inputEvent != null) {
            switch (inputEvent.getInputDevice()) {
                case TOUCHSCREEN:
                    switch (inputEvent.getInputAction()) {
                        case DOWN:
                            Vector3 screenTouchPosition = new Vector3(
                                    (inputEvent.getValues()[0] / (screenWidth / 2) - 1),
                                    -(inputEvent.getValues()[1] / (screenHeight / 2) - 1),
                                    0);

                            Vector3 worldTouchPosition = menuCam.unproject(screenTouchPosition, 1);

                            Point touchPoint = new Point(
                                    worldTouchPosition.getX(),
                                    worldTouchPosition.getY());


                            AABB aabb = aabbMenu[11];


                            if (touchPoint.intersects(aabb)) {
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                                onMenuItemClicked(game);
                            }
                    }
                    break;
            }

            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();
        }
    }

    public void draw(Game game, float deltaSeconds) {
        GraphicsDevice graphicsDevice = game.getGraphicsDevice();
        Renderer renderer = game.getRenderer();

        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(menuCam);
        renderer.drawText(textTitle, matTitle);
        for (int i = 0; i < textMenu.length; ++i)
            renderer.drawText(textMenu[i], matMenu[i]);
        for (int i = 0; i < textMenu2.length; ++i)
            renderer.drawText(textMenu2[i], matMenu2[i]);
    }

    public void resize(Game game, int width, int height) {
        float aspect = (float)width / (float)height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        menuCam.setProjection(projection);

        matTitle.setIdentity();
        matTitle.translate(-width / 2, height / 2 - 64, 0);
    }

    public void pause(Game game) {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void resume(Game game) {
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    public void shutdown(Game game) {
        // TODO Auto-generated method stub

    }

    private void onMenuItemClicked(Game game) {

        mediaPlayer.release();
        game.getGameStateManager().setGameState(new MGDMenuState());

    }
}
