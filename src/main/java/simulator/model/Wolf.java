package simulator.model;

import simulator.misc.Utils;
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
        if(){
            //Si la posición está fuera del mapa, ajustarla y cambiar su estado a NORMAL.
            this.state = State.NORMAL;
        }
        if(this.energy == 0.0 || this.age > 14.0){
            this.state = State.DEAD;
        }
        if(this.state != State.DEAD){ //not sure
            double newEnergy = this.energy + getfood(this, dt);
            if(newEnergy < 100.0 && newEnergy > 0){
                this.energy = newEnergy;
            }
        }
    }
    void avanza(double dt){
        if(this.dest.distanceTo(this.pos) < Constantes.COLLISION_RANGE){
            double dest_x = Utils.RAND.nextDouble() * (regionMngr.getWidth() - 1);
            double dest_y = Utils.RAND.nextDouble() * (regionMngr.getHeight() - 1);
            this.dest = new Vector2D(dest_x, dest_y);
        }
        move(speed*dt*Math.exp((energy-Constantes.MAX_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
        this.age = age + dt;
        this.energy = energy - (Constantes.FOOD_DROP_RATE_WOLF*dt);//mantenerlo entre 0.0 y 100.0
        this.desire = desire + (Constantes.DESIRE_INCREASE_RATE_WOLF*dt);// mantener entre 0.0 y 100.0
    }

    void updateNormal(double dt){
        //1
        avanza(dt);
        //2
        if(this.energy < Constantes.FOOD_THRSHOLD_WOLF){
            this.state = State.HUNGER;
        }
        else {
            if(this.desire > Constantes.DESIRE_THRESHOLD_WOLF){
                this.state = State.MATE;
            }
        }
    }

    
    void updateHunger(double dt){
        //1
        if(this.huntTarget == null||this.huntTarget.getState() == State.DEAD){ 
            // falta poner o esta fuera del campo visual

        } 
        //2
        if(this.huntTarget == null){
            avanza(dt);
        }
        else {
            this.dest = huntTarget.getPosition();
            move(Constantes.BOOST_FACTOR_WOLF*speed*dt*Math.exp((energy-Constantes.MAX_ENERGY)*Constantes.HUNGER_DECAY_EXP_FACTOR));
            this.age = age + dt;
            this.energy = energy - (Constantes.FOOD_DROP_RATE_WOLF*Constantes.FOOD_DROP_BOOST_FACTOR_WOLF*dt);//mantenerlo entre 0.0 y 100.0
            this.desire = desire + (Constantes.DESIRE_INCREASE_RATE_WOLF*dt);// mantener entre 0.0 y 100.0
            if(this.getPosition().distanceTo(this.huntTarget.getPosition())< Constantes.COLLISION_RANGE){
                this.huntTarget.state = State.DEAD;
                this.huntTarget = null;
                this.energy = energy + Constantes.FOOD_EAT_VALUE_WOLF; //mantenerlo entre 0.0 y 100.0
            }
        }
        //3
        if(this.energy > Constantes.FOOD_THRSHOLD_WOLF){
            if(this.desire < Constantes.DESIRE_THRESHOLD_WOLF){
                this.state = State.NORMAL;
            }
            else{
                this.state = State.MATE;
            }
        }
    }

    void updateMate(double dt){
        
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
