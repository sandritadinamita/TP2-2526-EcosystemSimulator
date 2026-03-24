package simulator.model;
import simulator.misc.Utils;

public class DynamicSupplyRegion extends DefaultRegion{
    private double food;
    private double factor;
    public DynamicSupplyRegion(double comida, double factor){ 
        if(comida <= 0.0)
            throw new IllegalArgumentException("comida no puede puede ser un numero negativo o cero");
        if(factor < 0.0)
            throw new IllegalArgumentException("factor no puede ser un numero negativo");
        this.food = comida;
        this.factor = factor;
    }
    @Override
    public double getFood(AnimalInfo a, double dt){
        if (a.getDiet() == Diet.CARNIVORE){
			return 0.0;
        }
        else{
            int n = (int) this.getAnimals().stream().filter((e) -> e.getDiet() == Diet.HERBIVORE).count();
            double comida = 0;
            comida = Math.min(food,Constantes.FOOD_EAT_RATE_HERBS*Math.exp(-Math.max(0, n-Constantes.FOOD_SHORTAGE_TH_HERBS)*Constantes.FOOD_SHORTAGE_EXP_HERBS)*dt);
            food -= comida;
            return comida;
        }
    }


    @Override
    public void update(double dt) {
        if(Utils.RAND.nextDouble() < Constantes.FOOD_GROWTH_PROBABILITY){
            this.food += dt*this.factor;
        }
    }
}
