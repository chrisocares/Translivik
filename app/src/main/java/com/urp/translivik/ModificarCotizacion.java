package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Chris on 08/12/15.
 */
public class ModificarCotizacion extends Activity {
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    private EditText txtprecioservicio,txtdescuento,txtadicional;
    private TextView txttotal,textviewnn;
    private Button btnmodificar;
    private String get;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.costos);

        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtprecioservicio=(EditText)findViewById(R.id.txtps);
        txtdescuento=(EditText)findViewById(R.id.txtd);
        txtadicional=(EditText)findViewById(R.id.txta);
        txttotal=(TextView)findViewById(R.id.txttotal);
        textviewnn=(TextView)findViewById(R.id.textviewnn);
        btnmodificar=(Button)findViewById(R.id.btnmodificar);
        textviewnn.setVisibility(View.INVISIBLE);
        txttotal.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idservicio");

        if(getprecioservicio(id).equals("null")){
            txtprecioservicio.setText("NO REGISTRADO");
        }else{
            txtprecioservicio.setText(getprecioservicio(id));
        }
        if(getdescuento(id).equals("null")){
            txtdescuento.setText("NO REGISTRADO");
        }else{
            txtdescuento.setText(getdescuento(id));
        }
        if(getadicional(id).equals("null")){
            txtadicional.setText("NO REGISTRADO");
        }else{
            txtadicional.setText(getadicional(id));
        }

        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtprecioservicio.getText().toString().equals("NO REGISTRADO")||txtdescuento.getText().toString().equals("NO REGISTRADO")||txtadicional.getText().toString().equals("NO REGISTRADO")){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ModificarCotizacion.this);
                    alertDialogBuilder.setTitle("ADVERTENCIA");
                    alertDialogBuilder.setMessage("COMPLETE LOS CAMPOS CON NUMEROS");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
               updatecostos(txtprecioservicio.getText().toString(), txtdescuento.getText().toString(), txtadicional.getText().toString(), id);
                txtadicional.setEnabled(false);
                txtdescuento.setEnabled(false);
                txtprecioservicio.setEnabled(false);
                textviewnn.setVisibility(View.VISIBLE);
                txttotal.setVisibility(View.VISIBLE);
                String pr=txtprecioservicio.getText().toString();
                double precio=Double.parseDouble(pr);
                double descuento=Double.parseDouble(txtdescuento.getText().toString());
                double adicional=Double.parseDouble(txtadicional.getText().toString());
                txttotal.setText(""+(precio+adicional-descuento));
                btnmodificar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }



    private String updatecostos(String variable,String variable2,String variable3,String variable4){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/UpdateCostos/"+variable+"/b/"+variable2+"/c/"+variable3+"/d/"+variable4);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);

            get = "MODIFICADO EXITOSAMENTE";



        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getprecioservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getPrecioServicio/"+variable);

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getdescuento(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getDescuento/"+variable);

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getadicional(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getAdicional/"+variable);

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }
}
