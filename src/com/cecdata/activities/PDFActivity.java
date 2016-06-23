package com.cecdata.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.cecdata.cordova.openPDF.FakeR;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFActivity extends Activity {

  public static final String TAG = "@@@PDFActivity###";
  private PDFView pdfView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FakeR fakeR = new FakeR(this);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(fakeR.getId("layout", "activity_pdf"));
    pdfView = (PDFView) findViewById(fakeR.getId("id", "pdfview"));
    String pdfUrl = (String) getIntent().getExtras().get("url");
    Log.i(TAG, "pdfUrl:" + pdfUrl);
    String filename;
    if (pdfUrl != null && pdfUrl.length() > 0) {
      filename = pdfUrl.substring(pdfUrl.lastIndexOf("/")+1);
      String dirPath = getApplicationContext().getFilesDir().getAbsolutePath();
      File file = new File(dirPath+"/"+filename);
      if (file.exists()) {
        Log.i(TAG, "已下载,读本地文件");
        showPDF(file);
      } else {
        Log.i(TAG, "未下载，先下载到本地在读取pdf");
        new ShowRemotePDFTask(this).execute(pdfUrl);
      }
    }else {
      Toast.makeText(this, "无法获取传递的url地址", Toast.LENGTH_SHORT).show();
    }
  }

  class ShowRemotePDFTask extends AsyncTask<String, Integer, String> {
    ProgressDialog pdialog;
    public ShowRemotePDFTask(Context context){
      pdialog = new ProgressDialog(context, 0);
      pdialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int i) {
          dialog.cancel();
        }
      });
      pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
          finish();
        }
      });
      pdialog.setCancelable(true);
      pdialog.setMax(100);
      pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      pdialog.show();
    }
    @Override
    protected String doInBackground(String... params) {
      String pdfUrl = params[0];
      URL url = null;
      HttpURLConnection conn = null;
      InputStream inputStream = null;
      OutputStream outputStream = null;
      String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/")+1);
      try {
        url = new URL(pdfUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setConnectTimeout(20 * 1000);
        inputStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
          outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
          byte[] data = new byte[1024];
          int seg = 0;
          long total = conn.getContentLength();
          long current = 0;
          while (!isCancelled()&& (seg = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, seg);
            current += seg;
            int progress = (int) ((float) current / total * 100);
            publishProgress(progress);// 通知进度条UI更新
          }
        }else{
          Log.i(TAG, "网络错误异常!");
          filename = null;
        }
      } catch (Exception e) {
        e.printStackTrace();
        filename = null;
      } finally {
        try{
          if (conn != null) {
            conn.disconnect();
          }
          if (inputStream != null) {
            inputStream.close();
          }
          if (outputStream != null) {
            outputStream.close();
          }
        }catch (Exception e){
          e.printStackTrace();
        }
      }
      return filename;
    }

    @Override
    protected void onCancelled() {
      super.onCancelled();
    }

    @Override
    protected void onPostExecute(String fileName) {
      if(fileName!=null){
        String dirPath = getApplicationContext().getFilesDir().getAbsolutePath();
        showPDF(new File(dirPath + "/" + fileName));
      }
      pdialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      pdialog.setProgress(values[0]);
    }

  }

  private void showPDF(File file) {
    pdfView.fromFile(file)
      .defaultPage(1)
      .showMinimap(false)
      .enableSwipe(true)
      .load();
  }
}
