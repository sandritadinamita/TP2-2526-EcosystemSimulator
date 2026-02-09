package simulator.model;

public class DefaultRegion {
    getfood(a, dt){
        if(a){
            return 0.0;
        }
        else{
            return 60.0*Math.exp(-Math.max(0, n-5.0)*2.0)*dt;
        }
    }

}
