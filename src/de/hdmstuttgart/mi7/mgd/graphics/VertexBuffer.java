package de.hdmstuttgart.mi7.mgd.graphics;

import java.nio.ByteBuffer;

/**
 * Created by florianporada on 25.08.15.
 */
public class VertexBuffer {
    private VertexElement elements[];
    private ByteBuffer buffer;
    private int numVertices;

    public VertexElement[] getElements() {
        return elements;
    }

    public void setVertexElements(VertexElement[] vertexElements) {
        this.elements = vertexElements;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }
}
