package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Specific implementation of a Graphical User Interface (GUI) for an ARPGPlayer.
 */
public class ARPGStatusGUI implements Graphics {

	// Enum for all digits to be used to represent the amount of money stored in the player's inventory.
	protected enum ARPGDigits{

		ZERO(new RegionOfInterest(16, 32, 16, 16)),
		ONE(new RegionOfInterest(0, 0, 16, 16)),
		TWO(new RegionOfInterest(16, 0, 16, 16)),
		THREE(new RegionOfInterest(32, 0, 16, 16)),
		FOUR(new RegionOfInterest(48, 0, 16, 16)),
		FIVE(new RegionOfInterest(0, 16, 16, 16)),
		SIX(new RegionOfInterest(16, 16, 16, 16)),
		SEVEN(new RegionOfInterest(32, 16, 16, 16)),
		EIGHT(new RegionOfInterest(48, 16, 16, 16)),
		NINE(new RegionOfInterest(0, 32, 16, 16)),;
		
		private RegionOfInterest portion;
		
		/**
		 * Definition of the position of each element in the image array.
		 * @param portion (RegionOfInterest) : portion of the image that corresponds to the actual digit.
		 */
		ARPGDigits(RegionOfInterest portion){
			this.portion = portion;
		}
		
		/**
		 * Getter for the portion of a given digit.
		 * @return the portion of the image (RegionOfInterest)
		 */
		protected RegionOfInterest getPortion() {
			return this.portion;
		}
	}
	
	// Enum for all images to be used to represent the player's health.
	protected enum ARPGHearts {

		FULL(new RegionOfInterest(32, 0, 16, 16)),
		HALF_FULL(new RegionOfInterest(16, 0, 16, 16)),
		EMPTY(new RegionOfInterest(0, 0, 16, 16));
		
		private RegionOfInterest portion;
		
		/**
		 * Definition of the position of each element in the image array.
		 * @param portion (RegionOfInterest) : portion of the image.
		 */
		ARPGHearts(RegionOfInterest portion){
			this.portion = portion;
		}
		
		/**
		 * Getter for the portion of a given heart type.
		 * @return the portion of the image (RegionOfInterest)
		 */
		protected RegionOfInterest getPortion() {
			return portion;
		}
	}
	// Constants paths for the images to be loaded
	private final String gearDisplayImage = "zelda/gearDisplay";
	private final String coinsDisplayImage = "zelda/coinsDisplay";
	private final String heartDisplayImage = "zelda/heartDisplay";
	private final String digitsImage = "zelda/digits";

	private final float DEPTH = 1000f; // Depth of the GUI : the greater, the higher in the hierarchy
	
	private ARPGPlayer player;
	
	
	private float width;
	private float height;

	private Vector anchor;

	private ImageGraphics gearDisplay;
	private ImageGraphics currentGearDisplay;
	private ImageGraphics coinsDisplay;
	private ImageGraphics[] heartDisplay;
	private ImageGraphics[] digitsDisplay;
	
	/**
	 * Default constructor for ARPGStatusGUI.
	 * @param arpgPlayer (ARPGPlayer) : the player from which the infos will be displayed.
	 */
	protected ARPGStatusGUI(ARPGPlayer arpgPlayer) {
		player = arpgPlayer;
		gearDisplay = new ImageGraphics(ResourcePath.getSprite(gearDisplayImage),
				1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32));

		currentGearDisplay = new ImageGraphics(null, 1f, 1f, new RegionOfInterest(0, 0, 16, 16));

		coinsDisplay = new ImageGraphics(ResourcePath.getSprite(coinsDisplayImage),
				3f, 1.5f, new RegionOfInterest(0, 0, 64, 32));
		
		heartDisplay = new ImageGraphics[5];
		
		digitsDisplay = new ImageGraphics[3];
	}
	
	/**
	 * Changes the heart portions according to the amount of health of the player.
	 */
	protected void heartEvolution() {
		int heartsToEnergyRatio = (int) (player.getMaxPlayerEnergy() / heartDisplay.length);
		float intDivisionGap = (player.getMaxPlayerEnergy() / 10) * 0.5f;
		for(int i = 0; i < this.heartDisplay.length; ++i) {
			if(i < (int) (player.getPlayerEnergy() / heartsToEnergyRatio)) {
				heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite(heartDisplayImage),
						1f, 1f, ARPGHearts.FULL.getPortion());
			}
			
			else if(i >= (player.getPlayerEnergy() / heartsToEnergyRatio)){
				heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite(heartDisplayImage),
						1f, 1f, ARPGHearts.EMPTY.getPortion());
			}
			
			else if(i < (player.getPlayerEnergy() / heartsToEnergyRatio) + intDivisionGap){
				heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite(this.heartDisplayImage),
					1f, 1f, ARPGHearts.HALF_FULL.getPortion());
			}	
		}	
	}
	
	/**
	 * Updates the sprite of the current selected item in the inventory.
	 */
	protected void updateCurrentItem() {
		if (player.getInventorySize() > 0) {
			currentGearDisplay.setName(ResourcePath.getSprite(((ARPGItem) player.selectItem(player.getItemIndex())).getSpriteName()));
		}
		else {
			currentGearDisplay.setName(null);
		}
	}
	
	/**
	 * Changes the digits portions according to the amount of money stored in the player's inventory.
	 */
	protected void moneyEvolution() {
		int money = player.getPlayerMoney();
		if (money > player.getMaxPlayerMoney()) {
			money = player.getMaxPlayerMoney();
		}
		int hundreds = money/100;
		int tens = (money - (100 * hundreds)) / 10;
		int ones = money-(tens * 10 + hundreds * 100);
		
		//INDEX TABLEAU AFFICHAGE CHIFFRES
		for (ARPGDigits value : ARPGDigits.values()) {
			if (hundreds == value.ordinal()) {
				this.digitsDisplay[0] = new ImageGraphics(ResourcePath.getSprite(digitsImage),
						0.75f, 0.75f, value.getPortion());
			}
			
			if (tens == value.ordinal()) {
				this.digitsDisplay[1] = new ImageGraphics(ResourcePath.getSprite(digitsImage),
						0.75f, 0.75f, value.getPortion());
			}
			
			if (ones == value.ordinal()) {
				this.digitsDisplay[2] = new ImageGraphics(ResourcePath.getSprite(digitsImage),
						0.75f, 0.75f, value.getPortion());
			}
		}
	}
	
	/**
	 * Updates all informations to display them accordingly to all changes during the game.
	 */
	public void update(float deltaTime) {
        //UPDATE DE L'IMAGE EN FONCTION DE L'ITEM
        updateCurrentItem();
        heartEvolution();
        moneyEvolution();
	}
	
	// Displaying the infos
	@Override
	public void draw(Canvas canvas) {
		width = canvas.getScaledWidth();
		height = canvas.getScaledHeight();
		anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
		
		gearDisplay.setAnchor(this.anchor.add(new Vector(0, height - 1.75f)));
		gearDisplay.setDepth(DEPTH);
		gearDisplay.draw(canvas);

		currentGearDisplay.setAnchor(this.anchor.add(new Vector(0.25f, height - 1.50f)));
		currentGearDisplay.setDepth(DEPTH);
		currentGearDisplay.draw(canvas);
		
		coinsDisplay.setAnchor(this.anchor.add(new Vector(0.1f, 0)));
		coinsDisplay.setDepth(DEPTH);
		coinsDisplay.draw(canvas);
		
		for (int i = 0; i < digitsDisplay.length; ++i) {
			digitsDisplay[i].setAnchor(anchor.add(new Vector(((float) i / 2) + 1.15f, 0.4f)));
			digitsDisplay[i].setDepth(DEPTH);
			digitsDisplay[i].draw(canvas);
		}
		for (int i = 0; i < heartDisplay.length; ++i) {
			heartDisplay[i].setAnchor(anchor.add(new Vector(i + 1.75f, height - 1.50f)));
			heartDisplay[i].setDepth(DEPTH);
			heartDisplay[i].draw(canvas);
		}
	}

}
