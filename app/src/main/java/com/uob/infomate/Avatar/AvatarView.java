package com.uob.infomate.Avatar;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class AvatarView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int BORDER_WIDTH = 0;

    private Context mContext;

    private AvatarThread mAvatarThread;

    public void setMouthMorphs(int[] timeMorphs) {
        mAvatarThread.setMouthMorphs(timeMorphs);
    }

    public void endMouthMorphs() {
        mAvatarThread.endMouthMorphs();
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        mContext = context;

        int sizeType = Utils.getSizeType();
        int size = Utils.calculateSize(480, sizeType);
        size += BORDER_WIDTH;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.setFixedSize(size, size);

        mAvatarThread = new AvatarThread(holder, context, sizeType);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mAvatarThread.setRunning(true);

        if(mAvatarThread.isStarted()) return;

        mAvatarThread.start();
        mAvatarThread.setAvatar();
        mAvatarThread.setStarted();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        mAvatarThread.setRunning(false);

        while (retry) {
            try {
                mAvatarThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
