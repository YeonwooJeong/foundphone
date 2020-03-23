package com.example.foundphone;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Parsing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36";
    DBHelper dbHelper; //이렇게
    SQLiteDatabase db;  //이렇게
    String tableName,id,pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);
        Bundle intent = getIntent().getExtras();
        id = intent.getString("id");
        pw = intent.getString("pw");
        dbHelper = new DBHelper(Parsing.this); //이렇게
        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase(); //이렇게
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //AsyncTask 작동시킴(파싱)
        new Description().execute();
    }

    private long putDB(String asset_number, String item_number,String phone_name,String status){
        //DB insert하는 부분
        ContentValues values = new ContentValues();
        values.put(AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER, asset_number);
        values.put(AssetListData.AssetEntry.COLUMN_NAME_ITEMNUMBER, item_number);
        values.put(AssetListData.AssetEntry.COLUMN_NAME_PHONENAME, phone_name);
        values.put(AssetListData.AssetEntry.COLUMN_NAME_STATUS, status);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("assets",null, values);
        return newRowId;
    }
    public void readAllDb(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER,
                AssetListData.AssetEntry.COLUMN_NAME_ITEMNUMBER,
                AssetListData.AssetEntry.COLUMN_NAME_PHONENAME,
                AssetListData.AssetEntry.COLUMN_NAME_STATUS
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                AssetListData.AssetEntry.COLUMN_NAME_ITEMNUMBER + " DESC";

        Cursor cursor = db.query(
                AssetListData.AssetEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            System.out.println("결과 -- : "+cursor.getString(2));
            System.out.println("--------------------------------------------------");
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(AssetListData.AssetEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();
    }
    private List readDb(String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER,
                AssetListData.AssetEntry.COLUMN_NAME_ITEMNUMBER,
                AssetListData.AssetEntry.COLUMN_NAME_PHONENAME,
                AssetListData.AssetEntry.COLUMN_NAME_STATUS
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                AssetListData.AssetEntry.COLUMN_NAME_ITEMNUMBER + " DESC";

        Cursor cursor = db.query(
                AssetListData.AssetEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List itemIds = new ArrayList<>();
        int i = 0;
        while(cursor.moveToNext()) {
            System.out.println("itemId -- :"+i+","+cursor.getString(1));
            System.out.println("itemId -- :"+i+","+cursor.getString(2));
            System.out.println("itemId -- :"+i+","+cursor.getString(3));
            System.out.println("--------------------------------------------------");
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(AssetListData.AssetEntry._ID));
            System.out.println("itemId : "+itemId);
            itemIds.add(itemId);

        }
        cursor.close();

        return itemIds;
    }

    private int deleteDb(String[] selectionArgs){
        // Define 'where' part of query.
        String selection = AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER + " LIKE ?";
        // Specify arguments in placeholder order.
        // Issue SQL statement.
        int result = db.delete(AssetListData.AssetEntry.TABLE_NAME, selection, selectionArgs);
        return result;
    }

    private int upDateDb(String title, String[] selectionArgs){
        //체크여부 수정 코드 필요
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER, title);

        // Which row to update, based on the title
        String selection = AssetListData.AssetEntry.COLUMN_NAME_ASSETNUMBER + " LIKE ?";

        int count = db.update(
                AssetListData.AssetEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    private void closeDb(){
        if(dbHelper!=null)
            dbHelper.close();
        if(db!=null)
            db.close();
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
                data.put("os_username", id);
                data.put("os_password", pw);
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
                int i = 0;
                for(Element elem : mElementDataSize){
                    //다시 원하는 데이터를 추출해 낸다.tr td[class=highlight-grey confluenceTd] tr
                    Element element = elem.select("td").first();
                    String asset_number = element.text();
//                    System.out.println("asset_number : "+asset_number);
                    element = element.nextElementSibling();
                    String item_number = element.text();
//                    System.out.println("item_number : "+item_number);
                    element = element.nextElementSibling();
                    String phone_name = element.text();
//                    System.out.println("phone_name : "+phone_name);
                    //DB 데이터 집어넣기
                    putDB(asset_number,item_number,phone_name,"x");
                    //ArrayList에 계속 추가한다.
                    list.add(new ItemObject(asset_number,item_number, phone_name,"x"));

                    System.out.println("----------------------------------------------------"+list.get(i).getAssetNumber());
                    System.out.println("----------------------------------------------------"+list.get(i).getStatus());
                    i++;
                }
//                System.out.println("readDB"+readDb(new String[]{"AC01498351"}));
                //추출한 전체 <li> 출력해 보자.
//                Log.d("debug :", "List " + mElementDataSize);
                readAllDb();
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