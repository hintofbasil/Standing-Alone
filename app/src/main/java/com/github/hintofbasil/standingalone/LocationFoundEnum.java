package com.github.hintofbasil.standingalone;

/**
 * Created by will on 05/01/17.
 */
public enum LocationFoundEnum {

    BROWNIE(R.drawable.brownie_title, R.drawable.blank,
            R.drawable.blank, R.array.location_found_brownie,
            1f),
    GLAISTIG(R.drawable.glaistig_title, R.drawable.glaistig,
            R.drawable.glaistig_background, R.array.location_found_glaistig,
            1f),
    CU_SITH(R.drawable.cu_sith_title, R.drawable.cu_sith,
            R.drawable.cu_sith_background, R.array.location_found_cu_sith,
            1.6f),
    BLUE_WITCH(R.drawable.blue_witch_title, R.drawable.blue_witch,
            R.drawable.blue_witch_background, R.array.location_found_blue_witch,
            1.5f),
    MERMAID(R.drawable.mermaid_title, R.drawable.mermaid,
            R.drawable.mermaid_background, R.array.location_found_mermaid,
            1.2f),
    SELKIE(R.drawable.selkie_title, R.drawable.selkie,
            R.drawable.selkie_background, R.array.location_found_selkie,
            1f),
    KELPIE(R.drawable.kelpie_title, R.drawable.kelpie,
            R.drawable.introduction_background_2, R.array.location_found_kelpie,
            2.2f),
    CAIT_SITH(R.drawable.cait_sith_title, R.drawable.cait_sith,
            R.drawable.introduction_background_4, R.array.location_found_cait_sith,
            1f),
    BRIAN(R.drawable.brian_title, R.drawable.brian,
            R.drawable.brian_background, R.array.location_found_brian,
            1.6f),
    TREASURE(R.drawable.treasure_title, R.drawable.treasure,
            R.drawable.treasure_background, R.array.location_found_treasure,
            1.7f);

    int titleDrawableId;
    int characterDrawableId;
    int backgroundDrawableId;
    int textStringId;
    float scale;

    LocationFoundEnum(int titleDrawableId, int characterDrawableId,
                      int backgroundDrawableId, int textStringId,
                      float scale) {
        this.titleDrawableId = titleDrawableId;
        this.characterDrawableId = characterDrawableId;
        this.backgroundDrawableId = backgroundDrawableId;
        this.textStringId = textStringId;
        this.scale = scale;
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
