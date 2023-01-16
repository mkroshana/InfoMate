package com.uob.infomate.Avatar;

public class MouthMorph extends Morph {

    private boolean mEnded;

    public MouthMorph() {
        mEnded = true;
    }

    public int getMorph(long currentTime) {
        if(mEnded) return 0;

        while(currentTime - mChangeTime >= mNextTimeout[mCurrentIndex]) {
            mCurrentMorph = mMorphs[mCurrentIndex];
            mChangeTime = currentTime;

            mCurrentIndex++;
            if(mCurrentIndex == mLength) {
                mEnded = true;
                break;
            }
        }

        return mCurrentMorph;
    }

    public void setMouthMorphs(int[] timeMorphs) {
        mLength = timeMorphs.length / 2;
        mNextTimeout = new long[mLength];
        mMorphs = new int[mLength];

        mChangeTime = 0;
        mCurrentIndex = 0;
        mCurrentMorph = 0;

        int idx = 0;
        for(int i = 0; i < mLength; i++) {
            mNextTimeout[i] = milliToNano(timeMorphs[idx++]);
            mMorphs[i] = timeMorphs[idx++];
        }

        mEnded = false;
    }

    public void endMouthMorphs() {
        mEnded = true;
    }

}
