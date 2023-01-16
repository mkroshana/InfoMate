package com.uob.infomate.Avatar;

import com.uob.infomate.R;

public class AvatarInit {

    public static Avatar getEmbedded() {
        Avatar avatar = new Avatar();

        avatar.setBodyDrawableId(R.drawable.jessica_p0);

        int[] eyesMorphs = {
                R.drawable.jessica_p0_e0, R.drawable.jessica_p0_e1, R.drawable.jessica_p0_e2
        };
        avatar.setEyesMorphIds(eyesMorphs);

        int[] mouthMorphs = {
                R.drawable.jessica_p0_v0, R.drawable.jessica_p0_v1, R.drawable.jessica_p0_v2,
                R.drawable.jessica_p0_v3, R.drawable.jessica_p0_v4, R.drawable.jessica_p0_v5,
                R.drawable.jessica_p0_v6, R.drawable.jessica_p0_v7, R.drawable.jessica_p0_v8,
                R.drawable.jessica_p0_v9, R.drawable.jessica_p0_v10
        };

        avatar.setMouthMorphIds(mouthMorphs);

        avatar.setEyes(new AvatarPart(168, 123, 153, 54));
        avatar.setMouth(new AvatarPart(177, 213, 126, 69));

        return avatar;
    }
}
