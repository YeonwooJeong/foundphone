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
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Parsing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36";
    final String LOGIN_FORM_URL = "https://wiki.navercorp.com/login.action";
    final String LOGIN_ACTION_URL = "https://wiki.navercorp.com/dologin.action";
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
                Connection.Response loginPageResponse = Jsoup.connect("https://wiki.navercorp.com/dologin.action")
                        .timeout(3000)
                        .header("Origin", "https://wiki.navercorp.com")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Upgrade-Insecure-Requests", "1")
                        .method(Connection.Method.GET)
                        .execute();
//                 로그인 페이지에서 얻은 쿠키
                Map<String, String> loginTryCookie = loginPageResponse.cookies();

//                 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
                Document loginPageDocument = loginPageResponse.parse();

//                String os_username = loginPageDocument.select("input.os_username").val();
//                String os_password = loginPageDocument.select("input.os_password").val();

                // 전송할 폼 데이터
                Map<String, String> data = new HashMap<>();
                data.put("os_username", "nt11062");
                data.put("os_password", "");
                // 로그인(POST)
                Connection.Response response = Jsoup.connect("https://wiki.navercorp.com/dologin.action")
                        .userAgent(USER_AGENT)
                        .timeout(3000)
                        .header("Origin", "https://wiki.navercorp.com")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Upgrade-Insecure-Requests", "1")
                        .method(Connection.Method.POST)
//                        .cookies(loginTryCookie)
                        .data(data)
                        .execute();
                // 로그인 성공 후 얻은 쿠키.
                Map<String, String> loginCookie = response.cookies();

                Document doc = Jsoup.connect("https://wiki.navercorp.com/pages/viewpage.action?pageId=324075067")
                        .userAgent(USER_AGENT)
                        .cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
                        .get();
//                System.out.println(doc);
                Elements mElementDataSize = doc.select("table[class=relative-table wrapped confluenceTable]").select("tbody tr"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다.

                for(Element elem : mElementDataSize){
                    //다시 원하는 데이터를 추출해 낸다.tr td[class=highlight-grey confluenceTd] tr
                    Element element = elem.select("td").first();
//                    String assetNumber = elem.select("tr[class=highlight-grey confluenceTd]").text();
                    String assetNumber = element.text();
                    System.out.println("assetNumber : "+assetNumber);
                    element = element.nextElementSibling();
                    String itemNumber = element.text();
                    System.out.println("itemNumber : "+itemNumber);
                    element = element.nextElementSibling();
                    String phoneName = element.text();
                    System.out.println("phoneName : "+phoneName);
                    //ArrayList에 계속 추가한다.
                    list.add(new ItemObject(assetNumber,itemNumber , phoneName));
                    int i = 0;
                    System.out.println("----------------------------------------------------"+list.get(i).getAssetNumber());
                    i++;
                }

                //추출한 전체 <li> 출력해 보자.
//                Log.d("debug :", "List " + mElementDataSize);
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