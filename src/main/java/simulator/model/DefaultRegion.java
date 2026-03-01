package simulator.model;


public class DefaultRegion extends Region{ 
    public double getFood(AnimalInfo a, double dt){
        if(a.getDiet() == Diet.CARNIVORE){
            return 0.0;
        }
        else{
            int n = 0;
            for (Animal animal : this.lista) { 
                if (animal.getDiet() == Diet.HERBIVORE &&
                    animal.getState() != State.DEAD) {
                    n++;
                }
            }
            return Constantes.FOOD_EAT_RATE_HERB*Math.exp(-Math.max(0, n-Constantes. FOOD_SHORTAGE_TH_HERB )*Constantes.FOOD_SHORTAGE_EXP_HERB)*dt;
            
        }
    }

    @Override
    public void update(double dt) {}


}
