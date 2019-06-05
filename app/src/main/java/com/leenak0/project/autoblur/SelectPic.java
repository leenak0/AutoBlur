package com.leenak0.project.autoblur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SelectPic extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        ImageView imageView = (ImageView)findViewById(R.id.select_picture_view);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray=getIntent().getByteArrayExtra("selectPic");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        imageView.setImageBitmap(bitmap);
    }
}
