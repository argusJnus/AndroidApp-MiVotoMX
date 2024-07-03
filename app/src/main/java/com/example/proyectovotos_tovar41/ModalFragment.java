package com.example.proyectovotos_tovar41;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ModalFragment extends DialogFragment  {

    onBtnClic onBtnClic;
    View rootView;
    ProgressBar progressBarConIne;
    EditText txtINE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_modal, container, false);
        progressBarConIne = rootView.findViewById(R.id.progressBarConIne);
        progressBarConIne.setVisibility(View.GONE);
        txtINE = rootView.findViewById(R.id.txtINE);
        txtINE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtINE.length()<13){
                    txtINE.setError("Ingrese los 13 dígitos al reverso de su credencial");
                }else{
                    txtINE.setError(null);
                }
            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        Button btnMSend = rootView.findViewById(R.id.btnMSend);
        btnMSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBtnClic.validarINE();
            }
        });
        return rootView;
    }

    public String getTxtINE(){
        if (rootView != null) {
            EditText txtINE = rootView.findViewById(R.id.txtINE);
            return txtINE.getText().toString();
        } else {
            return "";
        }
    }

    public interface onBtnClic{
        void validarINE();

    }

}
