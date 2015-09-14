package de.hdmstuttgart.mi7.mgd;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioAttributes;
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
 * Created by christophkramer on 14.09.15.
 */
public class MGDCreditState implements GameState {

    private Camera menuCam;

    private SpriteFont fontTitle;
    private TextBuffer textTitle;
    private Matrix4x4 matTitle;

    private SpriteFont fontMenu;
    private TextBuffer[] textMenu;
    private Matrix4x4 projection, view;
    private Matrix4x4[] matrixMenu;
    private AABB[] aabbMenu;
    private float textOffset;

    //MEDIAPLAYER
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSound;

    public void initialize(Game game) {
        float width = game.getScreenWidth();
        float height = game.getScreenHeight();

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        //projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        menuCam = new Camera();
        menuCam.setProjection(projection);
        menuCam.setView(view);

        matTitle = Matrix4x4.createTranslation(-300, 200, 0);

        textOffset = -20;

    }

    public void loadContent(Game game) {
        GraphicsDevice graphicsDevice = game.getGraphicsDevice();

        fontTitle = graphicsDevice.createSpriteFont(Typeface.DEFAULT, 100);
        textTitle = graphicsDevice.createTextBuffer(fontTitle, 16);
        textTitle.setText("It's something!");

        fontMenu = graphicsDevice.createSpriteFont(null, 64);
        textMenu = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30),
                graphicsDevice.createTextBuffer(fontMenu, 30)
        };
        textMenu[0].setText("Game by:");
        textMenu[1].setText("Florian Porada &");
        textMenu[2].setText("Christop Kramer");
        textMenu[3].setText("Back");

        matrixMenu = new Matrix4x4[]{
                Matrix4x4.createTranslation(-200, 0+(textOffset+1), 0),
                Matrix4x4.createTranslation(-200, -64+(textOffset*2), 0),
                Matrix4x4.createTranslation(-200, -128+(textOffset*3), 0),
                Matrix4x4.createTranslation(-200, -192+(textOffset*4), 0)
        };

        aabbMenu = new AABB[]{
                new AABB(-300, 0+(textOffset*1), 300, 64),
                new AABB(-300, -64+(textOffset*2), 300, 64),
                new AABB(-300, -128+(textOffset*3), 300, 64),
                new AABB(-300, -192+(textOffset*4), 300, 64)
        };

        Context context = game.getContext();

        //LOAD MEDIAPLAYER
        while (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_loop1);
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

                            AABB aabb = aabbMenu[3];
                            if (touchPoint.intersects(aabb)) {
                                if (soundPool != null)
                                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                                onMenuItemClicked(game);
                            }
                    break;
                }
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
            renderer.drawText(textMenu[i], matrixMenu[i]);
    }

    public void resize(Game game, int width, int height) {
        float aspect = (float)width / (float)height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        menuCam.setProjection(projection);
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
