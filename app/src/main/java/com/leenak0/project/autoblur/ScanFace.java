package com.leenak0.project.autoblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ScanFace extends Activity implements SurfaceHolder.Callback {

    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;
    ImageButton startscan;
    ImageView arrow;
    ImageView scantop;
    ImageButton btn_stop;
    Bitmap scanbitmap;
    File uploadFile;
    String imageFileName;
    Uri photoUri;
    int i;
    int countFrame=0;
    int finishscan=0;
    String firebaseurl="************************";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_face);

        scantop=(ImageView)findViewById(R.id.img_scan_top);
        mCameraView = (SurfaceView)findViewById(R.id.cameraView);
        btn_stop=(ImageButton) findViewById(R.id.btn_stop);
        startscan = (ImageButton)findViewById(R.id.btn_start_scan);
        arrow=(ImageView)findViewById(R.id.scan_arrow);

        init();

        btn_stop.setOnClickListener(new View.OnClickListener(){ //스캔시작 버튼 눌렀을때

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScanFace.this, SelectProfile.class);
                startActivity(intent);
                finish();
            }
        });

        startscan.setOnClickListener(new View.OnClickListener(){ //스캔시작 버튼 눌렀을때

            @Override
            public void onClick(View v) {
                startscan.setVisibility(View.INVISIBLE);
                arrow.setVisibility(View.VISIBLE);
                scantop.setImageResource(R.drawable.explain_02);

                mCamera.setPreviewCallback(new Camera.PreviewCallback() {

                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        Camera.Parameters parameters = camera.getParameters();
                        int width = parameters.getPreviewSize().width;
                        int height = parameters.getPreviewSize().height;

                        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

                        byte[] bytes = out.toByteArray();
                        Bitmap oribitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Matrix m = new Matrix();
                        m.setRotate((float)270, oribitmap.getWidth(), oribitmap.getHeight());
                        scanbitmap= Bitmap.createBitmap(oribitmap, 0, 0, oribitmap.getWidth(), oribitmap.getHeight(), m, true);
                        Log.e("TAG","프레임: "+countFrame);
                        if(finishscan==20){ //업로드 끝나면
                            arrow.setVisibility(View.INVISIBLE);
                            scantop.setVisibility(View.INVISIBLE);
                            btn_stop.setVisibility(View.VISIBLE);
                        }
                        if (countFrame == 5) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(1);
                        }
                        if (countFrame == 10) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(2);
                        }
                        if (countFrame == 15) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(3);
                        }
                        if (countFrame == 20) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(4);
                        }
                        if (countFrame == 25) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(5);
                        }
                        if (countFrame == 30) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(6);
                        }
                        if (countFrame == 35) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(7);
                        }
                        if (countFrame == 40) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(8);
                        }
                        if (countFrame == 45) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(9);
                        }
                        if (countFrame == 50) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(10);
                            arrow.setScaleX(-1);
                        }
                        if (countFrame == 55) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(11);
                        }
                        if (countFrame == 60) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(12);
                        }
                        if (countFrame == 65) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(13);
                        }
                        if (countFrame == 70) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(14);
                        }
                        if (countFrame == 75) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(15);
                        }
                        if (countFrame == 80) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(16);
                        }
                        if (countFrame == 85) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(17);
                        }
                        if (countFrame == 90) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(18);
                        }
                        if (countFrame == 95) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(19);
                        }
                        if (countFrame == 100) {
                            Log.e("TAG", i + ": " + scanbitmap);
                            uploadFile(20);
                        }
                        countFrame++;
                    }
                });
            }
        });
    }

    private void init(){

        mCamera = Camera.open(1);
        mCamera.setDisplayOrientation(90);

        // surfaceview setting
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // surfaceholder 와 관련된 구현 내용
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // View 가 존재하지 않을 때
        if (mCameraHolder.getSurface() == null) {
            return;
        }

        // 작업을 위해 잠시 멈춘다
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // 에러가 나더라도 무시한다.
        }

        // 카메라 설정을 다시 한다.
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);

        // View 를 재생성한다.
        try {
            mCamera.setPreviewDisplay(mCameraHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
    //여러장 업로드 (반복문?)
    //upload the file
    private void uploadFile(int i) {
        //bitmap to file
        uploadFile = SaveBitmapToFileCache(scanbitmap,i);
        //업로드할 파일이 있으면 수행
        if (uploadFile != null) {
            //file to uri
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(this, "Autoblur", uploadFile);
            } else {
                photoUri = Uri.fromFile(uploadFile);
            }
//            //업로드 진행 Dialog 보이기
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("업로드중...");
//            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl(firebaseurl).child(imageFileName+".jpeg");
            //올라가거라...
            storageRef.putFile(photoUri)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            finishscan+=1;
//                            Toast.makeText(getApplicationContext(), ""+finishscan, Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            //Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           // @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    //double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }
    }
    public File SaveBitmapToFileCache(Bitmap bitmap, int i) {

        imageFileName = "AutoBlur_scan_"+i;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/AutoBlur/");
        Log.e("TAG","사진경로설정");
        if (!storageDir.exists()) storageDir.mkdirs();

        File fileCacheItem = new File(storageDir + imageFileName);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            Log.e("TAG","사진저장");

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileCacheItem;
    }
}
