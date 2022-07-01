package ch.epfl.cs107.play.game.areagame.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class CollectableAreaEntity extends AreaEntity {

	public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
	}

	public void grabItem() {
		this.getOwnerArea().unregisterActor(this);
	}
	
	@Override
	public boolean takeCellSpace() {
		return false;
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
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		
	}
	
	

}
