package de.hdmstuttgart.mi7.mgd.graphics;

import de.hdmstuttgart.mi7.mgd.math.Matrix4x4;

/**
 * Created by florianporada on 25.08.15.
 */
public class Renderer {
    private GraphicsDevice graphicsDevice;

    public Renderer(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
    }

    public void drawMesh(Mesh mesh, Matrix4x4 world, Material material) {
        graphicsDevice.setWorldMatrix(world);

        setupMaterial(material);

        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        graphicsDevice.bindVertexBuffer(vertexBuffer);
        graphicsDevice.draw(mesh.getMode(), 0, vertexBuffer.getNumVertices());
        graphicsDevice.unbindVertexBuffer(vertexBuffer);
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }

    private void setupMaterial(Material material) {
        graphicsDevice.bindTexture(material.getTexture());
        graphicsDevice.setTextureFilters(material.getTextureFilterMin(), material.getTextureFilterMag());
        graphicsDevice.setTextureWrapMode(material.getTextureWrapModeU(), material.getTextureWrapModeV());
        graphicsDevice.setTextureBlendMode(material.getTextureBlendMode());
        graphicsDevice.setTextureBlendColor(material.getTextureBlendColor());

        graphicsDevice.setMaterialColor(material.getMaterialColor());
        graphicsDevice.setBlendFactors(material.getBlendFactorSrc(), material.getBlendFactorDest());

        graphicsDevice.setCullSide(material.getCullSide());
        graphicsDevice.setDepthTest(material.getDepthTestFunction());
        graphicsDevice.setDepthWrite(material.isDepthWrite());
        graphicsDevice.setAlphaTest(material.getAlphaTestFunction(), material.getAlphaTestValue());
    }

    public void drawText(TextBuffer textBuffer, Matrix4x4 world) {
        drawMesh(textBuffer.getMesh(), world, textBuffer.getSpriteFont().getMaterial());
    }
}
