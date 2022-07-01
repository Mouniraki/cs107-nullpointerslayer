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

public class Coin extends CollectableAreaEntity{
	private final int ANIMATION_DURATION = 2;
	private final String spriteName = "zelda/coin";
	private final int coinValue = 50;
	private RPGSprite[][] coinSprite;
	private Animation[] coinAnim;
	
	/**
	 * Default Coin constructor.
	 * @param owner (Area) : the area where it will be displayed.
	 * @param orientation (Orientation) : the orientation of the object.
	 * @param coordinates (DiscreteCoordinates) : the coordinates of the object on the grid.
	 */
	public Coin(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		coinSprite = new RPGSprite[4][4];
		
		for(int i = 0; i < coinSprite[0].length; ++i) {
			coinSprite[0][i] = new RPGSprite(spriteName, 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		
		coinAnim = RPGSprite.createAnimations(ANIMATION_DURATION, coinSprite, true);
	}
	
	// Coin extends CollectableAreaEntity
	public void grabItem() {
		super.grabItem();
	} 
	
	public int getCoinValue() {
		return coinValue;
	}
	
	public void update(float deltaTime) {
		coinAnim[0].update(deltaTime);
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
		coinAnim[0].draw(canvas);
	}
}
