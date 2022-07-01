package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity implements Interactable{

	// Constant probability to drop an item
	private final double ITEM_DROP_RATE = 0.4;
	// Constant probability to drop a heart
	private final double HEART_DROP_RATE = 0.3;
	// Constant for the animation
	private final int ANIMATION_DURATION = 4;
	// Constants for the names of the sprites to load
	private final String grassName = "zelda/grass";
	private final String cutGrassName = "zelda/grass.sliced";

	private Sprite grass;
	private Sprite cutGrass[][];
	private Animation[] animGrass;
	//Used to inform that entities can take its cell space when it is cut
	private boolean isCut;
	//Used to stop the cut animation
	private boolean alreadyCut;

	/**
	 * Default constructor for the Grass entity.
	 * @param owner (Area) : the area where it will be displayed, not null
	 * @param orientation (Orientation) : orientation of the object, not really representative here, not null
	 * @param coordinates(DiscreteCoordinates) : the coordinates of the cell where it will be put, not null
	 */
	public Grass(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		grass = new RPGSprite(grassName, 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		cutGrass = new Sprite[4][4];
		for (int i = 0; i < cutGrass[0].length; ++i) {
			cutGrass[0][i] = new RPGSprite(cutGrassName, 2, 2, this, new RegionOfInterest(i * 32, 0, 32, 32));
		}
		animGrass = RPGSprite.createAnimations(ANIMATION_DURATION, cutGrass, false);
		alreadyCut = false;
	}

	// Grass extends AreaEntity implements Interactable
	@Override
	public void update(float deltaTime) {
		if (isCut) {
			animGrass[0].update(deltaTime);
		}
		super.update(deltaTime);
	}

	/**
	 * Random item picker that chooses between a Coin or a Heart to display when a Grass is cut.
	 * @return the choosen item
	 */
	public CollectableAreaEntity pickRandomItem() {
		double random = RandomGenerator.getInstance().nextDouble();
		if (random < ITEM_DROP_RATE) {
			return new Coin(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
		}
		else if (random >= ITEM_DROP_RATE && random < HEART_DROP_RATE + ITEM_DROP_RATE) {
			return new Heart(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
		}
		else return null;
	}
	
	// Cut the grass
	public void cut() {
		isCut = true;
		if (!alreadyCut) {
			CollectableAreaEntity loot = pickRandomItem();
			if (loot != null) getOwnerArea().registerActor(loot);
			if (animGrass[0].isCompleted()) {
				getOwnerArea().unregisterActor(this);
			}
			alreadyCut = true;
		}
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
			return !isCut;
	}

	@Override
	public boolean isCellInteractable() { return true; }

	@Override
	public boolean isViewInteractable() { return true; }

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (isCut) {
			if (!animGrass[0].isCompleted()) {
				animGrass[0].draw(canvas);
			}
		}
		else {
			grass.draw(canvas);
		}
		
	}
}
