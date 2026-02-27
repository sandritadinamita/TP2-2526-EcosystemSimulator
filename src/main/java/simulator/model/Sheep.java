package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal{
    private Animal dangerSource;
    private SelectionStrategy dangerStrategy;

    public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos){
        super(Constantes.SHEEP_GENETIC_CODE, Diet.HERBIVORE, Constantes.INIT_SIGHT_SHEEP, Constantes.INIT_SPEED_SHEEP, mateStrategy, pos); 
        this.dangerStrategy = dangerStrategy;
    }

    protected Sheep(Sheep p1, Animal p2){
        super(p1, p2);
        this.dangerStrategy = p1.dangerStrategy;
        this.dangerSource = null;
    }
    Animal buscarPareja(){
         List<Animal> parejas = new ArrayList<>();
        Animal pareja = null;
        parejas = this.regionMngr.getAnimalsInRange(this, a -> a.getGeneticCode() == Sheep.this.getGeneticCode());
        if(!parejas.isEmpty()){
            pareja =this.mateStrategy.select(this, parejas);
            setMateStateAction();
        }
        return pareja;
    }
     Animal buscarPeligro(){
         List<Animal> depredadores = new ArrayList<>();
        Animal depredador = null;
        depredadores = this.regionMngr.getAnimalsInRange(this, a -> a.getDiet() == Diet.CARNIVORE);
        if(!depredadores.isEmpty()){
            depredador =this.dangerStrategy.select(this, depredadores);
            setDangerStateAction();
        }
        return depredador;
    }

    @Override
    public void update(double dt) {
        if(this.state == State.DEAD){
            return;
        }
        switch(this.state){
            case NORMAL:
                updateNormal(dt);
                break;
            case DANGER:
                updateDanger(dt);
                break;
            case MATE:
                updateMate(dt);
                break;
            default:
                break;
        }        
        if(!isPosEnMapa(this.getPosition())){
            ajustarPosicionDentroMapa(this.pos);
            this.state = State.NORMAL;
            setNormalStateAction();
        }
        if(this.energy == Constantes.ENERGY_DEAD || this.age > Constantes.MAX_AGE_SHEEP){
            setDeadStateAction();
        }
        if(this.state != State.DEAD){
            double newEnergy = this.energy + this.regionMngr.getFood(this, dt);
            if(newEnergy < Constantes.MAX_ENERGY && newEnergy > Constantes.ENERGY_DEAD){
                this.energy = newEnergy;
            }
        }
    }

    void avanza(double dt){
        if(this.dest.distanceTo(this.pos) < Constantes.COLLISION_RANGE){
            this.dest = this.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(Constantes.NEARBY_FACTOR*(Utils.RAND.nextGaussian()+1)));
        }
        move(speed*dt*Math.exp((energy-Constantes.INIT_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR)); 
        this.age = age + dt;
        this.energy = Utils.constrainValueInRange(energy - Constantes.FOOD_DROP_RATE_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY,Constantes.MAX_ENERGY);
        this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
    }



    void updateNormal(double dt){
        avanza(dt);
        if(this.dangerSource == null){
            //buscar nuevo animal peligroso;
            this.dangerSource = buscarPeligro();
        }
        if(this.dangerSource != null){
            setDangerStateAction();
        }
        else if(this.dangerSource == null && this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                setMateStateAction();
        }
    }

    void updateDanger(double dt){
        if(this.dangerSource != null && this.state == State.DEAD){
            this.dangerSource = null;
        }
        if(this.dangerSource == null){
            avanza(dt);
        }
        else{
            this.dest = pos.plus(pos.minus(dangerSource.getPosition()).direction());
            move(Constantes.BOOST_FACTOR_SHEEP*speed*dt*Math.exp((energy-Constantes.INIT_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy - Constantes.FOOD_DROP_RATE_SHEEP*Constantes.FOOD_DROP_BOOST_FACTOR_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
        }
        if(this.dangerSource == null || this.pos.distanceTo(this.dangerSource.getPosition()) > this.sightRange){ //COMPROBAR
            this.dangerSource = buscarPeligro();
            if(this.dangerSource == null){
                if(this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                    setMateStateAction();
                }
                else{
                    setNormalStateAction();
                }
            }
        }
    }

    void updateMate(double dt){
        if(this.mateTarget != null && (this.state == State.DEAD || this.pos.distanceTo(this.dangerSource.getPosition()) > this.sightRange)){
            this.mateTarget = null;
        }
        if(this.mateTarget == null){
            this.mateTarget = buscarPareja(); //Animal tentativeMate = mateStrategy.select(this, );
            if(this.mateTarget == null){
                avanza(dt);
            }
        }
        else{
            this.dest = mateTarget.getPosition();
            move(2.0*speed*dt*Math.exp((energy-Constantes.INIT_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy - Constantes.FOOD_DROP_RATE_SHEEP*Constantes.FOOD_DROP_BOOST_FACTOR_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + Constantes.DESIRE_INCREASE_RATE_SHEEP*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
            if(this.mateTarget.getPosition().distanceTo(this.pos) < Constantes.COLLISION_RANGE){
                this.desire = Constantes.DESIRE_INIT;
                this.mateTarget.desire = Constantes.DESIRE_INIT; //deberiamos hacer un setdesire?
                if(!this.isPregnant()){
                    if(Utils.RAND.nextDouble() < Constantes.PREGNANT_PROBABILITY_SHEEP){ 
                        this.baby = new Sheep(this, mateTarget);
                        //this.mateTarget.baby = this.baby; //preguntar
                    }
                }
                else{
                    this.mateTarget = null;
                }
            }
        }
        if(this.dangerSource == null){
            this.dangerSource = buscarPeligro();
        }
        if(this.dangerSource != null){
            setDangerStateAction();

        }
        else{
            if(this.desire < Constantes.DESIRE_THRESHOLD_SHEEP){
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
        mateTarget = null; 
        dangerSource = null; 
    }

    @Override
    protected void setMateStateAction() {
        this.state = State.MATE;
        dangerSource = null;
    }

    @Override
    protected void setHungerStateAction() {
       //comprobar
    }

    @Override
    protected void setDangerStateAction() {
        this.state = State.DANGER;
        mateTarget = null;
       //comprobar
    }


    @Override
    protected void setDeadStateAction() {
        this.state = State.DEAD;
        mateTarget = null;
        dangerSource = null;
    }

}
