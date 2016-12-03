package MVC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelAndView {

	private String viewName;
	private Map<String, Object> newobjects = new ConcurrentHashMap<String, Object>();
	private Map<String, String> oldobjects = new ConcurrentHashMap<String, String>();
	
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void addObject(String name, Object object) {
		newobjects.put(name, object);
	}

	public Object getObject(String key) {
		return newobjects.get(key);
	}

	public Object getMap(String key) {
		return oldobjects.get(key);
	}

	public void setMap(String key, String value) {
		oldobjects.put(key, value);
	}
	
}
