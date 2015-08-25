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

    public void drawMesh(Mesh mesh, Matrix4x4 world) {
        graphicsDevice.setWorldMatrix(world);

        VertexBuffer vertexBuffer = mesh.getBuffer();
        graphicsDevice.bindVertexBuffer(vertexBuffer);
        graphicsDevice.draw(mesh.getMode(), 0, vertexBuffer.getNumVertices());
        graphicsDevice.unbindVertexBuffer(vertexBuffer);
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }
}
