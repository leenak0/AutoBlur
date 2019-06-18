package com.leenak0.project.autoblur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CheckProfile extends AppCompatActivity {

    TextView txt_name;
    ImageView img_face_pic;
    ImageButton btn_change_name;
    ImageButton btn_delete_profile;
    ImageView img_pda;
    ImageButton btn_pyes;
    ImageButton btn_pno;

    String name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_profile);

        img_face_pic=(ImageView)findViewById(R.id.img_face_pic);
        btn_change_name=(ImageButton) findViewById(R.id.btn_change_name);
        btn_delete_profile=(ImageButton) findViewById(R.id.btn_delete_profile);
        txt_name=(TextView) findViewById(R.id.txt_name);
        img_pda = (ImageView)findViewById(R.id.profile_delete_answer_view);
        btn_pno = (ImageButton)findViewById(R.id.btn_delete_profile_no);
        btn_pyes = (ImageButton)findViewById(R.id.btn_delete_profile_yes);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        name=pref.getString("name", "");
        txt_name.setText(name);

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
                Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/4, bitmap.getWidth(), bitmap.getHeight()/2);
                //Toast.makeText(getApplicationContext(), "다운로드 완료!", Toast.LENGTH_SHORT).show();
                img_face_pic.setImageBitmap(bm1);
                //Toast.makeText(getApplicationContext(), "이미지 설정 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(), "다운로드 실패!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete_profile.setOnClickListener(new View.OnClickListener(){ //삭제버튼 눌렀을때

            @Override
            public void onClick(View v) {
                img_face_pic.setVisibility(View.INVISIBLE);
                txt_name.setVisibility(View.INVISIBLE);
                btn_change_name.setVisibility(View.INVISIBLE);
                btn_delete_profile.setVisibility(View.INVISIBLE);

                img_pda.setVisibility(View.VISIBLE);
                btn_pno.setVisibility(View.VISIBLE);
                btn_pyes.setVisibility(View.VISIBLE);
            }
        });

        btn_pno.setOnClickListener(new View.OnClickListener(){ //삭제버튼->X

            @Override
            public void onClick(View v) {
                img_face_pic.setVisibility(View.VISIBLE);
                txt_name.setVisibility(View.VISIBLE);
                btn_change_name.setVisibility(View.VISIBLE);
                btn_delete_profile.setVisibility(View.VISIBLE);

                img_pda.setVisibility(View.INVISIBLE);
                btn_pno.setVisibility(View.INVISIBLE);
                btn_pyes.setVisibility(View.INVISIBLE);
            }
        });

        btn_pyes.setOnClickListener(new View.OnClickListener(){ //삭제버튼->삭제

            @Override
            public void onClick(View v) {
                //종료 후 재시작
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("name");
                editor.commit();//이름삭제

                ActivityCompat.finishAffinity(CheckProfile.this);
                Intent intent=new Intent(CheckProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
