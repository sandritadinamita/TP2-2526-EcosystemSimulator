package simulator.view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;


public class InfoTable extends JPanel {

  private String title;
  private TableModel tableModel;

  InfoTable(String title, TableModel tableModel) {
    this.title = title;
    this.tableModel = tableModel;
    initGUI();
  }

  private void initGUI() {
    // TODO cambiar el layout del panel a BorderLayout() (hecho)
    this.setLayout(new BorderLayout());
    // TODO añadir un borde con título al JPanel, con el texto this.titl (hecho)
    this.setBorder(BorderFactory.createTitledBorder(this.title));
    // TODO añadir un JTable (con barra de desplazamiento vertical) que use
    //      this.tableModel (hecho)
    JTable table = new JTable(this.tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.CENTER);
  }
}