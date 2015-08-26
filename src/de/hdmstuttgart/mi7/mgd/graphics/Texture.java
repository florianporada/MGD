package de.hdmstuttgart.mi7.mgd.graphics;

/**
 * Created by florianporada on 26.08.15.
 */
public class Texture {

    private int height, width, handle;

    Texture(int height, int width, int handle){
        this.height = height;
        this.width = width;
        this.handle = handle;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    int getHandle() {
        return handle;
    }
}
