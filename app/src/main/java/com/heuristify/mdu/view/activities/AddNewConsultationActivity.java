package com.heuristify.mdu.view.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.databinding.ActivityAddNewConsultationBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.pojo.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class AddNewConsultationActivity extends BindingBaseActivity<ActivityAddNewConsultationBinding> implements OnClickHandlerInterface {
    String[] Gender = {"Male", "Female"};
    ArrayAdapter<String> gender_adapter;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 101;
    private Uri imageToUploadUri;
    private String path = "";
    private File compressedImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);


        gender_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Gender);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getDataBinding().materialSpinnerGender.setAdapter(gender_adapter);

        getDataBinding().materialSpinnerGender.getEditText().setText(Gender[0]);


    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
            else {
                openCamera();
            }
        }
    }

    private void openCamera() {
        ContentValues values;
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image File name");
        imageToUploadUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
        chooserIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(chooserIntent, 0);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_new_consultation;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewBack:
                finish();
                break;
            case R.id.buttonTakePatientPhoto:
                checkCameraPermission();
                break;
            case R.id.buttonNextConsultation:
                saveDataIntoDb();
                break;
        }
    }

    private void saveDataIntoDb() {
        if (getDataBinding().editTextConsultationName.getText().toString().length() > 0 && getDataBinding().editTextConsultatioCnicFirstTwoDigit.getText().toString().length() > 1
                && dataBinding.editTextConsultatioCnicLastFourDigit.getText().toString().length() > 3 && dataBinding.editTextConsultatioAge.getText().toString().length() > 0
                && dataBinding.materialSpinnerGender.getEditText().getText().toString().length() > 0) {

            saveData(getDataBinding().editTextConsultationName.getText().toString(), getDataBinding().editTextConsultatioCnicFirstTwoDigit.getText().toString(),
                    dataBinding.editTextConsultatioCnicLastFourDigit.getText().toString(), dataBinding.editTextConsultatioAge.getText().toString(), dataBinding.materialSpinnerGender.getEditText().getText().toString());

        } else {
            Toast.makeText(mContext, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveData(String name, String cNicFirstTwoDigit, String cNicLastFourDigit, String age, String gender) {

        new Thread(() -> {

            Patient patient = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getPatient(name, Integer.parseInt(cNicFirstTwoDigit), Integer.parseInt(cNicLastFourDigit), Integer.parseInt(age));
            if (patient != null) {
                runOnUiThread(() -> Toast.makeText(mContext, "Patient Already Exist", Toast.LENGTH_SHORT).show());
            } else {
                Patient patient1 = new Patient();
                patient1.setFullName(name);
                patient1.setCnicFirst2Digits(Integer.parseInt(cNicFirstTwoDigit));
                patient1.setCnicLast4Digits(Integer.parseInt(cNicLastFourDigit));
                patient1.setAge(Integer.parseInt(age));
                patient1.setGender(gender);
                patient1.setImage_path(compressedImageFile.getPath());
                int insert = (int) MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().insertPatient(patient1);

                if (insert > 0) {
                    runOnUiThread(() -> {
                        Toast.makeText(mContext, "Patient Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }

        }).start();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String compressImage(String filePath) {


        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();


        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;

        int actualWidth = options.outWidth;


        float maxHeight = 512.0f;
        float maxWidth = 512.0f;

        float imgRatio = actualWidth / actualHeight;

        float maxRatio = maxWidth / maxHeight;


        if (actualHeight > maxHeight || actualWidth > maxWidth) {

            if (imgRatio < maxRatio) {

                imgRatio = maxHeight / actualHeight;

                actualWidth = (int) (imgRatio * actualWidth);

                actualHeight = (int) maxHeight;

            } else if (imgRatio > maxRatio) {

                imgRatio = maxWidth / actualWidth;

                actualHeight = (int) (imgRatio * actualHeight);

                actualWidth = (int) maxWidth;

            } else {

                actualHeight = (int) maxHeight;

                actualWidth = (int) maxWidth;

            }
        }


        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);


        //      inJustDecodeBounds set to false to load the actual bitmap

        options.inJustDecodeBounds = false;


        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;

        options.inInputShareable = true;

        options.inTempStorage = new byte[16 * 1024];


        try {

            //          load the bitmap from its path

            bmp = BitmapFactory.decodeFile(filePath, options);

        } catch (OutOfMemoryError exception) {

            exception.printStackTrace();

        }
        try {

            scaledBitmap = Bitmap.createBitmap(actualWidth,
                    actualHeight, Bitmap.Config.ARGB_8888);

        } catch (OutOfMemoryError exception) {

            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;

        float ratioY = actualHeight / (float) options.outHeight;

        float middleX = actualWidth / 2.0f;

        float middleY = actualHeight / 2.0f;


        Matrix scaleMatrix = new Matrix();

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);


        Canvas canvas = new Canvas(scaledBitmap);

        canvas.setMatrix(scaleMatrix);

        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        //      check the rotation of the image and display it properly

        ExifInterface exif;

        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);

            Log.d("EXIF", "Exif: " + orientation);

            Matrix matrix = new Matrix();

            if (orientation == 6) {

                matrix.postRotate(90);

                Log.d("EXIF", "Exif: " + orientation);

            } else if (orientation == 3) {

                matrix.postRotate(180);

                Log.d("EXIF", "Exif: " + orientation);

            } else if (orientation == 8) {

                matrix.postRotate(270);

                Log.d("EXIF", "Exif: " + orientation);

            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e) {

            e.printStackTrace();
        }

        FileOutputStream out;

        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);


            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        return filename;

    }

    public String getFilename() {
        File file = new File(path);
        return file.getAbsolutePath();

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float)
                    reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = Math.min(heightRatio, widthRatio);

        }
        final float totalPixels = width * height;

        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {
                    if (imageToUploadUri != null) {
                        path = getRealPathFromURI(imageToUploadUri);
                        try {
                            compressedImageFile = new Compressor(getApplicationContext()).compressToFile(new File(compressImage(path)));
                            //                       profileImage.setImageBitmap(Bitmap.createScaledBitmap(compressedImageBitmap, (int) (compressedImageBitmap.getWidth() * 0.5), (int) (compressedImageBitmap.getHeight() * 0.5), true));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        DisplayLog.showLog("imagePath2", "" + compressedImageFile.getPath());


                    }
                }
            } else {
                throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
                    }
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }  // permission not granted

            }
        }
    }
}