package simulator.control;

import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Simulator;

public class Controller {
    private Simulator sim;
    public Controller(Simulator sim){
        this.sim = sim;
    }
    public void loadData(JSONObject data){
        if(data != null){
            JSONArray region = data.getJSONArray("regions");
            for(JSONArray r: region){

            }
        }
        else{
            throw new IllegalArgumentException("data is null");
        }

    }
    JSONObject
        {"row": [rf, rt], "col": [cf, ct], "spec": O}


          {"amount": N, "spec": O}
    
          public void run(double t, double dt, boolean sv, OutputStream out){
            
          }
}
