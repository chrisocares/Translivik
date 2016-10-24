package com.urp.translivik;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ScrollView;
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
import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Chris on 13/11/15.
 */
public class datefragment extends Activity {
    private TextView txtdescripcion, txttipo, txthora, txtservicio, txtestado, txtsetfecha,txt;
    private ScrollView scroll;
    private Spinner spinnerd;
    private int año, mes, dia;
    Ipvariable ip = new Ipvariable();
    final String ipconfig = ip.direccionIp;
    private String objetoseleccionado, get;
    private ImageButton ibcalendario;
    private static final int TIPO_DIALOGO = 0;
    private static DatePickerDialog.OnDateSetListener oyenteSelectorFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_selector);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        spinnerd = (Spinner) findViewById(R.id.spinnerdate);
        txtdescripcion = (TextView) findViewById(R.id.txtdescripcion);
        txttipo = (TextView) findViewById(R.id.txtincidencia);
        txthora = (TextView) findViewById(R.id.txthora);
        txtservicio = (TextView) findViewById(R.id.txtservicio);
        txtestado = (TextView) findViewById(R.id.txtestado);
        ibcalendario = (ImageButton) findViewById(R.id.ibcalendar);
        txtsetfecha = (TextView) findViewById(R.id.textView70);
        txt=(TextView)findViewById(R.id.textView68);
        scroll = (ScrollView)findViewById(R.id.scrollView6);

        spinnerd.setVisibility(View.INVISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        txtsetfecha.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);



        Calendar calendario = Calendar.getInstance();
        año = calendario.get(calendario.YEAR);
        mes = calendario.get(calendario.MONTH);
        dia = calendario.get(calendario.DAY_OF_MONTH);
        mostrarfecha();

        oyenteSelectorFecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                año = year;
                mes = monthOfYear;
                dia = dayOfMonth;
                mostrarfecha();
            }
        };

        ibcalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarcalendario();


            }
        });

        spinnerd.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String x = parent.getItemAtPosition(position).toString();
                        //objetoseleccionado = x.toString().substring(13, 14);
                        getdatosincidencia obj = new getdatosincidencia();
                        obj.execute(x);
                        getdescripcion a = new getdescripcion();
                        a.execute(getidtype(x));
                        getdescripcionestado b = new getdescripcionestado();
                        b.execute(getidtypeestado(x));

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    public void mostrarfecha() {
        txtsetfecha.setText(año + "-" + (mes+1) + "-" + dia);
        spinnerd.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.VISIBLE);
        txtsetfecha.setVisibility(View.VISIBLE);
        txt.setVisibility(View.VISIBLE);
        Listarincidencia obj = new Listarincidencia();
        obj.execute(txtsetfecha.getText().toString());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, oyenteSelectorFecha, año, mes, dia);
        }
        return null;
    }

    public void mostrarcalendario() {
        showDialog(TIPO_DIALOGO);

    }

    private class Listarincidencia extends AsyncTask<String, Integer, Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String id = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig + "/SGSTTSERVICES/service/user/getAllincidenciasxfecha/" + id);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                servicios = new String[respJSON.length()];

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String idserv = obj.getString("firstName");

                    servicios[i] = idserv;
                }
            } catch (Exception ex) {
                resul = false;
                Log.e("ServicioRest", "Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(datefragment.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinnerd.setAdapter(adaptador);
            }
        }
    }


    private class getdatosincidencia extends AsyncTask<String, Integer, Boolean> {


        private String descripcion, fecha, servicioid;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig + "/SGSTTSERVICES/service/user/getdatosincidencia/" + id);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");
                servicioid = respJSON.getString("lastName");
                fecha = respJSON.getString("email");

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                txtdescripcion.setText(descripcion);
                txthora.setText(fecha);
                txtservicio.setText(servicioid);

            }
        }
    }

    private class getdescripcion extends AsyncTask<String, Integer, Boolean> {


        private String descripcion;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig + "/SGSTTSERVICES/service/user/descripcionincidencia/" + id);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");


            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                txttipo.setText(descripcion);

            }
        }
    }

    private String getidtypeestado(String id) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig + "/SGSTTSERVICES/service/user/estadoid/" + id);

        del.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("firstName");

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private class getdescripcionestado extends AsyncTask<String, Integer, Boolean> {


        private String descripcion;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig + "/SGSTTSERVICES/service/user/descripcionestado/" + id);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                descripcion = respJSON.getString("firstName");


            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                txtestado.setText(descripcion);

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
}