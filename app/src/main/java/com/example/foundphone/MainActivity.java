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
        mContext = this;

        parsingBtn = (Button) findViewById(R.id.parse);
        scanQRBtn = (Button) findViewById(R.id.scanQR);
        ListBtn = (Button) findViewById(R.id.dbList);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pw = (EditText) findViewById(R.id.et_pw);
        cb_save = (CheckBox) findViewById(R.id.cb_save);

        boolean boo = PreferenceManager.getBoolean(mContext,"check");
        if(boo){
            et_id.setText(PreferenceManager.getString(mContext, "id"));
            et_pw.setText(PreferenceManager.getString(mContext, "pw"));
            cb_save.setChecked(true);
        }

        parsingBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                PreferenceManager.setString(mContext, "id", et_id.getText().toString());
                PreferenceManager.setString(mContext, "pw", et_pw.getText().toString());

                Intent intent = new Intent(MainActivity.this, Parsing.class);
                String checkId = PreferenceManager.getString(mContext, "id");
                String checkPw = PreferenceManager.getString(mContext, "pw");
                if (TextUtils.isEmpty(checkId) || TextUtils.isEmpty(checkPw)){
                    Toast.makeText(MainActivity.this, "아이디/암호를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                }else {
                    intent.putExtra("id",checkId);
                    intent.putExtra("pw",checkPw);
                    startActivity(intent);
                }
            }
        });


        cb_save.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    PreferenceManager.setString(mContext, "id", et_id.getText().toString());
                    PreferenceManager.setString(mContext, "pw", et_pw.getText().toString());
                    PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked());
                } else {
                    PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked());
                    PreferenceManager.clear(mContext);
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
