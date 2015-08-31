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
    private float hitboxWidth, hitboxHeight, hitboxRadius;

    public GameObject(View view){
        this.context = view.getContext();
    }

    public GameObject(Matrix4x4 matrix, float hitBoxWidth, float hitboxHeight) {
        this.matrix = matrix;
        this.hitboxWidth = hitBoxWidth;
        this.hitboxHeight = hitboxHeight;
        this.hitBoxAABB = new AABB(matrix.m[12]-(hitBoxWidth/2), matrix.m[13]-(hitboxHeight/2), hitBoxWidth, hitboxHeight);}

    public GameObject(Matrix4x4 matrix, float hitBoxRadius) {
        this.matrix = matrix;
        this.hitboxRadius = hitBoxRadius;
        this.hitBoxCircle = new Circle(new Vector2(matrix.m[12], matrix.m[13]), hitBoxRadius);
    }

    public void loadObject(String objectPath, String texturePath, GraphicsDevice graphicsDevice, Context context) throws IOException {
        InputStream stream;

        stream = context.getAssets().open(objectPath);
        this.mesh = Mesh.loadFromOBJ(stream);

        stream = context.getAssets().open(texturePath);
        this.material = new Material();

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
        hitBoxCircle = new Circle(new Vector2(matrix.m[12], matrix.m[13]), hitboxRadius);
    }

    public void updateHitBoxAABB() {
        hitBoxAABB = new AABB(this.matrix.m[12]-(hitboxWidth/2), this.matrix.m[13]-(hitboxHeight/2), hitboxWidth, hitboxHeight );

    }
}
