
import java.awt.*;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class Tray extends TrayIcon {
  GraphicPanel gp;

  public Tray(Main m, Image img) {
    super(img, "Battery app");
    setImageAutoSize(true);
    setToolTip("Battery Icon");
    addMouseListener(new MyMouseInputAdapter(m));
    gp = new GraphicPanel(m);
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
