package com.example.ncrchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;
import static com.example.ncrchallenge.HelperCamera.getCameraInstance;
import static com.example.ncrchallenge.Picture.MEDIA_TYPE_IMAGE;
import static com.example.ncrchallenge.Picture.getOutputMediaFile;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Camera.PictureCallback mPicture = (data, camera) -> {

        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        resumeCamer();
        // Add a listener to the Capture button
        Button captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                v -> {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                    preview.removeView(mPreview);
                    resumeCamer();
                }
        );
    }

    public void resumeCamer(){
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCamera==null) {
            preview.removeView(mPreview);
            resumeCamer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}