package com.leenak0.project.autoblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SelectPic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        ImageView imageView = (ImageView)findViewById(R.id.select_picture_view);
        ImageButton btn_send = (ImageButton)findViewById(R.id.btn_send);
        ImageButton btn_prev = (ImageButton)findViewById(R.id.btn_prev);

        Bundle extras = getIntent().getExtras();
        final byte[] byteArray=getIntent().getByteArrayExtra("selectPic");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        imageView.setImageBitmap(bitmap);

        btn_send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent send = new Intent(SelectPic.this,ConvertedPic.class);
                send.putExtra("convertPic", byteArray);
                startActivity(send);
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent prev = new Intent(SelectPic.this,ChoosePic.class);
                startActivity(prev);
            }
        });
    }
}
