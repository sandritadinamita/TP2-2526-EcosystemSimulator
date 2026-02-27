package simulator.model;
import java.util.ArrayList;
import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal{
    //Es un animal carnívoro con código genético "Wolf". Es un animal que caza a otros animales herbívoros y también puede comer lo que proporciona la región en la que está, y puede emparejarse con otros animales con el mismo código genético.
    private Animal huntTarget;
    private SelectionStrategy huntingStrategy;

    public Wolf(SelectionStrategy mateStrategy, SelectionStrategy huntingStrategy,  Vector2D pos){
        super(Constantes.WOLF_GENETIC_CODE, Diet.CARNIVORE, Constantes.INIT_SIGHT_WOLF, Constantes.INIT_SPEED_WOLF, mateStrategy, pos);
        this.huntingStrategy = huntingStrategy;
    }

    protected Wolf(Wolf p1, Animal p2){
        super(p1, p2);
        this.huntingStrategy = p1.huntingStrategy;
        this.huntTarget = null;
    }
    @Override
    public void update(double dt) {
        if(this.state == State.DEAD){
            return;
        }
        //Actualizar el objeto según el estado del animal (ver la descripción abajo).
        switch(this.state){
            case NORMAL:
                updateNormal(dt);
                break;
            case DANGER:
                updateHunger(dt);
                break;
            case MATE:
                updateMate(dt);
                break;
            default:
                break;
        }
        if(!isPosEnMapa(this.getPosition())){
            ajustarPosicionDentroMapa(this.pos);
            setNormalStateAction();
        }
        if(this.energy == 0.0 || this.age > 14.0){
            this.state = State.DEAD;
        }
        if(this.state != State.DEAD){ //not sure
            double newEnergy = this.energy + this.regionMngr.getFood(this, dt);
            if(newEnergy < 100.0 && newEnergy > 0){
                this.energy = newEnergy;
            }
        }
    }
    Animal buscarPresa(){
        List<Animal> presas = new ArrayList<>();
        Animal presa = null;
        presas = this.regionMngr.getAnimalsInRange(this, a -> a.getDiet() == Diet.HERBIVORE);
        if(!presas.isEmpty()){
            presa =this.huntingStrategy.select(this, presas);
            setHungerStateAction();
        }
        return presa;
    }
    Animal buscarPareja(){
         List<Animal> parejas = new ArrayList<>();
        Animal pareja = null;
        parejas = this.regionMngr.getAnimalsInRange(this, a -> a.getGeneticCode() == Wolf.this.getGeneticCode());
        if(!parejas.isEmpty()){
            pareja =this.mateStrategy.select(this, parejas);
            setMateStateAction();
        }
        return pareja;
    }
    void avanza(double dt){
        if(this.dest.distanceTo(this.pos) < Constantes.COLLISION_RANGE){
            this.dest = this.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(Constantes.NEARBY_FACTOR*(Utils.RAND.nextGaussian()+1)));
        }
        move(speed*dt*Math.exp((energy-Constantes.MAX_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
        this.age = age + dt;
        this.energy = Utils.constrainValueInRange(energy + Constantes.FOOD_DROP_RATE_WOLF*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
        this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_WOLF*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
    }

    void updateNormal(double dt){
        //1
        avanza(dt);
        //2
        if(this.energy < Constantes.FOOD_THRSHOLD_WOLF){
            setHungerStateAction();
        }
        else {
            if(this.desire > Constantes.DESIRE_THRESHOLD_WOLF){
                setMateStateAction();
            }
        }
    }

    
    void updateHunger(double dt){
        //1
        if(this.huntTarget == null||this.huntTarget != null && this.huntTarget.getState() == State.DEAD||this.pos.distanceTo(this.huntTarget.getPosition()) > this.sightRange){ 
            this.huntTarget = buscarPresa();
        } 
        //2
        if(this.huntTarget == null){
            avanza(dt);
        }
        else {
            this.dest = huntTarget.getPosition();
            move(Constantes.BOOST_FACTOR_WOLF*speed*dt*Math.exp((energy-Constantes.MAX_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy + Constantes.FOOD_DROP_RATE_WOLF*Constantes.FOOD_DROP_BOOST_FACTOR_WOLF*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_WOLF*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
            if(this.getPosition().distanceTo(this.huntTarget.getPosition())< Constantes.COLLISION_RANGE){
                this.huntTarget.state = State.DEAD;
                this.huntTarget = null;
                this.energy = Utils.constrainValueInRange(energy + Constantes.FOOD_EAT_VALUE_WOLF, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY); 
            }
        }
        //3
        if(this.energy > Constantes.FOOD_THRSHOLD_WOLF){
            if(this.desire < Constantes.DESIRE_THRESHOLD_WOLF){
                setNormalStateAction();
            }
            else{
                setMateStateAction();
            }
        }
    }

    void updateMate(double dt){
        //1
        if((this.mateTarget != null && this.mateTarget.getState() == State.DEAD)|| this.pos.distanceTo(this.mateTarget.getPosition()) > this.sightRange){
            this.mateTarget = null;
        }
        //2
        if(this.mateTarget == null){
            this.mateTarget = buscarPareja();
            if(this.mateTarget == null){
                avanza(dt);
            }
        }
        else {
            this.dest = mateTarget.getPosition();
            move(speed*dt*Math.exp((energy-Constantes.MAX_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy - Constantes.FOOD_DROP_RATE_WOLF*Constantes.FOOD_DROP_BOOST_FACTOR_WOLF*dt,Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_WOLF*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
            if(this.getPosition().distanceTo(this.mateTarget.getPosition())< Constantes.COLLISION_RANGE){
                this.desire = 0.0;
                this.mateTarget.desire = 0.0;
                if(!(this.isPregnant() || this.mateTarget.isPregnant())){//esto ns si esta bien PREGUNTAR
                    if(Utils.RAND.nextDouble() < Constantes.PREGNANT_PROBABILITY_WOLF){ //no se si esta bien
                        this.baby = new Wolf(this, mateTarget);
                        //this.mateTarget.baby = this.baby; //preguntar
                    }
                }

                this.energy = Utils.constrainValueInRange(this.energy - Constantes.FOOD_DROP_DESIRE_WOLF,Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
                ///ns si lo de la ebergia hay q hacerlo al mate target tambien
                this.mateTarget = null;
            }
        }
        //3
        if(this.energy < Constantes.FOOD_THRSHOLD_WOLF){
            setHungerStateAction();
        }
        else {
            if(this.desire < Constantes.DESIRE_THRESHOLD_WOLF){
                setNormalStateAction();
          }
        }
    }
        
        @Override
    public State getState() {
        return this.state;
    }

    @Override
    public Vector2D getPosition() {
        return this.pos;
    }

    @Override
    public String getGeneticCode() {
        return this.geneticCode;
    }

    @Override
    public Diet getDiet() {
        return this.diet;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public double getSightRange() {
        return this.sightRange;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public double getAge() {
        return this.age;
    }

    @Override
    public Vector2D getDestination() {
        return this.dest;
    }

    @Override
    public boolean isPregnant() {
        if(this.baby == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    protected void setNormalStateAction() {
        this.state = State.NORMAL;
        this.huntTarget = null;
        this.mateTarget = null;
    }

    @Override
    protected void setMateStateAction() {
        this.state = State.MATE;
        this.huntTarget = null;
    }

    @Override
    protected void setHungerStateAction() {
        this.state = State.HUNGER;
        mateTarget = null;
    }

    @Override
    protected void setDangerStateAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDangerStateAction'");
    }

    @Override
    protected void setDeadStateAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDeadStateAction'");
    }
    

}
