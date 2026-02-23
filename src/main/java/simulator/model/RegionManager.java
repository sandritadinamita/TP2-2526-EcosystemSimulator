package simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class RegionManager implements AnimalMapView{ //sin terminar
    private int mapWidth;
    private int mapHeight;
    private int cols;
    private int rows;
    private int regionWidth;
    private int regionHeight;
    private Map<Animal, Region> animalRegion;
    private Region[][] regions;

    public RegionManager(int cols, int rows, int width, int height){
        this.cols = cols;
        this.rows = rows;
        this.mapWidth = width;
        this.mapHeight = height;
        this.regionWidth = width/cols;
        this.regionHeight = height/rows;
        this.regions = new Region[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.regions[i][j] = new DefaultRegion();
            }
        }
        this.animalRegion = new HashMap<>();
    }

    void setRegion(int row, int col, Region r){ //preguntar si hay que borrar los animales de la region anterior
        List<Animal> animals = this.regions[row][col].getAnimals();
        for(Animal a : animals){
            r.addAnimal(a);
            this.animalRegion.put(a, r);
        }
    }
    void registerAnimal(Animal a){
        Vector2D pos = a.getPosition();

        Region r = this.region[pos.getY()]//no sabemos como sacar la region a partir de la x y la y del animal 
        r.addAnimal(a);
        this.animalRegion.put(a, r);
        a.init(this);
    

    }
    void unregisterAnimal(Animal a){
        this.animalRegion.get(a).removeAnimal(a);
        this.animalRegion.remove(a);
    }

    void updateAnimalRegion(Animal a){
         Vector2D pos = a.getPosition();

        Region r = this.region[pos.getY()]//no sabemos como sacar la region a partir de la x y la y del animal
        if(r != this.animalRegion.get(a)){
            r.addAnimal(a);
            this.animalRegion.get(a).removeAnimal(a);
            this.animalRegion.put(a, r);
        }
    }
    public double getFood(AnimalInfo a, double dt){
        return this.animalRegion.get(a).getFood(a, dt);

    }
    void updateAllRegions(double dt){
        for (int i = 0; i <rows; i ++){
            for (int j = 0; j<cols; j ++){
                this.regions[i][j].update(dt);
            }
        }
    }
    public List<Animal> getAnimalsInRange(Animal a, Predicate<Animal> filter){
        // lo hacemos cuando los filters mirar de sheep y wolf lo de campo de vision

    }
    public JSONObject asJSON(){
          	JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		for (int i = 0; i< rows; i ++ ) {
			for (int j = 0; j < cols; j ++){
				JSONObject reg = new JSONObject();
				reg.put("row", i);
				reg.put("col", j);
				reg.put("data", this.regions[i][j].asJSON());
				a.put(reg);
			}
		}
		o.put("regiones",a);
		
		return o;
	}

   // updatearlo 
   @Override
	public int getCols() {
		return cols;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getWidth() {
		return mapWidth;
	}

	@Override
	public int getHeight() {
		return mapHeight;
	}

	@Override
	public int getRegionWidth() {
		return regionWidth;
	}

	@Override
	public int getRegionHeight() {
		return regionHeight;
	}

}
