package de.hdmstuttgart.mi7.mgd.graphics;

/**
 * Created by florianporada on 26.08.15.
 */
public class Texture {

    private int height, width, handle;

    public Texture(int height, int width, int handle){
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

    public Texture getDefaultTexture(){
        return new Texture(32, 32, 3);
    }

    int getHandle() {
        return handle;
    }
}
