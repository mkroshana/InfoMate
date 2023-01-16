package com.uob.infomate.Avatar;

import android.content.res.Resources;

public class Utils {

    public static int BASE_HEIGHT = 480;

    public static int getSizeType() {
        int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        if(displayWidth > 1000) return 3;
        if(displayWidth > 700) return 2;

        return 1;
    }

    public static int calculateSize(int size, int sizeType) {
        return size / 3 * sizeType;
    }

    public static int getWrapperHeight() {
        int sizeType = Utils.getSizeType();

        switch (sizeType) {
            case 3:
                return 600;
            case 2:
                return 400;
            default:
                return 220;
        }
    }

    public static int dpToPixels(int dp) {
        return dp * (int)Resources.getSystem().getDisplayMetrics().density;
    }
}
