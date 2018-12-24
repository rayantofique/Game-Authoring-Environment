package game_object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import authoring.backend.MainComponentPropertyManager;
import game_engine.ElementManager;
import game_engine.EngineObject;
import game_engine.Team;
import transform_library.Transform;
import transform_library.Vector2;

/**
 * 
 * @author Rayan, shichengrao
 * Allows access to the gameobjects in the scene by runnning their update methods.
 * Allows the game engine to restrict access of gameobjects from the game player
 */

public class GameObjectManager extends ElementManager implements Serializable{

	
	public GameObjectManager()
	{
		super();
	}
		
	public GameObjectManager(GameObjectManager other)
	{
		super(other);
	}
	
	/**
	 * 
	 * @param gameObjects
	 * Transfer gameobjects between managers
	 */
	public void transferGameObjects(GameObjectManager gameObjects)
	{
		this.clearManager();
		List<GameObject> objList = gameObjects.getElements();
		this.changeBaseID(objList.size());
		for(GameObject obj : objList)
		{
			this.addElement(obj);
		}
	}
	
	
	public int createGameObject(Vector2 startingPosition, List<String> tags, String name, Team t)
	{
		int newID = calculateID();
		GameObject obj = new GameObject(newID, startingPosition, tags, name, t);
		this.addElement(obj);
		return newID;
	}
	
	public int createGameObject(Transform transform, ObjectLogic logic, MainComponentPropertyManager manager, Team team )
	{
		int newID = calculateID();
		GameObject obj = new GameObject(newID, transform, logic, manager, team);
		this.addElement(obj);
		return newID;
	}
	
	public int copyGameObject(GameObject other)
	{
		int newID = calculateID();
		System.out.println("new id: " + newID);
		GameObject copy = new GameObject(newID, other);
		this.addElement(copy);
		
		return newID;
	}
	
	
	public List<GameObject> getElements()
	{
		List<GameObject> gameObjects = new ArrayList<>();
		
		for (EngineObject eObj : getElementsRaw()) {
			GameObject gObj = (GameObject) eObj;
			gameObjects.add(gObj);
		}
		return gameObjects;
	}
	
	
	
	/**
	 *  This will allow the game player to cycle through all the objects and runs their game loop.
	 *  Also removes dead elements at the start of every cycle.
	 */
	public void runGameObjectLoop(double stepTime)
	{
		for(GameObject obj : getElements())
		{
			obj.setElapsedTime(stepTime);
			obj.Update();
		}
		
		
	}
	
	/**
	 * 
	 * @param target
	 * @return
	 * Game player can use this to move a specific game object to a position on the map 
	
	public void queueMovement(GameObject obj, Vector2 target)
	{
		obj.queueMovement(target);
	} */
	
	/**
	 * 
	 * @return
	 * Returns positional information about each object. Can be used to populate grid for pathfinding
	 */
	public List<Transform> accessGameObjectTransforms()
	{
		List<Transform> transformList = new ArrayList<>();
		
		for(EngineObject g : getElements())
		{
			GameObject gObj = (GameObject) g;
			transformList.add(gObj.getTransform());
		}
		
		return Collections.unmodifiableList(transformList);
		
		
	}


	public GameObject getGameObject(int id)
	{
		return (GameObject)(this.get(id));
	}
	
	public void setupImages() {
		for(EngineObject g : getElements())
		{
			GameObject gObj = (GameObject) g;
			gObj.setupImages();
		}
	}
	
}
