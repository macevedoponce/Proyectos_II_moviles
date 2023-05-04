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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.SedeAdapter;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.android.volley.AuthFailureError;
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

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.acevedo.rutaexperienciauc.util.Util;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.textfield.TextInputLayout;


public class SolicitarInformacionFragment extends Fragment {

    //Variables del layout
    TextInputLayout tilSolInfoNombres,tilSolInfoApellidoPaterno,tilSolInfoApellidoMaterno,tilSolInfoEmail,tilSolInfoCelular,tilSolInfoFechaNacimiento;
    EditText edtSolInfoNombres, edtSolInfoApellidoPaterno, edtSolInfoApellidoMaterno, edtSolInfoEmail, edtSolInfoCelular, edtSolInfoFechaNacimiento;
    RadioButton rbPresencial, rbSemiPresencial, rbADistancia, rbCorreoElectronico, rbCelular, rbWhatsApp, rbConsentimiento;
    Button btnSolicitarInformacion;
    RadioGroup rgModalidad,rgMetodoContacto,rgConsentimiento;

    Spinner spSedes, spCarreras;

    //Variables para utilizar internamente
    String ModalidadInteres, MetodoContacto;

    RequestQueue requestQueue;

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
        spSedes = vista.findViewById(R.id.spSedes);
        spCarreras = vista.findViewById(R.id.spCarreras);

        modalidad_metodoContacto();

        //uso del spinner sedes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.mis_sedes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSedes.setAdapter(adapter);

        //uso del spinner carreras
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.mis_carreras, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarreras.setAdapter(adapter1);


        edtSolInfoNombres = tilSolInfoNombres.getEditText().findViewById(R.id.edtSolInfoNombres);
        edtSolInfoApellidoPaterno = tilSolInfoApellidoPaterno.getEditText().findViewById(R.id.edtSolInfoApellidoPaterno);
        edtSolInfoApellidoMaterno = tilSolInfoApellidoMaterno.getEditText().findViewById(R.id.edtSolInfoApellidoMaterno);
        edtSolInfoEmail = tilSolInfoEmail.getEditText().findViewById(R.id.edtSolInfoEmail);
        edtSolInfoCelular = tilSolInfoCelular.getEditText().findViewById(R.id.edtSolInfoCelular);
        edtSolInfoFechaNacimiento = tilSolInfoFechaNacimiento.getEditText().findViewById(R.id.edtSolInfoFechaNacimiento);

        implementarCalendario();
        requestQueue= Volley.newRequestQueue(getContext());


        //boton para enviar los datos
        btnSolicitarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampo()){
                    if(rbConsentimiento.isChecked()==true){
                        solicitarInformacion();
                     }

                }
            }
        });
        //llamarNombresSedes();
        //llamarNombresCarreras();
        return vista;
    }

    private void llamarNombresCarreras() {
        String url = Util.RUTA_LLAMARNOMBRE_CARRERA;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> nombresCarrera = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try{
                                JSONObject carrera = response.getJSONObject(i);
                                String nombreCarrera = carrera.getString("CaNombre");
                                nombresCarrera.add(nombreCarrera);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresCarrera);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spCarreras.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void llamarNombresSedes() {
        String url = Util.RUTA_LLAMARNOMBRE_SEDE;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> nombresSede = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try{
                                JSONObject sede = response.getJSONObject(i);
                                String nombreSede = sede.getString("SeNombre");
                                nombresSede.add(nombreSede);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresSede);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSedes.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private boolean validarEmail(String email) {
        String expresion = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern patterns = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        return patterns.matcher(email).matches();
    }
    private void solicitarInformacion() {

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String fechaHoraActualString = fechaActual.format(formatter);

        String url = Util.RUTA_SOLICITAR_INFORMACION;

        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent =new Intent(getContext(), PopupSolicitarInfo.class);
                startActivity(intent);
                edtSolInfoNombres.setText("");
                edtSolInfoApellidoPaterno.setText("");
                edtSolInfoApellidoMaterno.setText("");
                edtSolInfoEmail.setText("");
                edtSolInfoCelular.setText("");
                edtSolInfoFechaNacimiento.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("SiNombre",edtSolInfoNombres.getText().toString());
                params.put("SiApellidoPaterno",edtSolInfoApellidoPaterno.getText().toString());
                params.put("SiApellidoMaterno",edtSolInfoApellidoMaterno.getText().toString());
                params.put("SiCorreo",edtSolInfoEmail.getText().toString());
                params.put("SiTelefono",edtSolInfoCelular.getText().toString());
                params.put("SiFechaNacimiento",edtSolInfoFechaNacimiento.getText().toString());
                params.put("CaNombre",spCarreras.getSelectedItem().toString());
                params.put("SeNombre",spSedes.getSelectedItem().toString());
                params.put("SiModalidad","ModalidadInteres");
                params.put("SiFechaSolicitud",fechaHoraActualString);
                params.put("SiTipoContacto","MetodoContacto");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(postResquest);
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

