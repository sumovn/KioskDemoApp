package com.n2t.kioskdemo;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KioskService extends Service {

  private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 2 seconds

  private static KioskService instance;
  private Thread t = null;
  private Context ctx = null;
  private boolean running = false;

  public static KioskService getInstance(){
    return instance;
  }

  @Override
  public void onDestroy() {
    System.out.println("Stopping service 'KioskService'");
    running =false;
    PrefUtils.setKioskModeActive(false, this);
    instance = null;
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    System.out.println("Starting service 'KioskService'");
    running = true;
    ctx = this;
    instance = this;

    // start a thread that periodically checks if your app is in the foreground
    t = new Thread(new Runnable() {
      @Override
      public void run() {
        do {
          //System.out.println("check visible...");
          handleKioskMode();
          try {
            Thread.sleep(INTERVAL);
          } catch (InterruptedException e) {
            System.out.println("Thread interrupted: 'KioskService'");
          }
        }while(running);
        stopSelf();
      }
    });

    t.start();
    return Service.START_NOT_STICKY;
  }

  private void handleKioskMode() {
    // is Kiosk Mode active? 
    if(PrefUtils.isKioskModeActive(ctx)) {
      // is App in background?
      if(isInBackground()) {
        restoreApp(); // restore!
      }
    }
  }

  private boolean isInBackground() {
    ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
    ComponentName componentInfo = taskInfo.get(0).topActivity;
    return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));
  }

  public void restoreApp() {
    System.out.println("...in background...restore");
    // Restart activity
    Intent i = new Intent(ctx, MainActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(i);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}