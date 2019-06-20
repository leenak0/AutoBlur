package com.leenak0.project.autoblur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IdSetting extends AppCompatActivity {

    ImageView img_face_pick;
    EditText edit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_setting);

        img_face_pick=(ImageView)findViewById(R.id.img_face_pic);
        edit_name=(EditText)findViewById(R.id.edit_name);

        //사진 firebase에서 가져오기
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("/AutoBlur_scan_1.jpeg");

        final long ONE_MEGABYTE = 1024 * 1024;
        desertRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/4, bitmap.getWidth(), bitmap.getHeight()/2);
                //Toast.makeText(getApplicationContext(), "다운로드 완료!", Toast.LENGTH_SHORT).show();
                img_face_pick.setImageBitmap(bm1);
                //Toast.makeText(getApplicationContext(), "이미지 설정 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(), "다운로드 실패!", Toast.LENGTH_SHORT).show();
            }
        });

        edit_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(edit_name.getText().toString()!=""){
                        String name = edit_name.getText().toString();
                        SharedPreferences pref = getSharedPreferences( "pref" , MODE_PRIVATE);
                        SharedPreferences.Editor ed = pref.edit();
                        ed.putString( "name" , name );
                        ed.commit(); //이름 저장
                        ActivityCompat.finishAffinity(IdSetting.this);
                        Intent intent=new Intent(IdSetting.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
    }
}
