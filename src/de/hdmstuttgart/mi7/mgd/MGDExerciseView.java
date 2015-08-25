package de.hdmstuttgart.mi7.mgd;

import android.opengl.GLSurfaceView;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import de.hdmstuttgart.mi7.mgd.graphics.MGDRenderer;

/**
 * Created by florianporada on 25.08.15.
 */
public class MGDExerciseView extends GLSurfaceView {

    public MGDExerciseView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        MGDRenderer renderer = new MGDRenderer();
        setRenderer(renderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

}
