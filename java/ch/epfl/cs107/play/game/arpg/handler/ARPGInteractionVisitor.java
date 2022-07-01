package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.Coin;
import ch.epfl.cs107.play.game.arpg.actor.Heart;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor
{
	/**
     * General use method for ARPG Items
     * @param item (ARPGItem), not null
     */
	default void use(ARPGItem item) {
		switch(item) {
			case ARROW :
				useArrow();
				break;
			case SWORD :
				useSword();
				break;
			case STAFF :
				useStaff();
				break;
			case BOW :
				useBow();
				break;
			case BOMB :
				useBomb();
				break;
			case CASTLE_KEY:
				useKey();
				break;
		}
	}
	/**
     * Use method for specific ARPG Items
     */
	default void useArrow() {
		//To Implement
	}
	default void useSword() {
		//To Implement
	}
	default void useStaff() {
		//To Implement
	}
	default void useBow() {
		//To Implement
	}
	default void useBomb() {
		//To Implement
	}
	default void useKey() {
		//To Implement
	}

	 /**
     * Simulates an interaction between ARPG Interactor and a FireSpell
     * @param fireSpell (FireSpell), not null
     */
	default void interactWith(FireSpell fireSpell) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Player
     * @param player (Player), not null
     */
	default void interactWith(Player player){
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Grass
     * @param grass (Grass), not null
     */
	default void interactWith(Grass grass){
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a ARPGCell
     * @param cell (ARPGCell), not null
     */
	default void interactWith(ARPGCell cell){
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Bomb
     * @param bomb (Bomb), not null
     */
	default void interactWith(Bomb bomb){
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Monster
     * @param monster (Monster), not null
     */
	default void interactWith(Monster monster) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a CastleKey
     * @param castleKey (CastleKey), not null
     */
	default void interactWith(CastleKey castleKey) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a CastleDoor
     * @param door (CastleDoor), not null
     */
	default void interactWith(CastleDoor door){
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Coin
     * @param coin (Coin), not null
     */
	default void interactWith(Coin coin) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Heart
     * @param heart (Heart), not null
     */
	default void interactWith(Heart heart) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Sword
     * @param sword (Sword), not null
     */
	default void interactWith(Sword sword) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and an Arrow
     * @param arrow (Arrow), not null
     */
	default void interactWith(Arrow arrow) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a MagicWaterProjectile
     * @param magicWaterProjectile (MagicWaterProjectile), not null
     */
	default void interactWith(MagicWaterProjectile magicWaterProjectile) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and an Orb
     * @param orb (Orb), not null
     */
	default void interactWith(Orb orb) {
		// by default the interaction is empty
	}

	/**
     * Simulates an interaction between ARPG Interactor and a Staff
     * @param staff (Staff), not null
     */
	default void interactWith(Staff staff) {
		// by default the interaction is empty
	}
}
