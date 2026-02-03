package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal{
    //Es un animal herbívoro con código genético "Sheep". Es un animal que no caza a otros animales, sólo come lo que proporciona la región en la que está, y puede emparejarse con otros animales con el mismo código genético.
    private Animal dangerSource;
    private SelectionStrategy dangerStrategy;

    public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos){
        super(Constantes.SHEEP_GENETIC_CODE,Diet.HERBIVORE , Constantes.INIT_SIGHT_SHEEP, Constantes.INIT_SPEED_SHEEP,mateStrategy, pos) 
        this.dangerStrategy = dangerStrategy;
        }

    protected Sheep(Sheep p1, Animal p2){
        super(p1, p2);
        this.dangerStrategy = p1.dangerStrategy;
        this.dangerSource = null;
    }

    @Override
    public void update(double dt) {
        if(this.state == State.DEAD){
            return;
        }
        //Actualizar el objeto según el estado del animal (ver la descripción abajo).
        if(){
            //Si la posición está fuera del mapa, ajustarla y cambiar su estado a NORMAL.
            this.state = State.NORMAL;
        }
        if(this.energy == Constantes.ENERGY_DEAD || this.age > Constantes.MAX_AGE_SHEEP){
            this.state = State.DEAD;
        }
        if(this.state != State.DEAD){ //not sure
            double newEnergy = this.energy + getFood(this, dt);
            if(newEnergy < Constantes.MAX_ENERGY && newEnergy > Constantes.ENERGY_DEAD){
                this.energy = newEnergy;
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
        return this.;
    }

    @Override
    protected void setNormalStateAction() {
        mateTarget = null; 
        dangerSource = null;
        //comprobar  
    }

    @Override
    protected void setMateStateAction() {
        dangerSource = null;
        //comprobar
    }

    @Override
    protected void setHungerStateAction() {
       //comprobar
    }

    @Override
    protected void setDangerStateAction() {
        mateTarget = null;
       //comprobar
    }


    @Override
    protected void setDeadStateAction() {
        mateTarget = null;
        dangerSource = null;
    }

}
