package com.acevedo.rutaexperienciauc.ui.solicitarInformacion;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.acevedo.rutaexperienciauc.util.Util;
import com.google.android.material.textfield.TextInputLayout;


public class SolicitarInformacionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    //Variables del layout
    TextInputLayout tilSolInfoNombres,tilSolInfoApellidoPaterno,tilSolInfoApellidoMaterno,tilSolInfoEmail,tilSolInfoCelular,tilSolInfoFechaNacimiento;
    EditText edtSolInfoNombres, edtSolInfoApellidoPaterno, edtSolInfoApellidoMaterno, edtSolInfoEmail, edtSolInfoCelular, edtSolInfoFechaNacimiento;
    RadioButton rbPresencial, rbSemiPresencial, rbADistancia, rbCorreoElectronico, rbCelular, rbWhatsApp, rbConsentimiento;
    Button btnSolicitarInformacion;
    RadioGroup rgModalidad,rgMetodoContacto,rgConsentimiento;

    //Variables para utilizar internamente
    String ModalidadInteres, MetodoContacto;

    RequestQueue requestQueue;

    JsonObjectRequest jsonObjectRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_solicitar_informacion, container, false);
        tilSolInfoNombres = vista.findViewById(R.id.tilSolInfoNombres);
        tilSolInfoApellidoPaterno = vista.findViewById(R.id.tilSolInfoApellidoPaterno);
        tilSolInfoApellidoMaterno = vista.findViewById(R.id.tilSolInfoApellidoMaterno);
        tilSolInfoEmail = vista.findViewById(R.id.tilSolInfoEmail);
        tilSolInfoCelular = vista.findViewById(R.id.tilSolInfoCelular);
        tilSolInfoFechaNacimiento = vista.findViewById(R.id.tilSolInfoFechaNacimiento);
        rgModalidad = vista.findViewById(R.id.rgModalidad);
        rbPresencial = vista.findViewById(R.id.rbPresencial);
        rbSemiPresencial = vista.findViewById(R.id.rbSemiPresencial);
        rbADistancia = vista.findViewById(R.id.rbADistancia);
        rgMetodoContacto = vista.findViewById(R.id.rgMetodoContacto);
        rbCorreoElectronico = vista.findViewById(R.id.rbCorreoElectronico);
        rbCelular = vista.findViewById(R.id.rbCelular);
        rbWhatsApp = vista.findViewById(R.id.rbWhatsApp);
        rgConsentimiento = vista.findViewById(R.id.rgConsentimiento);
        rbConsentimiento = vista.findViewById(R.id.rbConsentimiento);
        btnSolicitarInformacion =vista.findViewById(R.id.btnSolicitarInformacion);

        requestQueue = Volley.newRequestQueue(getContext());
        modalidad_metodoContacto();

        btnSolicitarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarCampo()){
                    if(rbConsentimiento.isChecked()==true){
                        Intent intent =new Intent(getContext(), PopupSolicitarInfo.class);
                        startActivity(intent);
                     }
                        //solicitarInformacion();
                }
            }
        });
        edtSolInfoNombres = tilSolInfoNombres.getEditText().findViewById(R.id.edtSolInfoNombres);
        edtSolInfoApellidoPaterno = tilSolInfoApellidoPaterno.getEditText().findViewById(R.id.edtSolInfoApellidoPaterno);
        edtSolInfoApellidoMaterno = tilSolInfoApellidoMaterno.getEditText().findViewById(R.id.edtSolInfoApellidoMaterno);
        edtSolInfoEmail = tilSolInfoEmail.getEditText().findViewById(R.id.edtSolInfoEmail);
        edtSolInfoCelular = tilSolInfoCelular.getEditText().findViewById(R.id.edtSolInfoCelular);
        edtSolInfoFechaNacimiento = tilSolInfoFechaNacimiento.getEditText().findViewById(R.id.edtSolInfoFechaNacimiento);

        implementarCalendario();
        return vista;
    }
    private boolean validarEmail(String email) {
        String expresion = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern patterns = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        return patterns.matcher(email).matches();
    }
    private boolean validarCampo() {
        boolean camposCompletos = true;
        if(edtSolInfoNombres.getText().toString().isEmpty()){
            edtSolInfoNombres.setError("Ingrese su nombre");
            camposCompletos = false;
        }
        if(edtSolInfoApellidoPaterno.getText().toString().isEmpty()){
            edtSolInfoApellidoPaterno.setError("Ingrese su apellido paterno");
            camposCompletos = false;
        }
        if(edtSolInfoApellidoMaterno.getText().toString().isEmpty()){
            edtSolInfoApellidoMaterno.setError("Ingrese su apellido paterno");
            camposCompletos = false;
        }
        String email = edtSolInfoEmail.getText().toString();
        if(email.isEmpty()){
            edtSolInfoEmail.setError("Ingrese su email");
            camposCompletos = false;
        }else if(!validarEmail(email)){
            edtSolInfoEmail.setError("Email inválido");
            camposCompletos = false;
        }
        if(edtSolInfoCelular.getText().toString().isEmpty()){
            edtSolInfoCelular.setError("Ingrese su número de celular");
            camposCompletos = false;
        }
//        if(edtSolInfoFechaNacimiento.getText().toString().isEmpty()){
//            edtSolInfoFechaNacimiento.setError(("Campo Obligatorio"));
//            camposCompletos = false;
//        }
        if(rgModalidad.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Seleccione modalidad", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }
        if(rgMetodoContacto.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Seleccione metodo de contacto", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }
        if(rgConsentimiento.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Marque consentimiento", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }

        return camposCompletos;
    }

    private void solicitarInformacion() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

        String SiNombre = edtSolInfoNombres.getText().toString();
        String SiApellidoPaterno = edtSolInfoApellidoPaterno.getText().toString();
        String SiApellidoMaterno = edtSolInfoApellidoMaterno.getText().toString();
        String CaNombre = "";
        String SeNombre = "";
        String SiModalidad = ModalidadInteres;
        String SiCorreo = edtSolInfoEmail.getText().toString();
        String SiTelefono = edtSolInfoCelular.getText().toString();
        String SiFechaNacimiento = edtSolInfoFechaNacimiento.getText().toString();
        String SiFechaContacto = dateFormat.format(calendar.getTime());
        String SiTipoContacto = MetodoContacto;
        String URL = Util.RUTA_SOLICITAR_INFORMACION + "api" + "&SiNombre="+SiNombre+"&SiApellidoPaterno="+SiApellidoPaterno+"&CcApellidoMaterno="+SiApellidoMaterno+
                "&CaNombre="+CaNombre+"&SeNombre="+SeNombre+"&SiModalidad"+SiModalidad+"&SiCorreo="+SiCorreo+"&SiTelefono="+SiTelefono
                +"&SiFechaNacimiento="+SiFechaNacimiento+"&SiFechaContacto="+SiFechaContacto+"$SiTipoContacto"+SiTipoContacto;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        requestQueue.add(jsonObjectRequest);
        }
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Registro correcto", Toast.LENGTH_SHORT).show();
        edtSolInfoNombres.setText("");
        edtSolInfoApellidoPaterno.setText("");
        edtSolInfoApellidoMaterno.setText("");
        edtSolInfoEmail.setText("");
        edtSolInfoCelular.setText("");
        edtSolInfoFechaNacimiento.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error de insercion", Toast.LENGTH_SHORT).show();
        Log.i("error", error.toString());
    }

    private void modalidad_metodoContacto() {

        switch (rgModalidad.getCheckedRadioButtonId()){
            case R.id.rbPresencial:
                ModalidadInteres = "Presencial";
                break;
            case R.id.rbSemiPresencial:
                ModalidadInteres = "SemiPresencial";
                break;
            case R.id.rbADistancia:
                ModalidadInteres = "A distancia";
                break;
        }
//        if(rbPresencial.isChecked() == true){
//            ModalidadInteres = "Presencial";
//        }else
//        if(rbSemiPresencial.isChecked() == true){
//            ModalidadInteres = "SemiPresencial";
//        }else{
//            if(rbADistancia.isChecked() == true){
//                ModalidadInteres= "A Distancia";
//            }
//        }
        switch (rgMetodoContacto.getCheckedRadioButtonId()){
            case R.id.rbCorreoElectronico:
                MetodoContacto = "Correo Electronico";
                break;
            case R.id.rbCelular:
                MetodoContacto = "Celular";
                break;
            case R.id.rbWhatsApp:
                MetodoContacto = "WhatsApp";
                break;
        }

//        if(rbCorreoElectronico.isChecked() == true){
//            MetodoContacto = "Correo Electronico";
//        }else
//        if(rbCelular.isChecked() == true){
//            MetodoContacto = "Celular";
//        }else{
//            if(rbWhatsApp.isChecked() == true){
//                MetodoContacto= "WhatsApp";
//            }
//        }
    }
    private void implementarCalendario() {
        //Añadiendo calendario para que escoja su fecha de nacimiento
        edtSolInfoFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Crear una instancia de DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Actualizar el texto del EditText con la fecha seleccionada
                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                                edtSolInfoFechaNacimiento.setText(date);
                            }
                        }, year, month, dayOfMonth);
                // Mostrar el diálogo de selección de fecha
                datePickerDialog.show();
            }
        });
    }
}

