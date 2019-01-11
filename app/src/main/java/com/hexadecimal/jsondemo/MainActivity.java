package com.hexadecimal.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){

                    char  current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        // asynctask islemini yaparken kullanici arayuzunde hicbir seye dokunmamali,
        // kullanici arayuzunu ilgilendiren hersey onPostExecute' metodu icinde gerceklestirilmeli

        @Override
        protected void onPostExecute(String s) {        // buradaki string ifade yukaridaki result degerini alir
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather"); // sadece weather basligi altindaki verileri getirecek

                JSONArray arr = new JSONArray(weatherInfo);         // gelen verileri JSON tipinde olusturdugumuz array'e yerlestirdik

                for(int i = 0; i<arr.length(); i++){

                    JSONObject jsonPart = arr.getJSONObject(i);   // JSON sorgusundan gelen verileri atadigimiz diziden alıp parcalara bolduk

                    Log.i("main", jsonPart.getString("main"));                  // kullanacagimiz verileri basligi ile belirterek kullandik
                    Log.i("description", jsonPart.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // almak istedigimiz url'yi girdigimiz yer

        DownloadTask task = new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
    }
}
