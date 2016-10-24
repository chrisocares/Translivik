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
public class AdminChofer extends Activity {
    private TextView txtnombre,txtapellido,txtdireccion,txtcorreo,txttelefono,txtcelular,txtcategoria,txtsede,Intentatras,Intentatrasz,Intentatraszz;
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
        setContentView(R.layout.adminchofer);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idsede");
        final String iduser=b.getString("idusuario");
        spinner=(Spinner)findViewById(R.id.spinner);
        txtnombre=(TextView)findViewById(R.id.txtnombrec);
        txtapellido=(TextView)findViewById(R.id.txtapellidoc);
        txttelefono=(TextView)findViewById(R.id.txttelefonoc);
        txtcategoria=(TextView)findViewById(R.id.txtcategoria);
        txtcelular=(TextView)findViewById(R.id.txtcelularc);
        txtsede=(TextView)findViewById(R.id.txtsedec);
        Intentatras=(TextView)findViewById(R.id.atras);
        llamar=(Button)findViewById(R.id.button);
        Intentatrasz=(TextView)findViewById(R.id.mnmc);
        Intentatraszz=(TextView)findViewById(R.id.mnmmc);
        Intentatrasz.setText(id);
        Intentatraszz.setText(iduser);

        TareaWSListar obj=new TareaWSListar();
        obj.execute(id);

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminChofer.this);

                alertDialogBuilder.setTitle("Advertencia");
                alertDialogBuilder.setMessage("� Llamar Celular ?");


                alertDialogBuilder.setPositiveButton("Celular", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        String phone = txtcelular.getText().toString();
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
                        String cad=parent.getItemAtPosition(position).toString();
                        String cadcort=cad.substring(cad.indexOf(".") + 2, cad.length());
                        Toast.makeText(AdminChofer.this,cadcort, Toast.LENGTH_SHORT).show();

                        String x=parent.getItemAtPosition(position).toString();
                        objetoseleccionado=x.toString().substring(8,x.indexOf("."));
                        getdatoschofer txt=new getdatoschofer();
                        txt.execute(objetoseleccionado);


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
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listchofer/"+id);

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

                    String idserv = "Chofer :"+obj.getString("firstName");
                    String nombre= obj.getString("direcc");

                    servicios[i] = idserv+". "+nombre;
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
                        new ArrayAdapter<String>(AdminChofer.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }


    private class getdatoschofer extends AsyncTask<String,Integer,Boolean> {


        private String nombre,apellido,categoria,sede,empresa,celular;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdatochofer/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido = respJSON.getString("lastName");
                categoria = respJSON.getString("email");
                sede = respJSON.getString("telf");
                celular=respJSON.getString("direcc");
                empresa=respJSON.getString("text");


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
                txtcategoria.setText(categoria);
                txtsede.setText(sede);
                txtcelular.setText(celular);
                txttelefono.setText(empresa);

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