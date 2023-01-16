package com.uob.infomate.Avatar;

public class EyesMorph extends Morph {

    public EyesMorph() {
        mCurrentIndex = 0;
        mChangeTime = 0;
        mCurrentMorph = 0;

        mLength = 21;

        mNextTimeout = new long[mLength];
        mMorphs = new int[mLength];

        openEyes(0, 0);

        eyesBlink(1, 2100);

        eyesBlink(5, 2500);

        eyesBlink(9, 2900);
        eyesBlink(13, 70);

        eyesBlink(17, 2200);
    }

    public int getMorph(long currentTime) {
        if(currentTime - mChangeTime >= mNextTimeout[mCurrentIndex]) {
            mCurrentMorph = mMorphs[mCurrentIndex];
            mChangeTime = currentTime;

            mCurrentIndex++;
            if(mCurrentIndex == mLength) {
                mCurrentIndex = 0;
            }
        }

        return mCurrentMorph;
    }

    private void openEyes(int startIndex, int startAfter) {
        mNextTimeout[startIndex] = milliToNano(startAfter);
        mMorphs[startIndex] = 0;
    }

    private void eyesBlink(int startIndex, int startAfter) {
        mNextTimeout[startIndex] = milliToNano(startAfter);
        mMorphs[startIndex++] = 1;
        mNextTimeout[startIndex] = milliToNano(70);
        mMorphs[startIndex++] = 2;
        mNextTimeout[startIndex] = milliToNano(70);
        mMorphs[startIndex++] = 1;
        mNextTimeout[startIndex] = milliToNano(70);
        mMorphs[startIndex++] = 0;
    }
}
