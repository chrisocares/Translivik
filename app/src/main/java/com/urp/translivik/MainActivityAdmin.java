package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Chris on 27/12/15.
 */
public class MainActivityAdmin extends Activity{
    private ImageButton btnchofer,btntrasladita,btnservicios,btnserviciosr,btnpassword,btnincidencias;
    private TextView txtsd;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    Bundle b,busu;
    String get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_principal);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idsede");
        final String idusuario=b.getString("idusuario");
        btnchofer=(ImageButton)findViewById(R.id.btnadmchof);
        btnincidencias=(ImageButton)findViewById(R.id.btnadminc);
        btntrasladita=(ImageButton)findViewById(R.id.btnadmtrasl);
        btnservicios=(ImageButton)findViewById(R.id.btnadmserv);
        btnserviciosr=(ImageButton)findViewById(R.id.btnadmsevr);
        btnpassword=(ImageButton)findViewById(R.id.btnadmpas);
        txtsd=(TextView)findViewById(R.id.txtms);

        txtsd.setText(id);

        btnchofer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityAdmin.this,AdminChofer.class);
                b=new Bundle();
                busu=new Bundle();
                b.putString("idsede", txtsd.getText().toString());
                busu.putString("idusuario",idusuario);
                intent.putExtras(b);
                intent.putExtras(busu);
                startActivity(intent);
                finish();

            }
        });

        btnincidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityAdmin.this,Adminincidencias.class);
                b=new Bundle();
                busu=new Bundle();
                b.putString("idsede", txtsd.getText().toString());
                busu.putString("idusuario",idusuario);
                intent.putExtras(b);
                intent.putExtras(busu);
                startActivity(intent);
                finish();
            }
        });

        btntrasladita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityAdmin.this,AdminTrasladistas.class);
                b=new Bundle();
                busu=new Bundle();
                b.putString("idsede", txtsd.getText().toString());
                busu.putString("idusuario",idusuario);
                intent.putExtras(b);
                intent.putExtras(busu);
                startActivity(intent);
                finish();

            }
        });

        btnservicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getcountservA().equals("0")){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityAdmin.this);
                    alertDialogBuilder.setTitle("Mensaje");
                    alertDialogBuilder.setMessage("No existen servicios pendientes");
                    alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    Intent intent=new Intent(MainActivityAdmin.this,Adminserviciospendientes.class);
                    b=new Bundle();
                    busu=new Bundle();
                    b.putString("idsede", txtsd.getText().toString());
                    busu.putString("idusuario",idusuario);
                    intent.putExtras(b);
                    intent.putExtras(busu);
                    startActivity(intent);
                    finish();
                }


            }
        });

        btnserviciosr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityAdmin.this,AdminServiciostodos.class);
                b=new Bundle();
                busu=new Bundle();
                b.putString("idsede", txtsd.getText().toString());
                busu.putString("idusuario",idusuario);
                intent.putExtras(b);
                intent.putExtras(busu);
                startActivity(intent);
                finish();
            }
        });

        btnpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityAdmin.this,Admin_Cambiar_pass.class);
                b=new Bundle();
                busu=new Bundle();
                b.putString("idsede", txtsd.getText().toString());
                busu.putString("idusuario",idusuario);
                intent.putExtras(b);
                intent.putExtras(busu);
                startActivity(intent);
                finish();
            }
        });


        if(getcountserv(getfechasistema()).equals("0")){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityAdmin.this);
            alertDialogBuilder.setTitle("Mensaje");
            alertDialogBuilder.setMessage("Bienvenido , no existen servicios pendientes para hoy !");
            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }

            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else{
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityAdmin.this);
            alertDialogBuilder.setTitle("Mensaje");
            alertDialogBuilder.setMessage("Bienvenido , existe "+getcountserv(getfechasistema())+" servicios pendientes para hoy !");
            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }

            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }



    }




    private String getcountserv(String id){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/countservall/"+id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("userid");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }


    private String getcountservA(){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/countservallA/");

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("userid");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getfechasistema (){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getCurdate/");

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
