package com.example.kimja.a10th_homework;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Mycanvas mycanvas;
    CheckBox Stamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkpermission();
        init();
    }
    public void init(){
        mycanvas = (Mycanvas)findViewById(R.id.Mycanvas);
        Stamp =(CheckBox)findViewById(R.id.Stamp);

        Stamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mycanvas.setDrawmode("Stamp");
                }
                else {
                    mycanvas.setDrawmode("Pen");
                }
            }
        });
    }
    void checkpermission(){
        int permissioninfo = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){ //권한이 이미 허용 됬는지 확인
            Toast.makeText(getApplicationContext(),
                    "SD Card 쓰기 권한 있음",Toast.LENGTH_SHORT).show();
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),
                        "권한이 없으면 외부메모리를 사용할 수 없습니다.",Toast.LENGTH_SHORT).show();
                //한번 거부하면 실행하는 부분.
                //권한 재요청
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if (requestCode == 100){
            if(grantResults.length >0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                str = "SD Card 쓰기권한 승인";
            else str = "SD Card 쓰기권한 거부";
            Toast.makeText(this, str
                    ,Toast.LENGTH_SHORT).show();
        }
    }
    public String getExternalPath(){  //ppt 부연설명 // 외부 메모리 파일처리를 위해 꼭 만들어야 함
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)) {
            sdPath =
                    Environment.getExternalStorageDirectory ().getAbsolutePath() + "/";
//sdPath = "/mnt/sdcard/";
        }else
            sdPath = getFilesDir() + "";
//        Toast.makeText(getApplicationContext(),
//                sdPath, Toast.LENGTH_SHORT).show();
        return sdPath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Bluring :
                if(item.isChecked()) {
                    mycanvas.onblur ="OFF";
                    item.setChecked(false);
                } else {
                    mycanvas.onblur = "ON";
                    item.setChecked(true);
                }
                break;
            case R.id.Coloring :
                if(item.isChecked()) {
                    mycanvas.oncolor = "OFF";
                    item.setChecked(false);
                } else {
                    mycanvas.oncolor = "ON";
                    item.setChecked(true);
                }
                break;
            case R.id.Penbig :
                if(item.isChecked()){
                    mycanvas.pensize = 3;
                    item.setChecked(false);
                } else {
                    mycanvas.pensize = 5;
                    item.setChecked(true);
                }
                break;
            case R.id.Penred :
                if(mycanvas.pencolor.equals("RED")){
                    mycanvas.pencolor = "BLACK";
                } else {
                    mycanvas.pencolor = "RED";
                }
                break;
            case R.id.Penblue :
                if(mycanvas.pencolor.equals("BLUE")){
                    mycanvas.pencolor = "BLACK";
                } else {
                    mycanvas.pencolor = "BLUE";
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void onCick (View v){
        if(v.getId() == R.id.ERASER){
            mycanvas.clear();
        }
        else if (v.getId() == R.id.OPEN) { //파일 읽기
            mycanvas.Read(getExternalPath()+"/sample.jpg");
        }
        else if(v.getId() == R.id.SAVE) {
            mycanvas.Save(getExternalPath()+"/sample.jpg");
        }
        else if(v.getId() == R.id.ROTATE) {
            Stamp.setChecked(true);
            mycanvas.rotate +=1;
        }
        else if(v.getId() == R.id.MOVE) {
            mycanvas.move +=1;
            Stamp.setChecked(true);
        }
        else if(v.getId() == R.id.SCALE) {
            Stamp.setChecked(true);
            if(mycanvas.onscale.equals("ON")){
                mycanvas.onscale = "OFF";
            } else {
                mycanvas.onscale = "ON";
            }
        }
        else if(v.getId() == R.id.SKEW) {
            Stamp.setChecked(true);
            if(mycanvas.onskew.equals("onprogress")){
                mycanvas.onskew = "OFF";
            } else if(mycanvas.onskew.equals("onprogress2")){
                mycanvas.onskew = "ON";
            }

        }
    }



}
