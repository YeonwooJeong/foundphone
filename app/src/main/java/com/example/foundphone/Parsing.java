package com.example.foundphone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Parsing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36";
    final String LOGIN_FORM_URL = "https://wiki.navercorp.com/login.action";
    final String LOGIN_ACTION_URL = "https://wiki.navercorp.com/dologin.action";
    final String USERNAME = "";
    final String PASSWORD = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        //AsyncTask 작동시킴(파싱)
        new Description().execute();
    }


    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(Parsing.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();
                Document loginDoc = loginForm.parse(); // this is the document containing response html
                HashMap<String, String> cookies = new HashMap<>(loginForm.cookies()); // save the cookies to be passed on to next request
                String authToken = loginDoc.select("#login > form > div:nth-child(1) > input[type=\"hidden\"]:nth-child(2)")
                        .first()
                        .attr("value");

                Document doc = Jsoup.connect("https://wiki.navercorp.com/pages/viewpage.action?pageId=324075067").get();
                System.out.println(doc);
                Elements mElementDataSize = doc.select("table[class=relative-table wrapped confluenceTable]").select("tbody tr"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                    Element element = elem.select("tr[class=highlight-grey confluenceTd]").first();
//                    String assetNumber = elem.select("tr[class=highlight-grey confluenceTd]").text();
                    String assetNumber = element.text();
                    element = element.nextElementSibling();
                    String itemNumber = element.text();
                    element = element.nextElementSibling();
                    String phoneName = element.text();
                    //ArrayList에 계속 추가한다.
//                    list.add(new ItemObject(assetNumber,itemNumber , phoneName));
                    list.add(new ItemObject(assetNumber,itemNumber , phoneName));
                }

                //추출한 전체 <li> 출력해 보자.
                Log.d("debug :", "List " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            MyAdapter myAdapter = new MyAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            progressDialog.dismiss();
        }
    }
}