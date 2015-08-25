package de.hdmstuttgart.mi7.mgd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by florianporada on 25.08.15.
 */
public class Mesh {
    private VertexBuffer buffer;
    private int mode;

    public VertexBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(VertexBuffer buffer) {
        this.buffer = buffer;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static Mesh loadFromObj(InputStream inputStream){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
    }
}
