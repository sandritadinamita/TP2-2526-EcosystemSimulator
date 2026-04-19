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
import simulator.model.Animal.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

  // TODO definir atributos necesarios
  private Map<RegionData, Map<Diet, Integer>> info; 
	private List<String> cols;
	private final Controller ctrl;

  RegionsTableModel(Controller ctrl) {
    // TODO inicializar estructuras de datos correspondientes
    // TODO registrar this como observador
    this.ctrl = ctrl;
		info = new HashMap<>();
		cols = new ArrayList<>();
		cols.add("Row");
    cols.add("Col");
    cols.add("Desc");
		for(Diet d: Diet.values()) {
			cols.add(d.toString());
		}
		ctrl.addObserver(this);
  }
  // TODO el resto de métodos van aquí…

  @Override
  public int getRowCount() {
    return info.size();
  }

  @Override
  public int getColumnCount() {
    return cols.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    List<RegionData> keys = new ArrayList<>(info.keySet()); 
		RegionData key = keys.get(rowIndex);
		if(columnIndex == 0){
      return key.row();
    }
    if(columnIndex == 1){
      return key.col();
    }
    if(columnIndex == 2){
      return key.r(); //están definidos los toString
    }
		else {
			Diet dieta = Diet.values()[columnIndex - 3];
      return info.get(key).get(dieta);
		}
  }

  public String getColumnName(int col) {
		return cols.get(col);
	}

  @Override
  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
    updateRegions(map);
  }

  @Override
  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
    updateRegions(map);
  }

  @Override
  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
    updateRegions(map);
  }

  @Override
  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
    updateRegions(map);
  }

  @Override
  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
    updateRegions(map);
  }

  private void updateRegions(MapInfo map) {
		info.clear();
		for(RegionData region: map) { //MapInfo es iterable
			Map<Diet, Integer> mapaDietas = new HashMap<>();
			for(Diet dieta: Diet.values()) {
				int valor = animalesConDieta(region.r().getAnimalsInfo(), dieta);
				mapaDietas.put(dieta, valor);
			}
			info.put(region, mapaDietas);
		}
		fireTableDataChanged();
	}

  private int animalesConDieta(List<AnimalInfo> animals, Diet dieta) {
		int cont = 0;
		for(AnimalInfo a: animals) {
			if(a.getDiet().equals(dieta)) {
				cont++;
			}
		}
		return cont;
	}
}
