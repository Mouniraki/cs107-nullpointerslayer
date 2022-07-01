package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * A Projectile is a type of MovableAreaEntity which can fly
 */
public abstract class Projectile extends MovableAreaEntity implements FlyableEntity, Interactor {
	private int maxDistance;
	private int movingSpeed;
	private DiscreteCoordinates mainCoordinate;
	
	/**
	 * Default constructor for the type Projectile.
	 * @param area (Area) : the area where it will be registered
	 * @param orientation (Orientation) : the orientation of the projectile, used to orientate the movement of the projectile
	 * @param position (DiscreteCoordinates) : the coordinates where to spawn the projectile initially
	 * @param aMaxDistance (int) : the maximal distance it can travel
	 * @param aMovingSpeed (int) : the speed at which it travels
	 */
	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int aMaxDistance, int aMovingSpeed) {
		super(area, orientation, position);
		maxDistance = aMaxDistance;
		movingSpeed = aMovingSpeed;
		mainCoordinate = position;
	}
	
	// Stops the movement of the projectile if it reaches the limit of the map, or if it reaches its maximum distance
	public void stopMovement() {
		if(!isDisplacementOccurs() || DiscreteCoordinates.distanceBetween(mainCoordinate, getCurrentMainCellCoordinates()) > maxDistance) {
			getOwnerArea().leaveAreaCells(this, getCurrentCells());
			resetMotion();
			getOwnerArea().unregisterActor(this);
		}
	}	
	
	/**
	 * Setter for the orientation of the projectile.
	 * @param orientation (Orientation) : the orientation of the projectile.
	 */
	public void moveOrientate(Orientation orientation) {
		if (getOrientation() == orientation) {
			move(movingSpeed);
		}
	}

	// Projectile extends MovableAreaEntity implements FlyableEntity, Interactor
	@Override
	public void update(float deltaTime) {
		stopMovement();
		super.update(deltaTime);
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return canFly();
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

}
