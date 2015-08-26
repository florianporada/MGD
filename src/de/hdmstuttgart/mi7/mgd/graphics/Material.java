package de.hdmstuttgart.mi7.mgd.graphics;

import java.security.InvalidParameterException;

/**
 * Created by florianporada on 26.08.15.
 */
public class Material {
    private Texture texture;
    private TextureFilter textureFilterMin, textureFilterMag;
    private TextureWrapMode textureWrapModeU, textureWrapModeV;
    private TextureBlendMode textureBlendMode;
    private float[] textureBlendColor;

    private float[] materialColor;
    private BlendFactor blendFactorSrc, blendFactorDest;

    private Side cullSide;
    private CompareFunction depthTestFunction, alphaTestFunction;
    private boolean depthWrite;
    private float alphaTestValue;

    public Material(){
        texture = null;
        textureFilterMin = TextureFilter.LINEAR;
        textureFilterMag = TextureFilter.LINEAR;
        textureWrapModeU = TextureWrapMode.REPEAT;
        textureWrapModeV = TextureWrapMode.REPEAT;
        textureBlendMode = TextureBlendMode.MODULATE;
        textureBlendColor = new float[] {0.0f, 0.0f, 0.0f, 0.0f};

        materialColor = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
        blendFactorSrc = BlendFactor.ONE;
        blendFactorDest = BlendFactor.ZERO;

        cullSide = Side.NONE;
        depthTestFunction = CompareFunction.LESS;
        alphaTestFunction = CompareFunction.ALWAYS;
        depthWrite = true;
        alphaTestValue = 0;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public TextureFilter getTextureFilterMin() {
        return textureFilterMin;
    }

    public TextureFilter getTextureFilterMag() {
        return textureFilterMag;
    }

    public void setTextureFilter(TextureFilter textureFilterMin, TextureFilter textureFilterMag) {
        if (textureFilterMag != TextureFilter.NEAREST && textureFilterMag != TextureFilter.LINEAR)
            throw new InvalidParameterException("Magnification filter must be either NEAREST or LINEAR");

        this.textureFilterMin = textureFilterMin;
        this.textureFilterMag = textureFilterMag;
    }

    public TextureWrapMode getTextureWrapModeU() {
        return textureWrapModeU;
    }

    public TextureWrapMode getTextureWrapModeV() {
        return textureWrapModeV;
    }

    public void setTextureWrapMode(TextureWrapMode textureWrapModeU, TextureWrapMode textureWrapModeV) {
        this.textureWrapModeU = textureWrapModeU;
        this.textureWrapModeV = textureWrapModeV;
    }

    public TextureBlendMode getTextureBlendMode() {
        return textureBlendMode;
    }

    public void setTextureBlendMode(TextureBlendMode textureBlendMode) {
        this.textureBlendMode = textureBlendMode;
    }

    public float[] getTextureBlendColor() {
        return textureBlendColor;
    }

    public void setTextureBlendColor(float[] textureBlendColor) {
        if (materialColor.length != 4)
            throw new InvalidParameterException("Color must contain 4 elements (RGBA).");

        this.textureBlendColor = textureBlendColor;
    }

    public float[] getMaterialColor() {
        return materialColor;
    }

    public void setMaterialColor(float[] materialColor) {
        if (materialColor.length != 4)
        throw new InvalidParameterException("Color must contain 4 elements (RGBA).");

        this.materialColor = materialColor;
    }

    public BlendFactor getBlendFactorSrc() {
        return blendFactorSrc;
    }

    public BlendFactor getBlendFactorDest() {
        return blendFactorDest;
    }

    public void setBlendFactor(BlendFactor blendFactorSrc, BlendFactor blendFactorDest) {
        if (blendFactorSrc == BlendFactor.SRC_COLOR || blendFactorSrc == BlendFactor.ONE_MINUS_SRC_COLOR)
            throw new InvalidParameterException("Invalid source factor.");
        if (blendFactorDest == BlendFactor.DST_COLOR || blendFactorDest == BlendFactor.ONE_MINUS_DST_COLOR)
            throw new InvalidParameterException("Invalid destination factor.");

        this.blendFactorSrc = blendFactorSrc;
        this.blendFactorDest = blendFactorDest;
    }

    public Side getCullSide() {
        return cullSide;
    }

    public void setCullSide(Side cullSide) {
        this.cullSide = cullSide;
    }

    public CompareFunction getDepthTestFunction() {
        return depthTestFunction;
    }

    public void setDepthTestFunction(CompareFunction depthTestFunction) {
        this.depthTestFunction = depthTestFunction;
    }

    public CompareFunction getAlphaTestFunction() {
        return alphaTestFunction;
    }

    public void setAlphaTestFunction(CompareFunction alphaTestFunction) {
        this.alphaTestFunction = alphaTestFunction;
    }

    public boolean isDepthWrite() {
        return depthWrite;
    }

    public void setDepthWrite(boolean depthWrite) {
        this.depthWrite = depthWrite;
    }

    public float getAlphaTestValue() {
        return alphaTestValue;
    }

    public void setAlphaTestValue(float alphaTestValue) {
        if (alphaTestValue < 0.0f || alphaTestValue > 1.0f)
            throw new InvalidParameterException("Alpha test reference value must lie in the range between 0 and 1");

        this.alphaTestValue = alphaTestValue;
    }
}
