package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region>{

    public DefaultRegionBuilder(String typeTag, String desc) {
        super("default", " ");
    }

    @Override
    protected Region createInstance(JSONObject data) {
        return new DefaultRegion();
    }

}
