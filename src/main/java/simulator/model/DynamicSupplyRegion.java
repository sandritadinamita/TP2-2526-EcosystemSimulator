package simulator.model;

public class DynamicSupplyRegion {
    DynamicSupplyRegion(double comida, double factor){ //factor de crecimiento (número no negativo de tipo double).

    }
    getFood(a, dt){
        if(a){
            return 0.0;
        }
        else{
            return Math.min(food,Constantes.FOOD_EAT_RATE_HERBS*Math.exp(-Math.max(0,n-Constantes.FOOD_SHORTAGE_TH_HERBS)*Constantes.FOOD_SHORTAGE_EXP_HERBS)*dt);
        }
        //Además quita el valor devuelto a la cantidad de comida food que tiene la región actualmente. 
    }

    //Su método update incrementa, con probabilidad 0.5, la cantidad de comida por dt*factor donde factor es el factor de crecimiento.

}
