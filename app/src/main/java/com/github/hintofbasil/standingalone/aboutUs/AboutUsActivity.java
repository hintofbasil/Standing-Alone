package com.github.hintofbasil.standingalone.aboutUs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.github.hintofbasil.standingalone.BaseActivity;
import com.github.hintofbasil.standingalone.LocationFoundEnum;
import com.github.hintofbasil.standingalone.R;

import static com.github.hintofbasil.standingalone.aboutUs.CharacterDetailsActivity.CHARACTER_DETAILS;

public class AboutUsActivity extends BaseActivity {

    public AboutUsActivity() {
        super(R.drawable.blank, R.layout.activity_about_us);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

        AutoHeightGridView characterGridContainer = (AutoHeightGridView) findViewById(R.id.character_grid_container);
        characterGridContainer.setScrollView(scrollView);
        characterGridContainer.setAdapter(new CharacterGridAdapter(this));

        characterGridContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AboutUsActivity.this, CharacterDetailsActivity.class);
                intent.putExtra(CHARACTER_DETAILS, LocationFoundEnum.get(i + 1));
                startActivity(intent);
            }
        });
    }
}
