package com.acevedo.rutaexperienciauc.ui.solicitarInformacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class SolicitarInformacionActivity extends AppCompatActivity {

    //Variables para inicializar el layout
    TextInputLayout tilSolInfoNombres,tilSolInfoApellidoPaterno,tilSolInfoApellidoMaterno,tilSolInfoEmail,tilSolInfoCelular,tilSolInfoFechaNacimiento;
    EditText edtSolInfoNombres, edtSolInfoApellidoPaterno, edtSolInfoApellidoMaterno, edtSolInfoEmail, edtSolInfoCelular, edtSolInfoFechaNacimiento;
    RadioButton rbPresencial, rbSemiPresencial, rbADistancia, rbCorreoElectronico, rbCelular, rbWhatsApp, rbConsentimiento;
    Button btnSolicitarInformacion;
    RadioGroup rgModalidad,rgMetodoContacto,rgConsentimiento;

    Spinner spSedes, spCarreras;

    LinearLayout llVolver;

    //Variables para utilizar internamente
    private boolean fechaSeleccionada = false;

    int idSedeSeleccionada;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_informacion);
        //Inicializar los elementos de la interfaz
        tilSolInfoNombres = findViewById(R.id.tilSolInfoNombres);
        tilSolInfoApellidoPaterno = findViewById(R.id.tilSolInfoApellidoPaterno);
        tilSolInfoApellidoMaterno = findViewById(R.id.tilSolInfoApellidoMaterno);
        tilSolInfoEmail = findViewById(R.id.tilSolInfoEmail);
        tilSolInfoCelular = findViewById(R.id.tilSolInfoCelular);
        tilSolInfoFechaNacimiento = findViewById(R.id.tilSolInfoFechaNacimiento);
        rgModalidad = findViewById(R.id.rgModalidad);
        rbPresencial = findViewById(R.id.rbPresencial);
        rbSemiPresencial = findViewById(R.id.rbSemiPresencial);
        rbADistancia = findViewById(R.id.rbADistancia);
        rgMetodoContacto = findViewById(R.id.rgMetodoContacto);
        rbCorreoElectronico = findViewById(R.id.rbCorreoElectronico);
        rbCelular = findViewById(R.id.rbCelular);
        rbWhatsApp = findViewById(R.id.rbWhatsApp);
        rgConsentimiento = findViewById(R.id.rgConsentimiento);
        rbConsentimiento = findViewById(R.id.rbConsentimiento);

        btnSolicitarInformacion =findViewById(R.id.btnSolicitarInformacion);
        spSedes = findViewById(R.id.spSedes);
        spCarreras = findViewById(R.id.spCarreras);
        llVolver = findViewById(R.id.llVolver);

        edtSolInfoNombres = tilSolInfoNombres.getEditText().findViewById(R.id.edtSolInfoNombres);
        edtSolInfoApellidoPaterno = tilSolInfoApellidoPaterno.getEditText().findViewById(R.id.edtSolInfoApellidoPaterno);
        edtSolInfoApellidoMaterno = tilSolInfoApellidoMaterno.getEditText().findViewById(R.id.edtSolInfoApellidoMaterno);
        edtSolInfoEmail = tilSolInfoEmail.getEditText().findViewById(R.id.edtSolInfoEmail);
        edtSolInfoCelular = tilSolInfoCelular.getEditText().findViewById(R.id.edtSolInfoCelular);
        edtSolInfoFechaNacimiento = tilSolInfoFechaNacimiento.getEditText().findViewById(R.id.edtSolInfoFechaNacimiento);

        // Implementar el calendario para seleccionar la fecha de nacimiento
        implementarCalendario();

        //crear una instancia de la cola de solicitudes de Volley en el contexto actual
        requestQueue= Volley.newRequestQueue(this);

        // Llamar a los métodos para obtener los nombres de las sedes y carreras
        llamarNombresSedes();
        llamarNombresCarreras();

        // Configurar el botón Volver para cerrar la actividad actual
        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Configurar el botón Solicitar Información para realizar la solicitud
        btnSolicitarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validar los campos antes de enviar la solicitud
                if(validarCampo()){
                    if(rbConsentimiento.isChecked()==true){
                        solicitarInformacion();
                    }
                }
            }
        });
    }

    // Método para obtener los nombres de las carreras desde el servidor
    private void llamarNombresCarreras() {

        // Construir la URL para la solicitud GET
        String url = Util.RUTA_CARRERAS+"/"+1;

        // Crear una solicitud GET utilizando la clase JsonArrayRequest
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear una lista para almacenar los nombres de las carreras
                        List<String> nombresCarrera = new ArrayList<>();
                        // Iterar sobre la respuesta JSON obtenida
                        for(int i = 0; i < response.length(); i++){
                            try{
                                // Obtener el objeto JSON correspondiente a cada carrera
                                JSONObject jsonObject = response.getJSONObject(i);
                                // Obtener el nombre de la carrera y agregarlo a la lista
                                String nombreCarrera = jsonObject.getString("CaNombre");
                                nombresCarrera.add(nombreCarrera);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        // Crear un adaptador de ArrayAdapter para mostrar los nombres de las carreras en un Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SolicitarInformacionActivity.this, android.R.layout.simple_spinner_item, nombresCarrera);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Asignar el adaptador al Spinner de carreras
                        spCarreras.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // Agregar la solicitud a la cola de solicitudes del RequestQueue
        requestQueue.add(request);
    }

    private void llamarNombresSedes() {

        // Construir la URL para la solicitud GET
        String url = Util.RUTA_SEDE;

        // Crear una solicitud GET utilizando la clase JsonArrayRequest
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> nombresSede = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try{
                                // Obtener el objeto JSON correspondiente a cada carrera
                                JSONObject jsonObject = response.getJSONObject(i);
                                // Obtener el nombre de la carrera y agregarlo a la lista
                                String nombreSede = jsonObject.getString("SeNombre");
                                nombresSede.add(nombreSede);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        // Crear un adaptador de ArrayAdapter para mostrar los nombres de las carreras en un Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SolicitarInformacionActivity.this, android.R.layout.simple_spinner_item, nombresSede);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Asignar el adaptador al Spinner de carreras
                        spSedes.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // Agregar la solicitud a la cola de solicitudes del RequestQueue
        requestQueue.add(request);
    }

    private boolean validarEmail(String email) {
        // Expresión regular para validar el formato del correo electrónico
        String expresion = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        // Compilar la expresión regular en un objeto de tipo Pattern
        Pattern patterns = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);

        // Verificar si el email coincide con el patrón de la expresión regular
        return patterns.matcher(email).matches();
    }

    private void solicitarInformacion() {

        // Obtener la modalidad y el método de contacto
        Pair<String, String> result = modalidad_metodoContacto();
        String modalidad = result.first;
        String contacto = result.second;

        // Obtener la fecha y hora actual
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraActualString = fechaActual.format(formatter);

        //Construir el objeto JSON con los datos de solicitud
        JSONObject datos = new JSONObject();
        String url = Util.RUTA_SOLICITAR_INFORMACION;
        requestQueue = Volley.newRequestQueue(SolicitarInformacionActivity.this);

        try{
            // Agregar los datos de solicitud al objeto JSON
            datos.put("SiNombre", edtSolInfoNombres.getText().toString());
            datos.put("SiApellidoPaterno", edtSolInfoApellidoPaterno.getText().toString());
            datos.put("SiApellidoMaterno", edtSolInfoApellidoMaterno.getText().toString());
            datos.put("SiCorreo", edtSolInfoEmail.getText().toString());
            datos.put("SiTelefono", edtSolInfoCelular.getText().toString());
            datos.put("SiFechaNacimiento", edtSolInfoFechaNacimiento.getText().toString());
            datos.put("CaNombre", spCarreras.getSelectedItem().toString());
            datos.put("SeNombre", spSedes.getSelectedItem().toString());
            datos.put("SiModalidad", modalidad);
            datos.put("SiFechaSolicitud", fechaHoraActualString);
            datos.put("SiTipoContacto", contacto);
        } catch (JSONException e){
            e.printStackTrace();
        }
        //Enviar la petición al servidor
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Mostrar mensaje de exito
                        Intent intent =new Intent(SolicitarInformacionActivity.this, PopupSolicitarInfo.class);
                        startActivity(intent);
                        // Limpiar los datos de los campos de texto
                        edtSolInfoNombres.setText("");
                        edtSolInfoApellidoPaterno.setText("");
                        edtSolInfoApellidoMaterno.setText("");
                        edtSolInfoEmail.setText("");
                        edtSolInfoCelular.setText("");
                        edtSolInfoFechaNacimiento.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("Volley Error", errorResponse);
                            Toast.makeText(SolicitarInformacionActivity.this, "E1: " + errorResponse, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SolicitarInformacionActivity.this, "Error al insertar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private boolean validarCampo() {
        boolean camposCompletos = true;

        // Validar campo Nombres
        if(edtSolInfoNombres.getText().toString().isEmpty()){
            edtSolInfoNombres.setError("Ingrese su nombre");
            camposCompletos = false;
        }
        // Validar campo Apellido Paterno
        if(edtSolInfoApellidoPaterno.getText().toString().isEmpty()){
            edtSolInfoApellidoPaterno.setError("Ingrese su apellido paterno");
            camposCompletos = false;
        }
        // Validar campo Apellido Materno
        if(edtSolInfoApellidoMaterno.getText().toString().isEmpty()){
            edtSolInfoApellidoMaterno.setError("Ingrese su apellido paterno");
            camposCompletos = false;
        }
        // Validar campo Email
        String email = edtSolInfoEmail.getText().toString();
        if(email.isEmpty()){
            edtSolInfoEmail.setError("Ingrese su email");
            camposCompletos = false;
        }else if(!validarEmail(email)){
            edtSolInfoEmail.setError("Email inválido");
            camposCompletos = false;
        }
        // Validar campo Celular
        if(edtSolInfoCelular.getText().toString().isEmpty()){
            edtSolInfoCelular.setError("Ingrese su número de celular");
            camposCompletos = false;
        }
        // Validar si se ha seleccionado una fecha de nacimiento
        if(!fechaSeleccionada){
            Toast.makeText(SolicitarInformacionActivity.this, "Seleccione una fecha de nacimiento", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }
        // Validar si se ha seleccionado una modalidad
        if(rgModalidad.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SolicitarInformacionActivity.this, "Seleccione modalidad", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }
        // Validar si se ha seleccionado un método de contacto
        if(rgMetodoContacto.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SolicitarInformacionActivity.this, "Seleccione metodo de contacto", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }
        // Validar si se ha marcado el consentimiento
        if(rgConsentimiento.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SolicitarInformacionActivity.this, "Marque consentimiento", Toast.LENGTH_SHORT).show();
            camposCompletos = false;
        }

        return camposCompletos;
    }

    private Pair<String, String> modalidad_metodoContacto() {
        String ModalidadInteres = null;
        String MetodoContacto = null;

        // Obtener la modalidad de interés seleccionada
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
        // Obtener el método de contacto seleccionado
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
        // Devolver la modalidad de interés y el método de contacto como un par (Pair)
        return new Pair<>(ModalidadInteres, MetodoContacto);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(SolicitarInformacionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Actualizar el texto del EditText con la fecha seleccionada
                                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                                edtSolInfoFechaNacimiento.setText(date);
                                fechaSeleccionada = true;
                            }
                        }, year, month, dayOfMonth);
                // Mostrar el diálogo de selección de fecha
                datePickerDialog.show();
            }
        });
    }
}