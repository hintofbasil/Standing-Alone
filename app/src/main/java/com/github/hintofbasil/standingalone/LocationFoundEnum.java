package com.github.hintofbasil.standingalone;

/**
 * Created by will on 05/01/17.
 */
public enum LocationFoundEnum {

    BROWNIE(R.drawable.brownie_title, R.drawable.blank,
            R.drawable.blank, R.array.location_found_brownie,
            1f, true, false, new int[] {}, R.raw.brownie_found,
            R.string.character_details_brownie),
    GLAISTIG(R.drawable.glaistig_title, R.drawable.glaistig,
            R.drawable.glaistig_background, R.array.location_found_glaistig,
            1f, false, true, new int[] {10000, 6000, 12000, 8000, 8000}, 0,
            R.string.character_details_glaistig),
    CU_SITH(R.drawable.cu_sith_title, R.drawable.cu_sith,
            R.drawable.cu_sith_background, R.array.location_found_cu_sith,
            1.6f, true, true, new int[] {10000, 11000, 5000, 8000, 8000, 8000},R.raw.cu_sith_found,
            R.string.character_details_cu_sith),
    BLUE_WITCH(R.drawable.blue_witch_title, R.drawable.blue_witch,
            R.drawable.blue_witch_background, R.array.location_found_blue_witch,
            1.5f, true, true, new int[] {8000, 10000, 6000, 8000, 6000, 11000, 10000}, R.raw.blue_witch_found,
            R.string.character_details_blue_witch),
    MERMAID(R.drawable.mermaid_title, R.drawable.mermaid,
            R.drawable.mermaid_background, R.array.location_found_mermaid,
            1.2f, false, true, new int[] {12000, 12000, 6000, 8000, 11000, 5000}, R.raw.mermaid_found,
            R.string.character_details_mermaid),
    SELKIE(R.drawable.selkie_title, R.drawable.selkie,
            R.drawable.selkie_background, R.array.location_found_selkie,
            1f, true, true, new int[] {8000, 9000, 13000, 13000, 7000}, R.raw.selkie_found,
            R.string.character_details_selkie),
    KELPIE(R.drawable.kelpie_title, R.drawable.kelpie,
            R.drawable.introduction_background_2, R.array.location_found_kelpie,
            2.2f, true, true, new int[] {13000, 9000, 9000, 20000, 7000}, R.raw.kelpie_found,
            R.string.character_details_kelpie),
    CAIT_SITH(R.drawable.cait_sith_title, R.drawable.cait_sith,
            R.drawable.introduction_background_4, R.array.location_found_cait_sith,
            1f, true, true, new int[] {7000, 9000, 11000, 9000, 8000, 11000}, R.raw.cait_sith_found,
            R.string.character_details_cait_sith),
    BRIAN(R.drawable.brian_title, R.drawable.brian,
            R.drawable.brian_background, R.array.location_found_brian,
            1.6f, false, false, new int[] {15000}, R.raw.brian_found,
            R.string.character_details_brian),
    TREASURE(R.drawable.treasure_title, R.drawable.treasure,
            R.drawable.treasure_background, R.array.location_found_treasure,
            1.7f, true, true, new int[] {10000}, 0,
            0);

    public int titleDrawableId;
    public int characterDrawableId;
    public int backgroundDrawableId;
    public int textStringId;
    public float scale;
    public boolean beginWithBrownie;
    public boolean endWithNoSpeech;
    public int[] textTimings;
    public int foundSoundFile;
    public int characterDetailsText;

    LocationFoundEnum(int titleDrawableId, int characterDrawableId,
                      int backgroundDrawableId, int textStringId,
                      float scale, boolean beginWithBrownie,
                      boolean endWithNoSpeech, int[] textTimings,
                      int foundSoundFile, int characterDetailsText) {
        this.titleDrawableId = titleDrawableId;
        this.characterDrawableId = characterDrawableId;
        this.backgroundDrawableId = backgroundDrawableId;
        this.textStringId = textStringId;
        this.scale = scale;
        this.beginWithBrownie = beginWithBrownie;
        this.endWithNoSpeech = endWithNoSpeech;
        this.textTimings = textTimings;
        this.foundSoundFile = foundSoundFile;
        this.characterDetailsText = characterDetailsText;
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
