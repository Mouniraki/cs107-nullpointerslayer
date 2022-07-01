package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile {

	private final ARPGMagicWaterProjectileHandler handler = new ARPGMagicWaterProjectileHandler();
	private final String spriteName = "zelda/magicWaterProjectile";
	private Sprite[][] graphics;
	private Animation[] animation;
	
	/**
	 * Default MagicWaterProjectile constructor
	 * @param area (Area) : the area where it will be registered.
	 * @param orientation (Orientation) : the orientation of the object
	 * @param position (DiscreteCoordinates) : the position of the object on the grid
	 * @param maxDistance (int) : the maximum distance it can travel
	 * @param maxSpeed (int) : the maximum speed at which it can travel
	 */
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int maxSpeed) {

		super(area, orientation, position, maxDistance, maxSpeed);
		graphics = new RPGSprite[4][4];
		for(int i = 0; i < graphics[0].length; ++i) {
			graphics[0][i] = new RPGSprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(i * 32, 0, 32, 32));
		}		
		animation = RPGSprite.createAnimations(maxSpeed, graphics, true);
	}

	// Action when the projectile reached its maximum distance, or the limit of the map
	public void targetReached() {
		if(isDisplacementOccurs()) {
			getOwnerArea().leaveAreaCells(this, getCurrentCells());
			resetMotion();
			stopMovement();
		}
	}
	
	// MagicWaterProjectile extends Projectile
	public void update(float deltaTime) {
		moveOrientate(getOrientation());
		animation[0].update(deltaTime);
        super.update(deltaTime);
	}
	
	public void draw(Canvas canvas) {
		animation[0].draw(canvas);
	}
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	// Specific handler of interactions for MagicWaterProjectile
	private class ARPGMagicWaterProjectileHandler implements ARPGInteractionVisitor {

		// Specific interaction with Monster entities
		public void interactWith(Monster monster) {
			for (DamageType type : monster.getVulnerabilities()) {
				if (type == DamageType.MAGIC && isDisplacementOccurs()) {
					monster.decreaseEnergy(1f);
					targetReached();
				}
			}
		}

		// Specific interaction with FireSpell objects
		public void interactWith(FireSpell f) {
			f.stopFire();
		}
	}
}
