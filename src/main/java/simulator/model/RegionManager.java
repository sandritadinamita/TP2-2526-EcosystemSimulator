package simulator.model;

import java.util.ArrayList;
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

    void setRegion(int row, int col, Region r){ 
        List<Animal> animals = this.regions[row][col].getAnimals();
        for(Animal a : animals){
            r.addAnimal(a);
            this.animalRegion.put(a, r);
        }
    }
    void registerAnimal(Animal a){
        Vector2D pos = a.getPosition();
        int row = (int) Math.floor(pos.getY()/mapHeight);
        int col = (int) Math.floor(pos.getX()/mapWidth);
        Region r = this.regions[row][col];
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
        int row = (int) Math.floor(pos.getY()/mapHeight);
        int col = (int) Math.floor(pos.getX()/mapWidth);
        Region r = this.regions[row][col];
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
//recorrer solo las regiones en el campo de vision-> no tiene que estar la region completa en el campo visual
        double x = a.getPosition().getX();
        double y = a.getPosition().getY();
        double sRange = a.getSightRange();
        int minRow = (int) Math.floor((y-sRange)/mapHeight);
        int maxRow = (int) Math.floor((y+sRange)/mapHeight);
        int minCol = (int) Math.floor((x-sRange)/mapWidth);
        int maxCol = (int) Math.floor((x+sRange)/mapWidth); 

        List<Animal> animals = new ArrayList<>();
        for (int i= minRow; i <= maxRow; i++){
            for(int j = minCol; j <= maxCol; j++){
                    for(Animal an : this.regions[i][j].getAnimals()){
                        if(filter.test(an) && an.getPosition().distanceTo(a.getPosition()) <= a.getSightRange()){
                            animals.add(an);
                        }
                    }
                }
            }
        return animals;
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
