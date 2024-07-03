package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.text.Normalizer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registro extends AppCompatActivity{

    private EditText txtIne, txtPass, txtCorreo, txtFecha, txtCurp;
    private ProgressBar progressBarRegistro;
    private Spinner spnEst;
    private AutoCompleteTextView aTxtMun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        final ScrollView scrollView = findViewById(R.id.scrollView);

        //txtFecha
        txtFecha = findViewById(R.id.txtFecha);
        txtFecha.setFocusableInTouchMode(false);
        txtFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //actualizarEstadoBoton();
            }
        });
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                year = year - 18;
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        registro.this, R.style.CustomDatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                String date = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(Utils.esMayorDeEdad(date)){
                                    txtFecha.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    txtFecha.setError(null);
                                }else{
                                    Toast.makeText(registro.this, "Tiene que tener la mayoría de edad", Toast.LENGTH_SHORT).show();
                                    txtFecha.setText("");
                                }


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.YEAR,-18);
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        //txtPass
        txtPass = findViewById(R.id.txtPass);
        txtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtPass.getText().toString().length() < 8 && !hasFocus) {
                    txtPass.setError("La contraseña debe tener al menos 8 caracteres");
                }
            }
        });

        //txtCorreo
        txtCorreo = findViewById(R.id.txtCorreo);
        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!Utils.validarCorreo(txtCorreo.getText().toString()) && !hasFocus) {
                    txtCorreo.setError("Escriba un correo válido");
                }
            }
        });


        //txtCurp
        txtCurp = findViewById(R.id.txtCurp);
        txtCurp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!Utils.validarCurp(txtCurp.getText().toString()) && !hasFocus) {
                    txtCurp.setError("Ingrese correctamente su CURP");
                }

            }
        });
        txtCurp.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String text = txtCurp.getText().toString();

                String upperText = text.toUpperCase();

                if (!text.equals(upperText)) {
                    txtCurp.setText(upperText);

                    txtCurp.setSelection(upperText.length());
                }

            }
        });

        //txtIne
        txtIne = findViewById(R.id.txtIne);
        txtIne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(txtIne.getText().toString().length()<13 && !hasFocus){
                    txtIne.setError("Ingrese los 13 dígitos al reverso de su credencial");
                }
            }
        });


        progressBarRegistro = findViewById(R.id.progressBarRegistro);
        progressBarRegistro.setVisibility(View.GONE);
        spnEst = findViewById(R.id.spnEst);


        aTxtMun = findViewById(R.id.aTxtMun);
        aTxtMun.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!aTxtMun.getText().toString().isEmpty()) {
                    String municipioIngresado = aTxtMun.getText().toString().toUpperCase();
                    ArrayAdapter adapter = (ArrayAdapter) aTxtMun.getAdapter();

                    boolean encontrado = false;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).toString().toUpperCase().equals(municipioIngresado)) {
                            encontrado = true;
                            break;
                        }
                    }
                    if (encontrado) {
                        aTxtMun.setError(null);
                    } else {
                        aTxtMun.setError("Seleccione un municipio válido");
                    }
                }

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.estados,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
                spnEst.setAdapter(adapter);
        spnEst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aTxtMun.setText("");

                String municipiosArray = "municipios_";
                String selectedEst = spnEst.getItemAtPosition(position).toString();

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
                    aTxtMun.setAdapter(adapterMun);
                    aTxtMun.setThreshold(1);
                } else {
                    aTxtMun.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aTxtMun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aTxtMun.showDropDown();
            }
        });
        aTxtMun.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    aTxtMun.showDropDown();
                }
            }
        });


        txtIne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (txtIne.hasFocus()) {

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, txtIne.getTop());
                        }
                    });
                }
            }
        });
    }

    public void registrar(View v){

        progressBarRegistro.setVisibility(View.VISIBLE);

        if(!Utils.validarCorreo(txtCorreo.getText().toString())){
            txtCorreo.setError("Escriba un correo válido");
        }else{
            txtCorreo.setError(null);
        }

        if(!Utils.validarMunicipios(aTxtMun)){
            aTxtMun.setError("Seleccione un municipio válido");
        }else{
            aTxtMun.setError(null);
        }

        if(!Utils.validarINE(txtIne.getText().toString())){
            txtIne.setError("Ingrese los 13 dígitos al reverso de su credencial");
        }else {
            txtIne.setError(null);
        }

        if(!Utils.validarCurp(txtCurp.getText().toString())){
            txtCurp.setError("Ingrese correctamente su CURP");
        }else{
            txtCurp.setError(null);
        }

        if(!Utils.validarPass(txtPass.getText().toString())){
            txtPass.setError("La contraseña debe tener al menos 8 caracteres");
        }else{
            txtPass.setError(null);
        }

        if(txtFecha.getText().toString().isEmpty()){
            txtFecha.setError("Ingrese una fecha válida");
        }else{
            txtFecha.setError(null);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String url = "https://argusjanus.000webhostapp.com/wolfram/3/";
        if(validacion()) {

            final String finalCurp = removeAccents(txtCurp.getText().toString()).toUpperCase();
            final String fec_nacimiento = txtFecha.getText().toString();
            final String finalEstado = removeAccents(spnEst.getSelectedItem().toString()).toUpperCase();
            final String finalMunicipio = removeAccents(aTxtMun.getText().toString()).toUpperCase();
            final String finalIne = txtIne.getText().toString();
            final String finalCorreo = txtCorreo.getText().toString();
            final String finalPass = txtPass.getText().toString();

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("curp", finalCurp);
                jsonBody.put("fec_nacimiento", fec_nacimiento);
                jsonBody.put("estado", finalEstado);
                jsonBody.put("municipio", finalMunicipio);
                jsonBody.put("ine", finalIne);
                jsonBody.put("correo", finalCorreo);
                jsonBody.put("pass", finalPass);
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
                                    Intent intent = new Intent(registro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (status == 0) {
                                    Toast.makeText(registro.this, "Llene los campos correctamente", Toast.LENGTH_SHORT).show();
                                    progressBarRegistro.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(registro.this, "El usuario ya está en uso", Toast.LENGTH_SHORT).show();
                                    progressBarRegistro.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(registro.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                progressBarRegistro.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("<--Error-->", "Error en la solicitud", error);
                    Toast.makeText(registro.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    progressBarRegistro.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Parámetros de la solicitud POST
                    Map<String, String> params = new HashMap<>();
                    params.put("curp", finalCurp);
                    params.put("fec_nacimiento", fec_nacimiento);
                    params.put("estado", finalEstado);
                    params.put("municipio", finalMunicipio);
                    params.put("ine", finalIne);
                    params.put("correo", finalCorreo);
                    params.put("pass", finalPass);
                    return params;
                }
            };

            // Agregar la solicitud a la cola
            queue.add(jsonObjectRequest);
        }else{
            Toast.makeText(registro.this, "Llene los campos correctamente", Toast.LENGTH_SHORT).show();
            progressBarRegistro.setVisibility(View.GONE);
        }
    }


    public String removeAccents(String text) {
        // Normalizar el texto para eliminar los acentos
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Definir la expresión regular para buscar caracteres diacríticos y de puntuación
        Pattern pattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks},.]+");

        // Reemplazar los caracteres diacríticos y de puntuación con una cadena vacía
        return pattern.matcher(normalized).replaceAll("");
    }

    private boolean validacion() {

        String curp = txtCurp.getText().toString();
        String fecha = txtFecha.getText().toString();
        String estado = removeAccents(spnEst.getSelectedItem().toString()).toUpperCase();
        String municipio = removeAccents(aTxtMun.getText().toString()).toUpperCase();
        String correo = txtCorreo.getText().toString();
        String pass = txtPass.getText().toString();
        String ine = txtIne.getText().toString();

        return(Utils.validarCampos(curp, fecha, estado, municipio, correo,
                pass, ine, aTxtMun));
    }

}

