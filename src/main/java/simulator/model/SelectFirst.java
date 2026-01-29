package simulator.model;

import java.util.List;

//devuelvo el primer animal de la lista as
public class SelectFirst implements SelectionStrategy{

    @Override
    public Animal select(Animal a, List<Animal> as) {
        if (as.isEmpty()){
            return null;
        }
        else{
            return as.get(0);
        }

    }

}
