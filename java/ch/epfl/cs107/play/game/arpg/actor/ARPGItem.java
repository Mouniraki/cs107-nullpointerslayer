package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.InventoryItem;

/**
 * Specific implementation of items for an ARPG Inventory.
 */
public enum ARPGItem implements InventoryItem {

	ARROW("Arrow", 1.f, 10, "zelda/arrow.icon"),
	SWORD("Sword", 0.f, 10, "zelda/sword.icon"),
	STAFF("Staff", 0.f, 10, "zelda/staff_water.icon"),
	BOW("Bow", 0.f, 10, "zelda/bow.icon"),
	BOMB("Bomb", 2.f, 10, "zelda/bomb"),
	CASTLE_KEY("Castle Key", 0.f, 10, "zelda/key"),;
	
	private String itemName;
	private float itemWeight;
	private int itemPrice;
	private String spriteName;
	
	/**
	 * Unique representation of item types
	 * @param itemName (String) : the name of the item type
	 * @param itemWeight (float) : the weight of the item type
	 * @param itemPrice (int) : the price of the item type
	 * @param spriteName (String) : the name of the sprite to be shown in the GUI
	 */
	ARPGItem(String itemName, float itemWeight, int itemPrice, String spriteName){
		this.itemName = itemName;
		this.itemWeight = itemWeight;
		this.itemPrice = itemPrice;
		this.spriteName = spriteName;
	}
	/** Getter for the name of the item. */
	@Override
	public String getItemName() {
		return itemName;
	}
	/** Getter for the weight of the item. */
	@Override
	public float getItemWeight() {
		return itemWeight;
	}
	/** Getter for the price of the item. */
	@Override
	public int getItemPrice() {
		return itemPrice;
	}
	/** Getter for the name of the sprite that represents the item in the GUI. */
	public String getSpriteName() { return spriteName; }
	
}
