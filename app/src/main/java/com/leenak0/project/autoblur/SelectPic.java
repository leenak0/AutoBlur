package com.leenak0.project.autoblur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectPic extends AppCompatActivity {

    Bitmap bitmap;
    File uploadFile;
    String imageFileName;
    Uri photoUri;
    String firebaseurl=**********;
    String serverurl=**********;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        ImageView imageView = (ImageView)findViewById(R.id.select_picture_view);
        ImageButton btn_send = (ImageButton)findViewById(R.id.btn_send);
        ImageButton btn_prev = (ImageButton)findViewById(R.id.btn_prev);

        final byte[] byteArray=getIntent().getByteArrayExtra("selectPic");
        bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        imageView.setImageBitmap(bitmap);

        btn_send.setOnClickListener(new View.OnClickListener(){ //선택버튼

            @Override
            public void onClick(View v) {
                deleteFile();
                uploadFile();
                new Thread() {
                    public void run() {
                        String Html = getHtml();

                        Bundle bun = new Bundle();
                        bun.putString("HTML", Html);
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener(){ //삭제버튼

            @Override
            public void onClick(View v) {
                Intent prev = new Intent(SelectPic.this,ChoosePic.class);
                startActivity(prev);
            }
        });
    }
    public File SaveBitmapToFileCache(Bitmap bitmap) {

        imageFileName = "AutoBlur_before";

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/AutoBlur/");
        if (!storageDir.exists()) storageDir.mkdirs();

        File fileCacheItem = new File(storageDir + imageFileName);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

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
    //delete file
    private void deleteFile(){
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("/AutoBlur_before.jpeg");
        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(), "삭제 실패!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //upload the file
    private void uploadFile() {
        //bitmap to file
        uploadFile = SaveBitmapToFileCache(bitmap);
        //업로드할 파일이 있으면 수행
        if (uploadFile != null) {
            //file to uri
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(this, "Autoblur", uploadFile);
            } else {
                photoUri = Uri.fromFile(uploadFile);
            }
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

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
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            //Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            Intent send = new Intent(SelectPic.this,ConvertedPic.class);
//                          send.putExtra("convertPic", byteArray);
                            startActivity(send);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }
    }
    private String getHtml(){
        String Html = "";

        URL url =null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            url = new URL(serverurl);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3*1000);
            http.setReadTimeout(3*1000);

            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                Html += str + "\n";
            }

        }catch(Exception e){
            Log.e("Exception", e.toString());
        }finally{
            if(http != null){
                try{http.disconnect();}catch(Exception e){}
            }

            if(isr != null){
                try{isr.close();}catch(Exception e){}
            }

            if(br != null){
                try{br.close();}catch(Exception e){}
            }
        }

        return Html;
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
        }
    };
}
