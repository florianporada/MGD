package de.hdmstuttgart.mi7.mgd.graphics;

import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;

/**
 * Created by florianporada on 25.08.15.
 */
public class GraphicsDevice {

    private GL10 gl;

    public void onSurfaceCreated(GL10 gl){
        this.gl = gl;
    }

    public void clear(float red, float green, float blue, float alpha){
        gl.glClearColor(red, green, blue, alpha);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void clear(float red, float green, float blue){
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void resize(int width, int height){
        gl.glViewport(0, 0, width, height);
    }

    public void setCamera(Camera camera){
        Matrix4x4 viewProjection = Matrix4x4.multiply(camera.getProjection(), camera.getView());

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(viewProjection.m, 0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public void setWorldMatrix(Matrix4x4 world) {
        gl.glLoadMatrixf(world.m, 0);
    }

    public void bindVertexBuffer(VertexBuffer vertexBuffer){
        ByteBuffer buffer = vertexBuffer.getByteBuffer();

        for (VertexElement element : vertexBuffer.getVertexElements()){
            int offset = element.getOffset();
            int stride = element.getStride();
            int type = element.getType();
            int count = element.getCount();

            buffer.position(offset);

            switch (element.getSemantic()){
                case VERTEX_ELEMENT_POSITION:
                    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                    gl.glVertexPointer(count, type, stride, buffer);
                    break;

                case VERTEX_ELEMENT_COLOR:
                    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                    gl.glColorPointer(count, type, stride, buffer);
                    break;

                case VERTEX_ELEMENT_TEXCOORD:
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glTexCoordPointer(count, type, stride, buffer);
            }

        }
    }

    public void unbindVertexBuffer(VertexBuffer vertexBuffer){
        for(VertexElement element : vertexBuffer.getVertexElements()){
            switch (element.getSemantic()){
                case VERTEX_ELEMENT_POSITION:
                    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
                    break;

                case VERTEX_ELEMENT_COLOR:
                    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
                    break;

                case VERTEX_ELEMENT_TEXCOORD:
                    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    break;
            }
        }

    }

    public void draw(int mode, int first, int count){
        gl.glDrawArrays(mode, first, count);
    }
}
