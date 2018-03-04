package com.digitalflow.belchior.appbelchior.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.digitalflow.belchior.appbelchior.Fragments.CameraFragment;
import com.digitalflow.belchior.appbelchior.Fragments.MusicFragment;
import com.digitalflow.belchior.appbelchior.R;

/**
 * Created by MarllonS on 01/03/2018.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private String[] abas = new String[]{"MUSICA", "CAMERA"};
    private int[] icons = new int[]{R.drawable.botao_musicas_blue,R.drawable.botao_camera_blue};
//    private int tamIcon;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
//        double scale = this.context.getResources().getDisplayMetrics().density;
//        tamIcon = (int) (96 * scale);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MusicFragment();
                break;
            case 1:
                fragment = new CameraFragment();
                break;
        }
        return fragment;
    }

/* ========= TAB BUTTONS ========= */
/*
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = ContextCompat.getDrawable(this.context, icons[position]);
        drawable.setBounds(0, 0, tamIcon*2, tamIcon);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
*/

    @Override
    public int getCount() {
        return icons.length;
    }
}
