package com.urp.translivik;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import com.parse.Parse;
import com.parse.PushService;

import java.lang.reflect.Type;

public class MainActivity extends Activity {



    private Button btningresar;
    private EditText usuario,password;
    private TextView sede;
    private String id1,id2,get,useradmin,passadmin;
    private CheckBox box,box2;
    private Spinner spinnersede;
    Bundle b,bsede;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Parse.initialize(this, "uzepuLnakfJPHxk6OutTxpgeSWwtIYpPDhQk00dZ", "z92nxGXs6EIEdu6DYwjhy5z7aQttfO5AZGZ1N2b3");
        PushService.setDefaultPushCallback(this, MainActivity.class);*/
        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        usuario=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpassword);
        sede=(TextView)findViewById(R.id.sedelogin);
        sede.setText(" ");
        box=(CheckBox)findViewById(R.id.checkBox);
        spinnersede=(Spinner)findViewById(R.id.spinnersede);
        //box2=(CheckBox)findViewById(R.id.checkBox2);
        btningresar=(Button)findViewById(R.id.btningresar);
        CargarPreferencias();
        TareaWSListar obj=new TareaWSListar();
        obj.execute();

        spinnersede.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String x = parent.getItemAtPosition(position).toString();
                        sede.setText(getidsede(x).toString());

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        sede.setText("");
                    }
                });

        btningresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                id1 = getusuarioid(usuario.getText().toString(),password.getText().toString(),sede.getText().toString());
                id2 = getpasswordid(password.getText().toString());

               // useradmin=getusuarioadmin();
               // passadmin=getpassadmin();
                if(box.isChecked()) {

                    if (usuario.getText().toString().equals("") || password.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Complete los campos", Toast.LENGTH_SHORT).show();

                    } else if (Integer.parseInt(getusuarioadmin(usuario.getText().toString(), sede.getText().toString(),password.getText().toString()))>0) {
                        Toast.makeText(getApplicationContext(), "Iniciando sesion...", Toast.LENGTH_SHORT).show();
                        GuardarPreferencias();
                        b = new Bundle();
                        bsede = new Bundle();
                        bsede.putString("idsede", sede.getText().toString());
                        b.putString("idusuario",getusuarioadmin(usuario.getText().toString(), sede.getText().toString(),password.getText().toString()));
                        Intent intent = new Intent(MainActivity.this, MainActivityAdmin.class);
                        intent.putExtras(bsede);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    } else {
                        usuario.setText("");
                        password.setText("");
                        Toast.makeText(getApplicationContext(), "Usuario o password incorrecto", Toast.LENGTH_SHORT).show();
                        GuardarPreferencias();
                    }



                }else {
                    if (usuario.getText().toString().equals("") || password.getText().toString().equals("")) {

                        Toast.makeText(getApplicationContext(), "Complete los campos", Toast.LENGTH_SHORT).show();

                    } else if (Integer.parseInt(id1) > 0) {
                        Toast.makeText(getApplicationContext(), "Iniciando sesion...", Toast.LENGTH_SHORT).show();
                        GuardarPreferencias();

                        Intent intent = new Intent(MainActivity.this, com.urp.translivik.Menu.class);
                        b = new Bundle();
                        bsede = new Bundle();
                        b.putString("idlogin", id1);
                        bsede.putString("idsede",sede.getText().toString());
                        intent.putExtras(b);
                        intent.putExtras(bsede);
                        startActivity(intent);
                        finish();
                    } else {
                        usuario.setText("");
                        password.setText("");
                        Toast.makeText(getApplicationContext(), "Usuario o password incorrecto", Toast.LENGTH_SHORT).show();

                        }
                    }

                }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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


    private String getusuarioid(String variable,String variable2,String variable3){

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del =
				new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidloginusuario/"+variable+"/clv/"+variable2+"/sd/"+variable3);

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

    private String getidsede(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidsedechofer/"+variable);

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

    private String getidloginsede(String variable,String variable2,String variable3){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidloginsede/"+variable+"/x/"+variable2+"/xz/"+variable3);

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

    private String getpasswordid(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidloginpassword/"+variable);

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

    private String  getusuarioadmin(String id,String idd,String iddd){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/nickadmin/"+id+"/sd/"+idd+"/cl/"+iddd);

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

    private String getpassadmin(String id,String idd){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/claveadmin/"+id+"/sde/"+idd);

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

    public void CargarPreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("PreferenciasdeUsuario", Context.MODE_PRIVATE);
        usuario.setText(mispreferencias.getString("user", ""));
    }

    public void GuardarPreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("PreferenciasdeUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=mispreferencias.edit();
        String user=usuario.getText().toString();
        editor.putString("user",user);
        editor.commit();
    }

    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] sede;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listasede");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                sede = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String idserv = obj.getString("firstName");

                    sede[i] = idserv;
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
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, sede);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinnersede.setAdapter(adaptador);
            }
        }
    }

}
