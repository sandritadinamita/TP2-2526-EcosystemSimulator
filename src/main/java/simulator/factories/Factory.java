package simulator.factories;

public interface Factory<T> {
	public T createInstance(JSONObject info);
	public List<JSONObject> getInfo();
}