package simulator.model;

public class DefaultRegion {
    getFood(a, dt){
        if(a){
            return 0.0;
        }
        else{
            return Constantes.FOOD_EAT_RATE_HERB*Math.exp(-Math.max(0, n-Constantes. FOOD_SHORTAGE_TH_HERB )*Constantes.FOOD_SHORTAGE_EXP_HERB)*dt;
            
        }
    }

}
