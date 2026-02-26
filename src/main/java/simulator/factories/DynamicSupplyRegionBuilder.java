package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

    public DynamicSupplyRegionBuilder(String typeTag, String desc) {
        super("dynamic", " ");
    }

    protected void fillInData(JSONObject o) {
        o.put("factor", "food increase factor");
		o.put("food", "initial amount of food in the region");

    }

    @Override
    protected Region createInstance(JSONObject data) { // esta yo lo haria como el de sheep y como el de wolf
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
        
        //double factor = data.optDouble("factor", 2.5);
		//double comida = data.optDouble("food", 1250.0);
         if(comida <= 0.0)
            throw new IllegalArgumentException("comida no puede puede ser un numero negativo o cero");
        if(factor < 0.0)
            throw new IllegalArgumentException("factor no puede ser un numero negativo");

        if(data.has("factor")){
            factor = data.getDouble("factor");
        }
            else factor = 2.5;
        if(data.has("food")){
            comida = data.getDouble("food");
        }
            else comida = 1250.0;
        
        return new DynamicSupplyRegion(comida, factor);
    }


}
