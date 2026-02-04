package simulator.model;

import simulator.misc.Vector2D;

public class Wolf extends Animal{
    //Es un animal carnívoro con código genético "Wolf". Es un animal que caza a otros animales herbívoros y también puede comer lo que proporciona la región en la que está, y puede emparejarse con otros animales con el mismo código genético.
    private Animal huntTarget;
    private SelectionStrategy huntingStrategy;

    public Wolf(SelectionStrategy mateStrategy, SelectionStrategy huntingStrategy,  Vector2D pos){
        super(Constantes.WOLF_GENETIC_CODE, Diet.CARNIVORE, Constantes.INIT_SIGHT_WOLF , Constantes.INIT_SPEED_WOLF, mateStrategy,pos);
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
        if(){
            //Si la posición está fuera del mapa, ajustarla y cambiar su estado a NORMAL.
            this.state = State.NORMAL;
        }
        if(this.energy == 0.0 || this.age > 14.0){
            this.state = State.DEAD;
        }
        if(this.state != State.DEAD){ //not sure
            double newEnergy = this.energy + getFood(this, dt);
            if(newEnergy < 100.0 && newEnergy > 0){
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
        if(this.baby == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    protected void setNormalStateAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNormalStateAction'");
    }

    @Override
    protected void setMateStateAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMateStateAction'");
    }

    @Override
    protected void setHungerStateAction() {
        mateTarget = null;
        //comprobar
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
