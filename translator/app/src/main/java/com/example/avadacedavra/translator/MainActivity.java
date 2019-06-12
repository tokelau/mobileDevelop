package com.example.avadacedavra.translator;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    List<String> fromList;
    HashMap<String, String> fromHash;
    HashMap<String, String> toHash;
    List<String> toList;

    Spinner fromSpinner;
    Spinner toSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromList = new ArrayList<>();
        fromHash = new HashMap<>();
        toHash = new HashMap<>();
        fromList.add("Русский");
        fromList.add("Английский");
        fromHash.put("Русский", "ru");
        fromHash.put("Английский", "en");

        //получаем спиннер
        fromSpinner = (Spinner) findViewById(R.id.from);
        toSpinner = (Spinner) findViewById(R.id.to);

        //настраиваем адаптер
        setAdapter(fromSpinner, fromList);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                List<String> choose = new ArrayList<>();
                choose = fromList;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ваш выбор: " + choose.get(selectedItemPosition), Toast.LENGTH_SHORT);
                toast.show();

                try {
                    Langs lang = new Langs();
                    String str = fromHash.get(choose.get(selectedItemPosition));
                    lang.execute(str);
                    toHash = lang.get();
                    toList =  new ArrayList<>(toHash.keySet());
//                    Log.e("Nasty", toList.toString());

                    setAdapter(toSpinner, toList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final EditText fromText = (EditText) findViewById(R.id.fromText);
        final EditText toText = (EditText) findViewById(R.id.toText);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Translator", fromSpinner.getSelectedItem().toString());
                Log.w("Translator", fromText.getText().toString());

                List<String> requestParams = new ArrayList<>();
                try {
                    requestParams.add(URLEncoder.encode(fromText.getText().toString(), "utf-8"));
                    requestParams.add(fromHash.get(fromSpinner.getSelectedItem().toString()));
                    requestParams.add(toHash.get(toSpinner.getSelectedItem().toString()));

                    TranslateTask tt = new TranslateTask();
                    tt.execute(requestParams);
//                    Log.w("Translator", tt.get());
                    toText.setText(tt.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class Langs extends AsyncTask<String, Void, HashMap<String, String>> {
        private static final String TAG = "LangsClass";
        String API = "trnsl.1.1.20190411T012535Z.6217ef0f83112e43.9256d84033f1c1de5b650ad7538af217c6f321b9";

        @Override
        protected final HashMap<String, String> doInBackground(String... strings) {
            HashMap<String, String> res = new HashMap<>();
            InputStream inputStream = null;
            String data = "";

            try {
                URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" + API + "&ui=" + strings[0]);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    byte[] result = bos.toByteArray();
                    bos.close();

                    data = new String(result);


                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject langs = jsonObject.getJSONObject("langs");
                    for (Iterator<String> it = langs.keys(); it.hasNext(); ) {
                        String key = it.next();

                        res.put(langs.getString(key), key);
                    }

                } else {
                    data = connection.getResponseMessage() + " . Error Code : " + responseCode;
                }
                connection.disconnect();

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Log.w("NastyLang", res.toString());
            return res;
        }
        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            super.onPostExecute(hashMap);
        }
    }

    private static class TranslateTask extends AsyncTask<List<String>, Void, String> {
        private static final String TAG = "TranslateTask";
        String API = "trnsl.1.1.20190411T012535Z.6217ef0f83112e43.9256d84033f1c1de5b650ad7538af217c6f321b9";

        @Override
        protected final String doInBackground(List<String>... strings) {
            InputStream inputStream = null;
            String data = "";
            String res = "";

            try {
                URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API + "&text=" + strings[0].get(0) +
                    "&lang=" + strings[0].get(1) + "-" + strings[0].get(2) + "&format=plain");

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    byte[] result = bos.toByteArray();
                    bos.close();

                    data = new String(result);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray text = jsonObject.getJSONArray("text");
                    res = text.get(0).toString();
                } else {
                    data = connection.getResponseMessage() + " . Error Code : " + responseCode;
                }
                connection.disconnect();

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
        }
    }

    void setAdapter(Spinner sp, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }
}
