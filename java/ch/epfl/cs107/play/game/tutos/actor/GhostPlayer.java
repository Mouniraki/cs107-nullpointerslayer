package ch.epfl.cs107.play.game.tutos.actor;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior.Tuto2CellType;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import java.awt.Color;

public class GhostPlayer extends MovableAreaEntity {
	private final static int ANIMATION_DURATION = 8;
	private TextGraphics hpText;
	private Sprite sprite;
	private float energyLevel;
	private boolean isPassingDoor = true;
	
	public GhostPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
		super(area, orientation, position);
		this.energyLevel = 10;
		this.hpText = new TextGraphics(Integer.toString((int)energyLevel), 0.4f, Color.BLUE);
		hpText.setParent(this);
		hpText.setAnchor(new Vector(-0.3f, 0.1f));
		this.sprite = new Sprite(spriteName, 1, 1.f, this);
		resetMotion();
	}
	
	public void strengthen() {
		energyLevel = 10;
	}
	
	public boolean isWeak() {
		return energyLevel <= 0.f;
	}
	
	public void update(float deltaTime) {
		if (energyLevel > 0) {
			energyLevel-=deltaTime;
			hpText.setText(Integer.toString((int)energyLevel));
		}
		else if(energyLevel < 0) {
			energyLevel = 0;
		}
		Keyboard keyboard = getOwnerArea().getKeyboard();
		moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
		moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
		moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
		moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
		super.update(deltaTime);
		
		List<DiscreteCoordinates> cells = getCurrentCells();
		if(cells!=null) {
			for(DiscreteCoordinates c : cells) {
				if(((Tuto2Area)getOwnerArea()).isDoor(c)) {
					setIsPassingADoor();
				}
			}
		}
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
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
		return true;
	}

	@Override
	public void draw(Canvas canvas) {
		this.sprite.draw(canvas);
		this.hpText.draw(canvas);
		
	}
	
	public void moveOrientate(Orientation orientation, Button b) {
		if(b.isDown()) {
			if(getOrientation() == orientation) {
				move(this.ANIMATION_DURATION);
			}
			else {
				orientate(orientation);
			}
		}
		
	}
	
	public void enterArea(Area area, DiscreteCoordinates position) {
		area.registerActor(this);
		area.setViewCandidate(this);
		setOwnerArea(area);
		this.setCurrentPosition(position.toVector());
		this.resetMotion();
	}
	
	public void leaveArea() {
		getOwnerArea().unregisterActor(this);
	}
	
	protected void setIsPassingADoor() {
		this.isPassingDoor = true;
	}
	
	public boolean isPassingADoor() {
		return this.isPassingDoor;
	}
	public boolean resetDoorState() {
		return this.isPassingDoor = false;
	}
	private void moveOrientation(Orientation orientation, Button b) {
		if(b.isDown()) {
			if(getOrientation() == orientation) {
				move(ANIMATION_DURATION);
			}
		}
	} 
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {}
	
}
