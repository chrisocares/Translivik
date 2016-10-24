package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

public class Menu extends Activity {
    private ImageButton btnservicios, btntrasladistas, btnsalir,btnpassword;
    private Bundle b,bsede;
    private TextView txt;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    String get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        final String idchofer = bundle.getString("idlogin");
        final String idsede=bundle.getString("idsede");

        btnpassword = (ImageButton) findViewById(R.id.imageButton4);
        btnservicios = (ImageButton) findViewById(R.id.btnconsultar);
        btntrasladistas = (ImageButton) findViewById(R.id.btntrasladista);
        btnsalir=(ImageButton)findViewById(R.id.btnsalir);
        txt=(TextView)findViewById(R.id.textView2);


        getNombrebienvenida tarea=new getNombrebienvenida();
        tarea.execute(idchofer);

        btnpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Cambiar_pass.class);
                b = new Bundle();
                bsede = new Bundle();
                b.putString("idlogin", idchofer);
                bsede.putString("idsede",idsede);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        btnservicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getcountserv(idchofer).equals("0")){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu.this);
                    alertDialogBuilder.setTitle("Mensaje");
                    alertDialogBuilder.setMessage("No tiene servicios Pendientes");
                    alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {


                    Intent intent = new Intent(Menu.this, Consultar_servicios.class);
                    b = new Bundle();
                    bsede = new Bundle();
                    b.putString("idlogin", idchofer);
                    bsede.putString("idsede", idsede);
                    intent.putExtras(b);
                    intent.putExtras(bsede);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btntrasladistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Consultar_trasladistas.class);
                b=new Bundle();
                bsede=new Bundle();
                b.putString("idlogin",idchofer);
                bsede.putString("idsede",idsede);
                intent.putExtras(b);
                intent.putExtras(bsede);
                startActivity(intent);
                finish();


            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getcountserv(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/countserv/"+variable);

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
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private class getNombrebienvenida extends AsyncTask<String,Integer,Boolean> {


        private String nombre;
        private String apellido;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido=respJSON.getString("lastName");


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
                txt.setText("Bienvenido " + nombre + " " + apellido);
            }
        }
    }

}
