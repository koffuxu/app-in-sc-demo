package com.android.udiskwriter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/11/17.
 */
public class CopyLogActivity extends Activity {
    static final String TAG = "koffuxu";
    private TextView tv1 = null;
    private Button bt6 = null;
    private ProgressBar pb = null;
    private String srcPath = "/data/Logs/";
    private String desPath = "/storage/DEB6-8C90/";

    private String prefix = null;
    private String desFolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_log);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        prefix = sdf.format(new Date());
        desFolder = desPath + "LETVLOGS-" + prefix + "/";

        Log.d(TAG, "CopyLogActivity onCreate desPath=" + desFolder);

        //copyLogFiles(srcPath, desPath+"/00/");

        tv1 = (TextView) findViewById(R.id.tv1);
        bt6 = (Button) findViewById(R.id.button6);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask mAsyncTask = new MyAsyncTask(tv1, pb);
                //mAsyncTask.execute(10, 20);
                mAsyncTask.execute(srcPath, desFolder);
                
            }
        });
    }



    //第一个参数Integer: 来自UI线程execute()的参数,也是doInBackground的参数
    //第二个参数Integer: 这是onProgressUpdate()的参数
    //第三个参数String: 这是doInBackground()的参数值

    class MyAsyncTask extends AsyncTask<String, Integer , Integer> {

        private TextView mTextView = null;
        private ProgressBar mProgressBar = null;

        //使用构造函数把MainActivity的参数传进来
        public MyAsyncTask(TextView mTextView, ProgressBar mProgressBar) {
            this. mTextView = mTextView;
            this. mProgressBar = mProgressBar;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "AsyncTask Starting...");
            mTextView.setText("AsyncTask Starting...");
            Toast.makeText(CopyLogActivity.this,"Start Copy LETV Logs, Do Not Return Please!", Toast.LENGTH_LONG).show();
        }


        //run as a other thread
        @Override
        protected Integer doInBackground(String... params) {
            Log.d(TAG, "doInBackground");
            int result = 0;
            /*int i = 0;
            for(i=0; i<=100; i+=10){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Thread Sleep Error");
                    e.printStackTrace();
                }
            }
            //execute()有几个参数, params数组就有几个元素
            return i + params[0].intValue() + params[1].intValue() +"";
            */
            String source = params[0];
            String destination = params[1];
            /*if(source==null || "".equals(souce) || destination == null || "".equals(destination)) {
                result = -1;
            }*/
            result = copyLogFiles(source, destination);
            return result;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "onProgressUpdate");
            int value = values[0];
            //TOBEFIX
            //mProgressBar.setProgress(value);
        }

        //参数来自doInBackground的返回值
        //result = -1;Source Folder Not Found
        //result = -2;Cannot access Source Folder Files, Maybe is Permission Issue
        //result = 0;Sucessful
        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "onPostExecute");
            mTextView.setText("AsyncTask Copy Logs Finished, result is:"+result);
            Toast.makeText(CopyLogActivity.this,"Operate finished:"+result, Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }

    //Copy File Function
    public int copyLogFiles(String src, String dst) {
        int ret = 0;
        int len, j;
        File[] currentFiles;
        File root = new File(src);
        if(!root.exists())
        {
            return -1;
        }
        currentFiles = root.listFiles();
        if(currentFiles != null){
            len = currentFiles.length;
            Log.d(TAG, "summary num is:"+len);
            for(j=0; j<len; j++){
            //    Log.d(TAG, "File is:"+currentFiles[j].getName());
            }
        } else {
            Log.d(TAG, "currentFiles is null");
            //return -2;
        }

        File targetDir = new File(dst);
        if(!targetDir.exists()){
            Log.d(TAG, "make target dir"+targetDir);
            targetDir.mkdirs();
        
        }
        for(int i=0; i<currentFiles.length; i++){
            if(currentFiles[i].isDirectory()){
                ret =copyLogFiles(currentFiles[i].getPath()+"/", dst + "/" + currentFiles[i].getName());
            } else {
                ret =copyFile(currentFiles[i].getPath(), targetDir + "/" +currentFiles[i].getName());
            }
        }
        //Toast.makeText(CopyLogActivity.this, "Copy Log Finished!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Copy Log Finished!");
        return ret;
    }

    public int copyFile(String fromFile, String toFile) {
        Log.d(TAG, "write file:"+fromFile + " ---to---> " +toFile);
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024 * 4];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosto.flush();
            fosfrom.close();
            fosto.close();
            Log.d(TAG, "wite successful:"+fromFile);
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }


}
