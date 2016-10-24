package com.urp.translivik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
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
 * Created by Chris on 09/12/15.
 */
public class Admin_Cambiar_pass extends Activity{
    private EditText txtactual,txtnueva,txtcnueva;
    private TextView intentatras,intentatras2;
    private Bundle b,bsede;
    private Button btncambiar;
    private String get;
    Ipvariable ip=new Ipvariable();
    final String ipconfig=ip.direccionIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtactual=(EditText)findViewById(R.id.txtactual);
        txtnueva=(EditText)findViewById(R.id.txtnueva);
        txtcnueva=(EditText)findViewById(R.id.txtcnueva);
        btncambiar=(Button)findViewById(R.id.btncambiar);
        intentatras=(TextView)findViewById(R.id.textView84);
        intentatras2=(TextView)findViewById(R.id.textView87);
        Intent intent= getIntent();
        Bundle bundle= this.getIntent().getExtras();
        final String idchofer=bundle.getString("idusuario");
        final String idsede=bundle.getString("idsede");
        intentatras.setText(idchofer);
        intentatras2.setText(idsede);
        Ipvariable ip=new Ipvariable();
        final String ipconfig=ip.direccionIp;

        btncambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nueva=txtnueva.getText().toString();
                String nuevac=txtcnueva.getText().toString();
                String actual=txtactual.getText().toString();
                String contraseñaactual=getcontraseñaactual(idchofer);
                if(actual.trim().equals("")&&nueva.trim().equals("")&&nuevac.trim().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_Cambiar_pass.this);

                    alertDialogBuilder.setTitle("Advertencia");
                    alertDialogBuilder.setMessage("COMPLETE LOS CAMPOS");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{

                if(contraseñaactual.trim().isEmpty()||nuevac.trim().isEmpty()||nueva.trim().isEmpty()){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_Cambiar_pass.this);

                    alertDialogBuilder.setTitle("Advertencia");
                    alertDialogBuilder.setMessage("COMPLETE LOS CAMPOS");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    if(contraseñaactual.equals(txtactual.getText().toString())&&txtnueva.getText().toString().equals(txtcnueva.getText().toString())){
                        changepass(idchofer, nuevac);
                        txtactual.setEnabled(false);
                        txtcnueva.setEnabled(false);
                        txtnueva.setEnabled(false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_Cambiar_pass.this);

                        alertDialogBuilder.setTitle("Mensaje");
                        alertDialogBuilder.setMessage("Contraseña cambiada con exito , se reiniciara la aplicación");
                        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Admin_Cambiar_pass.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }                else if(!contraseñaactual.equals(txtactual.getText().toString())){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_Cambiar_pass.this);

                        alertDialogBuilder.setTitle("Advertencia");
                        alertDialogBuilder.setMessage("NO ES SU CONTRASEÑA ACTUAL");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }else if(!txtnueva.equals(txtcnueva.getText().toString())){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_Cambiar_pass.this);

                        alertDialogBuilder.setTitle("Advertencia");
                        alertDialogBuilder.setMessage("LAS CONTRASEÑAS NO COINCIDEN , CONFIRME BIEN SU NUEVA CONTRASEÑA");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }

            }
            }
        });
    }
    private String changepass(String variable,String variable2){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/changepassA/"+variable+"/in/"+variable2);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get ="CONTRASEÑA CAMBIADA CON EXITO";

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    private String getcontraseñaactual(String variable){

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del =
                new HttpGet(ipconfig+"/SGSTTSERVICES/service/user/getpassA/"+variable);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONObject respJSON = new JSONObject(respStr);


            get =respJSON.getString("firstName");

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return get;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Admin_Cambiar_pass.this, MainActivityAdmin.class);
            b=new Bundle();
            bsede=new Bundle();
            bsede.putString("idsede",intentatras2.getText().toString());
            b.putString("idusuario",intentatras.getText().toString());

            intent.putExtras(b);
            intent.putExtras(bsede);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
