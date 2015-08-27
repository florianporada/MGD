package de.hdmstuttgart.mi7.mgd.collision;

import de.hdmstuttgart.mi7.mgd.math.Vector2;

/**
 * Created by florianporada on 27.08.15.
 */
public interface Shape2D {
    public boolean intersects(Shape2D shape2D);
    public boolean intersects(Point point);
    public boolean intersects(Circle circle);
    public boolean intersects(AABB box);

    public Vector2 getPosition();
    public void setPosition(Vector2 position);
}
