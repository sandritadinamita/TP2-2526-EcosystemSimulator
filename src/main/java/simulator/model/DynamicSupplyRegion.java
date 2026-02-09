package simulator.model;

public class DynamicSupplyRegion {
    DynamicSupplyRegion(double comida, double factor){ //factor de crecimiento (número no negativo de tipo double).

    }
    getfood(a, dt){
        if(a){
            return 0.0
        }
        else{
            return Math.min(food,60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt);
        }
        //Además quita el valor devuelto a la cantidad de comida food que tiene la región actualmente. 
    }

    //Su método update incrementa, con probabilidad 0.5, la cantidad de comida por dt*factor donde factor es el factor de crecimiento.

}
