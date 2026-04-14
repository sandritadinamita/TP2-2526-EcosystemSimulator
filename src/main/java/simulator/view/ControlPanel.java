package simulator.view;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import simulator.control.Controller;
import simulator.launcher.Main;

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

    // Change Regions Button
    this.changeRegionsButton= new JButton();
    this.changeRegionsButton.setToolTipText("Change Regions");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.changeRegionsButton.setIcon(new ImageIcon("..."));
    this.changeRegionsButton.addActionListener((e) -> changeRegionsAction());
    this.toolBar.add(changeRegionsButton);

    //run button
    this.toolBar.addSeparator();
    this.runButton= new JButton();
    this.runButton.setToolTipText("Run");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.runButton.setIcon(new ImageIcon("..."));
    this.runButton.addActionListener((e) -> runAction());
    this.toolBar.add(runButton);

    //stop button
    this.stopButton= new JButton();
    this.stopButton.setToolTipText("Stop");
    // TODO cargar la imagen como un recurso usando el ClassLoader y NO usando una ruta absoluta o relativa
    this.stopButton.setIcon(new ImageIcon("..."));
    this.stopButton.addActionListener((e) -> stopAction());
    this.toolBar.add(stopButton);

    //steps spinner
    this.toolBar.add(new JLabel("Steps: "));
	  this.stepsSpinner = new JSpinner(new SpinnerNumberModel(10000, 1, 10000, 100));
	  this.stepsSpinner.setToolTipText("Simulation steps to run: 1-10000");
	  this.stepsSpinner.setMinimumSize(new Dimension(100, 30));
	  this.stepsSpinner.setMaximumSize(new Dimension(100, 30));
	  this.stepsSpinner.setPreferredSize(new Dimension(100, 30));
	  this.toolBar.add(stepsSpinner);

    //deltaTime textfield
    this.toolBar.add(new JLabel("Delta Time: "));
	  this.deltaTimeTextField = new JTextField();
	  this.deltaTimeTextField.setText(String.valueOf(Main.deltaTime));
	  this.deltaTimeTextField.setToolTipText("Real time (seconds) corresponding to a step");
	  this.deltaTimeTextField.setMinimumSize(new Dimension(100, 30));
	  this.deltaTimeTextField.setMaximumSize(new Dimension(100, 30));
	  this.deltaTimeTextField.setPreferredSize(new Dimension(100, 30));
	  this.toolBar.add(deltaTimeTextField);

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
    fc = new JFileChooser();
    this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

    // TODO Inicializar this.changeRegionsDialog con instancias del diálogo de cambio
    // de regiones
    this.changeRegionsDialog = new ChangeRegionsDialog(this.ctrl);

  }
  // el resto de métodos van aquí…

  private void openFileAction() {
    // TODO mostrar el JFileChooser (this.fc) y cargar el fichero seleccionado usando this.ctrl.load(file)
   int file = this.fc.showOpenDialog(ViewUtils.getWindow(this));
   // completar
  
  }

  private void mapViewAction() {
    new MapWindow(ViewUtils.getWindow(this) , ctrl);
  }
  private void changeRegionsAction() {
  this.changeRegionsDialog.open(ViewUtils.getWindow(this));
  }
  private void runAction() {
    actdesactButtons(false);
    this.stopped = false;
    int steps = (int)this.stepsSpinner.getValue(); //no me deja de otra forma sin hacer un cast
    double dt = Double.parseDouble(this.deltaTimeTextField.getText());
    runSim(steps, dt);
  }
  private void stopAction() {
    this.stopped = true;
  }

  private void runSim(int n, double dt) {
  if (n > 0 && !this.stopped) {
    try {
          this.ctrl.advance(dt);
          SwingUtilities.invokeLater(() -> runSim(n - 1, dt));
    } catch (Exception e) {
      // TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
      //      que corresponda
        ViewUtils.showErrorMsg(e.getMessage());
        // TODO activar todos los botones
        actdesactButtons(true);
        this.stopButton.setEnabled(true); 
        this.stopped = true;
      // TODO activar todos los botones
      actdesactButtons(true);
      this.stopButton.setEnabled(true); 
      this.stopped = true;
    }
  } else {
    // TODO activar todos los botones
    actdesactButtons(true);
    this.stopButton.setEnabled(true); 
    this.stopped = true;
  }
}
void actdesactButtons(boolean action) {
  this.runButton.setEnabled(action);
  this.openButton.setEnabled(action);
  this.viewButton.setEnabled(action);
  this.changeRegionsButton.setEnabled(action);
}