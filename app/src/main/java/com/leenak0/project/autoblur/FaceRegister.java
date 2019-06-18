package com.leenak0.project.autoblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FaceRegister extends AppCompatActivity {

    ImageButton btn_register;
    ImageView img_face_pic;
    ImageView img_face_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_register);

        img_face_box = (ImageView)findViewById(R.id.img_face_box);
        img_face_pic = (ImageView)findViewById(R.id.img_face_pic);
        btn_register = (ImageButton)findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent scan = new Intent(FaceRegister.this,ScanFace.class);
                startActivity(scan);
            }
        });
    }
}
