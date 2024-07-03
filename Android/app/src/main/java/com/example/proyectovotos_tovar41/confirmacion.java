package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class confirmacion extends AppCompatActivity implements ModalFragment.onBtnClic {
    private int idCandidato;
    private String estado;
    private String municipio;
    private int idUsuario;
    /*private int fed;
    private int est;
    private int mun;*/
    private String ine;
    private String url = "https://argusjanus.000webhostapp.com/img/";
    private int tipoEleccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        //Identificar los elementos de la pantalla
        TextView lblTitle = findViewById(R.id.lblTitle);
        TextView lblNombre = findViewById(R.id.lblNombre);
        TextView lblPartido = findViewById(R.id.lblPartido);
        ImageView imgCandidato = findViewById(R.id.imgCandidato);

        //Extraer datos del intent y cambiar valores (véase candidatos.java)
        Bundle extras = getIntent().getExtras();
        lblTitle.setText(extras.getString("nombreEleccion"));
        //Sesión
        idUsuario = extras.getInt("id_usuario");
        estado = extras.getString("estado");
        municipio = extras.getString("municipio");
        /*fed = extras.getInt("fed");
        est = extras.getInt("est");
        mun = extras.getInt("mun");*/
        tipoEleccion = extras.getInt("tipoEleccion");
        idCandidato = extras.getInt("idCandidato");

        //Candidato
        lblNombre.setText(extras.getString("nombreCandidato"));
        lblPartido.setText(extras.getString("partido"));
        url = url+extras.getInt("idCandidato")+".jpg";
        Glide.with(this).load(url).into(imgCandidato);


    }

    private ModalFragment modalFragment;
    public void confirmar(View v){
        modalFragment = new ModalFragment();
        modalFragment.onBtnClic = this;

        modalFragment.show(getSupportFragmentManager(), "modal");

    }



    public void regresar(View v){

        onBackPressed();
    }

    @Override
    public void validarINE() {

        String url = "https://argusjanus.000webhostapp.com/wolfram/5/";
        ine = modalFragment.getTxtINE();
        ProgressBar progressBarModal = modalFragment.progressBarConIne;
        progressBarModal.setVisibility(View.VISIBLE);
        if(Utils.validarINE(ine)) {


            final String finalINE = ine;
            final String finalidUsuario = Integer.toString(this.idUsuario);
            Log.d("<<<<<<INE<<<<<<", finalINE);
            Log.d("<<<<<<IDUSUARIO<<<<<<", finalidUsuario);
            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("idUsuario", finalidUsuario);
                jsonBody.put("ine", finalINE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Procesar la respuesta
                            try {
                                int status = response.getInt("status");

                                if (status == 1) {
                                    // Si la respuesta es 1, pasar a la actividad principal
                                    votar();
                                } else {
                                    Toast.makeText(confirmacion.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                    progressBarModal.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(confirmacion.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                progressBarModal.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("<--Error-->", "Error en la solicitud", error);
                    Toast.makeText(confirmacion.this, "Error al verificar, compruebe su conexión", Toast.LENGTH_SHORT).show();
                    progressBarModal.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Parámetros de la solicitud POST
                    Map<String, String> params = new HashMap<>();
                    params.put("idUsuario", finalidUsuario);
                    params.put("ine", finalINE);
                    return params;
                }
            };

            // Agregar la solicitud a la cola
            queue.add(jsonObjectRequest);
        }else{
            progressBarModal.setVisibility(View.GONE);
            Toast.makeText(confirmacion.this, "Llene el campo correctamente", Toast.LENGTH_SHORT).show();
        }
    }


    public void votar() {

        String url = "https://argusjanus.000webhostapp.com/construct/1/";

        final String tipo = Integer.toString(tipoEleccion);
        final String finalidUsuario = Integer.toString(this.idUsuario);
        final String finalidCandidato = Integer.toString(this.idCandidato);
        Log.d("<<<<<<idCandidato<<<<<<", finalidCandidato);
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("idUsuario", finalidUsuario);
            jsonBody.put("tipo", tipo);
            jsonBody.put("idCandidato", finalidCandidato);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta
                        try {
                            int status = response.getInt("status");

                            if (status == 1) {
                                // Si la respuesta es 1, pasar a la actividad principal
                                Intent i = new Intent(confirmacion.this, completion.class);
                                i.putExtra("id_usuario",idUsuario);
                                i.putExtra("estado", estado);
                                i.putExtra("municipio", municipio);
                                i.putExtra("fed", response.getInt("fed"));
                                i.putExtra("est", response.getInt("est"));
                                i.putExtra("mun", response.getInt("mun"));
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(confirmacion.this, "Algo salió mal, intente más tarde", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(confirmacion.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("<--Error-->", "Error en la solicitud votar", error);
                Toast.makeText(confirmacion.this, "Error al votar, verifique su conexión", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", finalidUsuario);
                params.put("tipo", tipo);
                params.put("idCandidato", finalidCandidato);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        queue.add(jsonObjectRequest);

    }

}