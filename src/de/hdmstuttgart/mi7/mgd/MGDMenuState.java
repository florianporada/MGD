package de.hdmstuttgart.mi7.mgd;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Point;
import de.hdmstuttgart.mi7.mgd.game.Game;
import de.hdmstuttgart.mi7.mgd.game.GameState;
import de.hdmstuttgart.mi7.mgd.graphics.*;
import de.hdmstuttgart.mi7.mgd.input.InputEvent;
import de.hdmstuttgart.mi7.mgd.input.InputSystem;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector3;
import de.hdmstuttgart.mmi.mgd.R;

/**
 * Created by florianporada on 28.08.15.
 */
public class MGDMenuState implements GameState {

    private Camera menuCam;

    private SpriteFont fontTitle;
    private TextBuffer textTitle;
    private Matrix4x4 matTitle;

    private SpriteFont fontMenu;
    private TextBuffer[] textMenu;
    private Matrix4x4 projection, view;
    private Matrix4x4[] matMenu;
    private AABB[] aabbMenu;

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    public void initialize(Game game) {
        float width = game.getScreenWidth();
        float height = game.getScreenHeight();

        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 16.0f);
        //projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        menuCam = new Camera();
        menuCam.setProjection(projection);
        menuCam.setView(view);

        matTitle = Matrix4x4.createTranslation(-width / 2, height / 2 - 64, 0);

    }

    public void loadContent(Game game) {
        GraphicsDevice graphicsDevice = game.getGraphicsDevice();

        fontTitle = graphicsDevice.createSpriteFont(null, 64);
        textTitle = graphicsDevice.createTextBuffer(fontTitle, 16);
        textTitle.setText("DrivingSim");

        fontMenu = graphicsDevice.createSpriteFont(null, 64);
        textMenu = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16)
        };
        textMenu[0].setText("Start Game");
        textMenu[1].setText("Highscore");
        textMenu[2].setText("Credits");
        textMenu[3].setText("Quit");

        matMenu = new Matrix4x4[]{
                Matrix4x4.createTranslation(0, 0, 0),
                Matrix4x4.createTranslation(0, -64, 0),
                Matrix4x4.createTranslation(0, -128, 0),
                Matrix4x4.createTranslation(0, -192, 0)
        };

        aabbMenu = new AABB[]{
                new AABB(0, 0, 120, 64),
                new AABB(0, -64, 120, 64),
                new AABB(0, -128, 120, 64),
                new AABB(0, -192, 120, 64)
        };

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

                            for (int i = 0; i < aabbMenu.length; ++i) {
                                AABB aabb = aabbMenu[i];
                                if (touchPoint.intersects(aabb)) {
                                    if (soundPool != null)
                                        soundPool.play(clickSound, 1, 1, 0, 0, 1);

                                    onMenuItemClicked(game, i);
                                }
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

    private void onMenuItemClicked(Game game, int i) {
        switch (i) {
            case 0:
                mediaPlayer.release();
                game.getGameStateManager().setGameState(new MGDExerciseGame());
                break;

            case 1:
                mediaPlayer.release();
                //game.getGameStateManager().setGameState(new MGDHighscoreStat());
                break;

            case 3:
                game.finish();
                break;
        }
    }
}
