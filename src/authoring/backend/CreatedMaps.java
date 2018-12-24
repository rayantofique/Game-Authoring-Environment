package authoring.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import observables.Listener;
import observables.Speaker;
/**
 * Holds all created MapEntities
 * @author xlany
 *
 */
public class CreatedMaps implements Speaker {
	private List<MapEntity> createdmaps;
	private static List<Listener> listeners;
	/**
	 * Constructor initializes necessities
	 */
	public CreatedMaps() {
		createdmaps = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	/**
	 * Set list of created maps but input list
	 * @param maps
	 */
	public void setCreatedMaps(List<Object> mapLocations, List<Object> mapSettings) {
		createdmaps.clear();
		for(int i=0; i<mapLocations.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<AuthoringObject, List<AuthoringObject>> location = (Map<AuthoringObject, List<AuthoringObject>>) mapLocations.get(i);
			MapSettings settings = (MapSettings) mapSettings.get(i);
			MapEntity map = new MapEntity(location, settings);
			addMap(map);
		}
	}
	/**
	 * Adds
	 * @param myMap to list of created maps
	 */
	public void addMap(MapEntity myMap) {
		createdmaps.add(myMap);
	}
	/**
	 * 
	 * @return list of created maps
	 */
	public List<MapEntity> getCreatedMaps() {
		return createdmaps;
	}
	/**
	 * Create new MapEntity and adds to list of created maps
	 * @return created MapEntity
	 */
	public MapEntity makeNewMap() {
		MapEntity newmap = new MapEntity();
		createdmaps.add(newmap);
		notifyListeners();
		return newmap;
	}
	/**
	 * Get MapEntity from list of created maps by
	 * @param index
	 * @return
	 */
	public MapEntity getObjectByIndex(int index) {
		return createdmaps.get(index);
	}
	/**
	 * 
	 * @return number of created maps
	 */
	public int getSize() {
		return createdmaps.size();
	}
	/**
	 * @see
	 */
	@Override
	public void addListener(Listener l) {
		listeners.add(l);
	}
	/**
	 * @see
	 */
	@Override
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	/**
	 * @see
	 */
	@Override
	public void notifyListeners() {
		for (Listener l: listeners) {
			l.update();
		}
	}
}
