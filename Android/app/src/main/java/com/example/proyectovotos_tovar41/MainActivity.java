package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    private String loginStringURL = "https://argusjanus.000webhostapp.com/wolfram/2/";

    private EditText txtCorreo;
    private EditText txtPass;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCorreo = findViewById(R.id.txtCorreo);

        txtPass  = findViewById(R.id.txtPass);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
    }


    public void login(View v){
        progressBar.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        final String correo = this.txtCorreo.getText().toString();
        final String password = this.txtPass.getText().toString();
        if(Utils.validarCorreo(correo) && !password.isEmpty()) {


            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("correo", correo);
                jsonBody.put("pass", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginStringURL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Procesar la respuesta
                            try {
                                int status = response.getInt("status");
                                if (status == 1) {
                                    // Si la respuesta es 1, pasar a la actividad principal
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    intent.putExtra("idUsuario", response.getInt("idUsuario"));
                                    intent.putExtra("estado", response.getString("estado"));
                                    intent.putExtra("municipio", response.getString("municipio"));
                                    intent.putExtra("fed", response.getInt("fed"));
                                    intent.putExtra("est", response.getInt("est"));
                                    intent.putExtra("mun", response.getInt("mun"));
                                    startActivity(intent);
                                    finish();
                                } else  {
                                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("<--Error-->", "Error en la solicitud", error);
                    Toast.makeText(MainActivity.this, "Error en la solicitud, verifique su conexión", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Parámetros de la solicitud POST
                    Map<String, String> params = new HashMap<>();
                    params.put("correo", correo);
                    params.put("pass", password);
                    return params;
                }
            };

            // Agregar la solicitud a la cola
            queue.add(jsonObjectRequest);
        }else{
            Toast.makeText(MainActivity.this, "Llene los campos correctamente", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void registro(View v){
        Intent i = new Intent(this, registro.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }


}