package com.urp.translivik;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Chris on 09/12/15.
 */
public class ChoferRutasWaze extends Activity {
    private Spinner spinner;
    private EditText txtdescripcion;
    private TextView txtlatitud,txtlongitud,txtintenatras;
    Button btnwaze;
    String get,latitud,longitud;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chofer_rutas);
       Intent intent= getIntent();
        Bundle bundle = this.getIntent().getExtras();
        final String idserv=bundle.getString("idservicio");
        final String idchof=bundle.getString("idlogin");

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        spinner=(Spinner)findViewById(R.id.spinnerruta);
        txtdescripcion=(EditText)findViewById(R.id.txtDescripcion);
        txtlatitud=(TextView)findViewById(R.id.textView11);
        txtlongitud=(TextView)findViewById(R.id.textView12);
        txtintenatras=(TextView)findViewById(R.id.textView15);
        btnwaze=(Button)findViewById(R.id.btnwaze);
        txtdescripcion.setEnabled(false);
        txtintenatras.setText(idchof);
        TareaWSListar obj=new TareaWSListar();
        obj.execute(idserv);

        btnwaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitud=txtlatitud.getText().toString();
                longitud=txtlongitud.getText().toString();

                try
                {
                    String url = "waze://?ll="+latitud+","+longitud+"&z=10&navigate=yes";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
                    startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    Intent intent =
                            new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    startActivity(intent);
                }

            }
        });

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {


                        String x = parent.getItemAtPosition(position).toString();
                        String m=x.toString().substring( x.indexOf(',')+1,x.length());
                        Toast.makeText(getApplicationContext(), "Ruta: "+m , Toast.LENGTH_SHORT).show();
                        String objetoseleccionado = x.toString().substring(0, x.indexOf('.'));
                        String direc=getdireccion(objetoseleccionado);
                        txtdescripcion.setText(direc);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(ChoferRutasWaze.this, Consultar_servicios.class);
            b=new Bundle();
            b.putString("idlogin",txtintenatras.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String idsede=params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdestinosxidservicio/"+idsede);

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

                    String idserv = obj.getString("text");
                    String direccion = obj.getString("direcc");
                    String descripcion = obj.getString("lastName");
                    String tipo=obj.getString("firstName");

                        servicios[i] =idserv+". "+tipo+" , "+descripcion;

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
                        new ArrayAdapter<String>(ChoferRutasWaze.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }

    private String getdireccion(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdirecdestino/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("firstName");
            latitud=respJSON.getString("lastName");
            longitud=respJSON.getString("text");
            txtlatitud.setText(latitud);
            txtlongitud.setText(longitud);
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

}
