package de.hdmstuttgart.mi7.mgd.graphics;

import javax.microedition.khronos.opengles.GL10;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;
import java.util.Vector;

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

    public static Mesh loadFromOBJ(InputStream inputStream) throws IOException{
        Vector<float[]> positions = null;
        Vector<float[]> texCoords = null;
        Vector<short[]> indexGroups = null;


        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        for(String line = reader.readLine(); line != null; line = reader.readLine()){
            if(line.startsWith("#"))
                continue;

            StringTokenizer tokenizer = new StringTokenizer(line);
            int numTokens = tokenizer.countTokens();

            if(numTokens<1)
                continue;

            String command = tokenizer.nextToken();

            if(command.equals("v")){
                if(numTokens != 4)
                    throw new IOException("v: Only 3 Coordinates are supported (x y z)");

                float position[] = new float[] {
                        Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken())
                };

                if(positions==null)
                    positions = new Vector<float[]>();

                positions.add(position);
            }

            if(command.equals("vt")){
                if(numTokens != 3)
                    throw new IOException("vt: Only 2 Coordinates are supported (u v)");

                float textCoord[] = new float[] {
                        Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken())
                };

                if(texCoords==null)
                    texCoords = new Vector<float[]>();

                texCoords.add(textCoord);
            }

            if(command.equals("f")){
                if(numTokens != 4)
                    throw new IOException("f: Only triangles are supported");

                for(int i = 0; i < 3; i++){
                    short indexGroup[] = new short[3];
                    String indices[] = tokenizer.nextToken().split("/");
                    if (indices.length > 0)
                        indexGroup[0] = Short.parseShort(indices[0]);
                    if (indices.length > 1)
                        indexGroup[1] = Short.parseShort(indices[1]);
                    if (indices.length > 2)
                        indexGroup[2] = Short.parseShort(indices[2]);

                    if(indexGroups==null)
                        indexGroups = new Vector<short[]>();

                    indexGroups.add(indexGroup);
                }
            }
        }

        int numElements = 0;
        int vertexSize = 0;
        boolean hasPositionData = (positions != null);
        boolean hasTexCoordData = (texCoords != null);

        if (hasPositionData) {
            numElements++;
            vertexSize += 12;
        }
        if (hasTexCoordData) {
            numElements++;
            vertexSize += 8;
        }

        int elementIndex = 0;
        int elementOffset = 0;
        VertexElement[] elements = new VertexElement[numElements];
        if (hasPositionData) {
            elements[elementIndex] = new VertexElement(elementOffset, vertexSize, GL10.GL_FLOAT, 3, VertexElement.VertexSemantic.VERTEX_ELEMENT_POSITION);
            elementOffset += 12;
            elementIndex++;
        }
        if (hasTexCoordData) {
            elements[elementIndex] = new VertexElement(elementOffset, vertexSize, GL10.GL_FLOAT, 2, VertexElement.VertexSemantic.VERTEX_ELEMENT_TEXCOORD);
            elementOffset += 8;
            elementIndex++;
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(vertexSize * indexGroups.size());
        buffer.order(ByteOrder.nativeOrder());

        for (short[] indexGroup : indexGroups) {
            if (hasPositionData) {
                short vertexIndex = indexGroup[0];
                for (float f : positions.elementAt(vertexIndex - 1)) {
                    buffer.putFloat(f);
                }
            }

            if (hasTexCoordData) {
                short texCoordIndex = indexGroup[1];
                for (float f : texCoords.elementAt(texCoordIndex - 1)) {
                    buffer.putFloat(f);
                }
            }
        }

        buffer.position(0);

        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setVertexElements(elements);
        vertexBuffer.setByteBuffer(buffer);
        vertexBuffer.setNumVertices(indexGroups.size());


        Mesh mesh = new Mesh();
        mesh.buffer = vertexBuffer;
        mesh.mode = GL10.GL_TRIANGLES;

        return mesh;

    }
}
