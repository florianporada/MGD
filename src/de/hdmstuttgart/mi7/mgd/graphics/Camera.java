package de.hdmstuttgart.mi7.mgd.graphics;

import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

/**
 * Created by florianporada on 25.08.15.
 */
public class Camera {
    Matrix4x4 projection;
    Matrix4x4 view;

    public Camera() {
        projection = new Matrix4x4();
        view = new Matrix4x4();
    }

    public Matrix4x4 getProjection() {
        return projection;
    }

    public Matrix4x4 getView() {
        return view;
    }

    public void setProjection(Matrix4x4 projection) {
        this.projection = projection;
    }

    public void setView(Matrix4x4 view) {
        this.view = view;
    }
}
