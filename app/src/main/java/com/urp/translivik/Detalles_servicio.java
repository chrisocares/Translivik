package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.net.Uri;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * Created by Chris on 08/11/15.
 */
public class Detalles_servicio extends Activity {
    private Bundle b,bid,buser;
    private TextView txtid,txtfecha,txtservicio,txttiposervicio,bb,bb2,txtfile,txtpax,txtpasajeros,txttrasladista,txtvuelo,txtestado,txtexterno,
    txtvehiculo,txtchofer,txtcostod;
    String get;
    private ImageButton ibcosto,ibvehiculo,ibvuelo,ibmaps;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_servicio);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        b=this.getIntent().getExtras();
        final String id=b.getString("idserv");
        final String iduser=b.getString("idusuario");
        final String idsede=b.getString("idsede");

        txtid=(TextView)findViewById(R.id.txtidservicio);
        txtid.setText(id);
        txtfecha=(TextView)findViewById(R.id.txtfechad);
        bb=(TextView)findViewById(R.id.bb);
        bb2=(TextView)findViewById(R.id.bb2);
        txtservicio=(TextView)findViewById(R.id.txtserviciod);
        txttiposervicio=(TextView)findViewById(R.id.txttiposerviciod);
        txtfile=(TextView)findViewById(R.id.txtfiled);
        txtpax=(TextView)findViewById(R.id.txtpaxd);
        txtpasajeros=(TextView)findViewById(R.id.txtnropasajerosd);
        txttrasladista=(TextView)findViewById(R.id.txttrasladistad);
        txtvuelo=(TextView)findViewById(R.id.txtvuelod);
        txtestado=(TextView)findViewById(R.id.txtestadod);
        txtexterno=(TextView)findViewById(R.id.txtexternod);
        txtvehiculo=(TextView)findViewById(R.id.txtvehiculod);
        txtchofer=(TextView)findViewById(R.id.txtchoferd);
        ibcosto=(ImageButton)findViewById(R.id.ibcosto);
        ibvehiculo=(ImageButton)findViewById(R.id.ibvehiculo);
        ibvuelo=(ImageButton)findViewById(R.id.ibvuelo);
        ibmaps=(ImageButton)findViewById(R.id.ibmaps);
        txtcostod=(TextView)findViewById(R.id.txtcostod);
        bb.setText(iduser);
        bb2.setText(idsede);
        getFecha c=new getFecha();
        c.execute(id);
        txtservicio.setText(getdescripcionservicio(getidservicio(id)));
        txttiposervicio.setText(gettiposervicio(getidtiposervicio(id)));
        if(getpaxsd(id).equals("null")||getpaxsd(id).toString().trim().equals("")){
            getPax f= new getPax();
            f.execute(getidfile(id));
        }else{
            txtpax.setText(getpaxsd(id));
            getcuenta cta =new getcuenta();
            cta.execute(id);
        }
        txtpasajeros.setText(getnumpersonas(id));
        txttrasladista.setText(gettrasladista(getidtrasladista(id)));
        txtvuelo.setText(getdescripcionvuelo(getidvuelo(id)));
        txtestado.setText(getestadoservicio(id));
        txtexterno.setText(getexternalizado(id));
        txtvehiculo.setText(getdescripcion2(getidvehiculo(id)));
        txtchofer.setText(getchofer(getidchofer(id)));
        txtcostod.setText("S/."+(precioservicio(id)+adicional(id)-descuento(id)));
        ibcosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detalles_servicio.this);
                alertDialogBuilder.setTitle("Detalles de Costo");
                alertDialogBuilder.setMessage("Precio del servicio      :    " + getprecioservicio(id) + "\n" + "Descuento                    :   " + getdescuento(id) + "\n" + "Adicional                      :    " + getadicional(id) + "\n" + "--------------------------------------------------" + "\n" + "Precio Total                 :    "+(precioservicio(id)+adicional(id)-descuento(id)));
                alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });



        ibvehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Detalles_servicio.this,Detalles_vehiculo.class);
                b=new Bundle();
                bid=new Bundle();
                b.putString("idvehiculo", getidvehiculo(id));
                bid.putString("idserv",txtid.getText().toString());
                intent.putExtras(b);
                intent.putExtras(bid);
                startActivity(intent);
                finish();

            }
        });

        ibvuelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detalles_servicio.this);
                alertDialogBuilder.setTitle("Detalles del Vuelo");
                alertDialogBuilder.setMessage("Descripcion       :   " + getdescripcionvuelo(getidvuelo(id)) + "\n" + "Aerolinea           :   " + getaerolinea(getdatosvuelo(getidvuelo(id), "email")) + "\n" + "Horario               :   " + getdatosvuelo(getidvuelo(id), "lastName") + "\n" + "Origen                :   " + getdatosvuelo(getidvuelo(id), "direcc") + "\n" + "Destino              :   " + getdatosvuelo(getidvuelo(id), "text"));
                alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ibmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String cadena=getcoordenadas(getiddestino(getidservicio(id)));

                String directionweburl = "http://maps.google.com/maps?saddr="+cadena;

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directionweburl));
                myIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(myIntent);

            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this, AdminServiciostodos.class);
            b=new Bundle();
            buser=new Bundle();
            b.putString("idsede",bb2.getText().toString());
            buser.putString("idusuario",bb.getText().toString());
            intent.putExtras(b);
            intent.putExtras(buser);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Double precioservicio(String id){
        if(getprecioservicio(id).toString().equals("N.R")){
            String prize="0";
            Double precio=Double.parseDouble(prize);
            return precio;
        }else {
            Double precio=Double.parseDouble(getprecioservicio(id));
            return precio;
        }
    }


    private Double adicional(String id){
        if(getadicional(id).toString().equals("N.R")){
            String prize="0";
            Double precio=Double.parseDouble(prize);
            return precio;
        }else {
            Double precio=Double.parseDouble(getadicional(id));
            return precio;
        }
    }

    private Double descuento(String id){
        if(getdescuento(id).toString().equals("N.R")){
            String prize="0";
            Double precio=Double.parseDouble(prize);
            return precio;
        }else {
            Double precio=Double.parseDouble(getdescuento(id));
            return precio;
        }
    }


    private String getaerolinea(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcionaerolinea/"+variable);

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

    private String getdatosvuelo(String variable,String parametro){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcionvuelo/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get = respJSON.getString(parametro);

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
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
                    txtfecha.setText("NO ASIGNADO");
                }else{
                    txtfecha.setText("");
                    String año=fecha.substring(0,4);
                    String mes=fecha.substring(5,7);
                    String dia=fecha.substring(8,10);
                    String hora=fecha.substring(11,16);
                    txtfecha.setText(dia+"/"+mes+"/"+año+" "+hora);
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
            Log.e("ServicioRest", "Error!", ex);
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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String gettiposervicio(String variable){

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }



    private String cuenta(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getcuenta/"+variable);

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
        if(get.equals("null")){
            get="NO ASIGNADO";
            return get;
        }else{
            return get;
        }
    }

    private String getidpax(String variable){

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getnumpersonas(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getNumPersonas/"+variable);

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

    private String getidtrasladista(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getIdTrasladista/"+variable);

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

    private String gettrasladista(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getNombreApellidoTrasladista/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String nombre = respJSON.getString("firstName");
            String apellido = respJSON.getString("lastName");
            if(nombre.equals("null") && apellido.equals("null")){
                get="NO ASIGNADO";
            }else if(nombre.equals("null")){
                get=apellido;
            }else if(apellido.equals("null")){
                get=nombre;
            }else{
                get=nombre+" "+apellido;
            }
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }
    //FALTA VUELO
    private String getestadoservicio(String variable){

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
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getexternalizado(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getExternalizado/"+variable);

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

    private String getidvehiculo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidvehiculo/"+variable);

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

    private String getvehiculo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getPlacaDescripcionVehiculo/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String descripcion = respJSON.getString("firstName");
            String placa = respJSON.getString("lastName");

            get=descripcion+" "+placa;

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getdescripcion2(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcion2/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String descripcion = respJSON.getString("firstName");

            get=descripcion;

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getidchofer(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getidChofer/"+variable);

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

    private String getchofer(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getNombreApellidoChofer/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);

            String nombre = respJSON.getString("firstName");
            String apellido = respJSON.getString("lastName");
            if(nombre.equals("null") ||apellido.equals("null") ){
                get="NO ASIGNADO";
            }else{
                get=nombre+" "+apellido;
            }


        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
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

    private String getdescripcionvuelo(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getdescripcionvuelo/"+variable);

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
        if(get.equals("null")){
            get="NO APLICA";
            ibvuelo.setEnabled(false);
            return get;
        }else{
            return get;
        }
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
        if(get.equals("null")){
            get="N.R";
            return get;
        }else{
            return get;
        }

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
        if(get.equals("null")){
            get="N.R";
            return get;
        }else{
            return get;
        }
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
        if(get.equals("null")){
            get="N.R";
            return get;
        }else{
            return get;
        }
    }

    private String getcoordenadas(String id){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getCoordenadas/"+id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            String longitud = respJSON.getString("firstName");
            String latitud=respJSON.getString("lastName");
            get="&daddr="+longitud+","+latitud;
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);

        }
        return get;
    }

    private String getiddestino(String id){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getiddestino/"+id);

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
                txtpax.setText("");
                txtfile.setText("");
                txtpax.setText(Nombrepax);
                txtfile.setText(Cuenta);
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
                variable2 =respJSON.getString("lastName");


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
                txtfile.setText(variable1);

              /*  if(variable2.equals("null")){
                    txtcomentario.setText("NO EXISTEN OBSERVACIONES");
                }else{
                    txtcomentario.setText(variable2);
                }*/

            }
        }
    }
}
