package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.arpg.area.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG
{
	public final static float CAMERA_SCALE_FACTOR = 13.f;
	public final static float STEP = 0.05f;

	private ARPGPlayer player;
	private final String[] areas = {"zelda/Ferme", "zelda/Village", "zelda/Route", "zelda/Chateau", "zelda/RouteChateau", "zelda/RouteTemple", "zelda/Temple"};

	private int areaIndex;

	/**
	 * Add all the areas
	 */
	private void createAreas(){
		addArea(new Ferme());
		addArea(new Village());
		addArea(new Route());
		addArea(new Chateau());
		addArea(new RouteChateau());
		addArea(new RouteTemple());
		addArea(new Temple());
	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem)
	{
		if (super.begin(window, fileSystem))
		{
			createAreas();
			areaIndex = 0;
			Area area = setCurrentArea(areas[areaIndex], true);
			initPlayer(new ARPGPlayer(area, Orientation.DOWN, new DiscreteCoordinates(6, 10)));
			return true;
		}
		return false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	public void end() {
	}

	@Override
	public String getTitle() {
		return "NullPointerSlayer";
	}
}
