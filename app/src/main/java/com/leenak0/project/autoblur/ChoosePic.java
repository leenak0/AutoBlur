package com.leenak0.project.autoblur;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
            Uri photoUri = data.getData();
            Log.e("Tag","사진경로:"+photoUri);
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

            try {
                sendPic();
            } catch (IOException e) {
                Log.e("TAG","sendpic ei 오류");
            }

        } else if (requestCode == PICK_FROM_CAMERA) {

            try {
                sendPic();
            } catch (IOException e) {
                Log.e("TAG","sendpic ei 오류");
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
                        "Autoblur", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.e("Tag","누가사진경로:"+photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.e("Tag","이전사진경로:"+photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( AutoBlur_{시간} )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "AutoBlur_" + timeStamp;

        // 이미지가 저장될 폴더 이름 ( /storage/emulated/0/DCIM/AutoBlur/ )
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/AutoBlur/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    private void sendPic() throws IOException {

        ExifInterface ei = new ExifInterface(tempFile.getAbsolutePath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options(); //해상도 줄이는거
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(); //배열로 넘기는거
        Bitmap bitmap = originalBm;

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        float scale = (float) (1024/(float)rotatedBitmap.getWidth());
        int image_w = (int) (rotatedBitmap.getWidth() * scale);
        int image_h = (int) (rotatedBitmap.getHeight() * scale);
        Bitmap resize = Bitmap.createScaledBitmap(rotatedBitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 40, stream);//사이즈 조절하는부분?
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(ChoosePic.this, SelectPic.class);
        intent.putExtra("selectPic", byteArray);

        startActivity(intent);
        tempFile = null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
