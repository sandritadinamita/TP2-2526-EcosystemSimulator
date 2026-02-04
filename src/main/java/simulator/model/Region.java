package simulator.model;

import java.util.List;

import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{
 //atributos necesarios
    //Un atributo con la lista de animales que se encuentran en la región. Mantenerlo protected para poder acceder directamente desde las subclases.

    //Constructoras
    //Tiene solo una constructora por defecto que inicializa la lista de animales.
    final void addAnimal(Animal a){
        //añade el animal a la lista de animales.
    }
    final void removeAnimal(Animal a){
        //quita el animal de la lista de animales.
    }
    final List<Animal> getAnimals(){
        //devuelve una versión inmodificable de la lista de animales.
        return lista;
    }
    public JSONObject asJSON(){
        "animals": [a1,a2,...]
        //devuelve una estructura JSON como la siguiente donde ai es lo que devuelve asJSON() del animal correspondiente:
    }
}
