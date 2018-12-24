package game_engine;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import authoring.backend.MapSettings;
import game_data.Reader;
import game_data.Writer;
import game_object.GameObject;
import game_object.GameObjectManager;
import pathfinding.GridMap;
import scenemanager.SceneManager;
import transform_library.Transform;
import transform_library.Vector2;

/**
 * Codes for an instance of a game that is currently being run
 * @author andrew
 *
 */
public class GameInstance implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Sets up the GameInstance based on the information in the file
	 * @param filepath
	 */
	private boolean running;
	private GameInfo myGameInfo;
	private GameObjectManager myObjectManager;
	private TeamManager myTeamManager;
	private String background;
	private double gameTime;
	private List<ChatEntry> chat;
	private double mapHeight;
	private double mapWidth;
	private String mapName;
	private SceneManager mySceneManager;
	private transient Reader myReader = new Reader();
	private transient Writer myWriter = new Writer();
	
	public GameInstance(GameInfo g, GameObjectManager gom, MapSettings mapset, SceneManager sm) {

		myGameInfo = g;
		for(GameObject go: myGameInfo.getPossibleGameObjects())
		{
			go.setupImages();
		}
		myObjectManager = gom;
		myTeamManager = new TeamManager();
		chat = new ArrayList<ChatEntry>();
		running = false;
		
		mySceneManager = sm;
		try {
			setUp(mapset);
		}
		catch(Exception e) {}
	}
	public void setUp(MapSettings mapProperties) throws ClassNotFoundException, IOException {
		myTeamManager = new TeamManager();
		ResourceManager initialResources = mySceneManager.getTeams().get(0).getResourceManager();
		myTeamManager.addElement(mySceneManager.getTeams().get(0));
		for(int x = 2; x <= mapProperties.getNumPlayers(); x++) {
			Map<String,Double> initial = new HashMap<>();
			for(Entry<String,Double> s: initialResources.getResourceEntries()) {
				initial.put(s.getKey(), s.getValue());
			}
			myTeamManager.createTeam("Player " + x, new ResourceManager(initial));
		}
		mapName = mapProperties.getName();
		mapHeight = mapProperties.getMapHeight();
		mapWidth = mapProperties.getMapWidth();
		background = mapProperties.getImagePath();
	}
	/**
	 * Saves the information in the GameInstance to the specified file
	 * @param filepath
	 * @throws IOException 
	 */
	public void save(String filepath) throws IOException {
		List<Object> toWrite = new ArrayList<>();
		toWrite.add(this);
		myWriter.write(filepath,toWrite);
	}

	/**
	 * Read commands from the file that is updated by the GamePlayer
	 * @param filepath
	 */
	public void executeCommand(int source_id, int target_id, int interaction_ID, int xcor, int ycor) {
		if(!running)
			return;
		GridMap currentGridMap = new GridMap(mapHeight, mapWidth);
		myObjectManager.getGameObject(source_id).queueInteraction(myObjectManager.getGameObject(target_id), interaction_ID, myObjectManager, currentGridMap, new Vector2(xcor,ycor));
	}

	public void executeBuildCommand(int sourceID, String newUnitName, int interactionID, int xcor, int ycor) {
		if(!running)
			return;
		GameObject newGO = getGameInfo().get(newUnitName);
		newGO.setTransform(new Transform(new Vector2(xcor,ycor)));
		GameObject source = myObjectManager.getGameObject(sourceID);
		Vector2 direction = new Vector2(xcor,ycor);
		source.queueInteraction(newGO, interactionID, myObjectManager, getNewGridMap(), direction);
	}
	public void executeMovement(int id, double xcor, double ycor) {
		if(!running)
			return;
		Vector2 v = new Vector2(xcor,ycor);
		try {
		myObjectManager.getGameObject(id).queueMovement(v,myObjectManager,getNewGridMap());
		}
		catch(Exception e) {}
	}
	public GridMap getNewGridMap() {
		return new GridMap(mapHeight, mapWidth);
	}
	public void addToChat(int player_ID, String message) {
		chat.add(new ChatEntry(gameTime,player_ID,message));
	}

	public void loop() {
		new Thread(() ->{
		double startTime = 0;
		double endTime = 0;
		double diff = 0;
		while(running) {
			startTime = System.nanoTime();
			myObjectManager.runGameObjectLoop(diff);
			gameTime += diff;
			endTime = System.nanoTime();
			diff = endTime - startTime;
		}
		}).start();
	}
	public void play() {
		running = true;
		loop();
	}
	public void stop() {
		running = false;
	}
	public boolean getIsRunning() {
		return running;
	}
	public GameInfo getGameInfo() {
		return myGameInfo;
	}
	public GameObjectManager getGameObjects() {
		return myObjectManager;
	}
	public TeamManager getTeamManager() {
		return myTeamManager;
	}
	public String getBackground() {
		return background;
	}
	public double getGameTime(){
		return gameTime;
	}
	public List<ChatEntry> getChat(){
		return chat;
	}
	public String getName() {
		return mapName;
	}
	public SceneManager getSceneManager() {
		return mySceneManager;
	}
}
