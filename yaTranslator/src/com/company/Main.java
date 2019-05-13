package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main {
    final static String API_KEY = "trnsl.1.1.20190411T012535Z.6217ef0f83112e43.9256d84033f1c1de5b650ad7538af217c6f321b9";

    public static void main(String[] args) throws IOException {
        String lang;
        String text;
        Scanner reader = new Scanner(System.in);
        System.out.print("Input lang [from]-[to]: ");
        lang = reader.nextLine();
        System.out.print("Input text to translate: ");
        text = reader.nextLine();

        String set_server_url = "https://translate.yandex.net/api/v1.5/tr.json/translate";

        // need to URL encode form fields
        // https://docs.oracle.com/javase/8/docs/api/java/net/URLEncoder.html

        String encoded = URLEncoder.encode(text, "utf-8");
        String data = "lang="+lang+"&format=plain&text="+encoded;
        data += "&key=" + API_KEY;

        URL url = new URL(set_server_url);
        // creating connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true); // setting POST method
        // creating stream for writing request
        OutputStream out = urlConnection.getOutputStream();
        out.write(data.getBytes("utf-8"));

        // reading response
        Scanner in = new Scanner(urlConnection.getInputStream());
        if (in.hasNext()) {
            String line = in.nextLine();
            Gson gson = new Gson();
            Translation tr = gson.fromJson(line, Translation.class);
            System.out.println(tr.text.get(0));
        } else System.out.println("No output returned");
        urlConnection.disconnect();
    }
}
