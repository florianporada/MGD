package de.hdmstuttgart.mi7.mgd;

import java.nio.ByteBuffer;

/**
 * Created by florianporada on 25.08.15.
 */
public class VertexBuffer {
    private VertexElement vertexElement[];
    private ByteBuffer byteBuffer;
    private int vertices;

    public VertexElement[] getVertexElement() {
        return vertexElement;
    }

    public void setVertexElement(VertexElement[] vertexElement) {
        this.vertexElement = vertexElement;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }
}
