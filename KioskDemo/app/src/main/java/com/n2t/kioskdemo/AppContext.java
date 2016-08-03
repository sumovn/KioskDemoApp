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
    registerKioskModeScreenOffReceiver();
  }

  private void registerKioskModeScreenOffReceiver() {
    // register screen off receiver
    final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    onScreenOffReceiver = new OnScreenOffReceiver();
    registerReceiver(onScreenOffReceiver, filter);

    startKioskService();  // add this
  }

  public PowerManager.WakeLock getWakeLock() {
    if(wakeLock == null) {
      // lazy loading: first call, create wakeLock via PowerManager.
      PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
      wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
    }
    return wakeLock;
  }

  private void startKioskService() {
    // ... and this method
    System.out.println("start service...");
    startService(new Intent(this, KioskService.class));
    //set Kiosk is start
    PrefUtils.setKioskModeActive(true, this);
  }

  public void stopKioskService(){
    stopService(new Intent(this, KioskService.class));
  }

}