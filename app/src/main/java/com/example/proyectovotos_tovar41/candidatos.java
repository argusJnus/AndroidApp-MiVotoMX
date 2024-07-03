package com.example.proyectovotos_tovar41;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class candidatos extends AppCompatActivity implements AdapterActivity.OnItemClickListener, AdapterActivity.AdapterCallback {
    private int eleccion;
    private String nombreEleccion;

    private String estado;
    private String municipio;
    private int idUsuario;
    private int fed;
    private int est;
    private int mun;
    private int tipoEleccion;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatos);
        TextView lblTitle = findViewById(R.id.lblTitle);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        //id de la elección y nombre
        this.eleccion = extras.getInt("idEleccion");
        this.nombreEleccion = extras.getString("nombreEleccion");
        this.tipoEleccion = extras.getInt("tipoEleccion");

        //Sesión
        this.idUsuario = extras.getInt("id_usuario");
        this.municipio = extras.getString("municipio");
        this.estado = extras.getString("estado");
        this.fed = extras.getInt("fed");
        this.est = extras.getInt("est");
        this.mun = extras.getInt("mun");


        lblTitle.setText(this.nombreEleccion);
        RecyclerView recyclerView = findViewById(R.id.lista);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AdapterActivity adapter = new AdapterActivity(this, eleccion);
        adapter.setAdapterCallback(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

    }

    public void onItemClick(String titulo, String detalle, int idCandidato) {
        Intent i = new Intent(this, confirmacion.class);

        //Lo que se ocupa en la siguiente actividad
        i.putExtra("nombreCandidato", titulo);
        i.putExtra("partido", detalle);
        i.putExtra("idCandidato", idCandidato);
        i.putExtra("nombreEleccion", nombreEleccion);

        //Sesión
        int id_usuario = idUsuario;
        String estado = this.estado;
        String municipio = this.municipio;
        i.putExtra("id_usuario",id_usuario);
        i.putExtra("estado", estado);
        i.putExtra("municipio", municipio);
        i.putExtra("fed", fed);
        i.putExtra("est", est);
        i.putExtra("mun", mun);
        i.putExtra("tipoEleccion", this.tipoEleccion);

        startActivity(i);
    }

    @Override
    public void onListNotEmpty() {
        progressBar.setVisibility(View.GONE); // Ocultar la ProgressBar cuando la lista no está vacía
    }

    @Override
    public void onNetworkError(){
        onBackPressed();
    }
}