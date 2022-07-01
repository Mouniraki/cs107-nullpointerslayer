package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/** Arrow is a specific type of Projectile. */
public class Arrow extends Projectile {
	private final String spriteName = "zelda/arrow";
	private RPGSprite[] arrowSprite;
	private ARPGArrowHandler handler;
	private Orientation arrowOrientation;
	
	/**
	 * Default constructor for the Arrow entity.
	 * @param area (Area) : the area where it will be put.
	 * @param orientation (Orientation) : the orientation of the arrow.
	 * @param position (DiscreteCoordinates) : the position of the arrow on the grid.
	 * @param maxDistance (int) : the maximum distance it can travel.
	 * @param maxSpeed (int) : the speed at which it travels.
	 */
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int maxSpeed) {
		super(area, orientation, position, maxDistance, maxSpeed);
		arrowOrientation = orientation;
		handler = new ARPGArrowHandler();
		arrowSprite = new RPGSprite[4];
		for(int i=0; i<arrowSprite.length; ++i) {
			arrowSprite[i] = new RPGSprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(i*32, 0, 32, 32));
		}		
	}
	
	// Action when the arrow reached its maximum distance, or the limit of the map
	public void targetReached() {
		if(isDisplacementOccurs()) {
			getOwnerArea().leaveAreaCells(this, getCurrentCells());
			resetMotion();
			stopMovement();
		}
	}
	
	// Arrow extends Projectile
	public void update(float deltaTime) {
		moveOrientate(arrowOrientation);
        super.update(deltaTime);
	}
	
	public void draw(Canvas canvas) {
		switch(arrowOrientation) {
		case UP:
			arrowSprite[0].draw(canvas);
			break;
		case RIGHT :
			arrowSprite[1].draw(canvas);
			break;
		case DOWN :
			arrowSprite[2].draw(canvas);
			break;
		case LEFT :
			arrowSprite[3].draw(canvas);
			break;
		}
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	/**
	 * Specific handler of interactions for the Bomb.
	 */
	private class ARPGArrowHandler implements ARPGInteractionVisitor {
		public void interactWith(Grass grass) {
			grass.cut();
		}

		// Specific interaction with Bomb objects
		public void interactWith(Bomb bomb) {
			if(isDisplacementOccurs()) {
				bomb.explodes();
				targetReached();
			}
		}

		// Specific interaction with Monster entities
		public void interactWith(Monster monster) {
			for(DamageType type : monster.getVulnerabilities()) {
				if(type == DamageType.PHYSICAL && isDisplacementOccurs()) {
					monster.decreaseEnergy(1f);
					targetReached();
				}
			}
		}

		// Specific interaction with FireSpell objects
		public void interactWith(FireSpell f) {
			f.stopFire();
		}

		// Specific interaction with Orb objects
		public void interactWith(Orb orb) {
			if(isDisplacementOccurs()) {
				getOwnerArea().registerActor(new Bridge(getOwnerArea(), getOrientation(), new DiscreteCoordinates(15, 9)));
				targetReached();
			}

		}

	}
}
