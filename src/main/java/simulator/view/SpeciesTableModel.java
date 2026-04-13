package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.State;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
  
  private Map<String, SortedMap<State,Integer>> data;
	private List<String> cols;
	private final Controller ctrl;
  // TODO definir atributos necesarios

  SpeciesTableModel(Controller ctrl) {
    // TODO inicializar estructuras de datos correspondientes
    // TODO registrar this como observador
		this.ctrl = ctrl;
		data = new HashMap<>();
		cols = new ArrayList<>();
		cols.add("Species");
		for(State s: State.values()) {
			cols.add(s.toString());
		}
		ctrl.addObserver(this);
	}
  // TODO el resto de métodos van aquí …

  @Override
  public int getRowCount() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRowCount'");
  }

  @Override
  public int getColumnCount() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getColumnCount'");
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getValueAt'");
  }

  @Override
  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onRegister'");
  }

  @Override
  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onReset'");
  }

  @Override
  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onAnimalAdded'");
  }

  @Override
  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onRegionSet'");
  }

  @Override
  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onAdvance'");
  }
}
