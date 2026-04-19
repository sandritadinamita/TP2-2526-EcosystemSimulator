package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Utils;
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


    public enum Diet {
		HERBIVORE, CARNIVORE;
	}
	public enum State {
		NORMAL, MATE, HUNGER, DANGER, DEAD;
	}


    protected Animal(String geneticCode, Diet diet, double sightRange, double initSpeed, SelectionStrategy mateStrategy, Vector2D pos){
        if (geneticCode == null|| geneticCode.isBlank())
			throw new IllegalArgumentException("geneticCode no puede ser nulo");
		if (diet == null)
			throw new IllegalArgumentException("diet no puede ser nulo");
		if (sightRange <= 0.0)
			throw new IllegalArgumentException("sightRange no puede ser < 0");
        if (initSpeed <= 0.0)
			throw new IllegalArgumentException("initSpeed no puede ser < 0");
		if (mateStrategy == null)
			throw new IllegalArgumentException("mateStrategy no puede ser nulo");
        this.geneticCode = geneticCode;
        this.diet = diet;
        this.sightRange = sightRange;
        this.pos = pos; 
        this.mateStrategy = mateStrategy;
        this.speed = Utils.getRandomizedParameter(initSpeed, 0.1);
        this.state = State.NORMAL;
        this.energy = Constantes.INIT_ENERGY; 
        this.desire = Constantes.DESIRE_INIT;
        this.dest = null;
        this.mateTarget = null;
        this.baby = null;
        this.regionMngr = null;
        //this.age = 0.0;

    }

    protected Animal(Animal p1, Animal p2){
        this.dest = null;
        this.baby = null;
        this.mateTarget = null;
        this.regionMngr = null;
        this.state = State.NORMAL;
        this.desire = Constantes.DESIRE_INIT;
        this.geneticCode = p1.geneticCode;
        this.diet = p1.diet;
        this.mateStrategy = p2.mateStrategy;
        this.energy = (p1.energy + p2.energy)/2;
        this.pos = p1.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1)));
        this.sightRange = Utils.getRandomizedParameter((p1.getSightRange()+p2.getSightRange())/2,0.2);
        this.speed = Utils.getRandomizedParameter((p1.getSpeed()+p2.getSpeed())/2, 0.2);
        //this.age = 0.0;

    }

    public void init(AnimalMapView regMngr){
        this.regionMngr = regMngr;
        if (pos == null){
            double x = Utils.RAND.nextDouble() * (regionMngr.getWidth() - 1);
            double y = Utils.RAND.nextDouble() * (regionMngr.getHeight() - 1);
            this.pos = new Vector2D(x, y);
        }
        else {
            this.pos = ajustarPosicionDentroMapa(this.pos);
        }
        double dest_x = Utils.RAND.nextDouble() * (regionMngr.getWidth() - 1);
        double dest_y = Utils.RAND.nextDouble() * (regionMngr.getHeight() - 1);
        this.dest = new Vector2D(dest_x, dest_y);
    }

    Vector2D ajustarPosicionDentroMapa(Vector2D pos){
        double x = pos.getX();
        double y = pos.getY();
        double width = regionMngr.getWidth();
        double height = regionMngr.getHeight();
        while (x >= width) x = (x - width);
        while (x < 0) x = (x + width);
        while (y >= height) y = (y - height);
        while (y < 0) y = (y + height);
        return new Vector2D(x, y);
    }

    public Animal deliverBaby(){
        Animal b = this.baby;
	    this.baby = null;
	    return b;
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
    public boolean isPosEnMapa(Vector2D pos){
        if(pos.getX() >= 0 && pos.getX() < regionMngr.getWidth() && pos.getY() >= 0 && pos.getY() < regionMngr.getHeight())
            return true; 
        else return false;
    } 

    Animal buscarPareja(){
        List<Animal> parejas = new ArrayList<>();
        Animal pareja = null;
        parejas = this.regionMngr.getAnimalsInRange(this, a -> a.getGeneticCode().equals(this.getGeneticCode()));
        if(!parejas.isEmpty()){
            pareja =this.mateStrategy.select(this, parejas);
            setMateStateAction();
        }
        return pareja;
    }

    abstract protected void setNormalStateAction();
    abstract protected void setMateStateAction();
    abstract protected void setHungerStateAction();
    abstract protected void setDangerStateAction();
    abstract protected void setDeadStateAction();

    public JSONObject asJSON(){
        JSONObject o = new JSONObject();
	    o.put("pos", java.util.List.of(pos.getX(), pos.getY()));
	    o.put("gcode", geneticCode);
	    o.put("diet", diet.toString());
	    o.put("state", state.toString());
	    return o;
    }

}
