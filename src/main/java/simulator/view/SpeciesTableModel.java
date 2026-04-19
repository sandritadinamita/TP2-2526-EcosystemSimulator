package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.Animal;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
  private Map<String, SortedMap<Animal.State,Integer>> info; // ej. (sheep: {hunger:5, dead:1})
	private List<String> colsEstados;
	private final Controller ctrl;
  // TODO definir atributos necesarios

  SpeciesTableModel(Controller ctrl) {
    // TODO inicializar estructuras de datos correspondientes
    // TODO registrar this como observador
		this.ctrl = ctrl;
		info = new HashMap<>();
		colsEstados = new ArrayList<>();
		colsEstados.add("Species");
		for(Animal.State s: Animal.State.values()) {
			colsEstados.add(s.toString());
		}
		ctrl.addObserver(this);
	}
  // TODO el resto de métodos van aquí …

  @Override
  public int getRowCount() {
    return info.size();
  }

  @Override
  public int getColumnCount() {
    return colsEstados.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    List<String> keys = new ArrayList<>(info.keySet());
    String key = keys.get(rowIndex);
    if (columnIndex == 0) {
        return key;
    } 
    else {
        Animal.State estado = Animal.State.values()[columnIndex - 1];
        return info.get(key).get(estado);
    }
  }

  public String getColumnName(int col) {
		return colsEstados.get(col);
	}

  @Override
  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
    info = new HashMap<>();
		for(AnimalInfo a : animals) {
			addAnimal(a);
		}
  }

  @Override
  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
    info = new HashMap<>();
  }

  @Override
  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
    addAnimal(a);
  }

  @Override
  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}

  @Override
  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) { //en cada paso la tabla cambia, tiene que actualizarse
    info = new HashMap<>();
		for(AnimalInfo a : animals) {
			addAnimal(a);
		}
  }

  private void addAnimal(AnimalInfo a) {
		String key = a.getGeneticCode();
		if(info.containsKey(key)) {
			Animal.State estado = a.getState();
			Integer valor = info.get(key).get(estado);
			 info.get(key).put(estado, valor + 1);
		}
		else {
			SortedMap<Animal.State, Integer> nuevaInfo = new TreeMap<>();
			for(Animal.State estado : Animal.State.values()) {
				if(estado.equals(a.getState())) {
					nuevaInfo.put(estado, 1);
				}
				else nuevaInfo.put(estado, 0);
			}
			info.put(key, nuevaInfo);
		}
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}
}
