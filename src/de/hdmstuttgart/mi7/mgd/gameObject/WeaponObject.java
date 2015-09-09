package de.hdmstuttgart.mi7.mgd.gameObject;

import android.content.Context;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Circle;
import de.hdmstuttgart.mi7.mgd.graphics.GraphicsDevice;
import de.hdmstuttgart.mi7.mgd.graphics.Material;
import de.hdmstuttgart.mi7.mgd.graphics.Mesh;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

import java.io.IOException;

/**
 * Created by florianporada on 29.08.15.
 */
public class WeaponObject extends GameObject {
    float missileSpeed = 0.2f;
    boolean alive = true;
    public WeaponObject(View view) {
        super(view);
    }

    public WeaponObject(Matrix4x4 matrix, float hitboxWidth, float hitboxHeight) {
        super(matrix, hitboxWidth, hitboxHeight);
    }

    public WeaponObject(Matrix4x4 matrix, float hitboxRadius) {
        super(matrix, hitboxRadius);
    }

    public float getMissileSpeed() {
        return missileSpeed;
    }

    public void setMissileSpeed(float missileSpeed) {
        this.missileSpeed = missileSpeed;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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
