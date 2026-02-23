package simulator.model;
import java.util.List;
import java.util.function.Predicate;

import simulator.misc.Utils;

public class DefaultRegion implements FoodSupplier{ //pregunat si es extends region e implements food suplier y animal info
    public double getFood(AnimalInfo a, double dt){
        if(a.getDiet() == Diet.CARNIVORE){
            return 0.0;
        }
        else{
            
            return Constantes.FOOD_EAT_RATE_HERB*Math.exp(-Math.max(0, n-Constantes. FOOD_SHORTAGE_TH_HERB )*Constantes.FOOD_SHORTAGE_EXP_HERB)*dt;
            
        }
    }



}
