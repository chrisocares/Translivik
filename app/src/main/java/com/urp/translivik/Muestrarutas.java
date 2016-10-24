package com.urp.translivik;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Chris on 08/12/15.
 */
public class Muestrarutas extends Activity {
    private TextView lblEtiqueta;
    private ListView lstOpciones;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;

    private Titular[] datos =
            new Titular[]{
                    new Titular("Título 1", "Subtítulo largo 1"),
                    new Titular("Título 2", "Subtítulo largo 2"),
                    new Titular("Título 3", "Subtítulo largo 3"),
                    new Titular("Título 4", "Subtítulo largo 4"),
                    new Titular("Título 5", "Subtítulo largo 5"),
                    new Titular("Título 6", "Subtítulo largo 6"),
                    new Titular("Título 7", "Subtítulo largo 7"),
                    new Titular("Título 8", "Subtítulo largo 8"),
                    new Titular("Título 9", "Subtítulo largo 9"),
                    new Titular("Título 10", "Subtítulo largo 10"),
                    new Titular("Título 11", "Subtítulo largo 11"),
                    new Titular("Título 12", "Subtítulo largo 12"),
                    new Titular("Título 13", "Subtítulo largo 13"),
                    new Titular("Título 14", "Subtítulo largo 14"),
                    new Titular("Título 15", "Subtítulo largo 15")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muestrarutas);
        lblEtiqueta = (TextView)findViewById(R.id.LblEtiqueta);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        TareaWSListar obj=new TareaWSListar();
        obj.execute("1");
        //Cabecera
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        lstOpciones.addHeaderView(header);

        //Adaptador
       // AdaptadorTitulares adaptador =
        //        new AdaptadorTitulares(this, datos);

      //  lstOpciones.setAdapter(adaptador);

        //Eventos
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //Alternativa 1:
              //  String opcionSeleccionada =
              //          ((Titular)a.getItemAtPosition(position)).getTitulo();

                //Alternativa 2:
                String opcionSeleccionada =
                		((TextView)v.findViewById(R.id.LblTitulo))
                			.getText().toString();

                lblEtiqueta.setText("Opción seleccionada: " + opcionSeleccionada);
            }
        });

}
    class AdaptadorTitulares extends ArrayAdapter<Titular> {

        public AdaptadorTitulares(Context context, Titular[] datos) {
            super(context, R.layout.listitem_titular, datos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_titular, null);

            TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
            lblTitulo.setText(datos[position].getTitulo());

            TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
            lblSubtitulo.setText(datos[position].getSubtitulo());

            return(item);
        }
    }


    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] servicios;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String id = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdestinosxidservicio/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                servicios = new String[respJSON.length()];

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String idserv =obj.getString("firstName");
                    String descr=obj.getString("lastName");
                    String direccion=obj.getString("direcc");
                    // servicios[i] = idserv+" - "+descr +"\n"+direccion;
                    servicios[i] = idserv;
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
                        new ArrayAdapter<String>(Muestrarutas.this,
                                android.R.layout.simple_list_item_1, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_selectable_list_item);

                lstOpciones.setAdapter(adaptador);
            }
        }
    }

}
