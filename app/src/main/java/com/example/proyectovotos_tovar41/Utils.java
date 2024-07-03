package com.example.proyectovotos_tovar41;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils{
    public static boolean validarCurp(String curp) {

        String regex = "^[A-Z]{4}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[HM](AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9]\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(curp);
        return matcher.matches();
        //return false;
    }

    public static boolean validarCampos(String curp, String fecha, String estado, String municipio, String correo, String pass,
                                        String ine, AutoCompleteTextView aTxtMun) {
        // Verificar si algún campo está vacío
        if (TextUtils.isEmpty(curp) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(estado) ||
                TextUtils.isEmpty(municipio) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(ine)
                || !validarCurp(curp) || !validarPass(pass) || !validarCorreo(correo) || !validarINE(ine) || !validarMunicipios(aTxtMun)) {
            return false;
        }
        return true;
    }

    public static boolean esMayorDeEdad(String fechaNacimiento) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(fechaNacimiento);
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(date);
            Calendar today = Calendar.getInstance();

            today.add(Calendar.YEAR, -18);

            return !birthDate.after(today);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validarPass(String pass){
        if (pass.length() < 8) {
            return false;
        } else {
           return true;
        }
    }

    public static boolean validarCorreo(String correo){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public static boolean validarINE(String ine){
        if (ine.length() < 13) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validarMunicipios(AutoCompleteTextView aTxtMun){
        String municipioIngresado = aTxtMun.getText().toString().toUpperCase();
        ArrayAdapter adapter = (ArrayAdapter) aTxtMun.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().toUpperCase().equals(municipioIngresado)) {
                return true;
            }
        }

        return false;
    }

    public static String removeAccents(String text) {

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks},.]+");

        return pattern.matcher(normalized).replaceAll("");
    }


}
