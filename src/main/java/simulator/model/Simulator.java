package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver> {
    private Factory<Animal> factoriaAnimales;
    private Factory<Region> factoriaRegiones;
    private RegionManager regionMngr;
    private List<Animal> animals;
    private double tiempo;
    private List<EcoSysObserver> observadores;

    public Simulator(int cols, int rows, int width, int height,
        Factory<Animal> animalsFactory, Factory<Region> regionsFactory){
            this.tiempo = 0.0;
            this.regionMngr = new RegionManager(cols, rows, width, height);
            this.animals = new ArrayList<Animal>();
            this.observadores = new ArrayList<EcoSysObserver>();
            this.factoriaRegiones = regionsFactory;
            this.factoriaAnimales = animalsFactory;
    }

    private void setRegion(int row, int col, Region r){
        this.regionMngr.setRegion(row, col, r);
    }

    public void setRegion(int row, int col, JSONObject rJson){ 
        Region R = factoriaRegiones.createInstance(rJson); 
        setRegion(row, col, R);
        notifyOnRegionSet(row, col, R);
    }

    private void addAnimal(Animal a){
        this.animals.add(a);
        this.regionMngr.registerAnimal(a);
    }

    public void addAnimal(JSONObject aJson){
        Animal A = factoriaAnimales.createInstance(aJson); 
        addAnimal(A);
        notifyOnAnimalAdded(A);
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
        List<Animal> animalesMuertos = new ArrayList<Animal>();
         List<Animal> babies = new ArrayList<Animal>();
        tiempo += dt;
        for (Animal a: this.animals){
            a.update(dt);
            this.regionMngr.updateAnimalRegion(a);
            if(a.getState() == State.DEAD){
                animalesMuertos.add(a);
            }
        }
         for(Animal a: animalesMuertos){
            animals.remove(a);
            this.regionMngr.unregisterAnimal(a);
        }

        for(Animal a: this.animals){
            if(a.isPregnant()){
                Animal animalBaby = a.deliverBaby();
                babies.add(animalBaby);
            }

        }
        for(Animal a: babies){
            addAnimal(a);
        }
        //for(Animal a: animalesMuertos){
           // animals.remove(a);
           // this.regionMngr.unregisterAnimal(a);
        //}
        regionMngr.updateAllRegions(dt);
        notifyOnAdvance(dt);

    }
    public JSONObject asJSON(){
        JSONObject o = new JSONObject();
		o.put("time", tiempo);
		o.put("state", regionMngr.asJSON());
		return o;
    }

    public void reset(int cols, int rows, int width, int height){
        this.tiempo = 0.0;
        this.animals = new ArrayList<Animal>();
        this.regionMngr = new RegionManager(cols, rows, width, height);
        notifyOnRest();
    }

    @Override
    public void addObserver(EcoSysObserver o) {
        if (!observadores.contains(o)){
            observadores.add(o);
        }
        notifyOnRegister(o);
    }
        

    @Override
    public void removeObserver(EcoSysObserver o) {
        observadores.remove(o);
    }   

    private void notifyOnRegister(EcoSysObserver o){
        List<AnimalInfo> animalsInfo = new ArrayList<>(animals);
        o.onRegister(this.tiempo, this.regionMngr, animalsInfo);

    }
    private void notifyOnRest(){
         List<AnimalInfo> animalsInfo = new ArrayList<>(animals);
         for(EcoSysObserver o: observadores){
            o.onReset(this.tiempo, this.regionMngr, animalsInfo);
         }
    }
    private void notifyOnAnimalAdded(Animal a){
          List<AnimalInfo> animalsInfo = new ArrayList<>(animals);
         for(EcoSysObserver o: observadores){
            o.onAnimalAdded(this.tiempo, this.regionMngr, animalsInfo, a);
         }
    }
    private void notifyOnRegionSet(int row, int col, Region r){
        for(EcoSysObserver o: observadores){
            o.onRegionSet(row, col, this.regionMngr, r);
         }
    }
    private void notifyOnAdvance(double dt){
        List<AnimalInfo> animalsInfo = new ArrayList<>(animals);
         for(EcoSysObserver o: observadores){
            o.onAdvance(this.tiempo, this.regionMngr, animalsInfo, dt);
         }
    }
}

