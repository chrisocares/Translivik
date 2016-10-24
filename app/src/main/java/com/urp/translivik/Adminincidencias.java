package com.urp.translivik;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
 * Created by Chris on 23/06/15.
 */
public class Adminincidencias extends Activity {

    private TextView txtdescripcion,txttipo,txtfecha,txtservicio,txtestado,txtcalendar,hjh,hjhh;
    private Bundle b,buser;
    private Spinner spinner;
    private int año,mes,dia;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    private String objetoseleccionado,get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminincidencias);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String idsedeA=b.getString("idsede");
        final String iduser=b.getString("idusuario");
        hjh=(TextView)findViewById(R.id.hjh);
        hjhh=(TextView)findViewById(R.id.hjhh);
        spinner=(Spinner)findViewById(R.id.spinnerruta);
        txtdescripcion=(TextView)findViewById(R.id.txtdesc);
        txttipo=(TextView)findViewById(R.id.txttipo);
        txtfecha=(TextView)findViewById(R.id.txtdatetime);
        txtservicio=(TextView)findViewById(R.id.txtidservicio);
        txtestado=(TextView)findViewById(R.id.txtestado);
        hjh.setText(idsedeA);
        hjhh.setText(iduser);

        TareaWSListar obj=new TareaWSListar();
        obj.execute(idsedeA);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String x = parent.getItemAtPosition(position).toString();
                        objetoseleccionado = x.toString().substring(0,x.indexOf("."));
                        getdatosincidencia obj=new getdatosincidencia();
                        obj.execute(objetoseleccionado.trim());
                        getdescripcion a =new getdescripcion();
                        a.execute(getidtype(objetoseleccionado.trim()));


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
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listaidincidenciasA/"+id);

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
                    String descripcion = obj.getString("lastName");

                    servicios[i] = idserv+".- "+descripcion;
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
                        new ArrayAdapter<String>(Adminincidencias.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }

    private class getdatosincidencia extends AsyncTask<String,Integer,Boolean> {


        private String descripcion,fecha,servicioid,estado;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdatosincidencia/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");
                servicioid = respJSON.getString("lastName");
                fecha = respJSON.getString("email");
                estado= respJSON.getString("text");

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
                txtdescripcion.setText(descripcion);
                txtfecha.setText(fecha);
                txtservicio.setText(servicioid);
                txtestado.setText(estado);
            }
        }
    }

    private String getidtype(String id){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idtypeinc/"+id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("firstName");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private class getdescripcion extends AsyncTask<String,Integer,Boolean> {


        private String descripcion;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/descripcionincidencia/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");


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
                txttipo.setText(descripcion);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, MainActivityAdmin.class);
            b=new Bundle();
            buser=new Bundle();
            b.putString("idsede",hjh.getText().toString());
            buser.putString("idusuario",hjhh.getText().toString());
            intent.putExtras(b);
            intent.putExtras(buser);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

