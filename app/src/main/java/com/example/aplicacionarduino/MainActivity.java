package com.example.aplicacionarduino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    private static final String URL_READ = "https://api.thingspeak.com/channels/2383483/fields/2.json?api_key=CZWGMFOUVMBZO0UK&results=1";
    private static final String URL_READ_PESTILLO = "https://api.thingspeak.com/channels/2383483/fields/1.json?api_key=CZWGMFOUVMBZO0UK&results=1";
    private TextView txt;
    private TextView txtPestillo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.infoText);
        txtPestillo = findViewById(R.id.estadoPestillo);

        readJSON();
        leerJSONpestillo();
    }

    public void leerJSONpestillo(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_READ_PESTILLO, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    String response = new String(responseBody);
                    procesoJSONpestillo(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void procesoJSONpestillo(String json){
        try {
            JSONObject root = new JSONObject(json);
            JSONArray feeds = root.getJSONArray("feeds");
            String valor, texto="";
            for (int i=0; i<feeds.length();i++){
                valor = feeds.getJSONObject(i).getString("field1");
                if (valor.toString()!="null"){
                    int valorNumerico = Integer.parseInt(valor);
                    if (valorNumerico == 1){
                        texto = texto + "Desbloqueado" + "\n";
                    } else if (valorNumerico == 0) {
                        texto = texto + "Bloqueado" + "\n";
                    }
                } else if (valor.toString() == "null") {
                    texto = texto + "Error de conexión con ThinkSpeak" + "\n";
                }


            }
            txtPestillo.setText(texto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readJSON(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_READ, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    String response = new String(responseBody);
                    processJSON(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void processJSON(String json){
        try {
            JSONObject root = new JSONObject(json);
            JSONArray feeds = root.getJSONArray("feeds");
            String valor, texto="";
            for (int i=0; i<feeds.length();i++){
                valor = feeds.getJSONObject(i).getString("field2");
                if (valor.toString() != "null"){
                    int valorNumerico = Integer.parseInt(valor);
                    if (valorNumerico > 11){
                        texto = texto + "Abierto" + "\n";
                    }else {
                        texto = texto + "Cerrado" + "\n";
                    }
                } else if (valor.toString() == "null") {
                    texto = texto + "Error de conexión" +"\n";
                }


            }
            txt.setText(texto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void servoOn(View view) {
        String url = "https://api.thingspeak.com/update?api_key=0G836HYCYGK2RCHS&field1=1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    String respuesta = new String(responseBody);
                    Toast.makeText(MainActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Si la respuesta fue 0, espere unos segundos e intente nuevamente", Toast.LENGTH_SHORT).show();
                    leerJSONpestillo();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }





    public void servoOff(View view) {
        String url = "https://api.thingspeak.com/update?api_key=0G836HYCYGK2RCHS&field1=0";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    String respuesta = new String(responseBody);
                    Toast.makeText(MainActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Si la respuesta fue 0, espere unos segundos e intente nuevamente", Toast.LENGTH_SHORT).show();
                    leerJSONpestillo();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void Refresh(View view){
        readJSON();
    }
}