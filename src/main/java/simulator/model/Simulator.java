package simulator.model;

import java.util.List;

import org.json.JSONObject;

public class Simulator implements JSONable {
    private double tiempo;
    public Simulator(int cols, int rows, int width, int height,
        Factory<Animal> animalsFactory, Factory<Region> regionsFactory){
            this.tiempo = 0.0;

    }

    private setRegion(int row, int col, Region r){

    }
    void setRegion(int row, int col, JSONObject rJson){

    }
    private void addAnimal(Animal a){

    }
    public void addAnimal(JSONObject aJson){

    }
    public MapInfo getMapInfo(){

    }
    public List<? extends AnimalInfo> getAnimals(){

    }
    public double getTime(){
        return this.tiempo;
    }
    public void advance(double dt){

    }
    public JSONObject asJSON(){
          {
   "time": t,
   "state": s
  }
    }
}
