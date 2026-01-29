package simulator.model;

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
    


}
