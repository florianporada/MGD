package de.hdmstuttgart.mi7.mgd.gameObject;

import android.content.Context;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Circle;
import de.hdmstuttgart.mi7.mgd.graphics.GraphicsDevice;
import de.hdmstuttgart.mi7.mgd.graphics.Material;
import de.hdmstuttgart.mi7.mgd.graphics.Mesh;
import de.hdmstuttgart.mi7.mgd.math.MathHelper;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

import java.io.IOException;

/**
 * Created by florianporada on 12.09.15.
 */
public class PowerUpObject extends GameObject {

    private boolean alive = true;
    private boolean active = false;
    private int powerup = MathHelper.randInt(0,2);
    private float shootSpeed = 0.059f;
    private float controlSpeed = 0.31f;
    private float boxSpeed = 0.019f;

    public PowerUpObject(View view) {
        super(view);
    }

    public PowerUpObject(Matrix4x4 matrix, float hitBoxWidth, float hitboxHeight) {
        super(matrix, hitBoxWidth, hitboxHeight);
    }

    public PowerUpObject(Matrix4x4 matrix, float hitBoxRadius) {
        super(matrix, hitBoxRadius);
    }

    public int getPowerup() {
        return powerup;
    }

    public void setPowerup(int powerup) {
        this.powerup = powerup;
    }

    public float getShootSpeed() {
        return shootSpeed;
    }

    public void setShootSpeed(float shootSpeed) {
        this.shootSpeed = shootSpeed;
    }

    public float getBoxSpeed() {
        return boxSpeed;
    }

    public void setBoxSpeed(float boxSpeed) {
        this.boxSpeed = boxSpeed;
    }

    public float getControlSpeed() {
        return controlSpeed;
    }

    public void setControlSpeed(float controlSpeed) {
        this.controlSpeed = controlSpeed;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
