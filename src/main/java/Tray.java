
import java.awt.*;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class Tray extends TrayIcon {
  GraphicPanel gp;
  Main m;

  public Tray(Main m, Image img) {
    super(img, "Battery app");
    setImageAutoSize(true);
    setToolTip("Max : " + m.max + "\nMin : " + m.min + "\nDelay : " + m.sec);
    addMouseListener(new MyMouseInputAdapter(m));
    gp = new GraphicPanel(m);
    this.m = m;
  }

  public void setToolTip() {
    var m = this.m;
    super.setToolTip("Max : " + m.max + "\nMin : " + m.min + "\nRep : " + m.sec);
  }

  public void sendMsg(String... msg) {
    displayMessage(msg[0], msg[1], MessageType.WARNING);
  }

  class MyMouseInputAdapter extends MouseInputAdapter {
    Main m;

    MyMouseInputAdapter(Main m) {
      this.m = m;
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
      if (SwingUtilities.isRightMouseButton(e))
        gp.setVisible(true);
      else {
        m.getMyRunnable().interrupt();
        m.removeIcon();
        gp.dispose();
      }
    }
  }

}
