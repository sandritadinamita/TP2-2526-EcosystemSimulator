package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{
    protected List<Animal> lista; 
    public Region(){ 
        this.lista = new ArrayList<>(); 
    }

    final void addAnimal(Animal a){
        this.lista.add(a);
    }
    final void removeAnimal(Animal a){
        this.lista.remove(a);
    }
    final List<Animal> getAnimals(){
        return Collections.unmodifiableList(lista);
    }
    public JSONObject asJSON(){
        JSONObject o = new JSONObject();

        List<JSONObject> animalsJSON = new ArrayList<>();
        for (Animal a : lista) {
            animalsJSON.add(a.asJSON());
        }

        o.put("animals", animalsJSON);
        return o;
    }
}
