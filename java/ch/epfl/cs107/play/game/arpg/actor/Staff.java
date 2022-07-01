package ch.epfl.cs107.play.game.arpg.actor;

import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends CollectableAreaEntity {
	private final String spriteName = "zelda/staff";
	private RPGSprite[][] staffSprite;
	private Animation[] staffAnim;
	private final int ANIMATION_DURATION = 4;
	
	/**
	 * Default Staff constructor.
	 * @param owner (Area) : the area where it will be registered.
	 * @param orientation (Orientation) : the orientation of the Staff.
	 * @param coordinates (DiscreteCoordinates) : the position of the staff on the grid.
	 */
	public Staff(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		staffSprite = new RPGSprite[4][8];
		for(int i=0; i<staffSprite[0].length; ++i) {
			staffSprite[0][i] = new RPGSprite(spriteName, 2, 2, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		staffAnim = RPGSprite.createAnimations(ANIMATION_DURATION, staffSprite, true);
	}
	

	//Staff extends CollectableAreaEntity
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
	public void update(float deltaTime) {
		staffAnim[0].update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		staffAnim[0].draw(canvas);
	}
}
