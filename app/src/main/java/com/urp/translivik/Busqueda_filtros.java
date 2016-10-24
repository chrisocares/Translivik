package com.urp.translivik;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by Chris on 08/11/15.
 */
public class Busqueda_filtros extends Activity{
    private TextView txt1,txt2;
    private Spinner spinner1,spinner2;
    private ScrollView scrollview;
    private LinearLayout linearlayout;
    private RadioGroup rdgroupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtrar_servicio);
        txt1=(TextView)findViewById(R.id.txtmodifica1);
        txt2=(TextView)findViewById(R.id.txtmodifica2);
        spinner1=(Spinner)findViewById(R.id.spinner6);
        spinner2=(Spinner)findViewById(R.id.spinner7);
        scrollview=(ScrollView)findViewById(R.id.scrollView4);
        rdgroupbutton=(RadioGroup)findViewById(R.id.radioSex);


        txt1.setVisibility(View.INVISIBLE);
        txt2.setVisibility(View.INVISIBLE);
        spinner1.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);
        scrollview.setVisibility(View.INVISIBLE);


        if(Build.VERSION.SDK_INT>9 ){
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rdgroupbutton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                if(checkedId==R.id.radioButton){

                    spinner1.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.VISIBLE);
                    txt1.setText("Selecciona el Tipo de Servicio");
                    txt1.setVisibility(View.VISIBLE);
                    txt2.setText("Selecciona el Servicio");
                    txt2.setVisibility(View.VISIBLE);
                }
                if(checkedId==R.id.radioButton2){
                    spinner1.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.VISIBLE);
                    txt1.setText("Selecciona el Estado de Servicio");
                    txt1.setVisibility(View.VISIBLE);
                    txt2.setText("Selecciona el Servicio");
                    txt2.setVisibility(View.VISIBLE);
                }
                if(checkedId==R.id.radioButton3){
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                    spinner1.setVisibility(View.INVISIBLE);
                    spinner2.setVisibility(View.INVISIBLE);
                    scrollview.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}

