package com.github.hintofbasil.standingalone;

/**
 * Created by will on 05/01/17.
 */
public enum LocationFoundEnum {

    GLAISTIG(R.drawable.glaistig_title, R.drawable.glaistig,R.drawable.
            glaistig_background, R.string.location_found_glaistig);

    int titleDrawableId;
    int characterDrawableId;
    int backgroundDrawableId;
    int textStringId;

    LocationFoundEnum(int titleDrawableId, int characterDrawableId,
                      int backgroundDrawableId, int textStringId) {
        this.titleDrawableId = titleDrawableId;
        this.characterDrawableId = characterDrawableId;
        this.backgroundDrawableId = backgroundDrawableId;
        this.textStringId = textStringId;
    }
}
