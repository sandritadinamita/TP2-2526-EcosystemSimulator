package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import netscape.javascript.JSObject;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
    private Simulator sim;
    public Controller(Simulator sim){
        this.sim = sim;
    }
    public void loadData(JSONObject data){
        if(data != null){
            if(data.has("regions")){
                JSONArray regiones = data.getJSONArray("regions");
                for(int i = 0; i < regiones.length(); i++){
                    JSONObject r = regiones.getJSONObject(i);
                    JSONArray rowRange = r.getJSONArray("row"); 
                    int rf = rowRange.getInt(0);
                    int rt = rowRange.getInt(1);
                    JSONArray colRange = r.getJSONArray("col"); 
                    int cf = colRange.getInt(0);
                    int ct = colRange.getInt(1);
                    JSONObject spec = r.getJSONObject("spec");
                    for (int row = rf; row <= rt; row++) {
                        for (int col = cf; col <= ct; col++) {
                            sim.setRegion(row, col, spec); //PREGUNTAR
                        }
                    }
                }

            }
            JSONArray animales = data.getJSONArray("animals");
            for(int i = 0; i < animales.length(); i++){
                JSONObject a = animales.getJSONObject(i);
                int amount = a.getInt("amount");
                JSONObject spec = a.getJSONObject("spec");
                for (int n = 0; n < amount; n++){
                    sim.addAnimal(spec);
                }
            }
        }

        else{
            throw new IllegalArgumentException("data is null");
        }

    }

    public void run(double t, double dt, boolean sv, OutputStream out){
        SimpleObjectViewer view = null;
        if (sv) {
            MapInfo m = sim.getMapInfo();
            view = new SimpleObjectViewer("[ECOSYSTEM]", m.getWidth(), m.getHeight(), m.getCols(), m.getRows());
            view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);
        }
        JSONObject initState = sim.asJSON();
        while(sim.getTime() <= t){
            sim.advance(dt);
            if (sv) view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);
        }
        JSONObject finalState = sim.asJSON();
        if (sv) view.close();

        PrintStream p = new PrintStream(out);
        JSONObject output = new JSONObject();
	output.put("in", initState);
	output.put("out", finalState);

	p.println(output.toString());


    }
    
    private List<ObjInfo> toAnimalsInfo(List<? extends AnimalInfo> animals) {
        List<ObjInfo> ol = new ArrayList<>(animals.size());
        for (AnimalInfo a : animals)
            ol.add(new ObjInfo(a.getGeneticCode(), (int) a.getPosition().getX(), (int) a.getPosition().getY(),(int)Math.round(a.getAge())+2));
        return ol;
    }
}
