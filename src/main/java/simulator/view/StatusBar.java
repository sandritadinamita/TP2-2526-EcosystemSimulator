package simulator.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import java.awt.FlowLayout;

class StatusBar extends JPanel implements EcoSysObserver {
  JLabel time;
	JLabel animals;
	JLabel dimensiones;
  private double currentTime;
  private int numAnimals;
  private int rows;
  private int cols;
  private int height;
  private int width;


  StatusBar(Controller ctrl) { // mirar que controller hay q importar xq yo siempre importo el del control pero ns si es
    initGUI();
    ctrl.addObserver(this);
  }

  private void initGUI() {
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.setBorder(BorderFactory.createBevelBorder(1));
    time = new JLabel("Time: 0.0");
    animals = new JLabel("Animals: 0");
    dimensiones = new JLabel("Dimensions: 0x0");
    this.add(time);
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(10, 20));
        this.add(s);
    this.add(animals);
        JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
        s2.setPreferredSize(new Dimension(10, 20));
        this.add(s2);
    this.add(dimensiones);
  }

  @Override
  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
    currentTime = time;
    cols = map.getCols();
    rows = map.getRows();
    height = map.getHeight();
    width = map.getWidth();
    numAnimals = animals.size();
    updateLabels();
  }

  @Override
  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
    onRegister(time, map, animals);
  }

  @Override
  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
    numAnimals = animals.size();
    updateLabels();
  }

  @Override
  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
    cols = col;
    rows = row;
    height = map.getHeight();
    width = map.getWidth();
    updateLabels();
  }

  @Override
  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
    currentTime = time;
    numAnimals = animals.size();
    updateLabels();
  }

  // TODO el resto de métodos van aquí…
  void updateLabels() {
    time.setText("Time: " + currentTime);
    animals.setText("Animals: " + numAnimals); 
    dimensiones.setText("Dimensions: " + height + "x" + width+ " " + rows + "x" + cols); 
  }
}
