package com.urp.translivik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Chris on 11/11/15.
 */
public class Detalles_vehiculoP extends Activity {
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    private TextView txtdescripcionv,txtplacav,txtmarcav,txtmodelov,txtanov,txtcapacidadv,txtcolorv,txttipov,txtsedev,txtiddv;
    private String get;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_vehiculo);

        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtdescripcionv=(TextView)findViewById(R.id.txtdescripcionv);
        txtplacav=(TextView)findViewById(R.id.txtplacav);
        txtmarcav=(TextView)findViewById(R.id.txtmarcav);
        txtmodelov=(TextView)findViewById(R.id.txtmodelov);
        txtanov=(TextView)findViewById(R.id.txtanov);
        txtcapacidadv=(TextView)findViewById(R.id.txtcapacidadv);
        txtcolorv=(TextView)findViewById(R.id.txttotal);
        txttipov=(TextView)findViewById(R.id.txttipov);
        txtsedev=(TextView)findViewById(R.id.txtsedev);
        txtiddv=(TextView)findViewById(R.id.iddv);
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idvehiculo");
        final String idserv=b.getString("idserv");
        txtiddv.setText(idserv);
        descripcionvehiculo obj= new descripcionvehiculo();
        obj.execute(id);
        txtsedev.setText(getdescripcionsede(getidsede(id)));
        txtmarcav.setText(getnombremarca(getmarcaid(id)));
        txtmodelov.setText(getnombremodelo(getmarcaid(id)));
        txttipov.setText(getnombretipovehiculo(getidtipovehiculo(id)));

    }

    private class descripcionvehiculo extends AsyncTask<String,Integer,Boolean> {


        private String descripcion,placa,año,capacidad,color;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdetallesvehiculo/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");
                placa= respJSON.getString("lastName");
                año= respJSON.getString("cell");
                capacidad= respJSON.getString("direcc");
                color= respJSON.getString("email");



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
                if(descripcion.equals("null")){
                    txtdescripcionv.setText("NO REGISTRADO");
                }else{
                    txtdescripcionv.setText(descripcion);
                }

                if(placa.equals("null")){
                    txtplacav.setText("NO REGISTRADO");
                }else{
                    txtplacav.setText(placa);
                }

                if(año.equals("null")){
                    txtanov.setText("NO REGISTRADO");
                }else{
                    txtanov.setText(año);
                }

                if(capacidad.equals("null")){
                    txtcapacidadv.setText("NO REGISTRADO");
                }else{
                    txtcapacidadv.setText(capacidad);
                }
                if(color.equals("null")){
                    txtcolorv.setText("NO REGISTRADO");
                }else{
                    txtcolorv.setText(color);
                }
            }
        }
    }

    private String getidsede(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidsede/"+variable);

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

    private String getdescripcionsede(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcionsede/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            if(get.equals("null")){
                get = "NO REGISTRADO";
            }else{
                get = respJSON.getString("firstName");
            }

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getidtipovehiculo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdetallesvehiculo/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("telf");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getnombretipovehiculo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getnombretipovehiculo/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);

            if(get.equals("null")){
                get="NO REGISTRADO";
            }else {
                get = respJSON.getString("firstName");
            }

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getmarcaid(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdetallesvehiculo/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("text");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getnombremarca(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getnombremarca/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);

            if(get.equals("null")){
                get="NO REGISTRADO";
            }else {
                get = respJSON.getString("firstName");
            }
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getnombremodelo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getnombremodelo/"+variable);

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
        if(get.equals("null")){
            get="NO REGISTRADO";
            return get;
        }else{
            return get;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, Detalles_servicioP.class);
            b=new Bundle();
            b.putString("idserv",txtiddv.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
