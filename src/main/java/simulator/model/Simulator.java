package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable {
    //añadir factoria animales y regiones
    private RegionManager regionMngr;
    private List<Animal> animals;
    private double tiempo;
    public Simulator(int cols, int rows, int width, int height,
        Factory<Animal> animalsFactory, Factory<Region> regionsFactory){
            this.tiempo = 0.0;
            this.regionMngr = new RegionManager(cols, rows, width, height);
            this.animals = new ArrayList<Animal>();
            //hay que inicializar las factorias 

    }

    private void setRegion(int row, int col, Region r){
        this.regionMngr.setRegion(row, col, r);
    }
    private void setRegion(int row, int col, JSONObject rJson){
        Region R = //mas adelante con las factorias 

        setRegion(row, col, R);

    }
    private void addAnimal(Animal a){
        this.animals.add(a);
        this.regionMngr.registerAnimal(a);
    }
    public void addAnimal(JSONObject aJson){
        Animal A = //mas adelante con las factorias 

        addAnimal(A);

    }
    public MapInfo getMapInfo(){
        return this.regionMngr;
    }
    public List<? extends AnimalInfo> getAnimals(){
        return Collections.unmodifiableList(this.animals);
    }
    public double getTime(){
        return this.tiempo;
    }
    public void advance(double dt){
        List<Animal> animalesMuertos = new ArrayList<>();
        tiempo += dt;
        for (Animal a: this.animals){
            if(a.getState() ==State.DEAD){
                animalesMuertos.add(a);
                a.update(dt);
                this.regionMngr.updateAnimalRegion(a);
            }
        }
        regionMngr.updateAllRegions(dt);
        for(Animal a: this.animals){
            if(a.isPregnant()){
                Animal animalBaby = a.deliverBaby();
                addAnimal(animalBaby);
            }

        }
        for(Animal a: animalesMuertos){
            animals.remove(a);
            this.regionMngr.unregisterAnimal(a);
        }

    }
    public JSONObject asJSON(){
         JSONObject o = new JSONObject();
		o.put("time", tiempo);
		o.put("state", regionMngr.asJSON());
		return o;
    }
}
