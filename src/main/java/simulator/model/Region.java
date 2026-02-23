package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{
 //atributos necesarios
    protected List<Animal> lista; 
    //Constructoras 
    //Tiene solo una constructora por defecto que inicializa la lista de animales.
    public Region(){ 
        this.lista = new ArrayList<>(); 
    }
    //Métodos

    final void addAnimal(Animal a){
        //añade el animal a la lista de animales.
        this.lista.add(a);
    }
    final void removeAnimal(Animal a){
        //quita el animal de la lista de animales.
        this.lista.remove(a);
    }
    final List<Animal> getAnimals(){
        //devuelve una versión inmodificable de la lista de animales.
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
