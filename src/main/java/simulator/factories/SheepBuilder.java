package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends Builder<Animal>{
    private Factory <SelectionStrategy> factory;
    SelectionStrategy mateStrategy;
    SelectionStrategy dangerStrategy;
    Vector2D position;

    public SheepBuilder(String typeTag, String desc) {
        super("sheep", " ");
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

            JSONArray xr = pos.getJSONArray("x_range");
			JSONArray yr = pos.getJSONArray("y_range");

			double xmin = xr.getDouble(0);
			double xmax = xr.getDouble(1);
			double ymin = yr.getDouble(0);
			double ymax = yr.getDouble(1);

            double x = xmin + (xmax - xmin) * Utils.RAND.nextDouble();
			double y = ymin + (ymax - ymin) * Utils.RAND.nextDouble();

			position = new Vector2D(x, y);
        }
        else position = null;
        return new Sheep(mateStrategy, dangerStrategy, position);
    }


}
