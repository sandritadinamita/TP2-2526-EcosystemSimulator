package simulator.view;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

import simulator.control.Controller;

class ControlPanel extends JPanel {

  private Controller ctrl;
  private ChangeRegionsDialog changeRegionsDialog;

  private JToolBar toolBar;
  private JFileChooser fc;
  private boolean stopped = true; // utilizado en los botones de run/stop
  private JButton quitButton; //apagado
  private JButton runButton; //flecha
  private JButton changeRegionsButton; // el boton del mundo 
  private JButton viewButton; // el del mapa
  private JButton openButton; // el de la carpeta
  private JButton stopButton; // el de parar
  private JTextField deltaTimeTextField; // el campo de texto para el deltaTime
  private JSpinner stepsSpinner; // el spinner para los pasos a ejecutar

  // TODO añade más atributos aquí … (hecho creo)

  ControlPanel(Controller ctrl) {
    this.ctrl = ctrl;
    initGUI();
  }


  private void initGUI() {
    setLayout(new BorderLayout());
    toolBar = new JToolBar();
    add(toolBar, BorderLayout.PAGE_START);

    // TODO crear los diferentes botones/atributos y añadirlos a la toolBar.
    //      Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
    //      this.toolaBar.addSeparator() para añadir la línea de separación vertical
    //      entre las componentes que lo necesiten.

    // Open Button

    this.openButton = new JButton();
    this.openButton.setToolTipText("OpenFile");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.openButton.setIcon(new ImageIcon("..."));
    this.openButton.addActionListener((e) -> openFileAction());
    this.toolBar.add(openButton);

    //Viewer Button
    this.toolBar.addSeparator();
    this.viewButton = new JButton();
    this.viewButton.setToolTipText("View");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.viewButton.setIcon(new ImageIcon("..."));
    this.viewButton.addActionListener((e) -> mapViewAction());
    this.toolBar.add(viewButton);

    // Quit Button
    this.toolBar.add(Box.createGlue()); // this aligns the button to the right
    this.toolBar.addSeparator();
    this.quitButton = new JButton();
    this.quitButton.setToolTipText("Quit");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.quitButton.setIcon(new ImageIcon("..."));
    this.quitButton.addActionListener((e) -> ViewUtils.quit(this));
    this.toolBar.add(quitButton);

    // TODO Inicializar this.fc con una instancia de JFileChooser. Para que siempre
    // abre en la carpeta de ejemplos puedes usar:
    //
    //   this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

    // TODO Inicializar this.changeRegionsDialog con instancias del diálogo de cambio
    // de regiones

  }
  // TODO el resto de métodos van aquí…
}