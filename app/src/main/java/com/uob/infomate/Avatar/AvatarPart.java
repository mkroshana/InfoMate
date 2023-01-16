package com.uob.infomate.Avatar;

public class AvatarPart {

    private int mX;

    private int mY;

    private int mWidth;

    private int mHeight;

    public AvatarPart(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
