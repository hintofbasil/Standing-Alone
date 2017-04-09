package com.github.hintofbasil.standingalone.aboutUs;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.standingalone.BaseActivity;
import com.github.hintofbasil.standingalone.LocationFoundEnum;
import com.github.hintofbasil.standingalone.R;

public class CharacterDetailsActivity extends BaseActivity {

    public CharacterDetailsActivity() {
        super(R.drawable.blank, R.layout.activity_character_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationFoundEnum details = LocationFoundEnum.BROWNIE;

        ImageView titleImage = (ImageView) findViewById(R.id.character_title_image);
        titleImage.setImageResource(details.titleDrawableId);

        ImageView characterImage = (ImageView) findViewById(R.id.character_image);
        if (details == LocationFoundEnum.BROWNIE) {
            characterImage.setImageResource(R.drawable.brownie);
        } else {
            characterImage.setImageResource(details.characterDrawableId);
        }

        TextView characterText = (TextView) findViewById(R.id.character_text);
        characterText.setText(details.characterDetailsText);

    }
}
