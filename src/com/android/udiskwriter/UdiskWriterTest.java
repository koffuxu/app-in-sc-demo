/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.udiskwriter;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.util.Log;
import android.os.Environment;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.content.Intent;

//java lib
import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class UdiskWriterTest extends Activity {
    static final String TAG = "KoffuKit";
    static final String filename = "testfile_0201_kf";
    static final String filecontent= "test content more...";
    //get the external sdcard path
    String exStorageState = Environment.getExternalStorageState();
    String exStorage = Environment.getExternalStorageDirectory().toString();

    //the *real external* sdcard path
    //String SD_PATH = "/storage/external_storage/sdcard1";
    String SD_PATH = "/storage/DEB6-8C90";
    //the *real external* udisk path
    String USB_PATH ="/storage/external_storage/udisk0";
    
    //Button
    private Button btEnable = null;
    private Button btDisable = null;
    private Button btState = null;
    private Button bt4 = null;
    private WifiManager wifiManager = null;

    @Override
    protected void onCreate(Bundle icycle) {
        super.onCreate(icycle);
        // load layout xml file
        setContentView(R.layout.activity_main);
        
        bt4 = (Button)findViewById(R.id.bt4);

        Log.e(TAG, "the external storage state is " + exStorageState);
        Log.e(TAG, "the external storage is " + exStorage);

        /*
         * Wifi Test 
         */
        wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
       btEnable = (Button) findViewById(R.id.bt1);
       btEnable.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v){
              wifiManager.setWifiEnabled(true);
              Toast.makeText(UdiskWriterTest.this, "Wifi already open", Toast.LENGTH_SHORT).show();
           }
       });

       btDisable = (Button) findViewById(R.id.bt2);
       btDisable.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v){
              wifiManager.setWifiEnabled(false);
              Toast.makeText(UdiskWriterTest.this, "Wifi already Disabble", Toast.LENGTH_SHORT).show();
           }
       });
       
       btState= (Button) findViewById(R.id.bt3);
       btState.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v){
             // wifiManager.setWifiEnabled(false);
              Toast.makeText(UdiskWriterTest.this, "Wifi current state:"+wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();
           }
       });


    }

    /*
     * Bitmap Test
     */
    public void onClickForBitmap(View view) {
        Toast.makeText(UdiskWriterTest.this, "Click Button Bitmap Test", Toast.LENGTH_SHORT).show();

    }
    
    public void onClickForTestUsb(View view) {
        Toast.makeText(UdiskWriterTest.this, "Click Button USB Test", Toast.LENGTH_SHORT).show();

        //write filename in /data/data/com.android.udiskwriter/files/testfile_0201_kf 
        //content is:test content
        //no need permission
        doInterlWriteTest();

        //storage/emulated/0 means sdcard0
        doExternalWriteTest();

        doRealExtWriteTest();
        
    }

    /*
     * for test copy Logs File
     */
    public void onClickCopyFiles(View view) {
        Intent i = new Intent(UdiskWriterTest.this, CopyLogActivity.class);
        startActivity(i);

    }

    private void doInterlWriteTest() {
        try {
            FileOutputStream os = openFileOutput(filename, Context.MODE_PRIVATE);
            os.write(filecontent.getBytes());
            os.close();
            Log.d(TAG, "do Interl write succsful!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void doExternalWriteTest(){
        File file = new File(exStorage, filename);
        if(exStorageState.equals(Environment.MEDIA_MOUNTED)) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = filecontent.getBytes();
                fos.write(buffer);
                fos.close();
                Log.d(TAG, "do External write succsful!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private void doRealExtWriteTest() {
        File file = new File(SD_PATH, "testFile_usb_1");
        Log.e(TAG,"file attr:"+file.canWrite() + "; " + file.canRead());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = filecontent.getBytes();
           // for(int i=0; i<10000; i++) {
              //  for (int j=0; i<1000; j++) {
                    fos.write(buffer);
             //   }

            //}
            fos.close();
            Log.e(TAG, "do U disk write succsful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
