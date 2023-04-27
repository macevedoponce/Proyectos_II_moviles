package com.acevedo.rutaexperienciauc.ui.solicitarInformacion;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

import com.acevedo.rutaexperienciauc.util.Util;


public class SolicitarInformacionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    //Variables del layout
    EditText edtSolInfoNombres, edtSolInfoApellidoPaterno, edtSolInfoApellidoMaterno, edtSolInfoEmail, edtSolInfoCelular, edtSolInfoFechaNacimiento;
    RadioButton RBPresencial, RBSemiPresencial, RBADistancia, RBCorreoElectronico, RBCelular, RBWhatsApp, RDConsentimiento;
    Button btnSolicitarInformacion;

    //Variables para utilizar internamente
    String modal_interes, metodo_contacto;

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
        edtSolInfoNombres = vista.findViewById(R.id.edtSolInfoNombres);
        edtSolInfoApellidoPaterno = vista.findViewById(R.id.edtSolInfoApellidoPaterno);
        edtSolInfoApellidoMaterno = vista.findViewById(R.id.edtSolInfoApellidoMaterno);
        edtSolInfoEmail = vista.findViewById(R.id.edtSolInfoEmail);
        edtSolInfoCelular = vista.findViewById(R.id.edtSolInfoCelular);
        edtSolInfoFechaNacimiento = vista.findViewById(R.id.edtSolInfoFechaNacimiento);
        RBPresencial = vista.findViewById(R.id.RBPresencial);
        RBSemiPresencial = vista.findViewById(R.id.RBSemiPresencial);
        RBADistancia = vista.findViewById(R.id.RBADistancia);
        RBCorreoElectronico = vista.findViewById(R.id.RBCorreoElectronico);
        RBCelular = vista.findViewById(R.id.RBCelular);
        RBWhatsApp = vista.findViewById(R.id.RDWhatsApp);
        RDConsentimiento = vista.findViewById(R.id.RDConsentimiento);
        btnSolicitarInformacion =vista.findViewById(R.id.btnSolicitarInformacion);


        requestQueue = Volley.newRequestQueue(getContext());

        modalidad_metodoContacto();

        if(RDConsentimiento.isChecked() == false){
            btnSolicitarInformacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Marque la casilla por favor", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            btnSolicitarInformacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitarInformacion();
                }
            });
        }
        return vista;
    }

    private void solicitarInformacion() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

        String CcNombre = edtSolInfoNombres.getText().toString();
        String CcApellidoPaterno = edtSolInfoApellidoPaterno.getText().toString();
        String CcApellidoMaterno = edtSolInfoApellidoMaterno.getText().toString();
        String CcCorreo = edtSolInfoEmail.getText().toString();
        String CcTelefono = edtSolInfoCelular.getText().toString();
        String CcFechaNacimiento = edtSolInfoFechaNacimiento.getText().toString();
        String CcFechaContacto = dateFormat.format(calendar.getTime());
        String IdTipoContacto = metodo_contacto;
        String IdTipoModalidad = modal_interes;
        String URL = Util.RUTA_SOLICITAR_INFORMACION + "api" + "&CcNombre="+CcNombre+"&CcApellidoPaterno="+CcApellidoPaterno+"&CcApellidoMaterno="+CcApellidoMaterno+
                "CcCorreo="+CcCorreo+"CcTelefono="+CcTelefono+"CcFechaNacimiento"+CcFechaNacimiento+"CcFechaContacto="+CcFechaContacto+"IdTipoContacto="+IdTipoContacto
                +"IdTipoModalidad="+IdTipoModalidad;

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
        if(RBPresencial.isChecked() == true){
            modal_interes = "1";
        }else
        if(RBSemiPresencial.isChecked() == true){
            modal_interes = "2";
        }else{
            if(RBADistancia.isChecked() == true){
                modal_interes= "3";
            }
        }

        if(RBCorreoElectronico.isChecked() == true){
            metodo_contacto = "1";
        }else
        if(RBCelular.isChecked() == true){
            metodo_contacto = "2";
        }else{
            if(RBWhatsApp.isChecked() == true){
                metodo_contacto= "3";
            }
        }
    }
}

