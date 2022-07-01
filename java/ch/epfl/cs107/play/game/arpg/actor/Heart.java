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

public class Heart extends CollectableAreaEntity{
	private final int ANIMATION_DURATION = 2;
	private final String spriteName = "zelda/heart";
	private final int heartValue = 1;
	private RPGSprite[][] heartSprite;
	private Animation[] heartAnim;
	
	/**
	 * Default Heart Constructor.
	 * @param owner (Area) : the area where it will be registered.
	 * @param orientation (Orientation) : the orientation of the object.
	 * @param coordinates (DiscreteCoordinates) : the position of the object on the grid.
	 */
	public Heart(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		heartSprite = new RPGSprite[4][4];
		
		for(int i = 0; i < heartSprite[0].length; ++i) {
			heartSprite[0][i] = new RPGSprite(spriteName, 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		
		heartAnim = RPGSprite.createAnimations(ANIMATION_DURATION, heartSprite, true);
	}
	

	// Heart extends CollectableAreaEntity
	public void grabItem() {
		super.grabItem();
	} 
	
	public int getHeartValue() {
		return heartValue;
	}
	
	public void update(float deltaTime) {
		heartAnim[0].update(deltaTime);
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
		heartAnim[0].draw(canvas);
	}
}
