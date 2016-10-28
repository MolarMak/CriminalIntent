package com.makasart.criminalintent;

import android.app.DialogFragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.R.attr.fragment;

/**
 * Created by Maxim on 28.10.2016.
 */

public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.makasart.android.criminalintent.image_path";

    private ImageView mView;

    public static ImageFragment newInstance(String imagePATH) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePATH);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);

        mView.setImageDrawable(bitmapDrawable);

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mView);
    }
}
