package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door{
	private final String doorOpen = "zelda/castleDoor.open";
	private final String doorClose = "zelda/castleDoor.close";
	private RPGSprite doorOpenSprite;
	private RPGSprite doorCloseSprite;
	private boolean open;
	
	/**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates position) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		doorCloseSprite = new RPGSprite(doorClose, 2.f, 2.f, this, new RegionOfInterest(0, 0, 32, 32));
		doorOpenSprite = new RPGSprite(doorOpen, 2.f, 2.f, this, new RegionOfInterest(0, 0, 32, 32));
		open = false;
	}
	 /**
     * Complementary CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param otherCells (DiscreteCoordinates...): Other cells occupied by the AreaEntity if any. Assume absolute coordinates, not null
     */
	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
		super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);
		doorCloseSprite = new RPGSprite(doorClose, 2.f, 2.f, this, new RegionOfInterest(0, 0, 32, 32));
		doorOpenSprite = new RPGSprite(doorOpen, 2.f, 2.f, this, new RegionOfInterest(0, 0, 32, 32));
		open = false;
	}
	
	/**
	 * Setter for the state of the CastleDoor.
	 * @param value (boolean) : state of the door (true if open, false if closed)
	 */
	public void setOpen(boolean value) {
		open = value;
		setSignal(Logic.TRUE);
	}

	
	// CastleDoor extends Door
	protected void setSignal(Logic signal) {
		super.setSignal(signal);
	}
	
	public boolean isOpen() {
	   	return open;
	}

	public void draw(Canvas canvas) {
		if (isOpen()) {
			this.doorOpenSprite.draw(canvas);
		}
		else {
			this.doorCloseSprite.draw(canvas);
		}
	}
	
	@Override
    public boolean takeCellSpace() {
        return !isOpen();
    }

    @Override
    public boolean isViewInteractable(){
        return !isOpen();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
