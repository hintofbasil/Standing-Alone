package com.github.hintofbasil.standingalone;

/**
 * Created by will on 05/01/17.
 */
public enum LocationFoundEnum {

    BROWNIE(R.drawable.brownie_title, R.drawable.blank,
            R.drawable.blank, R.array.location_found_brownie,
            1f, true, false, new int[] {}, R.raw.brownie_found,
            R.string.character_details_brownie, new int[] {R.raw.brownie1}),
    GLAISTIG(R.drawable.glaistig_title, R.drawable.glaistig,
            R.drawable.glaistig_background, R.array.location_found_glaistig,
            1f, false, true, new int[] {10000, 4000, 15000, 9000, 5000}, 0,
            R.string.character_details_glaistig,
            new int[] {R.raw.glaistig1, R.raw.glaistig2, R.raw.glaistig3,
            R.raw.glaistig4, R.raw.glaistig5}),
    CU_SITH(R.drawable.cu_sith_title, R.drawable.cu_sith,
            R.drawable.cu_sith_background, R.array.location_found_cu_sith,
            1.6f, true, true, new int[] {9000, 13000, 4000, 8000, 5000, 5000},R.raw.cu_sith_found,
            R.string.character_details_cu_sith,
            new int[] {R.raw.cu_sith1, R.raw.cu_sith2, R.raw.cu_sith3, R.raw.cu_sith4,
            R.raw.cu_sith5, R.raw.cu_sith6}),
    BLUE_WITCH(R.drawable.blue_witch_title, R.drawable.blue_witch,
            R.drawable.blue_witch_background, R.array.location_found_blue_witch,
            1.5f, true, true, new int[] {6000, 12000, 4000, 5000, 14000, 14000, 10000}, R.raw.blue_witch_found,
            R.string.character_details_blue_witch,
            new int[] {R.raw.blue_witch1, R.raw.blue_witch2, R.raw.blue_witch3,
            R.raw.blue_witch4, R.raw.blue_witch5, R.raw.blue_witch6, R.raw.blue_witch7}),
    MERMAID(R.drawable.mermaid_title, R.drawable.mermaid,
            R.drawable.mermaid_background, R.array.location_found_mermaid,
            1.2f, false, true, new int[] {9000, 10000, 6000, 10000, 14000, 5000}, R.raw.mermaid_found,
            R.string.character_details_mermaid,
            new int[] {R.raw.mermaid1, R.raw.mermaid2, R.raw.mermaid3, R.raw.mermaid4,
            R.raw.mermaid5, R.raw.mermaid6}),
    SELKIE(R.drawable.selkie_title, R.drawable.selkie,
            R.drawable.selkie_background, R.array.location_found_selkie,
            1f, true, true, new int[] {8000, 8000, 19000, 13000, 5000}, R.raw.selkie_found,
            R.string.character_details_selkie,
            new int[] {R.raw.selkie1, R.raw.selkie2, R.raw.selkie3, R.raw.selkie4,
            R.raw.selkie5}),
    KELPIE(R.drawable.kelpie_title, R.drawable.kelpie,
            R.drawable.introduction_background_2, R.array.location_found_kelpie,
            2.2f, true, true, new int[] {16000, 9000, 10000, 17000, 8000}, R.raw.kelpie_found,
            R.string.character_details_kelpie,
            new int[] {R.raw.kelpie1, R.raw.kelpie2, R.raw.kelpie3, R.raw.kelpie4,
            R.raw.kelpie5}),
    CAIT_SITH(R.drawable.cait_sith_title, R.drawable.cait_sith,
            R.drawable.introduction_background_4, R.array.location_found_cait_sith,
            1f, true, true, new int[] {5000, 11000, 15000, 11000, 6000, 15000}, R.raw.cait_sith_found,
            R.string.character_details_cait_sith,
            new int[] {R.raw.cait_sith1, R.raw.cait_sith2, R.raw.cait_sith3, R.raw.cait_sith4,
            R.raw.cait_sith5, R.raw.cait_sith6, R.raw.cait_sith7}),
    BRIAN(R.drawable.brian_title, R.drawable.brian,
            R.drawable.brian_background, R.array.location_found_brian,
            1.6f, false, false, new int[] {15000}, R.raw.brian_found,
            R.string.character_details_brian,
            new int[] {R.raw.brian1, R.raw.brian2}),
    TREASURE(R.drawable.treasure_title, R.drawable.treasure,
            R.drawable.treasure_background, R.array.location_found_treasure,
            1.7f, true, true, new int[] {6000}, 0,
            0, new int[] {R.raw.treasure1});

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
    int[] speechFiles;

    LocationFoundEnum(int titleDrawableId, int characterDrawableId,
                      int backgroundDrawableId, int textStringId,
                      float scale, boolean beginWithBrownie,
                      boolean endWithNoSpeech, int[] textTimings,
                      int foundSoundFile, int characterDetailsText,
                      int[] speechFiles) {
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
        this.speechFiles = speechFiles;
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
