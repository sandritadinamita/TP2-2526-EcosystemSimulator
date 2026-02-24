package simulator.factories;

import org.json.JSONObject;

import simulator.model.Animal;

public class WolfBuilder extends Builder<Animal>{

    public WolfBuilder(String typeTag, String desc) {
        super(typeTag, desc);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected Animal createInstance(JSONObject data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInstance'");
    }

}
