import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class GraphicPanel extends JFrame {
  JPanel mainPanel;
  Main m;
  String[] values = new String[] { "Min batt", "Max batt", "Sec" };
  String[] defaultValues;
  List<JTextField> fields = new ArrayList<>();

  GraphicPanel(Main m) {
    this.m = m;
    mainPanel = new JPanel();

    defaultValues = new String[] { m.min + "", m.max + "", m.sec + "" };

    mainPanel.add(createLeftPanel1());
    mainPanel.add(createRightPanel());

    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    getContentPane().add(mainPanel);
    pack();
    setLocationRelativeTo(null);
    setVisible(false);
  }

  private JPanel createLeftPanel1() {
    JPanel leftPanel = new JPanel(new GridLayout(3, 2));
    for (int i = 0; i < 3; i++) {
      leftPanel.add(new JLabel(values[i]));
      fields.add(new JTextField(defaultValues[i]));
      leftPanel.add(fields.get(fields.size() - 1));
    }
    return leftPanel;
  }

  private JPanel createRightPanel() {
    JPanel rightPanel = new JPanel(new GridLayout(2, 1));
    var send = new JButton("Send");
    send.addActionListener(new SendActionListener(this));
    var quit = new JButton("Quit");
    quit.addActionListener(new QuitActionListener(this));
    rightPanel.add(send);
    rightPanel.add(quit);
    return rightPanel;
  }

  class QuitActionListener implements ActionListener {
    GraphicPanel gp;

    QuitActionListener(GraphicPanel gp) {
      this.gp = gp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      gp.dispose();
      m.getMyRunnable().interrupt();
      m.removeIcon();
    }
  }

  class SendActionListener implements ActionListener {
    GraphicPanel gp;

    SendActionListener(GraphicPanel gp) {
      this.gp = gp;
    }

    private int giveInt(int a, int b) {
      return a == -1 ? b : a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      int val = -1;
      for (int i = 0; i < values.length; i++) {
        try {
          val = Integer.parseInt(fields.get(i).getText());
        } catch (NumberFormatException ignored) {
        }
        if (values[i].toLowerCase().contains("min"))
          m.min = giveInt(val, m.min);
        else if (values[i].toLowerCase().contains("max"))
          m.max = giveInt(val, m.max);
        else if (values[i].toLowerCase().contains("sec"))
          m.sec = giveInt(val, m.sec);
      }
      m.getMyRunnable().interrupt();
      m.createNewRunnable();
      m.getMyRunnable().start();
      m.saveToFile();
      gp.setVisible(false);
    }
  }
}
