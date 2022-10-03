import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyRunnable implements Runnable {
  private static final String commandForPercentage = "WMIC PATH Win32_Battery Get EstimatedChargeRemaining";
  private static final String commandForPluggedIn = "WMIC Path Win32_Battery Get BatteryStatus";

  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean stopped = new AtomicBoolean(false);
  private Thread worker;
  private final Main m;

  public MyRunnable(Main m) {
    this.m = m;
  }

  public void start() {
    worker = new Thread(this);
    worker.start();
  }

  public void stop() {
    running.set(false);
  }

  public void interrupt() {
    running.set(false);
    worker.interrupt();
  }

  boolean isRunning() {
    return running.get();
  }

  boolean isStopped() {
    return stopped.get();
  }

  public void run() {
    running.set(true);
    stopped.set(false);
    while (running.get()) {
      int perc = getPercentage();
      boolean isCharging = isCharging();
      if (perc < m.min && !isCharging)
        m.trayIcon.sendMsg("Battery is at " + perc, "Plug the charger");
      else if (perc > m.max && isCharging)
        m.trayIcon.sendMsg("Battery is at " + perc, "\nUnplug the charger");
      try {
        Thread.sleep(m.sec * 1000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    stopped.set(true);
    // System.out.println("Stopped");
  }

  private int getPercentage() {
    return Integer.parseInt(runCommand(commandForPercentage).get(2).trim());
  }

  private boolean isCharging() {
    return Integer.parseInt(runCommand(commandForPluggedIn).get(2).trim()) == 2;
  }

  private List<String> runCommand(String cmd) {
    List<String> mem = new ArrayList<>();
    Process p;
    try {
      p = Runtime.getRuntime().exec(cmd);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String s;
      while ((s = bufferedReader.readLine()) != null) {
        mem.add(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mem;
  }

}
