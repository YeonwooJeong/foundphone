package com.example.foundphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button parsingBtn;
    private Button scanQRBtn;
    private Button ListBtn;
    private EditText et_id;
    private EditText et_pw;
    private CheckBox cb_save;
    String id,pw;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this; // 이거 필수!

        parsingBtn = (Button) findViewById(R.id.parse);
        scanQRBtn = (Button) findViewById(R.id.scanQR);
        ListBtn = (Button) findViewById(R.id.dbList);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pw = (EditText) findViewById(R.id.et_pw);
        cb_save = (CheckBox) findViewById(R.id.cb_save);


        boolean boo = PreferenceManager.getBoolean(mContext,"check"); //로그인 정보 기억하기 체크 유무 확인
        if(boo){ // 체크가 되어있다면 아래 코드를 수행
            //저장된 아이디와 암호를 가져와 셋팅한다.
            et_id.setText(PreferenceManager.getString(mContext, "id"));
            et_pw.setText(PreferenceManager.getString(mContext, "pw"));
            cb_save.setChecked(true); //체크박스는 여전히 체크 표시 하도록 셋팅
        }

        parsingBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ //로그인 버튼 눌렀을 때 동작
                //아이디 암호 입력창에서 텍스트를 가져와 PreferenceManager에 저장함
                PreferenceManager.setString(mContext, "id", et_id.getText().toString()); //id라는 키값으로 저장
                PreferenceManager.setString(mContext, "pw", et_pw.getText().toString()); //pw라는 키값으로 저장

                Intent intent = new Intent(MainActivity.this, Parsing.class); //이건 없어도 무방
                // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
                String checkId = PreferenceManager.getString(mContext, "id");
                String checkPw = PreferenceManager.getString(mContext, "pw");
                //아이디와 암호가 비어있는 경우를 체크
                if (TextUtils.isEmpty(checkId) || TextUtils.isEmpty(checkPw)){
                    //아이디나 암호 둘 중 하나가 비어있으면 토스트메시지를 띄운다
                    Toast.makeText(MainActivity.this, "아이디/암호를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                }else { //둘 다 충족하면 다음 동작을 구현해놓음
                    intent.putExtra("id",checkId);
                    intent.putExtra("pw",checkPw);
                    startActivity(intent);
                }
            }
        });

        //로그인 기억하기 체크박스 유무에 따른 동작 구현
        cb_save.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) { // 체크박스 체크 되어 있으면
                    //editText에서 아이디와 암호 가져와 PreferenceManager에 저장한다.
                    PreferenceManager.setString(mContext, "id", et_id.getText().toString()); //id 키값으로 저장
                    PreferenceManager.setString(mContext, "pw", et_pw.getText().toString()); //pw 키값으로 저장
                    PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                } else { //체크박스가 해제되어있으면
                    PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                    PreferenceManager.clear(mContext); //로그인 정보를 모두 날림
                }
            }
        }) ;


        scanQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivity(intent);
            }
        });

        ListBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                Intent intent = new Intent(MainActivity.this, DBHelper.class);
//                startActivity(intent);
            }
        });
    }
}
