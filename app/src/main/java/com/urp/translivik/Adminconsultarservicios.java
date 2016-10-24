package com.urp.translivik;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class Adminconsultarservicios extends Fragment {
    private Spinner spinner;
    private TextView servicio,descripcion,estado,set,Intentatras,txtbienvenida;
    private String objetoseleccionado,get;
    private Button btndetalles;
    private Bundle b;
    View rootview;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview=inflater.inflate(R.layout.adminservicios,container,false);
        spinner= (Spinner)rootview.findViewById(R.id.spinnerruta);
        servicio=(TextView)rootview.findViewById(R.id.txtservicio);
        descripcion=(TextView)rootview.findViewById(R.id.txtDescripcion);
        estado=(TextView)rootview.findViewById(R.id.txtestado);
        btndetalles=(Button)rootview.findViewById(R.id.btndetalles);

        set=(TextView)rootview.findViewById(R.id.txtset);
        txtbienvenida=(TextView)rootview.findViewById(R.id.txtbienvenida);
        txtbienvenida.setText("HISTORICO DE SERVICIOS");
        TareaWSListar obj= new TareaWSListar();
        obj.execute();

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String cad=parent.getItemAtPosition(position).toString();
                        String cadcort=cad.substring(cad.indexOf(".")+2,cad.length());
                        Toast.makeText(getActivity(),cadcort, Toast.LENGTH_SHORT).show();

                        String x=parent.getItemAtPosition(position).toString();
                        objetoseleccionado=x.toString().substring(10,11);
                        set.setText(objetoseleccionado);
                        String idservicio = getidservicio(objetoseleccionado);
                        descripcion.setText(getdescripcionservicio(idservicio));
                        String idtiposervicio = getidtiposervicio(idservicio);
                        servicio.setText(getdescripciontiposervicio(idtiposervicio));
                        estado.setText(getEstadoServicio(idservicio));
                        get = objetoseleccionado;

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        servicio.setText("");
                    }
                });

        btndetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),Detalles_servicio.class);
                b=new Bundle();
                b.putString("idserv",set.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootview;

    }



    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/listserviceadmin");

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

                    String idserv = "Servicio :" + obj.getString("firstName");
                    String descr = obj.getString("lastName");
                    String cadena=idserv+". "+descr;
                    servicios[i] = cadena;
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
                        new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adaptador);
            }
        }
    }



    private String getidtiposervicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/IdDescripcionservicio/"+variable);

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

    private String getidservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idservicio/"+variable);

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


    private String getdescripcionservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/OrigenDestino/"+variable);

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



    private String getdescripciontiposervicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/Descripcionservicio/"+variable);

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

    private String getEstadoServicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getEstadoServicio/"+variable);

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
