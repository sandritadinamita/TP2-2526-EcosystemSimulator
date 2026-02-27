package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy>{

    public SelectFirstBuilder() {// quite los strings xq creo q estaba mal pero lo comprobamos 
        super("first", " ");
    }

    @Override
    protected SelectionStrategy createInstance(JSONObject data) {
        return new SelectFirst();
    }

}
