package com.urp.translivik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Chris on 22/06/15.
 */
public class Adminconsultartrasladistas extends Fragment {
    private TextView txtnombre,txtapellido,txtdireccion,txtcorreo,txttelefono,txtcelular,Intentatras;
    private Spinner spinner;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    private String objetoseleccionado;
    private Bundle b;
    private Button llamar;
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.admintrasladistas,container,false);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        spinner=(Spinner)rootview.findViewById(R.id.spinner);
        txtnombre=(TextView)rootview.findViewById(R.id.txtnombre);
        txtapellido=(TextView)rootview.findViewById(R.id.txtapellido);
        txtdireccion=(TextView)rootview.findViewById(R.id.txtdireccion);
        txtcorreo=(TextView)rootview.findViewById(R.id.txtcorreo);
        txttelefono=(TextView)rootview.findViewById(R.id.txttelefono);
        txtcelular=(TextView)rootview.findViewById(R.id.txtcelular);
        Intentatras=(TextView)rootview.findViewById(R.id.atras);
        llamar=(Button)rootview.findViewById(R.id.button);



        TareaWSListar obj=new TareaWSListar();
        obj.execute();

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setTitle("Advertencia");
                alertDialogBuilder.setMessage("� Llamar Celular o Telefono fijo ?");


                alertDialogBuilder.setPositiveButton("Celular", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        String phone = txtcelular.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }

                });


                alertDialogBuilder.setNegativeButton("Telefono Fijo", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        String phone = txttelefono.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }

                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        Toast.makeText(getActivity(), "Seleccionado: " +
                                parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        String x=parent.getItemAtPosition(position).toString();
                        objetoseleccionado = x.toString().substring(0,x.indexOf('-'));
                        getdatostrasl txt=new getdatostrasl();
                        txt.execute(objetoseleccionado.trim());

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        return rootview;
    }
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listatrasl/1");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                servicios = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String idserv = obj.getString("firstName");
                    String nombre = obj.getString("lastName");
                    String apellido = obj.getString("direcc");
                    if (nombre.equals("null")&& apellido.equals("null")){
                        servicios[i] = idserv+" - Nombre no registrado";
                    }
                    else if(nombre.equals("null")){
                        servicios[i] = idserv+" - "+apellido;
                    }
                    else if(apellido.equals("null")){
                        servicios[i] = idserv+" - "+nombre;
                    }else{
                        servicios[i] = idserv+" - "+nombre+" "+apellido;
                    }
                }
            }
            catch(Exception ex)
            {
                resul = false;
                Log.e("ServicioRest", "Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }


    private class getdatostrasl extends AsyncTask<String,Integer,Boolean> {


        private String nombre,apellido,direcc,correo,telefono,celular;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdatostrasl/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido = respJSON.getString("lastName");
                direcc = respJSON.getString("email");
                correo = respJSON.getString("telf");
                telefono = respJSON.getString("cell");
                celular = respJSON.getString("direcc");

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                txtnombre.setText(nombre);
                txtapellido.setText(apellido);
                txtdireccion.setText(direcc);
                txtcorreo.setText(correo);
                txttelefono.setText(telefono);
                txtcelular.setText(celular);
            }
        }
    }
}
