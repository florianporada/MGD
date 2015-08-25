package de.hdmstuttgart.mi7.mgd;

/**
 * Created by florianporada on 25.08.15.
 */
public class VertexElement {

    public enum VertexSemantic {
        VERTEX_ELEMENT_NONE,
        VERTEX_ELEMENT_POSITION,
        VERTEX_ELEMENT_COLOR,
        VERTEX_ELEMENT_TEXCOORD
    }

    private int offset, stride, type, count;

    public VertexSemantic semantic;

    public VertexElement(int offset, int stride, int type, int count){
        this.offset = offset;
        this.stride = stride;
        this.type = type;
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public int getStride() {
        return stride;
    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public VertexSemantic getSemantic() {
        return semantic;
    }
}
