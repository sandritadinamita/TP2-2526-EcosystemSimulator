package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

    public DynamicSupplyRegionBuilder(String typeTag, String desc) {
        super("dynamic", " ");
    }

    @Override
    protected Region createInstance(JSONObject data) {
        double factor = data.optDouble("factor", 2.5);
		double comida = data.optDouble("food", 1250.0);
        return new DynamicSupplyRegion(comida, factor);
    }

}
