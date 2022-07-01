package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Bomb extends AreaEntity implements Interactor {

	// Handler of interactions of the bomb
	private final ARPGBombHandler handler = new ARPGBombHandler();
	private final int ANIMATION_DURATION = 5;

	//Constants for the names of the images files
	private final String bombName = "zelda/bomb";
	private final String explosionName = "zelda/explosion";

	private boolean isExploding;
	private int delay; // The delay befores it explodes
	private Sprite[][] bomb;
	private Animation[] bombAnim;
	private Sprite[][] explosion;
	private Animation[] explosionAnim;
	/**
	 * Bomb constructor
	 *
	 * @param owner (Area): Owner area. Not null
	 * @param orientation (Orientation): Initial orientation of the bomb in the Area. Not null
	 * @param coordinates (DiscreteCoordinate): Initial position of the bomb in the Area. Not null
	 */
	public Bomb(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		delay = 100;
		isExploding = false;
		bomb = new Sprite[4][2];
		for(int i = 0; i < bomb[0].length; ++i) {
			bomb[0][i] = new RPGSprite(bombName, 1, 1, this, new RegionOfInterest(i * 16, 0, 16, 16));
		}
		bombAnim = RPGSprite.createAnimations(ANIMATION_DURATION, bomb, true);
		explosion = new Sprite[4][7];	
		for(int i = 0; i < explosion[0].length; ++i) {
			explosion[0][i] = new RPGSprite(explosionName, 1, 1, this, new RegionOfInterest(i * 32, 0, 32, 32));
		}
		explosionAnim = RPGSprite.createAnimations(ANIMATION_DURATION / 2, explosion, false);
	}

	// Defining the explosion of a bomb
	public void explodes() {
		if (!isExploding) {
			isExploding = true;
		}
		if (explosionAnim[0].isCompleted()) {
			getOwnerArea().unregisterActor(this);
		}
	}
		

	// Bomb extends AreaEntity implements Interactor
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	// Directly adjacent cells are the field of view cells of a Bomb
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List <DiscreteCoordinates> nearestPoints = new ArrayList<>();
		nearestPoints.add(getCurrentMainCellCoordinates().jump(-1, 0));
		nearestPoints.add(getCurrentMainCellCoordinates().jump(0, -1));
		nearestPoints.add(getCurrentMainCellCoordinates().jump(1, 0));
		nearestPoints.add(getCurrentMainCellCoordinates().jump(0, 1));
		return nearestPoints;
	}
	
	@Override
	public boolean wantsCellInteraction() {
		return isExploding;
	}

	@Override
	public boolean wantsViewInteraction() {
		return isExploding;
	}
	
	@Override
	public void update(float deltaTime){
		if (delay > 0 && !isExploding) {
			delay -= deltaTime;
			bombAnim[0].update(deltaTime);
		}
		else {
			explodes();
			explosionAnim[0].update(deltaTime);
		}
		super.update(deltaTime);
	}
	
	
	@Override
	public boolean takeCellSpace() {
		return !isExploding;
	}
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

	@Override
	public boolean isCellInteractable() {
		return !isExploding;
	}

	@Override
	public boolean isViewInteractable() {
		return isExploding;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isExploding) {
			if (!explosionAnim[0].isCompleted()) explosionAnim[0].draw(canvas);
		}
		else {
			bombAnim[0].draw(canvas);
		}
	}
	

	/**
	 * Specific handler of interactions for the Bomb.
	 */
	private class ARPGBombHandler implements ARPGInteractionVisitor {
		// Specific interaction with Grass objects
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}

		// Specific interaction with Monster entities
		@Override
		public void interactWith(Monster monster) {
			for (DamageType vulnerability : monster.getVulnerabilities()) {
				if (vulnerability == DamageType.PHYSICAL)
					monster.decreaseEnergy(1.f);
			}
		}

		// Specific interaction with Player entities
		@Override
		public void interactWith(Player player) { ((ARPGPlayer) player).decreaseEnergy(1.f); }

		// Specific interaction with FlameSkull entities
		public void interactWith(FlameSkull flameSkull) {
			flameSkull.dies();
		}
	}

}
