package ch.epfl.cs107.play.game.rpg;

public interface InventoryItem {
	
	default String getItemName() { return null; }
	default float getItemWeight() { return 0; }
	default int getItemPrice() {
		return 0;
	}
}
