package com.example.jsontest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonActivity extends AppCompatActivity {
    // url = "http://52.78.72.175/data/restaurant";
    private static String TAG = "data";
    private static final String TAG_JSON = "restaurant";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_BUSINESS_HOURS = "business_hours";
    private static final String TAG_PHONE_NUMBER = "phone_number";
    private static final String TAG_CATEGORY_NAME = "category_name";

    private static final String TAG_IMAGE = "image";
    private TextView mTextViewResult;       // 에러 보여주는 용도
    ListView mlistView;
    ArrayList<HashMap<String, String>> listItems;
    String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = (TextView) findViewById(R.id.textView_main);
        mlistView = (ListView) findViewById(R.id.listView_main);
        listItems = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://52.78.72.175/data/restaurant");
    }

    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(JsonActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            mTextViewResult.setText(result);        // 읽어온 데이터 보여줌
            Log.d(TAG, "response : " + result);
            if(result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;               // 서버의 데이터를 문자열로 읽어와서 변수에 저장
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();

                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code : " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == conn.HTTP_OK){
                    inputStream = conn.getInputStream();
                }
                else {
                    inputStream = conn.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
    }

    private void showResult(){
        try{
            JSONArray jsonArray = new JSONArray(mJsonString);       // 전체 데이터를 배열에 저장

            for(int i=0; i<jsonArray.length(); i++){                // 한 그룹{} 씩 읽음
                JSONObject item = jsonArray.getJSONObject(i);       // 해당 그룹의 데이터 하나씩 읽어서 각각의 변수에 저장
                String id= item.getString(TAG_ID);
                String name = item.getString(TAG_NAME);
                String address = item.getString(TAG_ADDRESS);
                String business_hours = item.getString(TAG_BUSINESS_HOURS);
                String phone_number = item.getString(TAG_PHONE_NUMBER);
                String category_name = item.getString(TAG_CATEGORY_NAME);
                String image = item.getString(TAG_IMAGE);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_ADDRESS, address);
                hashMap.put(TAG_BUSINESS_HOURS, business_hours);
                hashMap.put(TAG_PHONE_NUMBER, phone_number);
                hashMap.put(TAG_CATEGORY_NAME, category_name);
                hashMap.put(TAG_IMAGE, image);                      // hashmap에 짝 지어 넣음

                listItems.add(hashMap);                             // 데이터 저장된 최종 변수
            }
            ListAdapter adapter = new SimpleAdapter(
                    JsonActivity.this, listItems, R.layout.activity_json,
                    new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS, TAG_BUSINESS_HOURS,
                    TAG_PHONE_NUMBER, TAG_CATEGORY_NAME, TAG_IMAGE},
                    new int[]{R.id.id, R.id.name, R.id.address, R.id.business_hours,
                    R.id.phone_number, R.id.category_name, R.id.image}
            );
            mlistView.setAdapter(adapter);                          // xml에서의 출력을 위한 리스트뷰에 넣어줌

        } catch (JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }

}
