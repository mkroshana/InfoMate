package com.uob.infomate.Avatar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.view.SurfaceHolder;

public class AvatarThread extends Thread {

    //region Properties

    private boolean mRun = false;
    private boolean mStarted = false;

    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    private int mSizeType;

    private Avatar mAvatar;

    private AvatarPart mEyesPart;
    private int mCurrentEyesMorph;

    private AvatarPart mMouthPart;
    private int mCurrentMouthMorph;

    private Bitmap[] mEyes;
    private Bitmap[] mMouths;
    private Paint mBodyPaint;
    private Paint mBorderPaint;
    private int mSize;

    private int mEyesX, mEyesY, mEyesWidth, mEyesHeight;
    private int mMouthX, mMouthY, mMouthWidth, mMouthHeight;

    //region Constructor

    public AvatarThread(SurfaceHolder surfaceHolder, Context context, int sizeType) {
        mSurfaceHolder = surfaceHolder;
        mContext = context;
        mSizeType = sizeType;
    }

    //endregion

    //region Methods

    public void setRunning(boolean run) {
        mRun = run;
    }

    public void setStarted() {
        mStarted = true;
    }

    public boolean isStarted() {
        return mStarted;
    }

    public void setMouthMorphs(int[] timeMorphs) {
        mAvatar.setMouthMorphs(timeMorphs);
    }

    public void endMouthMorphs() {
        mAvatar.endMouthMorphs();
    }

    public void setAvatar() {
        mAvatar = AvatarInit.getEmbedded();
        loadAvatarBitmaps();
    }

    private void loadAvatarBitmaps() {
        mEyesPart = mAvatar.getEyes();
        mMouthPart = mAvatar.getMouth();

        mEyesX = Utils.calculateSize(mEyesPart.getX(), mSizeType);
        mEyesY = Utils.calculateSize(mEyesPart.getY(), mSizeType);
        mEyesWidth = Utils.calculateSize(mEyesPart.getWidth(), mSizeType);
        mEyesHeight = Utils.calculateSize(mEyesPart.getHeight(), mSizeType);

        mMouthX = Utils.calculateSize(mMouthPart.getX(), mSizeType);
        mMouthY = Utils.calculateSize(mMouthPart.getY(), mSizeType);
        mMouthWidth = Utils.calculateSize(mMouthPart.getWidth(), mSizeType);
        mMouthHeight = Utils.calculateSize(mMouthPart.getHeight(), mSizeType);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        mSize = Utils.calculateSize(Utils.BASE_HEIGHT, mSizeType);

        Bitmap body = BitmapFactory.decodeResource(mContext.getResources(), mAvatar.getBodyDrawableId(), options);
        body = Bitmap.createScaledBitmap(body, mSize, mSize, false);
        BitmapShader bodyShader = new BitmapShader(body, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBodyPaint = new Paint();
        mBodyPaint.setShader(bodyShader);
        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.TRANSPARENT);

        mSize += AvatarView.BORDER_WIDTH;

        Bitmap temp;
        int[] ids = mAvatar.getEyesMorphIds();
        mEyes = new Bitmap[ids.length];

        for(int i = 0; i < ids.length; i++) {
            temp = BitmapFactory.decodeResource(mContext.getResources(), ids[i], options);
            mEyes[i] = Bitmap.createScaledBitmap(temp, mEyesWidth, mEyesHeight, false);
        }

        ids = mAvatar.getMouthMorphIds();
        mMouths = new Bitmap[ids.length];

        for(int i = 0; i < ids.length; i++) {
            temp = BitmapFactory.decodeResource(mContext.getResources(), ids[i], options);
            mMouths[i] = Bitmap.createScaledBitmap(temp, mMouthWidth, mMouthHeight, false);
        }
    }

    @Override
    public void run() {
        while(mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if(isStarted()) {
                        doDraw(c);
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private void doDraw(Canvas canvas) {
        if(canvas == null) return;

        long currentTime = System.nanoTime();
        mCurrentEyesMorph = mAvatar.getEyesMorph(currentTime);
        mCurrentMouthMorph = mAvatar.getMouthMorph(currentTime);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if(mBodyPaint != null) {
            //canvas.drawCircle(mSize/2, mSize/2, mSize/2, mBorderPaint);
            canvas.drawCircle(mSize/2, mSize/2, 400, mBodyPaint);
            //canvas.drawCircle(mSize/2, mSize/2, mSize/2, mBorderPaint);
            //canvas.drawCircle(mSize/2, mSize/2, (mSize-AvatarView.BORDER_WIDTH)/2, mBodyPaint);
        }

        if(mEyes[mCurrentEyesMorph] != null) {
            canvas.drawBitmap(mEyes[mCurrentEyesMorph], mEyesX, mEyesY, null);
        }
        if(mMouths[mCurrentMouthMorph] != null) {
            canvas.drawBitmap(mMouths[mCurrentMouthMorph], mMouthX, mMouthY, null);
        }
    }

}
