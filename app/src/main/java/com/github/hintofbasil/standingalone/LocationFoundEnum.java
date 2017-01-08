package com.github.hintofbasil.standingalone;

/**
 * Created by will on 05/01/17.
 */
public enum LocationFoundEnum {

    BROWNIE(R.drawable.brownie_title, R.drawable.blank,
            R.drawable.blank, R.string.location_found_brownie),
    GLAISTIG(R.drawable.glaistig_title, R.drawable.glaistig,
            R.drawable.glaistig_background, R.string.location_found_glaistig),
    CU_SITH(R.drawable.cu_sith_title, R.drawable.cu_sith,
            R.drawable.cu_sith_background, R.string.location_found_cu_sith),
    BLUE_WITCH(R.drawable.blue_witch_title, R.drawable.blue_witch,
            R.drawable.blue_witch_background, R.string.location_found_blue_witch),
    MERMAID(R.drawable.mermaid_title, R.drawable.mermaid,
            R.drawable.mermaid_background, R.string.location_found_mermaid),
    SELKIE(R.drawable.selkie_title, R.drawable.selkie,
            R.drawable.selkie_background, R.string.location_found_selkie),
    KELPIE(R.drawable.kelpie_title, R.drawable.kelpie,
            R.drawable.introduction_background_2, R.string.location_found_kelpie),
    CAIT_SITH(R.drawable.cait_sith_title, R.drawable.cait_sith,
            R.drawable.introduction_background_4, R.string.location_found_cait_sith),
    BRIAN(R.drawable.brian_title, R.drawable.brian,
            R.drawable.brian_background, R.string.location_found_brian),
    TREASURE(R.drawable.treasure_title, R.drawable.treasure,
            R.drawable.treasure_background, R.string.location_found_treasure);

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

    public static LocationFoundEnum get(int progress) {
        switch (progress) {
            case 1:
                return BROWNIE;
            case 2:
                return GLAISTIG;
            case 3:
                return CU_SITH;
            case 4:
                return BLUE_WITCH;
            case 5:
                return MERMAID;
            case 6:
                return SELKIE;
            case 7:
                return KELPIE;
            case 8:
                return CAIT_SITH;
            case 9:
                return BRIAN;
            case 10:
                return TREASURE;
            default:
                throw new RuntimeException("Invalid progress id: " + progress);
        }
    }
}
