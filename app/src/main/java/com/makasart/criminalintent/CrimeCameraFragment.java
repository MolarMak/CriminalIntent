package com.makasart.criminalintent;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

/**
 * Created by Maxim on 26.10.2016.
 */

@SuppressWarnings("deprecation")
 public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCamera";

    private Camera mCamera;
    private SurfaceView mSurfaceView;

    private boolean isLogged = true;

    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @TargetApi(9)
    @Override
    public void onPause() {
        super.onPause();
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crime_fragment_camera,
                parent, false);
        if (isLogged) {
            Log.d(TAG, "Start View");
        }
        Button takePictureButton = (Button)v.findViewById(R.id.make_photo_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                if (isLogged) {
                    Log.d(TAG, "Finish!");
                }
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.layout_camera);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    //I do this to support old devices
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                        if (isLogged) {
                            Log.d(TAG, "Surface Created");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) {
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(parameters);
                if (isLogged) {
                    Log.d(TAG, "In Surface Changed!");
                }
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Camera error in preview ", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    if (isLogged) {
                        Log.d(TAG, "Surface Destroyed");
                    }
                }
            }
        });

        return v;
    }

    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
       Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = s.height * s.width;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        if (isLogged) {
            Log.d(TAG, "Best Supported Size get!");
        }
        return bestSize;
    }

}
