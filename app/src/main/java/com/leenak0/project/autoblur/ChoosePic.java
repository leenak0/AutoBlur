package com.leenak0.project.autoblur;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChoosePic extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM=1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);
        tedPermission();

        final ImageButton btn_camera = (ImageButton)findViewById(R.id.btn_camera);
        final ImageButton btn_gallery = (ImageButton)findViewById(R.id.btn_gallery);

        btn_camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                takePhoto();
                /*int permissionCheck = ContextCompat.checkSelfPermission(ChoosePic.this, Manifest.permission.CAMERA);
                if(permissionCheck== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(ChoosePic.this,new String[]{Manifest.permission.CAMERA},0);
                }else{
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,PICK_FROM_CAMERA);
                }*/
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode != Activity.RESULT_OK) {

            //Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if(requestCode == PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();
                Cursor cursor = null;

                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                sendPic();

            } else if (requestCode == PICK_FROM_CAMERA) {

                sendPic();

            }
        }
    }

    private void tedPermission(){

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한요청성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void goToAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.leenak0.project.autoblur.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "AutoBlur_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/AutoBlur/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    private void sendPic(){

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = originalBm;

        float scale = (float) (1024/(float)bitmap.getWidth());
        int image_w = (int) (bitmap.getWidth() * scale);
        int image_h = (int) (bitmap.getHeight() * scale);
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(ChoosePic.this, SelectPic.class);
        intent.putExtra("selectPic", byteArray);

        startActivity(intent);
    }

}
