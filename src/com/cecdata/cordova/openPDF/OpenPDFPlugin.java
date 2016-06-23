package com.cecdata.cordova.openPDF;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.cecdata.activities.PDFActivity;

public class OpenPDFPlugin extends CordovaPlugin {

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("openWithUrl")) {
      JSONObject json = args.optJSONObject(0);
      String url = json.optString("url");
      Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), PDFActivity.class);
      intent.putExtra("url", url);
      this.cordova.getActivity().startActivity(intent);
      this.cordova.getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    return true;
  }

}
