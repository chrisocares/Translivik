package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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
public class AdminTrasladistas extends Activity {
    private TextView txtnombre,txtapellido,txtdireccion,txtcorreo,txttelefono,txtcelular,Intentatras,Intentatrasz,Intentatraszz;
    private Spinner spinner;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    private String objetoseleccionado;
    private Bundle b,buser;
    private Button llamar;
    View rootview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admintrasladistas);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idsede");
        final String iduser=b.getString("idusuario");
        spinner=(Spinner)findViewById(R.id.spinner);
        txtnombre=(TextView)findViewById(R.id.txtnombre);
        txtapellido=(TextView)findViewById(R.id.txtapellido);
        txtdireccion=(TextView)findViewById(R.id.txtdireccion);
        txtcorreo=(TextView)findViewById(R.id.txtcorreo);
        txttelefono=(TextView)findViewById(R.id.txttelefono);
        txtcelular=(TextView)findViewById(R.id.txtcelular);
        Intentatras=(TextView)findViewById(R.id.atras);
        llamar=(Button)findViewById(R.id.button);
        Intentatrasz=(TextView)findViewById(R.id.mnmt);
        Intentatraszz=(TextView)findViewById(R.id.mnmmt);
        Intentatrasz.setText(id);
        Intentatraszz.setText(iduser);

        TareaWSListar obj=new TareaWSListar();
        obj.execute(id);

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminTrasladistas.this);

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
                                               View v, int position, long id) {
                        Toast.makeText(AdminTrasladistas.this, "Seleccionado: " +
                                parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        String x=parent.getItemAtPosition(position).toString();
                        objetoseleccionado = x.toString().substring(0,x.indexOf('-'));
                        getdatostrasl txt=new getdatostrasl();
                        txt.execute(objetoseleccionado.trim());

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {
            String id = params[0];
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listatrasl/"+id);

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
                        new ArrayAdapter<String>(AdminTrasladistas.this,
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, MainActivityAdmin.class);
            b=new Bundle();
            buser=new Bundle();
            b.putString("idsede",Intentatrasz.getText().toString());
            buser.putString("idusuario",Intentatraszz.getText().toString());
            intent.putExtras(b);
            intent.putExtras(buser);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
