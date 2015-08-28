package de.hdmstuttgart.mi7.mgd;

import android.opengl.GLSurfaceView;
import android.content.Context;
import de.hdmstuttgart.mi7.mgd.game.Game;

/**
 * Created by florianporada on 25.08.15.
 */
public class MGDExerciseView extends GLSurfaceView {

    Game game;

    public MGDExerciseView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        game = new Game(this);

        game.getGameStateManager().setGameState(new MGDExerciseGame());

        setRenderer(game);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        game.pause();
    }

    @Override
    public void onResume() {
        game.resume();
        super.onResume();
    }

}
