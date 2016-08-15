package com.n2t.kioskdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public class AppContext extends Application {

  private AppContext instance;
  private PowerManager.WakeLock wakeLock;
  private OnScreenOffReceiver onScreenOffReceiver;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }

  private void registerKioskModeScreenOffReceiver() {
    // register screen off receiver
    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    onScreenOffReceiver = new OnScreenOffReceiver();
    registerReceiver(onScreenOffReceiver, filter);
  }

  public PowerManager.WakeLock getWakeLock() {
    if(wakeLock == null) {
      // lazy loading: first call, create wakeLock via PowerManager.
      PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
      wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
    }
    return wakeLock;
  }

  public void startKioskService() {

    //register broadcast receiver screen off
    registerKioskModeScreenOffReceiver();

    //start service catch app is background
    System.out.println("start service...");
    startService(new Intent(this, KioskService.class));
    //set Kiosk is start
    PrefUtils.setKioskModeActive(true, this);

  }

  public void stopKioskService(){
    //
    unregisterReceiver(onScreenOffReceiver);
    //
    stopService(new Intent(this, KioskService.class));
  }

}