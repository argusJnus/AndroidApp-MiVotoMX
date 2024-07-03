package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    //HashMaps para almacenas ids y nombres de las elecciones
    private static Map<String, Integer> idElecciones_map;
    private static Map<String, String> nombreElecciones_map;

    //Datos de la "sesión" que se irán pasando entre activities
    private String estado;
    private String municipio;
    private int idUsuario;
    private int fed;
    private int est;
    private int mun;

    //URL de la api
    private String url = "https://argusjanus.000webhostapp.com/da/1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Se inicializan los maps
        idElecciones_map = new HashMap<>();
        nombreElecciones_map = new HashMap<>();
        //Se extraen los contenidos mandados por el anterior intent
        Bundle extras = getIntent().getExtras();
        idUsuario = extras.getInt("idUsuario");
        estado = extras.getString("estado");
        municipio = extras.getString("municipio");
        fed = extras.getInt("fed");
        est = extras.getInt("est");
        mun = extras.getInt("mun");
        Log.d("-->Status-->", Integer.toString(fed));
        //Obtiene las elecciones disponibles al usuario y modifica el UI de acuerdo a los datos extraidos
        getIDs();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void getIDs(){
        final String estado = this.estado;
        final String municipio = this.municipio;

        String url = this.url+estado+"/"+municipio+"/";
        Log.d("url",url);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            //Se itera de acuerdo al numero de elementos dentro del arreglo de JSON
                            for (int i = 0; i < response.length(); i++) {
                                try{
                                    //Por cada uno, obtenemos su respectivo JSONObject para poder extraer los datos que necesitamos
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Log.d("idEleccion",Integer.toString(jsonObject.getInt("idEleccion")));

                                    if(jsonObject.getInt("tipo") == 1){
                                        Home.idElecciones_map.put("Fed",jsonObject.getInt("idEleccion"));
                                        Home.nombreElecciones_map.put("Fed", jsonObject.getString("nombreEleccion"));
                                    }else if(jsonObject.getInt("tipo") == 2){
                                        Home.idElecciones_map.put("Est",jsonObject.getInt("idEleccion"));
                                        Home.nombreElecciones_map.put("Est", jsonObject.getString("nombreEleccion"));
                                    }else{
                                        Home.idElecciones_map.put("Mun",jsonObject.getInt("idEleccion"));
                                        Home.nombreElecciones_map.put("Mun", jsonObject.getString("nombreEleccion"));
                                    }

                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("<--Response-->",response.getString(i));
                            }
                            updateUI();
                            // Llama al método de devolución de llamada con los valores obtenidos
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Home.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            error();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("<--Error-->", "Error en la solicitud", error);
                Toast.makeText(Home.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                error();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void updateUI() {
        // Actualiza la UI con los datos obtenidos, por ejemplo, actualiza TextViews
        Button btnFed = findViewById(R.id.btnFed);
        Button btnEst = findViewById(R.id.btnEst);
        Button btnMun = findViewById(R.id.btnMun);

        if(!idElecciones_map.containsKey("Fed") || fed > 0){
            btnFed.setEnabled(false);
        }

        if(!idElecciones_map.containsKey("Est") || est > 0){
            btnEst.setEnabled(false);
        }

        if(!idElecciones_map.containsKey("Mun") || mun > 0){
            btnMun.setEnabled(false);
        }

    }

    public void error(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void goToFed(View v){
        Intent i = new Intent(this, candidatos.class);
        String Titulo = "Federales";
        if(idElecciones_map.containsKey("Fed")){
            int id_eleccion = idElecciones_map.get("Fed");
            String nombre_eleccion = nombreElecciones_map.get("Fed");

            //Sesión provisional y Fake
            int id_usuario = idUsuario;
            String estado = this.estado;
            String municipio = this.municipio;
            i.putExtra("fed", fed);
            i.putExtra("est", est);
            i.putExtra("mun", mun);



            i.putExtra("lblTitulo",Titulo);
            i.putExtra("idEleccion",id_eleccion);
            i.putExtra("id_usuario",id_usuario);
            i.putExtra("estado", estado);
            i.putExtra("municipio", municipio);
            i.putExtra("nombreEleccion", nombre_eleccion);
            i.putExtra("tipoEleccion", 1);

            startActivity(i);
        }
    }

    public void goToEst(View v){
        Intent i = new Intent(this, candidatos.class);
        String Titulo = "Estatales";
        if(idElecciones_map.containsKey("Est")){
            int id_eleccion = idElecciones_map.get("Est");
            String nombre_eleccion = nombreElecciones_map.get("Est");

            //Sesión provisional y Fake
            int id_usuario = idUsuario;
            String estado = this.estado;
            String municipio = this.municipio;
            i.putExtra("fed", fed);
            i.putExtra("est", est);
            i.putExtra("mun", mun);

            i.putExtra("lblTitulo",Titulo);
            i.putExtra("idEleccion",id_eleccion);
            i.putExtra("id_usuario",id_usuario);
            i.putExtra("estado", estado);
            i.putExtra("municipio", municipio);
            i.putExtra("nombreEleccion", nombre_eleccion);
            i.putExtra("tipoEleccion", 2);

            startActivity(i);
        }
    }

    public void goToMun(View v){
        Intent i = new Intent(this, candidatos.class);
        String Titulo = "Municipales";
        if(idElecciones_map.containsKey("Mun")){
            int id_eleccion = idElecciones_map.get("Mun");
            String nombre_eleccion = nombreElecciones_map.get("Mun");

            //Sesión provisional y Fake
            int id_usuario = idUsuario;
            String estado = this.estado;
            String municipio = this.municipio;
            i.putExtra("fed", fed);
            i.putExtra("est", est);
            i.putExtra("mun", mun);

            i.putExtra("lblTitulo",Titulo);
            i.putExtra("idEleccion",id_eleccion);
            i.putExtra("id_usuario",id_usuario);
            i.putExtra("estado", estado);
            i.putExtra("municipio", municipio);
            i.putExtra("nombreEleccion", nombre_eleccion);
            i.putExtra("tipoEleccion", 3);

            startActivity(i);
        }
    }

    public void goToConf(View v){
        Intent i = new Intent(this, configuracion.class);
        i.putExtra("idUsuario", idUsuario);
        i.putExtra("estado", estado);
        i.putExtra("municipio", municipio);
        i.putExtra("fed", fed);
        i.putExtra("est", est);
        i.putExtra("mun", mun);
        startActivity(i);
    }
}