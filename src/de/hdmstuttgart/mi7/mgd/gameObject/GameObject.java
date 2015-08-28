package de.hdmstuttgart.mi7.mgd.gameObject;

import android.content.Context;
import android.view.View;
import de.hdmstuttgart.mi7.mgd.collision.AABB;
import de.hdmstuttgart.mi7.mgd.collision.Circle;
import de.hdmstuttgart.mi7.mgd.graphics.GraphicsDevice;
import de.hdmstuttgart.mi7.mgd.graphics.Material;
import de.hdmstuttgart.mi7.mgd.graphics.Mesh;
import de.hdmstuttgart.mi7.mgd.graphics.Texture;
import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;
import de.hdmstuttgart.mi7.mgd.math.Vector2;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by florianporada on 27.08.15.
 */
public abstract class GameObject {
    private Context context;
    private Mesh mesh;
    private Matrix4x4 matrix;
    private Material material;
    private AABB hitBoxAABB;
    private Circle hitBoxCircle;

    public GameObject(View view){
        this.context = view.getContext();
    }

    public GameObject(Matrix4x4 matrix, AABB aabb) {
        this.matrix = matrix;
        this.hitBoxAABB = aabb;}

    public GameObject(Matrix4x4 matrix) {
        this.matrix = matrix;
        this.hitBoxCircle = new Circle(new Vector2(matrix.m[12], matrix.m[13]), 150f);
    }

    public void loadObject(String objectPath, String texturePath, GraphicsDevice graphicsDevice, View view) throws IOException {
        InputStream stream;

        stream = view.getContext().getAssets().open(objectPath);
        this.mesh = Mesh.loadFromOBJ(stream);

        stream = view.getContext().getAssets().open(texturePath);
        material = new Material();

        this.material.setTexture(graphicsDevice.createTexture(stream));
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Matrix4x4 getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix4x4 matrix) {
        this.matrix = matrix;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public AABB getHitBoxAABB() {
        return hitBoxAABB;
    }

    public void setHitBoxAABB(AABB aabb) {
        this.hitBoxAABB = aabb;
    }

    public Circle getHitBoxCircle() {
        return hitBoxCircle;
    }

    public void setHitBoxCircle(Circle hitBoxCircle) {
        this.hitBoxCircle = hitBoxCircle;
    }

    public void updateHitBoxCircle(){
        hitBoxCircle = new Circle(new Vector2(matrix.m[12], matrix.m[13]), 200f);
    }
}
