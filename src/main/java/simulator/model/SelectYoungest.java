package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy{

    @Override
    public Animal select(Animal a, List<Animal> as) {  
        if(as.isEmpty()){
            return null;
        }
        else{
            Animal animalMasJoven = as.get(0);
            double edad = as.get(0).getAge();
            for (int i = 0; i < as.size(); i++) {
				if (as.get(i) != a && as.get(i).getAge() < edad) {
					edad = as.get(i).getAge();
					animalMasJoven = as.get(i);
				}
			}
            return animalMasJoven;
        }
    }

}
