package de.hdmstuttgart.mi7.mgd.gameObject;

import android.content.Context;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Circle;
import de.hdmstuttgart.mi7.mgd.graphics.GraphicsDevice;
import de.hdmstuttgart.mi7.mgd.graphics.Material;
import de.hdmstuttgart.mi7.mgd.graphics.Mesh;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector2;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by florianporada on 27.08.15.
 */
public class JetObject extends GameObject{
    private float controlSpeed = 0.1f;

    public JetObject(View view) {
        super(view);
    }

    public JetObject(Matrix4x4 matrix, float hitboxWidth, float hitboxHeight) {
        super(matrix, hitboxWidth, hitboxHeight);
    }

    public JetObject(Matrix4x4 matrix, float hitboxRadius) {
        super(matrix, hitboxRadius);
    }

    public float getControlSpeed() {
        return controlSpeed;
    }

    public void setControlSpeed(float controlSpeed) {
        this.controlSpeed = controlSpeed;
    }

    @Override
    public void loadObject(String objectPath, String texturePath, GraphicsDevice graphicsDevice, Context context) throws IOException {
        super.loadObject(objectPath, texturePath, graphicsDevice, context);
    }

    @Override
    public Mesh getMesh() {
        return super.getMesh();
    }

    @Override
    public void setMesh(Mesh mesh) {
        super.setMesh(mesh);
    }

    @Override
    public Matrix4x4 getMatrix() {
        return super.getMatrix();
    }

    @Override
    public void setMatrix(Matrix4x4 matrix) {
        super.setMatrix(matrix);
    }

    @Override
    public Material getMaterial() {
        return super.getMaterial();
    }

    @Override
    public void setMaterial(Material material) {
        super.setMaterial(material);
    }

    @Override
    public AABB getHitBoxAABB() {
        return super.getHitBoxAABB();
    }

    @Override
    public void setHitBoxAABB(AABB aabb) {
        super.setHitBoxAABB(aabb);
    }

    @Override
    public Circle getHitBoxCircle() {
        return super.getHitBoxCircle();
    }

    @Override
    public void setHitBoxCircle(Circle hitBoxCircle) {
        super.setHitBoxCircle(hitBoxCircle);
    }

    @Override
    public void updateHitBoxCircle() {
        super.updateHitBoxCircle();
    }

    @Override
    public void updateHitBoxAABB() {
        super.updateHitBoxAABB();
    }
}
