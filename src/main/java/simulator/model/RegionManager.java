package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class RegionManager implements AnimalMapView{ 
    private int mapWidth;
    private int mapHeight;
    private int cols;
    private int rows;
    private int regionWidth;
    private int regionHeight;
    private Map<Animal, Region> animalRegion;
    private Region[][] regions;

    public RegionManager(int cols, int rows, int width, int height){
        if (cols <= 0 || rows <= 0 || width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid map dimensions");
        this.cols = cols;
        this.rows = rows;
        this.mapWidth = width;
        this.mapHeight = height;
        this.regionWidth = width/cols;
        this.regionHeight = height/rows;
        if (regionWidth <= 0 || regionHeight <= 0)
            throw new IllegalStateException("Invalid region size");
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
        regions[row][col] = r;
    }

    private Region getRegionFromPosition(Vector2D pos){
        int row = (int) Math.floor(pos.getY()/regionHeight);
        int col = (int) Math.floor(pos.getX()/regionWidth);
        return this.regions[row][col];
    }

    void registerAnimal(Animal a){
        if (a == null) throw new IllegalArgumentException("Animal is null");
        a.init(this);
        Region r = getRegionFromPosition(a.getPosition());
        r.addAnimal(a);
        this.animalRegion.put(a, r);
    }
    void unregisterAnimal(Animal a){
        this.animalRegion.get(a).removeAnimal(a);
        this.animalRegion.remove(a);
    }

    void updateAnimalRegion(Animal a){
        Region r = getRegionFromPosition(a.getPosition());
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
        double x = a.getPosition().getX();
        double y = a.getPosition().getY();
        double sRange = a.getSightRange();
        int minRow = (int) Math.floor((y-sRange)/regionHeight); 
        int maxRow = (int) Math.floor((y+sRange)/regionHeight);
        int minCol = (int) Math.floor((x-sRange)/regionWidth);
        int maxCol = (int) Math.floor((x+sRange)/regionWidth);
        minCol = Math.max(0, minCol);
        minRow = Math.max(0, minRow);
        maxCol = Math.min(cols - 1, maxCol);
        maxRow = Math.min(rows - 1, maxRow);

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
		o.put("regions", a);
		
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

    @Override
    public Iterator<RegionData> iterator() {
        return new Iterator<RegionData>(){
        private int contCol = -1;
		private int contRow = 0;

            @Override
            public boolean hasNext() {
                if(contCol == cols - 1 && contRow < rows - 1) return true;
				else if(contCol < cols - 1) return true;
				else return false;
            }

            @Override
            public RegionData next() {
                contCol++;
				if(contCol == cols) {
					contCol = 0;
					contRow++;
				}
				RegionData data = new RegionData(contRow, contCol, regions[contRow][contCol]);
				return data;
            }

        };
    }

}
