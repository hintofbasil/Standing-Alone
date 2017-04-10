package com.github.hintofbasil.standingalone.aboutUs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.hintofbasil.standingalone.LocationFoundEnum;
import com.github.hintofbasil.standingalone.R;

import java.util.Arrays;

/**
 * Created by will on 09/04/17.
 */

public class CharacterGridAdapter extends BaseAdapter {

    private Context context;

    public CharacterGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return getValues().length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SquareImageView imageView;
        if (view == null) {
            imageView = new SquareImageView(context);
        } else {
            imageView = (SquareImageView) view;
        }
        LocationFoundEnum details = getValues()[i];
        if (details == LocationFoundEnum.BROWNIE) {
            imageView.setImageResource(R.drawable.brownie);
        } else {
            imageView.setImageResource(details.characterDrawableId);
        }
        return imageView;
    }

    private LocationFoundEnum[] getValues() {
        return Arrays.copyOfRange(
                LocationFoundEnum.values(),
                0,
                LocationFoundEnum.values().length-1
        );
    }

}
