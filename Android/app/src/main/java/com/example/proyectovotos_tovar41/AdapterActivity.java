package com.example.proyectovotos_tovar41;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectovotos_tovar41.Utils;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.MyViewHolder>  {

    private String[] nombreCandidatos;
    private int[] idCandidatos;
    private String[] partidos;
    private  Context aContext;


    public interface AdapterCallback {
        void onNetworkError();
    }
    private AdapterCallback callback;

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    public AdapterActivity(Context context, int idEleccion){
        aContext = context;
        nombreCandidatos = new String[0];
        idCandidatos = new int[0];
        partidos = new String[0];
        getCandidatos(idEleccion);

    }

    public void getCandidatos(int idElecciones){

        String url = "https://argusjanus.000webhostapp.com/kortifex/1/"+idElecciones+"/";
        RequestQueue queue = Volley.newRequestQueue(aContext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            nombreCandidatos = new String[response.length()];
                            idCandidatos = new int[response.length()];
                            partidos = new String[response.length()];
                            //Se itera de acuerdo al numero de elementos dentro del arreglo de JSON
                            for (int i = 0; i < response.length(); i++) {
                                try{
                                    //Por cada uno, obtenemos su respectivo JSONObject para poder extraer los datos que necesitamos
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Log.d("idEleccion",Integer.toString(jsonObject.getInt("idElecciones")));

                                    AdapterActivity.this.idCandidatos[i] = jsonObject.getInt("idCandidato");
                                    AdapterActivity.this.nombreCandidatos[i] = jsonObject.getString("nombre");
                                    AdapterActivity.this.partidos[i] = jsonObject.getString("partido");

                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("<--Response-->",response.getString(i));
                            }
                            updateData(idCandidatos, nombreCandidatos, partidos);
                            // Llama al método de devolución de llamada con los valores obtenidos
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(aContext, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            if (callback != null) {
                                callback.onNetworkError();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("<--Error-->", "Error en la solicitud", error);
                Toast.makeText(aContext, "Error, verifique su conexión a Internet", Toast.LENGTH_SHORT).show();
                if(callback!=null) {
                    callback.onNetworkError();
                }
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void updateData(int[] idCandidatos, String[] nombreCandidatos, String[] partidos) {
        this.idCandidatos = idCandidatos;
        this.nombreCandidatos = nombreCandidatos;
        this.partidos = partidos;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
        if (idCandidatos.length > 0 && mListener != null) {
            mListener.onListNotEmpty();
        }
    }


    public interface OnItemClickListener {
        void onItemClick(String titulo, String detalle, int idCandidato);

        void onListNotEmpty();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String url = "https://argusjanus.000webhostapp.com/img/"+idCandidatos[position]+".jpg";
        holder.titulo.setText(nombreCandidatos[position]);
        holder.detalle.setText(partidos[position]);
        Glide.with(holder.itemView.getContext()).load(url).into(holder.imagen);

        final String finalTitulo = (String) holder.titulo.getText();
        final String finalDetalle = (String) holder.detalle.getText();
        final int finalID = idCandidatos[position];

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(finalTitulo, finalDetalle, finalID);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return idCandidatos.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo, detalle;

        MyViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.img);
            titulo = itemView.findViewById(R.id.txtTitulo);
            detalle = itemView.findViewById(R.id.txtDesc);
        }
    }
}