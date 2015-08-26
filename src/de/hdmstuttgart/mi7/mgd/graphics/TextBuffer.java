package de.hdmstuttgart.mi7.mgd.graphics;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import de.hdmstuttgart.mi7.mgd.graphics.SpriteFont.CharacterInfo;


/**
 * Created by florianporada on 26.08.15.
 */
public class TextBuffer {
    private Mesh mesh;
    private SpriteFont spriteFont;

    public TextBuffer(GraphicsDevice graphicsDevice, SpriteFont spriteFont, int capacity){
        VertexElement[] elements = new VertexElement[]{
            new VertexElement(0, 16, GL10.GL_FLOAT, 2, VertexElement.VertexSemantic.VERTEX_ELEMENT_POSITION),
            new VertexElement(8, 16, GL10.GL_FLOAT, 2, VertexElement.VertexSemantic.VERTEX_ELEMENT_TEXCOORD)
        };

        ByteBuffer data = ByteBuffer.allocateDirect(6 * 16 * capacity);
        data.order(ByteOrder.nativeOrder());

        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setVertexElements(elements);
        vertexBuffer.setBuffer(data);
        vertexBuffer.setNumVertices(0);

        this.spriteFont = spriteFont;
        this.mesh = new Mesh(vertexBuffer, GL10.GL_TRIANGLES);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public SpriteFont getSpriteFont() {
        return spriteFont;
    }

    public void setText(String text) {
        Map<Character, SpriteFont.CharacterInfo> characterInfos = spriteFont.getCharacterInfos();
        Texture texture = spriteFont.getMaterial().getTexture();
        ByteBuffer data = mesh.getVertexBuffer().getBuffer();

        data.position(0);

        float x = 0;
        float y = 0;
        for (int index = 0; index < text.length(); ++index) {
            char c = text.charAt(index);

            CharacterInfo info = characterInfos.get(c);

            float posLeft = 	x + info.offset.x;
            float posRight = 	x + info.offset.x + info.bounds.width();
            float posTop = 		y - info.offset.y;
            float posBottom = 	y - (info.offset.y + info.bounds.height());
            float texLeft = 	(float) info.bounds.left 	/ (float) texture.getWidth();
            float texRight = 	(float) info.bounds.right 	/ (float) texture.getWidth();
            float texTop = 		1.0f - (float) info.bounds.top 		/ (float) texture.getHeight();
            float texBottom = 	1.0f - (float) info.bounds.bottom 	/ (float) texture.getHeight();

            // Dreieck 1
            data.putFloat(posLeft); 	data.putFloat(posTop); 		data.putFloat(texLeft); 	data.putFloat(texTop);
            data.putFloat(posLeft); 	data.putFloat(posBottom); 	data.putFloat(texLeft); 	data.putFloat(texBottom);
            data.putFloat(posRight); 	data.putFloat(posTop); 		data.putFloat(texRight); 	data.putFloat(texTop);

            // Dreieck 2
            data.putFloat(posRight); 	data.putFloat(posTop); 		data.putFloat(texRight); 	data.putFloat(texTop);
            data.putFloat(posLeft); 	data.putFloat(posBottom); 	data.putFloat(texLeft); 	data.putFloat(texBottom);
            data.putFloat(posRight); 	data.putFloat(posBottom); 	data.putFloat(texRight); 	data.putFloat(texBottom);

            x += info.width;


        }

        data.position(0);
        mesh.getVertexBuffer().setNumVertices(6 * text.length());
    }
}
