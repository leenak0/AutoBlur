package com.leenak0.project.autoblur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ConvertedPic extends AppCompatActivity {

    ImageView img_pic;
    ImageView img_da;
    ImageView img_pt;
    ImageView img_pn;
    ImageButton btn_save;
    ImageButton btn_share;
    ImageButton btn_no;
    ImageButton btn_yes;
    ImageButton btn_modify;
    ImageButton btn_finish;
    ImageButton btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converted_pic);

        img_pic = (ImageView)findViewById(R.id.converted_picture_view);
        img_da = (ImageView)findViewById(R.id.delete_answer_view);
        img_pt = (ImageView)findViewById(R.id.img_pic_top);
        img_pn=(ImageView)findViewById(R.id.img_pic_none);
        btn_save = (ImageButton)findViewById(R.id.btn_save);
        btn_share = (ImageButton)findViewById(R.id.btn_share);
        btn_no = (ImageButton)findViewById(R.id.btn_no);
        btn_yes = (ImageButton)findViewById(R.id.btn_yes);
        btn_modify = (ImageButton)findViewById(R.id.btn_modify);
        btn_finish = (ImageButton)findViewById(R.id.btn_finish);
        btn_delete = (ImageButton)findViewById(R.id.btn_delete);

        Bundle extras = getIntent().getExtras();
        final byte[] byteArray=getIntent().getByteArrayExtra("convertPic");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        img_pic.setImageBitmap(bitmap);

        btn_finish.setOnClickListener(new View.OnClickListener(){ //체크버튼 눌렀을때

            @Override
            public void onClick(View v) {
                btn_modify.setVisibility(View.INVISIBLE);
                btn_finish.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);

                btn_save.setVisibility(View.VISIBLE);
                btn_share.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener(){ //체크버튼->저장버튼

            @Override
            public void onClick(View v) {
                btn_save.setVisibility(View.INVISIBLE);
                btn_share.setVisibility(View.INVISIBLE);
                img_pn.setVisibility(View.INVISIBLE);

                img_pt.setImageResource(R.drawable.top_saved);

                //사진 받아와서 저장하는 코드 넣을것

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        ActivityCompat.finishAffinity(ConvertedPic.this);
                        Intent intent=new Intent(ConvertedPic.this, MainActivity.class);
                        startActivity(intent);
                    }
                },1500);
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener(){ //체크버튼->공유버튼

            @Override
            public void onClick(View v) {
                //공유
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                // Set default text message
                // 카톡, 이메일, MMS 다 이걸로 설정 가능
                String text = "얼굴인식 자동 모자이크 어플리케이션 AutoBlur"; //보낼문자
                intent.putExtra(Intent.EXTRA_TEXT, text);
                Intent chooser = Intent.createChooser(intent, "Share"); //공유창제목
                startActivity(chooser);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener(){ //삭제버튼 눌렀을때

            @Override
            public void onClick(View v) {
                btn_modify.setVisibility(View.INVISIBLE);
                btn_finish.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);
                img_pn.setVisibility(View.INVISIBLE);

                btn_no.setVisibility(View.VISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
                img_da.setVisibility(View.VISIBLE);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener(){ //삭제버튼->X

            @Override
            public void onClick(View v) {
                btn_modify.setVisibility(View.VISIBLE);
                btn_finish.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                img_pn.setVisibility(View.VISIBLE);

                btn_no.setVisibility(View.INVISIBLE);
                btn_yes.setVisibility(View.INVISIBLE);
                img_da.setVisibility(View.INVISIBLE);
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener(){ //삭제버튼->삭제

            @Override
            public void onClick(View v) {
                //종료 후 재시작
                ActivityCompat.finishAffinity(ConvertedPic.this);
                Intent intent=new Intent(ConvertedPic.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
