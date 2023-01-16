package com.uob.infomate.Avatar;

public class Avatar {
    //region Properties
    private EyesMorph mEyesMorph;
    private MouthMorph mMouthMorph;
    private int mBodyDrawableId;
    private int[] mEyesMorphIds;
    private int[] mMouthMorphIds;
    private AvatarPart mEyes;
    private AvatarPart mMouth;

    //region Constructor

    public Avatar() {
        mEyesMorph = new EyesMorph();
        mMouthMorph = new MouthMorph();
    }

    //region Methods

    public int getBodyDrawableId() {
        return mBodyDrawableId;
    }

    public void setBodyDrawableId(int bodyDrawableId) {
        mBodyDrawableId = bodyDrawableId;
    }

    public int[] getEyesMorphIds() {
        return mEyesMorphIds;
    }

    public void setEyesMorphIds(int[] eyesMorphIds) {
        mEyesMorphIds = eyesMorphIds;
    }

    public int[] getMouthMorphIds() {
        return mMouthMorphIds;
    }

    public void setMouthMorphIds(int[] mouthMorphIds) {
        mMouthMorphIds = mouthMorphIds;
    }

    public int getEyesMorph(long currentTime) {
        return mEyesMorph.getMorph(currentTime);
    }

    public int getMouthMorph(long currentTime) {
        return mMouthMorph.getMorph(currentTime);
    }

    public void setMouthMorphs(int[] timeMorphs) {
        mMouthMorph.setMouthMorphs(timeMorphs);
    }

    public void endMouthMorphs() {
        mMouthMorph.endMouthMorphs();
    }

    public void setEyes(AvatarPart eyes) {
        mEyes = eyes;
    }

    public AvatarPart getEyes() {
        return mEyes;
    }

    public void setMouth(AvatarPart mouth) {
        mMouth = mouth;
    }

    public AvatarPart getMouth() {
        return mMouth;
    }
}
