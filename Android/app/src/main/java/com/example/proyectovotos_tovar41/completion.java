package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class completion extends AppCompatActivity {
    private String estado;
    private String municipio;
    private int idUsuario;
    private int fed;
    private int est;
    private int mun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);
        Bundle extras = getIntent().getExtras();
        idUsuario = extras.getInt("id_usuario");
        estado = extras.getString("estado");
        municipio = extras.getString("municipio");
        fed = extras.getInt("fed");
        est = extras.getInt("est");
        mun = extras.getInt("mun");
    }

    public void continuar(View v){
        Intent i = new Intent(this, Home.class);
        i.putExtra("idUsuario",idUsuario);
        i.putExtra("estado", estado);
        i.putExtra("municipio", municipio);
        i.putExtra("fed", fed);
        i.putExtra("est", est);
        i.putExtra("mun", mun);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, Home.class);
        i.putExtra("idUsuario",idUsuario);
        i.putExtra("estado", estado);
        i.putExtra("municipio", municipio);
        i.putExtra("fed", fed);
        i.putExtra("est", est);
        i.putExtra("mun", mun);
        startActivity(i);
    }
}