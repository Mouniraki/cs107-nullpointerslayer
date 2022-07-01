package ch.epfl.cs107.play.game.arpg.actor;

import java.security.Key;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * Specific implementation of Player for an ARPG game.
 */
public class ARPGPlayer extends Player
{
	private final ARPGPlayerHandler handler = new ARPGPlayerHandler();
	private final int MAX_ENERGY = 10;
	// Frames of immunity after the player takes damage
	private final int MAX_IMMUNE_FRAMES = 24;
	// Animation duration in frame number
	private final int ANIMATION_DURATION = 4;

	private float energy;
	private int immuneFrames;
	private int currentState;

	// Variables for ARPGPlayer's graphics
	private final String spriteName = "zelda/player";
	private Sprite[][] graphics;
	private Animation[] animation;
	// Variables for ARPGPlayer's sword attack graphics
	private final String swordSpriteName = "zelda/player.sword";
	private Sprite[][] swordGraphics;
	private Animation[] swordAnimation;
	// Variables for ARPGPlayer's bow attack graphics
	private final String bowSpriteName = "zelda/player.bow";
	private Sprite[][] bowGraphics;
	private Animation[] bowAnimation;
	// Variables for ARPGPlayer's magic attack graphics
	private final String magicSpriteName = "zelda/player.staff_water";
	private Sprite[][] magicGraphics;
	private Animation[] magicAnimation;

	// Used to load an entity when interaction takes place
	private AreaEntity entity;
	private ARPGInventory playerInventory;
	private ARPGStatusGUI playerGUI;

	// Defining the possible states for ARPGPlayer
	private final String[] STATES = new String[] {"IDLE", "USING_SWORD", "USING_BOW", "USING_MAGIC"};

	// Index of the inventory array
	private int itemIndex = 0;

	/**
	 * ARPGPlayer constructor
	 * @param owner (Area): Owner Area, not null
	 * @param orientation (Orientation): Initial player orientation, not null
	 * @param coordinates (Coordinates): Initial position, not null
	 */
    public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates)
	{
    	super(owner, orientation, coordinates);
		playerInventory = new ARPGInventory();
    	energy = MAX_ENERGY; // DEFAULT ENERGY VALUE
		immuneFrames = 0;
		// IDLE GRAPHICS
		graphics = RPGSprite.extractSprites(spriteName, 4, 1, 2, this ,16, 32, new Orientation[]
				{ Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT });
		animation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, graphics);
		// SWORD ATTACK GRAPHICS
		swordGraphics = RPGSprite.extractSprites(swordSpriteName, 4, 2, 2, this ,32, 32, new Orientation[]
				{ Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		swordAnimation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, swordGraphics, false);
		// BOW ATTACK GRAPHICS
		bowGraphics = RPGSprite.extractSprites(bowSpriteName, 4, 2, 2, this ,32, 32, new Orientation[]
				{ Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		bowAnimation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, bowGraphics, false);
		// MAGIC ATTACK GRAPHICS
		magicGraphics = RPGSprite.extractSprites(magicSpriteName, 4, 2, 2, this ,32, 32, new Orientation[]
				{ Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		magicAnimation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, magicGraphics, false);
		// SETTING THE INVENTORY AND STATUS UP
		playerGUI = new ARPGStatusGUI(this);
		playerInventory.addItem(ARPGItem.SWORD, 1);
		playerInventory.addItem(ARPGItem.BOMB, 1);
		playerInventory.addItem(ARPGItem.BOW, 1);
		playerInventory.addItem(ARPGItem.ARROW, 10);
		resetMotion();
    }

	// TESTING ITEM POSSESSION
	public boolean possesses(InventoryItem item) {
		return playerInventory.isInInventory(item);
	}

	// ITEM SELECTOR
	public int selectItemIndex() {
		if (possesses(playerInventory.getItem(itemIndex)) && itemIndex < playerInventory.getSize() - 1) {
			++itemIndex;
		} else {
			itemIndex = 0;
		}
		return itemIndex;
	}

	/**
	 * Getter for the index of an item stored in the inventory
	 * @return (int)
	 */
	public int getItemIndex() {
		return itemIndex;
	}

	// ITEM SELECTOR
	public InventoryItem selectItem(int index) {
		if(index < playerInventory.getSize()) {
			return playerInventory.getItem(index);
		}
		return null;
	}

	public void useItem() {
    	handler.use((ARPGItem) selectItem(itemIndex));
	}

	/**
	 * Decreases the amount of energy of the player.
	 * @param amount (float) : The amount of energy to remove
	 */
	public void decreaseEnergy(float amount) {
		if (immuneFrames == 0) {
			if (amount > energy) energy = 0;
			else energy -= amount;
			immuneFrames = MAX_IMMUNE_FRAMES;
		}
	}

	/**
	 * Getter for the maximum energy level of the player
	 * @return (int)
	 */
	public int getMaxPlayerEnergy() {
		return MAX_ENERGY;
	}

	/**
	 * Getter for the actual energy level of the player
	 * @return (float)
	 */
	public float getPlayerEnergy() {
		return energy;
	}

	/**
	 * Setter for the amount of energy to add to the player
	 * @param heartValue (int) : the amount of energy to add
	 */
	private void setPlayerEnergy(int heartValue) {
		if (energy + heartValue <= MAX_ENERGY) {
			energy += heartValue;
		}

		else {
			energy  = MAX_ENERGY;
		}
	}

	/**
	 * Getter for the current amount of money of the player
	 * @return (int)
	 */
	public int getPlayerMoney() {
		return playerInventory.getMoney();
	}

	/**
	 * Getter for the maximum amount of money of the player
	 * @return (int)
	 */
	public int getMaxPlayerMoney() {
		return playerInventory.getMaxMoney();
	}

	/**
	 * Setter for the amount of money to add to the inventory
	 * @param money (int) : the amount of money to add
	 */
	public void setPlayerMoney(int money) {
		playerInventory.addMoney(money);
	}

	/**
	 * Getter for the size of the inventory
	 * @return (int)
	 */
	protected int getInventorySize() {
		return playerInventory.getSize();
	}
    
	@Override
	public void update(float deltaTime) {

		if (energy < 0) energy = 0.f;
		// Operates for the movement of the player
		Keyboard keyboard = getOwnerArea().getKeyboard();
		if (currentState == 0) {
			moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
			moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
			moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
			moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
		}
		/**
		 * Switch for the different states of the player
		 * case 0 : Idle state
		 * case 1 : Sword attack state
		 * case 2 : Bow attack state
		 * case 3 : Magic attack state
		 */
		switch (currentState) {
			case 0:
				if (isDisplacementOccurs()) {
					animation[getOrientation().ordinal()].update(deltaTime);
				}
				else {
					animation[getOrientation().ordinal()].reset();
				}
				break;
			case 1:
				swordAnimation[getOrientation().ordinal()].update(deltaTime);
				if (swordAnimation[getOrientation().ordinal()].isCompleted()) {
					swordAnimation[getOrientation().ordinal()].reset();
					currentState = 0;
				}
				break;
			case 2:
				bowAnimation[getOrientation().ordinal()].update(deltaTime);
				if (bowAnimation[getOrientation().ordinal()].isCompleted()) {
					bowAnimation[getOrientation().ordinal()].reset();
					currentState = 0;
				}
				break;
			case 3:
				magicAnimation[getOrientation().ordinal()].update(deltaTime);
				if (magicAnimation[getOrientation().ordinal()].isCompleted()) {
					magicAnimation[getOrientation().ordinal()].reset();
					currentState = 0;
				}
				break;
		}
		// Item pick
		if (keyboard.get(Keyboard.TAB).isPressed()) {
			if (playerInventory.getSize() > 0) {
				selectItemIndex();
			}
		}
		// Item use
		if (keyboard.get(Keyboard.SPACE).isDown()) {
			if (playerInventory.getSize() > 0 && currentState == 0) {
				useItem();
			}
		}
		// Image update according to current time
		playerGUI.update(deltaTime);
		if (immuneFrames > 0) --immuneFrames;
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
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		Keyboard keyboard = getOwnerArea().getKeyboard();
		return keyboard.get(Keyboard.E).isPressed();
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return (immuneFrames == 0);
	}

	@Override
	public boolean isViewInteractable() {
		return (immuneFrames == 0);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
    	// Draws the animation only half the time if immuneFrames is more than 0 (Simulates blinking)
		if ((immuneFrames > 0 && (immuneFrames % 2 == 0)) || (immuneFrames == 0))
		switch (currentState) {
			case 0:
				animation[getOrientation().ordinal()].draw(canvas);
				break;
			case 1:
				swordAnimation[getOrientation().ordinal()].draw(canvas);
				break;
			case 2:
				bowAnimation[getOrientation().ordinal()].draw(canvas);
				break;
			case 3:
				magicAnimation[getOrientation().ordinal()].draw(canvas);
				break;
		}

		playerGUI.draw(canvas);
	}

	/**
	 * Operates for the movement of the player.
	 * @param orientation (Orientation) : the orientation for the movement.
	 * @param b (Button) : the button to be pressed to do the movement.
	 */
	public void moveOrientate(Orientation orientation, Button b)
	{
		if (b.isDown())
		{
			if (getOrientation() == orientation) {
				move(this.ANIMATION_DURATION);
			}
			else {
				orientate(orientation);
			}
		}
		
	}
	/**
	 * Specific handler of interactions for the ARPGPlayer.
	 */
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		// Specific interaction with Door objects
		@Override
		public void interactWith(Door door){
			setIsPassingADoor(door);
		}

		// Specific interaction with Grass objects
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}

		// Specific interaction with Coin objects
		@Override
		public void interactWith(Coin coin) {
			coin.grabItem();
			setPlayerMoney(coin.getCoinValue());
		}

		// Specific interaction with Heart objects
		@Override
		public void interactWith(Heart heart) {
			heart.grabItem();
			setPlayerEnergy(heart.getHeartValue());
		}

		// Specific interaction with CastleKey objects
		@Override
		public void interactWith(CastleKey castleKey) {
			castleKey.grabItem();
			playerInventory.addItem(ARPGItem.CASTLE_KEY, 1);
		}

		// Specific interaction with Staff objects
		@Override
		public void interactWith(Staff staff) {
			staff.grabItem();
			playerInventory.addItem(ARPGItem.STAFF, 1);
		}

		// Specific interaction with CastleDoor objects
		@Override
		public void interactWith(CastleDoor castleDoor){
			if(castleDoor.isOpen()) {
				setIsPassingADoor(castleDoor);
				castleDoor.setOpen(false);
			}
			else if(possesses(ARPGItem.CASTLE_KEY)) {
				castleDoor.setOpen(true);
			}
		}

		// Specific implementation of the "use" method for Bomb objects
		@Override
		public void useBomb() {
			entity = new Bomb(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0));
			if (getOwnerArea().canEnterAreaCells(entity, getFieldOfViewCells())) {
				getOwnerArea().registerActor(entity);
			}
			playerInventory.removeItem(selectItem(itemIndex), 1);
		}

		// Specific implementation of the "use" method for Sword objects
		@Override
		public void useSword() {
			if (possesses(ARPGItem.SWORD)) {
				currentState = 1;
				entity = new Sword(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0));
				if (getOwnerArea().canEnterAreaCells(entity, getFieldOfViewCells())) {
					getOwnerArea().registerActor(entity);
				}
			}
		}

		// Specific implementation of the "use" method for Bow objects
		@Override
		public void useBow() {
			if (possesses(ARPGItem.ARROW) && possesses(ARPGItem.BOW)) {
				currentState = 2;
				entity = new Arrow(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0), 5, 2);
				if (getOwnerArea().canEnterAreaCells(entity, getFieldOfViewCells()) && playerInventory.removeItem(ARPGItem.ARROW, 1)) {
					getOwnerArea().registerActor(entity);
				}
			}
		}

		// Specific implementation of the "use" method for Staff objects
		@Override
		public void useStaff() {
			if (possesses(ARPGItem.STAFF)) {
				currentState = 3;
				entity = new MagicWaterProjectile(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0), 5, 2);
				if (getOwnerArea().canEnterAreaCells(entity, getFieldOfViewCells())) {
					getOwnerArea().registerActor(entity);
				}
			}
		}
	}
}
