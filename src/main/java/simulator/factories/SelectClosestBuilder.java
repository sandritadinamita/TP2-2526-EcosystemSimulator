package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;
import simulator.model.SelectionStrategy;

public class SelectClosestBuilder extends Builder<SelectionStrategy>{

    public SelectClosestBuilder() {// quite los strings xq creo q estaba mal pero lo comprobamos
        super("closest", " ");
    }

    @Override
    protected SelectionStrategy createInstance(JSONObject data) {
        return new SelectClosest();
    }

}
