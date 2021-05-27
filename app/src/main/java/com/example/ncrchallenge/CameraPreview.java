package com.example.ncrchallenge;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    Boolean isPreviewRunning = false;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            isPreviewRunning = true;
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void previewCamera()
    {
        try
        {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            isPreviewRunning = true;
        }
        catch(Exception e)
        {
            Log.d(TAG, "Cannot start preview", e);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if (isPreviewRunning)
            {
                mCamera.stopPreview();
            }

            Camera.Parameters parameters = mCamera.getParameters();
//            display = Activity.ORI

//            if(display.getRotation() == Surface.ROTATION_0)
//            {
//                parameters.setPreviewSize(height, width);
//                mCamera.setDisplayOrientation(90);
//            }
//
//            if(display.getRotation() == Surface.ROTATION_90)
//            {
//                parameters.setPreviewSize(width, height);
//            }
//
//            if(display.getRotation() == Surface.ROTATION_180)
//            {
//                parameters.setPreviewSize(height, width);
//            }
//
//            if(display.getRotation() == Surface.ROTATION_270)
//            {
//                parameters.setPreviewSize(width, height);
//                mCamera.setDisplayOrientation(180);
//            }

            mCamera.setParameters(parameters);
            previewCamera();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}