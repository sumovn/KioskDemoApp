package com.n2t.kioskdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    System.out.println("Hello! device boot completed");

    //stop test
    /*Intent myIntent = new Intent(context, MainActivity.class);
    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(myIntent);*/

  }
}