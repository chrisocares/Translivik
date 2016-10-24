package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Consultar_servicios extends Activity {
    private Spinner spinner;
    private TextView bienvenida,txtsdcs,txtcomentario,servicio,ruta,txtcuenta,datetime,trasladista,nropasajeros,pax,set,Intentatras,tiempoorigen,tiempodestino,vuelo,tiemporealizado,diasviaje;
    private String get,objetoseleccionado,act2;
    private Button btnincidencias,btnfinalizarservicio;
    private Button btntrazaruta;
    private Bundle b,bsede,bchof,bcho,bsd;
    private int x;
    private ScrollView linearlayout;
    private View view;
    private ImageButton ibtrasladista;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_servicio);
        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent= getIntent();
        Bundle bundle= this.getIntent().getExtras();
        String idchofer=bundle.getString("idlogin");
        final String idsede=bundle.getString("idsede");
        view=(View)findViewById(R.id.view7);
        txtsdcs=(TextView)findViewById(R.id.txtsdcs);
        set=(TextView)findViewById(R.id.set1);
        spinner= (Spinner)findViewById(R.id.spinnerruta);
       // tiempodestino=(TextView)findViewById(R.id.txttimedestino);
        diasviaje=(TextView)findViewById(R.id.txtdiashoras);
        vuelo=(TextView)findViewById(R.id.txtvuelo);
      //  tiempoorigen=(TextView)findViewById(R.id.txttimeorigen);
     //   tiemporealizado=(TextView)findViewById(R.id.txttiemporealizado);
        bienvenida=(TextView)findViewById(R.id.txtbienvenida);
        servicio=(TextView)findViewById(R.id.txtservicio);
        ruta=(TextView)findViewById(R.id.txtruta);
        txtcuenta=(TextView)findViewById(R.id.txtcuenta);
        datetime=(TextView)findViewById(R.id.txtdatetime);
        trasladista=(TextView)findViewById(R.id.txttrasladista);
        txtcomentario=(TextView)findViewById(R.id.txtcomentario);
        nropasajeros=(TextView)findViewById(R.id.txtnropasajeros);
        Intentatras=(TextView)findViewById(R.id.atras);
        linearlayout=(ScrollView)findViewById(R.id.scrollView);
        pax=(TextView)findViewById(R.id.txtpax);
        btnincidencias=(Button)findViewById(R.id.btnincidencias);
        btnfinalizarservicio=(Button)findViewById(R.id.btnfinalizar);
        btntrazaruta=(Button)findViewById(R.id.btntrazar);
        ibtrasladista=(ImageButton)findViewById(R.id.ibtrasladista);
        Intentatras.setText(idchofer);
        txtsdcs.setText(idsede);
        ibtrasladista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Consultar_servicios.this);
                alertDialogBuilder.setTitle("Detalles del Trasladista");
                alertDialogBuilder.setMessage("Nombre            :   " + getceltrasladista(getidtrasladista(objetoseleccionado), "firstName") + "\n" +
                        "Apellido            :   " + getceltrasladista(getidtrasladista(objetoseleccionado), "lastName") + "\n" +
                        "Direccion          :   " + "\n" + getceltrasladista(getidtrasladista(objetoseleccionado), "email") + "\n");


                alertDialogBuilder.setPositiveButton("Llamar Celular", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        String phone = getceltrasladista(getidtrasladista(objetoseleccionado), "direcc").toString();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }

                });

                alertDialogBuilder.setNeutralButton("Llamar Telefono", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        String phone = getceltrasladista(getidtrasladista(objetoseleccionado), "cell").toString();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }

                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btnincidencias.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

                 act2 = set.getText().toString();
                 Intent c = new Intent(Consultar_servicios.this, Incidencias.class);
                 b = new Bundle();
                 bsede=new Bundle();
                 bchof=new Bundle();
                 bchof.putString("idlogin",Intentatras.getText().toString());
                 b.putString("idsede",txtsdcs.getText().toString());
                 bsede.putString("idservicio", act2);
                 c.putExtras(bchof);
                 c.putExtras(b);
                 c.putExtras(bsede);
                 startActivity(c);
                 finish();


         }
         }
    );
        btnfinalizarservicio.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Consultar_servicios.this);

                                                        alertDialogBuilder.setTitle("Advertencia");
                                                        alertDialogBuilder.setMessage("¿Esta seguro de finalizar el servicio?");


                                                        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Toast.makeText(getApplicationContext(), finalizarservicio(set.getText().toString()), Toast.LENGTH_SHORT).show();
                                                                btnfinalizarservicio.setEnabled(false);
                                                                btnincidencias.setEnabled(true);
                                                            }

                                                        });


                                                        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                                                            public void onClick(DialogInterface dialog, int id) {


                                                                dialog.cancel();

                                                            }

                                                        });
                                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                                        alertDialog.show();

                                                    }

                                                }
        );

        btntrazaruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                act2 = set.getText().toString();
                Intent c = new Intent(Consultar_servicios.this, ChoferRutasWaze.class);
                b = new Bundle();
                bcho=new Bundle();
                b.putString("idservicio", act2);
                bcho.putString("idlogin",Intentatras.getText().toString());
                c.putExtras(b);
                c.putExtras(bcho);
                startActivity(c);
                finish();

            }
        });





            TareaWSListar obj=new TareaWSListar();
            obj.execute(idchofer);


        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String cad=parent.getItemAtPosition(position).toString();
                        String cadcort=cad.substring(cad.indexOf(".") + 2, cad.length());
                        Toast.makeText(getApplicationContext(),cadcort, Toast.LENGTH_SHORT).show();

                        String x=parent.getItemAtPosition(position).toString();
                        objetoseleccionado=x.toString().substring(10,11);
                        set.setText(objetoseleccionado.trim());
                        String idservicio=getidservicio(objetoseleccionado);
                        //Muestra Fecha del servicio
                        getFecha c=new getFecha();
                        c.execute(objetoseleccionado);
                        //Muestra Tipo de Servicio
                        getDescripcionservicio a= new getDescripcionservicio();
                        a.execute(getidtiposervicio(idservicio));
                        //Muestra el Servicio
                        getRuta b=new getRuta();
                        b.execute(idservicio);
                        //Muestra Nombre del Trasladista
                        if(getidtrasladista(objetoseleccionado).equals("null")){
                            trasladista.setText("NO ASIGNADO");
                            ibtrasladista.setEnabled(false);

                        }else{
                            getNombreTrasladista d=new getNombreTrasladista();
                            d.execute(getidtrasladista(objetoseleccionado));

                        }

                        //Muestra Numero de Pasajeros
                        getNroPasajeros e=new getNroPasajeros();
                        e.execute(objetoseleccionado);

                        if(getpaxsd(objetoseleccionado).equals("null")||getpaxsd(objetoseleccionado).toString().trim().equals("")){
                            getPax f= new getPax();
                            f.execute(getidfile(objetoseleccionado));
                        }else{
                            pax.setText(getpaxsd(objetoseleccionado));
                            getcuenta cta =new getcuenta();
                            cta.execute(objetoseleccionado);
                        }

                        //Muestra Vuelo
                        getDescripcionvuelo h = new getDescripcionvuelo();
                        h.execute(getidvuelo(objetoseleccionado));
                        //Muestra dias viaje
                        getDiasViaje n =new getDiasViaje();
                        n.execute(objetoseleccionado);
                        txtcomentario.setText(getcomentario(objetoseleccionado));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                        servicio.setText("");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Consultar_servicios.this, com.urp.translivik.Menu.class);
            b=new Bundle();
            bsd=new Bundle();
            b.putString("idlogin",Intentatras.getText().toString());
            bsd.putString("idsede",txtsdcs.getText().toString());
            intent.putExtras(b);
            intent.putExtras(bsd);
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
            String id = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/lista/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                servicios = new String[respJSON.length()];

                    for (int i = 0; i < respJSON.length(); i++) {
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
                Log.e("ServicioRest","Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

               ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(Consultar_servicios.this,
                                android.R.layout.simple_spinner_item, servicios);
                adaptador.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adaptador);
            }
        }
    }

    private class getFecha extends AsyncTask<String,Integer,Boolean> {


        private String fecha;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/HoraFecha/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                fecha = respJSON.getString("firstName");



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
                if(fecha.equals("null")){
                    datetime.setText("NO ASIGNADO");
                }else{
                    datetime.setText("");
                    String año=fecha.substring(0,4);
                    String mes=fecha.substring(5,7);
                    String dia=fecha.substring(8,10);
                    String hora=fecha.substring(11,16);
                    datetime.setText(dia+"/"+mes+"/"+año+" "+hora);
                }

            }
        }
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

    private class getRuta extends AsyncTask<String,Integer,Boolean> {


        private String direcc;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/OrigenDestino/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                direcc = respJSON.getString("firstName");



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
                ruta.setText("");
                if(direcc.equals("null")){
                    ruta.setText("NO ASIGNADO");
                }else{
                    ruta.setText(direcc);
                }

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

    private class getDescripcionservicio extends AsyncTask<String,Integer,Boolean> {


        private String descripcion;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/Descripcionservicio/"+id);

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
                servicio.setText("");
                if(descripcion.equals("null")){
                    servicio.setText("NO ASIGNADO");
                }else{
                    servicio.setText(descripcion);
                }
            }
        }
    }

    private String getidtrasladista(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idtrasl/"+variable);

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

    private class getNombreTrasladista extends AsyncTask<String,Integer,Boolean> {


        private String nombre,apellido;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/nombretrasl/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                nombre = respJSON.getString("firstName");
                apellido = respJSON.getString("lastName");


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
                if(nombre.equals("null")||nombre.equals(" ")||nombre.equals("")){
                    trasladista.setText("NO ASIGNADO");
                }else{
                    trasladista.setText(nombre+" "+apellido);
                }

            }
        }
    }

    private String getidfile(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/idPax/"+variable);

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

    private class getPax extends AsyncTask<String,Integer,Boolean> {


        private String Nombrepax,Cuenta;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/Nombrepax/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                Nombrepax = respJSON.getString("firstName");
                Cuenta= respJSON.getString("lastName");


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
                pax.setText("");
                txtcuenta.setText("");
                pax.setText(Nombrepax);
                txtcuenta.setText(Cuenta);
            }
        }
    }

    private String getpaxsd(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getpaxsd/"+variable);

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



    private String getidcliente(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getIdCliente/"+variable);

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

    private class getNroPasajeros extends AsyncTask<String,Integer,Boolean> {


        private String Nombrecliente;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                        new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getNumPersonas/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                Nombrecliente = respJSON.getString("firstName");


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
                nropasajeros.setText(Nombrecliente);
            }
        }
    }

    private String finalizarservicio(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/cambiarestadoservicio/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = "Servicio finalizado";

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String getdatetimesistema(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/HoraFechaSistema/");

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

    private String getlatitudlongitud(String id){
        double longitud;
        double latitud;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getOrigendestino/"+id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String origen = respJSON.getString("firstName");
            String destino=respJSON.getString("lastName");
            String orig="";
            get=orig+"&daddr="+destino;
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);

        }
        return get;
    }

    //CRONOMETRO PRUEBA THREAD

    public class Cronometro extends Thread{

        private TextView cronometro;
        private String chronometer;
        private int horas,minutos,segundos;
        private android.os.Handler escribirenUI;
        private boolean pausa;
        private String salida;

        public Cronometro(String nombre,TextView chrono){
            cronometro=chrono;
            salida="";
            segundos=0;
            minutos=0;
            horas=0;
            chronometer=nombre;
            escribirenUI= new android.os.Handler();
            pausa= Boolean.FALSE;
        }

        @Override
        public void run() {

            try {
                while (Boolean.TRUE) {
                    Thread.sleep(1000);
                    salida = "";
                    if (!pausa) {
                        segundos++;
                        if (segundos == 60) {
                            segundos = 0;
                            minutos++;
                        }
                        if (minutos == 60) {
                            minutos = 0;
                            horas++;

                        }
                        salida += "0";
                        salida += horas;
                        salida += ":";
                        if (minutos <= 9) {
                            salida += "0";
                        }
                        salida += minutos;
                        salida += ":";
                        if (segundos <= 9) {
                            salida += "0";
                        }
                        salida += segundos;

                        try {
                            escribirenUI.post(new Runnable() {
                                @Override
                                public void run() {
                                    cronometro.setText(salida);
                                }
                            });
                        } catch (Exception e) {
                            Log.i("Cronometro", "Error en el cronometro " + chronometer + " al escribir en la UI: " + e.toString());
                        }

                    }

                }
            } catch (InterruptedException e) {
                Log.i("Cronometro", "Error en el cronometro " + chronometer + ": " + e.toString());
            }
        }

    }


    //Contador de Clicks en el boton Iniciar Servicio

    private int cargarcontador(){
        int contador=x++;
        return contador;
    }

    private String insertardateorigen(int id,String fecha){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/insertdatetimeorigen/"+id+"/in/"+fecha);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = "Inicio :"+fecha;

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String insertardatedestino(int id,String fecha){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/insertdatetimedestino/"+id+"/in/"+fecha);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = "Terminado :"+fecha;

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }

    private String dniwhereid(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/dniwhereid/"+variable);

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

    private String getidvuelo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getIdVuelo/"+variable);

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

    private class getDescripcionvuelo extends AsyncTask<String,Integer,Boolean> {


        private String variable1,variable2,variable3;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcionvuelo/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                variable1 = respJSON.getString("firstName");
                variable2 = respJSON.getString("direcc");
                variable3 = respJSON.getString("text");


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
                if(variable1.equals("null")){
                    vuelo.setText("NO ASIGNADO");
                }else{
                    vuelo.setText(variable1 + " " + variable2 + "-" + variable3);
                }

            }
        }
    }

    private class getDiasViaje extends AsyncTask<String,Integer,Boolean> {


        private String variable1,variable2,variable3;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getDiasViaje/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                variable1 = respJSON.getString("firstName");


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
                if(variable1.equals("null")){
                    diasviaje.setText("NO REGISTRADO");
                }else{
                    diasviaje.setText(variable1);
                }

            }
        }
    }

    private class getcuenta extends AsyncTask<String,Integer,Boolean> {


        private String variable1,variable2,variable3;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                    new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getcuenta/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);


                variable1 = respJSON.getString("firstName");


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
                txtcuenta.setText(variable1);

            }
        }
    }


    private String getceltrasladista(String variable,String parametroselect){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdatostrasl/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString(parametroselect);

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        return get;
    }


    private String getcomentario(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getcuenta/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString("lastName");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }
        if(get.equals("")||get.isEmpty()){
            get="NO EXISTEN OBSERVACIONES";
            return get;
        }else{
            return get;
        }

    }
}
