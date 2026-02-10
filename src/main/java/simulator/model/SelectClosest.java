package simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy{

    @Override
    public Animal select(Animal a, List<Animal> as) {
        if (as.isEmpty()){
            return null;
        }
        else{
            Animal animalMasCercano = as.get(0);
            double distancia = a.pos.distanceTo(animalMasCercano.pos);
            for (int i = 0; i < as.size(); i++){
                if(as.get(i) != a && distancia > a.pos.distanceTo(as.get(i).pos)) {
					animalMasCercano = as.get(i);
					distancia = a.pos.distanceTo(as.get(i).pos);
				}
            }
            return animalMasCercano;
        }
    }

}

			