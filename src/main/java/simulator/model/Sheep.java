package simulator.model;

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
            this.state = State.DEAD;
            setDeadStateAction();
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
            this.dest = this.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1)));//nuevo destino random PREGUNTAR
        }
        move(speed*dt*Math.exp((energy-100.0)*0.007)); //CTE PREGUNTAR
        this.age = age + dt;
        this.energy = Utils.constrainValueInRange(energy - 20.0*dt, Constantes.MIN_DESIRE_ENERGY,Constantes.MAX_ENERGY);
        this.desire = Utils.constrainValueInRange(desire + 40.0*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
    }



    void updateNormal(double dt){
        avanza(dt);
        if(this.dangerSource == null){
            //buscar nuevo animal peligroso;
            this.dangerSource = buscarPeligro();
        }
        if(this.dangerSource != null){
            this.state = State.DANGER;
            setDangerStateAction();
        }
        else if(this.dangerSource == null && this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                this.state = State.MATE;
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
            move(2.0*speed*dt*Math.exp((energy-100.0)*0.007));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy - 20.0*1.2*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + 40.0*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
        }
        if(this.dangerSource == null || this.pos.distanceTo(this.dangerSource.getPosition()) > this.sightRange){ //COMPROBAR
            this.dangerSource = buscarPeligro();
            if(this.dangerSource == null){
                if(this.desire > Constantes.DESIRE_THRESHOLD_SHEEP){
                    this.state = State.MATE;
                    setMateStateAction();
                }
                else{
                    this.state = State.NORMAL;
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
            move(2.0*speed*dt*Math.exp((energy-100.0)*0.007));
            this.age = age + dt;
            this.energy = Utils.constrainValueInRange(energy - 20.0*1.2*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_ENERGY);
            this.desire = Utils.constrainValueInRange(desire + 40.0*dt, Constantes.MIN_DESIRE_ENERGY, Constantes.MAX_DESIRE);
            if(this.mateTarget.getPosition().distanceTo(this.pos) < Constantes.COLLISION_RANGE){
                this.desire = Constantes.DESIRE_INIT;
                this.mateTarget.desire = Constantes.DESIRE_INIT; //deberiamos hacer un setdesire?
                if(!this.isPregnant()){
                    //con probabilidad de 0.9 va a llevar a un nuevo bebé usando -> copiar wolf
                    this.baby = new Sheep(this, mateTarget);
                    //this.mate.baby ?????
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
            this.state = State.DANGER;
            setDangerStateAction();

        }
        else{
            if(this.desire < Constantes.DESIRE_THRESHOLD_SHEEP){
                this.state = State.NORMAL;
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
