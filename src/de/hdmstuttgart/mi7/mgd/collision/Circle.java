package de.hdmstuttgart.mi7.mgd.collision;

import de.hdmstuttgart.mi7.mgd.math.MathHelper;
import de.hdmstuttgart.mi7.mgd.math.Vector2;

/**
 * Created by florianporada on 27.08.15.
 */
public class Circle implements Shape2D {

    private Vector2 center;
    private float radius;

    public Circle() {
        this.center = new Vector2();
        this.radius = 0.0f;
    }

    public Circle(Vector2 center, float radius) {
        this.center = new Vector2(center.v[0], center.v[1]);
        this.radius = radius;
    }

    public Circle(float x, float y, float radius) {
        this.center = new Vector2(x, y);
        this.radius = radius;
    }

    @Override
    public boolean intersects(Shape2D shape2D) {
        return shape2D.intersects(this);
    }

    @Override
    public boolean intersects(Point point) {
        float distSqr = Vector2.subtract(point.getPosition(), this.getCenter()).getLengthSqr();
        return distSqr <= radius * radius;
    }

    @Override
    public boolean intersects(Circle circle) {
        float distSqr = Vector2.subtract(circle.center, this.center).getLengthSqr();
        return distSqr <= (this.radius + circle.radius) * (this.radius + circle.radius);
    }

    @Override
    public boolean intersects(AABB box) {
        Vector2 min = box.getMin();
        Vector2 max = box.getMax();

        if (center.getX() >= min.getX() && center.getX() <= max.getX()) return true;
        if (center.getY() >= min.getY() && center.getY() <= max.getY()) return true;

        Vector2 nearestPosition = new Vector2(
                MathHelper.clamp(center.getX(), min.getX(), max.getX()),
                MathHelper.clamp(center.getY(), min.getY(), max.getY()));

        return nearestPosition.getLengthSqr() < radius * radius;
    }

    @Override
    public Vector2 getPosition() {
        return center;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.center.v[0] = position.v[0];
        this.center.v[1] = position.v[1];
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
