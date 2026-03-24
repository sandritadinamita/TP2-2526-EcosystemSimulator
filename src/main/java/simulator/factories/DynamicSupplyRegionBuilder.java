package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

    public DynamicSupplyRegionBuilder() {
        super("dynamic", "Dynamic food supply");
    }

    protected void fillInData(JSONObject o) {
        o.put("factor", "food increase factor (optional, default 2.0)");
		o.put("food", "initial amount of food (optional, default 100.0)");
    }
  

    @Override
    protected Region createInstance(JSONObject data) { 
        double factor;
        double comida;
         if(data.has("factor")){
            factor = data.getDouble("factor");
        }
        else factor = 2.5;
        if(data.has("food")){
            comida = data.getDouble("food");
        }
        else comida = 1250.0;
        if(comida <= 0.0)
            throw new IllegalArgumentException("comida no puede puede ser un número negativo o cero");
        if(factor < 0.0)
            throw new IllegalArgumentException("factor no puede ser un número negativo");
        
        return new DynamicSupplyRegion(comida, factor);
    }

    



}
