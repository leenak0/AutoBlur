package com.leenak0.project.autoblur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn_main_face = (ImageButton)findViewById(R.id.btn_main_face);
        btn_main_face.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent face_register = new Intent(getApplicationContext(),FaceRegister.class);
                startActivity(face_register);
            }
        });

        ImageButton btn_main_pic = (ImageButton)findViewById(R.id.btn_main_picture);
        btn_main_pic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent choose_pic = new Intent(getApplicationContext(),ChoosePic.class);
                startActivity(choose_pic);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
