package com.leenak0.project.autoblur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class FaceRegister extends AppCompatActivity {

    ImageButton btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_register);

        btn_register = (ImageButton)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent scan = new Intent(FaceRegister.this,ScanFace.class);
                startActivityForResult(scan,1);
            }
        });
    }
}
