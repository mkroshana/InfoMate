package com.uob.infomate.Avatar;

public abstract class Morph {
    protected long[] mNextTimeout;
    protected int[] mMorphs;
    protected int mLength;

    protected long mChangeTime;

    protected int mCurrentIndex;
    protected int mCurrentMorph;

    protected long milliToNano(int milli) {
        return (long)milli * 1000000;
    }
}