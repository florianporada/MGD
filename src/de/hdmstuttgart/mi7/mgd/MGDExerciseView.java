package de.hdmstuttgart.mi7.mgd;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by florianporada on 25.08.15.
 */
public class MGDExerciseView extends GLSurfaceView {

    MGDExerciseGame game;

    public MGDExerciseView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        game = new MGDExerciseGame(context);

        setRenderer(game);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

}
