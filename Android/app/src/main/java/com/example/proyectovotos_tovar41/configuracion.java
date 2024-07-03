package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class configuracion extends AppCompatActivity {
    private String estado;
    private String municipio;
    private int idUsuario;
    private int fed;
    private int est;
    private int mun;

    private Spinner txtEstado;
    private AutoCompleteTextView txtMunicipio;
    private ProgressBar progressBarUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        txtEstado = findViewById(R.id.txtEstado);
        txtMunicipio = findViewById(R.id.txtMunicipio);

        Bundle extras = getIntent().getExtras();
        idUsuario = extras.getInt("idUsuario");
        estado = extras.getString("estado");
        municipio = extras.getString("municipio");
        fed = extras.getInt("fed");
        est = extras.getInt("est");
        mun = extras.getInt("mun");

        String normalEst = estado.replace('_',' ');


        progressBarUpdate = findViewById(R.id.progressBarUpdate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.estados,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        txtEstado.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(normalEst);
        if (spinnerPosition >= 0) {
            txtEstado.setSelection(spinnerPosition);
        }

        txtEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtMunicipio.setText("");
                String municipiosArray = "municipios_";
                String selectedEst = txtEstado.getItemAtPosition(position).toString();

                municipiosArray+=selectedEst.replace(" ","_");
                int municipiosArrayId = getResources().getIdentifier(municipiosArray, "array", getPackageName());
                if (municipiosArrayId != 0) {
                    String[] municipios = getResources().getStringArray(municipiosArrayId);
                    // Crear el adaptador con el array de strings
                    ArrayAdapter<String> adapterMun = new ArrayAdapter<>(
                            parent.getContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            municipios
                    );
                    adapterMun.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    txtMunicipio.setAdapter(adapterMun);
                    txtMunicipio.setThreshold(1);
                } else {
                    txtMunicipio.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.showDropDown();
            }
        });
        txtMunicipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtMunicipio.showDropDown();
                }
            }
        });

        txtMunicipio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtMunicipio.getText().toString().isEmpty()) {
                    String municipioIngresado = txtMunicipio.getText().toString().toUpperCase();
                    ArrayAdapter adapter = (ArrayAdapter) txtMunicipio.getAdapter();

                    boolean encontrado = false;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).toString().toUpperCase().equals(municipioIngresado)) {
                            encontrado = true;
                            break;
                        }
                    }
                    if (encontrado) {
                        txtMunicipio.setError(null);
                    } else {
                        txtMunicipio.setError("Seleccione un municipio válido");
                    }
                }
            }
        });

    }

    public void goToMain(View v){
        progressBarUpdate.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String url = "https://argusjanus.000webhostapp.com/wolfram/6/";
        if(validacion()) {

            final String finalEstado = Utils.removeAccents(txtEstado.getSelectedItem().toString().toUpperCase()).replace(' ','_');
            final String finalMunicipio = Utils.removeAccents(txtMunicipio.getText().toString().toUpperCase()).replace(' ','_');
            final String finalIdUsuario = Integer.toString(idUsuario);
            Log.d("-----idUsuario-----", finalIdUsuario);
            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("idUsuario", finalIdUsuario);
                jsonBody.put("estado", finalEstado);
                jsonBody.put("municipio", finalMunicipio);
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
                                    Intent intent = new Intent(configuracion.this, Home.class);
                                    intent.putExtra("idUsuario", configuracion.this.idUsuario);
                                    intent.putExtra("estado", finalEstado);
                                    intent.putExtra("municipio", finalMunicipio);
                                    intent.putExtra("fed", configuracion.this.fed);
                                    intent.putExtra("est", configuracion.this.est);
                                    intent.putExtra("mun", configuracion.this.mun);
                                    startActivity(intent);
                                    finish();
                                } else if (status == 0) {
                                    Toast.makeText(configuracion.this, "Llene los campos correctamente", Toast.LENGTH_SHORT).show();
                                    progressBarUpdate.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(configuracion.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                progressBarUpdate.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("<--Error-->", "Error en la solicitud", error);
                    Toast.makeText(configuracion.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    progressBarUpdate.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Parámetros de la solicitud POST
                    Map<String, String> params = new HashMap<>();
                    params.put("idUsuario", finalIdUsuario);
                    params.put("estado", finalEstado);
                    params.put("municipio", finalMunicipio);
                    return params;
                }
            };

            // Agregar la solicitud a la cola
            queue.add(jsonObjectRequest);
        }else{
            progressBarUpdate.setVisibility(View.GONE);
            Toast.makeText(configuracion.this, "Llene los campos correctamente", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validacion() {
        String estado = Utils.removeAccents(txtEstado.getSelectedItem().toString()).toUpperCase();
        String municipio = Utils.removeAccents(txtMunicipio.getText().toString()).toUpperCase();

        if(estado.isEmpty() || municipio.isEmpty() || !Utils.validarMunicipios(txtMunicipio)){
            return false;
        }else{
            return true;
        }
    }
}


