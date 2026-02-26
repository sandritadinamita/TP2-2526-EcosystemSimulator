package simulator.factories;


import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Animal>{
    private Factory <SelectionStrategy> factory;
    SelectionStrategy mateStrategy;
    SelectionStrategy dangerStrategy;
    Vector2D position;

    public WolfBuilder(String typeTag, String desc) {
        super("wolf", " ");
        //comprobar 

    }
    protected void fillInData(JSONObject o) {
        //JSONObject aux = new JSONObject();
		o.put("mate_strategy", "estrategía para buscar pareja");
		o.put("hunt_strategy", "estrategía para cazar presas");
		o.put("pos", "posicion del animal");
		//o.put("data", aux);
	}


    @Override
    protected Animal createInstance(JSONObject data) {
       if(data.has("mate_strategy")){
            mateStrategy = factory.createInstance(data.getJSONObject("mate_strategy"));
        }
        else mateStrategy = new SelectFirst();
        if(data.has("danger_strategy")){
            dangerStrategy = factory.createInstance(data.getJSONObject("danger_strategy"));
        }
        else dangerStrategy = new SelectFirst();
        if(data.has("pos")){
            JSONObject pos = data.getJSONObject("pos");

            JSONArray xRange = pos.getJSONArray("x_range");
			JSONArray yRange = pos.getJSONArray("y_range");

            if (xRange.length() < 2|| yRange.length() < 2)
			throw new IllegalArgumentException("Rangos de posición inválidos, deben tener dos elementos cada uno");

			double xmin = xRange.getDouble(0);
			double xmax = xRange.getDouble(1);
			double ymin = yRange.getDouble(0);
			double ymax = yRange.getDouble(1);

            
            double x = Utils.RAND.nextDouble(xmin, xmax+1);
            double y = Utils.RAND.nextDouble(ymin, ymax+1);

			position = new Vector2D(x, y);
        }
        else position = null;
        return new Wolf(mateStrategy, dangerStrategy, position);
    }

}
