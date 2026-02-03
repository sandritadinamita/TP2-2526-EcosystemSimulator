package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo{
    protected String geneticCode;
    protected Diet diet;
    protected State state;
    protected Vector2D pos;
    protected Vector2D dest;
    protected double energy;
    protected double speed;
    protected double age;
    protected double desire;
    protected double sightRange;
    protected Animal mateTarget;
    protected Animal baby;
    protected AnimalMapView regionMngr;
    protected SelectionStrategy mateStrategy;

    protected Animal(String geneticCode, Diet diet, double sightRange, double initSpeed, SelectionStrategy mateStrategy, Vector2D pos){
        this.geneticCode = geneticCode;
        this.diet = diet;
        this.sightRange = sightRange;
        this.pos = pos; //pos puede ser null y en ese caso se inicializa a un valor aleatorio en el método init
        this.mateStrategy = mateStrategy;
        this.speed = Utils.getRandomizedParameter(initSpeed, 0.1);
        this.state = State.NORMAL;
        this.energy = constantes.INIT_ENERGY; //crear constantes?
        this.desire = constantes.DESIRE_INIT
        this.dest = null;
        this.mateTarget = null;
        this.baby = null;
        this.regionMngr = null;
        //geneticCode tiene que ser una cadena de caracteres no vacía, sightRange y initSpeed números positivos y mateStrategy no es null. Hay que lanzar una excepción correspondiente con un mensaje informativo si algún valor es incorrecto (p.ej., IllegalArgumentException).
    }

    protected Animal(Animal p1, Animal p2){
        this.dest = null;
        this.baby = null;
        this.mateTarget = null;
        this.regionMngr = null;
        this.state = State.NORMAL;
        this.desire = constantes.DESIRE_INIT;
        this.geneticCode = p1.geneticCode;
        this.diet = p1.diet;
        this.mateStrategy = p2.mateStrategy;
        this.energy = (p1.energy + p2.energy)/2;
        this.pos = p1.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1)));
        this.sightRange = Utils.getRandomizedParameter((p1.getSightRange()+p2.getSightRange())/2,0.2);
        this.speed = Utils.getRandomizedParameter((p1.getSpeed()+p2.getSpeed())/2, 0.2);
    }

    void init(AnimalMapView regMngr){
        this.regionMngr = regMngr;
        //si pos es null hay que elegir una posición aleatoria dentro del rango del mapa (X entre 0 y regionMngr.getWidth()-1 e Y entre 0 y regionMngr.getHeight()-1). Si pos no es null hay que ajustarlo para que esté dentro del mapa si es necesario (ver el apartado Ajustar posiciones).
        //Elegir una posición aleatoria para dest (dentro del rango del mapa).
    }

    Animal deliverBaby(){
        return this.baby;
        this.baby = null;
    }

    protected void move(double speed){
        this.pos = pos.plus(dest.minus(pos).direction().scale(speed));
    }

    protected void setState(State state){
        this.state = state;
        switch (state) {
        case NORMAL:
            setNormalStateAction();
            break;
        case HUNGER:
            setHungerStateAction();
            break;
        case MATE:
            setMateStateAction();
            break;
        case DEAD:
            setDeadStateAction();
            break;
        case DANGER:
            setDangerStateAction();
            break;
        }
    }

    abstract protected void setNormalStateAction();
    abstract protected void setMateStateAction();
    abstract protected void setHungerStateAction();
    abstract protected void setDangerStateAction();
    abstract protected void setDeadStateAction();

    public JSONObject asJSON(){
        "pos": [28.90696391797469,22.009772194487613],
        "gcode": "Sheep",
        "diet": "HERBIVORE",
        "state": "NORMAL"
    }

}
