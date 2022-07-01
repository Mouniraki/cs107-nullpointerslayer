package ch.epfl.cs107.play.game.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.epfl.cs107.play.game.arpg.actor.ARPGItem;

public class Inventory {
	public interface Holder{
		default boolean possess (InventoryItem item) {
			return false;
		}
	}
	
	private int maxWeight;
	private List<InventoryItem> items;
	private Map<ARPGItem, Integer> itemQuantity;
	
	protected Inventory(int maxWeight) {
		this.maxWeight = maxWeight;
		items = new ArrayList<>();
		itemQuantity = new HashMap<>();
	}
	
	public boolean isInInventory(InventoryItem item) {
		boolean isThere = false;
		for (Entry <ARPGItem, Integer> pair : itemQuantity.entrySet()) {
			if(item.getItemName().equals(pair.getKey().getItemName())) {
				isThere = true;
				break;
			}
		}
		return isThere;
	}
	
	//GETTER FORTUNE
	protected int getFortune() {
		int fortune = 0;
		for (Integer values : itemQuantity.values()) {
			fortune += values;
		}
		return fortune;
	}
	
	
	protected boolean addItem(InventoryItem item, int quantity) {
		boolean isAddedCorrectly = false;
		if (items.size() < maxWeight && quantity < maxWeight && quantity+items.size() < maxWeight) {
			for (int i=0; i<quantity; ++i) {
				items.add(item);
				itemQuantity.put((ARPGItem) item, (Integer) i + 1);  //A REVOIR
				isAddedCorrectly = true;
			}
		}
		return isAddedCorrectly;
	}
	
	protected boolean removeItem(InventoryItem item, int quantity) {
		int j = 0;
		boolean isRemovedCorrectly = false;
		if(isInInventory(item)) {
			int registeredQuantity = itemQuantity.get(item);
			if (quantity <= registeredQuantity) {
				for(int i = 0; i < items.size(); ++i) {
					if(item.equals((InventoryItem) items.get(i)) && j < quantity) {
						items.remove(i);
						itemQuantity.remove((ARPGItem) item, (Integer)quantity);  //A REVOIR
						++j;
						isRemovedCorrectly = true;
					}
				}
			}
		}
		return isRemovedCorrectly;
	}
	
	protected InventoryItem getItem(int index) {
		if(isInInventory(items.get(index))) {
			return items.get(index);
		}
		else return null;
	}
	
	protected int getSize() {
		return items.size();
	}
	
}
