package ch.epfl.cs107.play.game.arpg.actor;

import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class CastleKey extends CollectableAreaEntity{
	private final String spriteName = "zelda/key";
	private RPGSprite keySprite;
	
	/**
	 * Default CastleKey constructor
	 * @param owner (Area) : the area where it will be displayed.
	 * @param orientation (Orientation) : the orientation of the CastleKey
	 * @param coordinates (DiscreteCoordinates) : the coordinates of the object on the grid
	 */
	public CastleKey(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
			keySprite = new RPGSprite(spriteName, 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	}
	
	// CastleKey extends CollectableAreaEntity
	public void grabItem() {
		super.grabItem();
	} 
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return super.getCurrentCells();
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	@Override
	public void draw(Canvas canvas) {
		keySprite.draw(canvas);
	}
}
