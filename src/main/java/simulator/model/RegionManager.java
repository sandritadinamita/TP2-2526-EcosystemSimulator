package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class RegionManager implements AnimalMapView{ //sin terminar
    private int width;
    private int height;
    private int cols;
    private int rows;
    private Map<Animal, Region> animalRegion;

    public RegionManager(int cols, int rows, int width, int height){
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
        //calcular la anchura y altura de una celda ( dividir anchura/altura total por el número de columnas/filas) y almacenarlo en los atributos correspondientes. Además, debe inicializar la matriz regions con regiones de tipo DefaultRegion (usando la constructora por defecto) e inicializar animalRegion con una estructura de datos adecuada.
    }

    void setRegion(int row, int col, Region r){

    }
    void registerAnimal(Animal a){

    }
    void unregisterAnimal(Animal a){

    }
    void updateanimalRegion(Animal a){

    }
    public double getFood(AnimalInfo a, double dt){

    }
    void updateAllRegions(double dt){

    }
    public List<Animal> getAnimalsInRange(Animal a, Predicate<Animal> filter){

    }
    public JSONObject asJSON(){
          {
    "regions": [o1,o2,...]
  }
   {
   "row": i,
   "col": j,
   "data": r
}
    }
}
