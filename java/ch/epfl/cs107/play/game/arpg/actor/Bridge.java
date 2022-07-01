package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/** Bridge is a specific type of AreaEntity */
public class Bridge extends AreaEntity {

	private final String bridgeSprite = "zelda/bridge";
	private RPGSprite bridge;
	
	/**
	 * Default constructor for the entity Bridge.
	 * @param area (Area) : the area where it will be displayed.
	 * @param orientation (Orientation) : the orientation of the image.
	 * @param position (DiscreteCoordinates) : the position of the bridge on the grid.
	 */
	public Bridge(Area area, Orientation orientation, DiscreteCoordinates position) {

		super(area, orientation, position);
		bridge = new RPGSprite(bridgeSprite, 4.f, 4.f, this, new RegionOfInterest(0, 0, 64, 48));
		bridge.setDepth(0f);
	}
	

	// Bridge extends AreaEntity
	@Override
	public void draw(Canvas canvas) {
		bridge.draw(canvas);
	}
	
	@Override
    public boolean takeCellSpace() {
        return false;
    }

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {}

}
