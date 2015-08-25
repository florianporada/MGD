package de.hdmstuttgart.mi7.mgd.graphics;

import java.nio.ByteBuffer;

/**
 * Created by florianporada on 25.08.15.
 */
public class VertexBuffer {
    private VertexElement vertexElements[];
    private ByteBuffer byteBuffer;
    private int numVertices;

    public VertexElement[] getVertexElements() {
        return vertexElements;
    }

    public void setVertexElements(VertexElement[] vertexElements) {
        this.vertexElements = vertexElements;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }
}
