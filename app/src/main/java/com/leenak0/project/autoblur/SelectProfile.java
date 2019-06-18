package com.leenak0.project.autoblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SelectProfile extends AppCompatActivity {

    ImageView img_scan_face;
    ImageButton btn_go_profile;
    ImageButton btn_cancel_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("TAG","----------------------열림---------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile);

        img_scan_face=(ImageView)findViewById(R.id.img_scan_face);
        btn_go_profile=(ImageButton) findViewById(R.id.btn_go_profile);
        btn_cancel_profile=(ImageButton) findViewById(R.id.btn_cancel_profile);

        //사진 firebase에서 가져오기
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("images/AutoBlur_scan_1");

        final long ONE_MEGABYTE = 1024 * 1024;
        desertRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                //Toast.makeText(getApplicationContext(), "다운로드 완료!", Toast.LENGTH_SHORT).show();
                img_scan_face.setImageBitmap(bitmap);
                //Toast.makeText(getApplicationContext(), "이미지 설정 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(), "다운로드 실패!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_go_profile.setOnClickListener(new View.OnClickListener(){ //체크버튼 눌렀을때

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectProfile.this, IdSetting.class);
                startActivity(intent);
            }
        });

        btn_cancel_profile.setOnClickListener(new View.OnClickListener(){ //삭제버튼 눌렀을때

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectProfile.this, ScanFace.class);
                startActivity(intent);
            }
        });
    }
}
