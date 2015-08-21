package com.jsoftkenya.apps.weatherstats;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.os.AsyncTask;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SendStats extends Activity {

    private EditText editTextLoc,editTextTemp,editTextHumidity,editTextRain;
    private EditText editTextAdd,editTextIp;
    private String ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_stats);

        editTextLoc = (EditText) findViewById(R.id.editTextLocation);
        editTextTemp = (EditText) findViewById(R.id.editTextTemp);
        editTextHumidity = (EditText) findViewById(R.id.editTextHumidity);
        editTextRain = (EditText) findViewById(R.id.editTextRain);
        editTextIp = (EditText) findViewById(R.id.editTextIp);


    }

    public void insert(View view){

        String loc, temp, humidity, rain;
        ip=editTextIp.getText().toString();
        loc = editTextLoc.getText().toString();
        temp = editTextTemp.getText().toString();
        humidity = editTextHumidity.getText().toString();
        rain = editTextRain.getText().toString();
        if(ip == null || ip.equals("")
            ||loc  == null || loc.equals("")
            ||temp == null || temp.equals("")
            ||humidity == null || humidity.equals("")
            ||rain == null || rain.equals("")){
            Toast.makeText(this,"Please Enter All Data!!",Toast.LENGTH_LONG).show();
        }
        else {

            insertToDatabase(loc, temp, humidity, rain);
        }
    }

    private void insertToDatabase(String l, String t,String h, String r){

      final  String loc=l,temp=t,humidity=h,rain=r;

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            String result="";
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramAddress = params[1];




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("location", loc));
                nameValuePairs.add(new BasicNameValuePair("temperature", temp));
                nameValuePairs.add(new BasicNameValuePair("humidity", humidity));
                nameValuePairs.add(new BasicNameValuePair("rainfall", rain));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://"+ip+"/weather-app/insert-db.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    //HttpEntity entity = response.getEntity();

                    InputStream content = response.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        result += s;
                    }

                } catch (ClientProtocolException e) {
                    result="Protocol Error:"+e.getMessage();

                } catch (IOException e) {
                    result="Protocol Error:"+e.getMessage();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                textViewResult.setText(result);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(loc,temp,humidity,rain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

