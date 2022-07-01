package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;

/**
 * Specific implementation of an RPG Inventory for ARPG Game
 */
public class ARPGInventory extends Inventory {

	private final int maxMoney = 999;
	private int money;
	private int fortune;

	public ARPGInventory() {
		super(100);
		money = 100;
		fortune = money + super.getFortune();
	}
	
	/**
	 * Adds items to the inventory
	 * @param item (InventoryItem) : the item to add
	 * @param quantity (int) : the number of items to add
	 * @return true if the item has been added correctly ; false otherwise
	 */
	protected boolean addItem(InventoryItem item, int quantity) {
		return super.addItem(item, quantity);
	}
	
	/**
	 * Removes items to the inventory
	 * @param item (InventoryItem) : the item to remove
	 * @param quantity (int) : the number of items to remove
	 * @return true if the item has been removed correctly ; false otherwise
	 */
	protected boolean removeItem(InventoryItem item, int quantity) {
		return super.removeItem(item, quantity);
	}

	/**
	 * Getter for the items
	 * @param index (int) : the index of the item to get
	 * @return the desired item
	 */
	protected InventoryItem getItem(int index) {
		return super.getItem(index);
	}

	/**
	 * Getter for the inventory size
	 * @return (int)
	 */
	protected int getSize() {
		return super.getSize();
	}


	/**
	 * Setter for the money stored in inventory
	 * @param amount (int) : the amount of money to add
	 */
	public void addMoney(int amount) {
		if (money + amount < maxMoney) {
			money += amount;
		}
		else {
			money = maxMoney;
		}
	}
	
	/**
	 * Getter for the money
	 * @return the current amount of money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Getter for the maximum amount of money that can be stored in the inventory
	 * @return the maximum amount of money
	 */
	public int getMaxMoney() {
		return maxMoney;
	}
	/**
	 * Getter for the fortune accumulated in inventory
	 * @return the current fortune
	 */
	public int getFortune() {
		return fortune;
	}
}
