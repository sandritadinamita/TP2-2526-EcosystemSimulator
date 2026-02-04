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
        if(){
            //Si la posición está fuera del mapa
            ajustarPosicionDentroMapa(this.pos);
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

    void avanza(double dt){
        if(this.dest.distanceTo(this.pos) < Constantes.COLLISION_RANGE){
            this.dest = ;//nuevo destino random
        }
        move(speed*dt*Math.exp((energy-100.0)*0.007));
        this.age = age + dt;
        this.energy = energy - 20.0*dt; // manteniéndolo siempre entre 0.0 y 100.0
        this.desire = desire + 40.0*dt; // manteniéndolo siempre entre 0.0 y 100.0
    }



    void updateNormal(double dt){
        avanza(dt);
        if(this.dangerSource == null){
            //buscar nuevo animal peligroso;
            if(this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                this.state = State.MATE;
            }
        }
        else{
            this.state = State.DANGER;
        }
    }

    void updateDanger(double dt){
        if(this.dangerSource == null){
            avanza(dt);
        }
        else if(this.dangerSource != null){
            this.dest = pos.plus(pos.minus(dangerSource.getPosition()).direction());
            move(2.0*speed*dt*Math.exp((energy-100.0)*0.007));
            this.age = age + dt;
            this.energy = energy - 20.0*1.2*dt; // manteniéndolo siempre entre 0.0 y 100.0
            this.desire = desire + 40.0*dt; // manteniéndolo siempre entre 0.0 y 100.0
            if(this.state == State.DEAD){
                this.dangerSource = null;
            }
        }
        else if(this.dangerSource == null || dangerSource no esta en el campo visual){
            //buscar un nuevo animal que se considere como peligro.
            if(this.dangerSource == null && this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                this.state = State.MATE;
            }
        }
    }

    void updateMate(double dt){
        if(this.mateTarget != null && (this.state == State.DEAD || fuera del campo visual)){
            this.mateTarget = null;
        }
        else if(this.mateTarget == null){
            //buscar un animal para emparejarse y si no se encuentra uno avanza normalmente como el punto 1 del caso NORMAL arriba
            avanza(dt);
        }
        else if(this.mateTarget != null){
            this.dest = mateTarget.getPosition();
            move(2.0*speed*dt*Math.exp((energy-100.0)*0.007));
            this.age = age + dt;
            this.energy = energy - 20.0*1.2*dt; //manteniéndola siempre entre 0.0 y 100.0
            this.desire = desire + 40.0*dt; //manteniéndola siempre 0-100
            if(this.mateTarget.getPosition().distanceTo(this.pos) < Constantes.COLLISION_RANGE){
                this.desire = Constantes.DESIRE_INIT;
                this.mateTarget.desire = Constantes.DESIRE_INIT; //deberiamos hacer un setdesire?
                if(!this.isPregnant()){
                    //con probabilidad de 0.9 va a llevar a un nuevo bebé usando 
                    this.baby = new Sheep(this, mateTarget);
                }
                this.mateTarget = null;
            }
        }
        if(this.dangerSource == null){
            //buscar un nuevo animal que se considere como peligroso.
            if(this.desire < Constantes.DESIRE_THRESHOLD_SHEEP){
                this.state = State.NORMAL;
            }
        }
        else{
            this.state = State.DANGER;
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
