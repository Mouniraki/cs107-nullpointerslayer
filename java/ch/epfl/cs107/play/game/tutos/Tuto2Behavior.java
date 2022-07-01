package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior{
	public enum Tuto2CellType {
		NULL(0, false),
		WALL(-16777216, false),
		IMPASSABLE(-8750470, false),
		INTERACT(-256, true),
		DOOR(-195580, true),
		WALKABLE(-1, true),;
		
		final int type;
		final boolean isWalkable;
		
		Tuto2CellType(int type, boolean isWalkable){
			this.type = type;
			this.isWalkable = isWalkable;
		}
		
		public static Tuto2CellType toType(int type) {
			switch(type) {
			case 0 :
				return Tuto2CellType.NULL;
			case -16777216 :
				return Tuto2CellType.WALL;
			case -8750470 :
				return Tuto2CellType.IMPASSABLE;
			case -256 :
				return Tuto2CellType.INTERACT;
			case -195580 :
				return Tuto2CellType.DOOR;
			case -1 :
				return Tuto2CellType.WALKABLE;
			default :
				return NULL;
			}
		}
	}
	
	public Tuto2Behavior(Window window, String name) {
		super(window, name);
		Tuto2Cell types;
		
		
		for(int y = 0; y < super.getHeight(); ++y) {
			for(int x = 0; x < super.getWidth(); ++x) {
				Tuto2CellType color = Tuto2CellType.toType(getRGB(super.getHeight()-1-y, x));
				setCell(x,y, new Tuto2Cell(x, y, color));
			}
		}
		 
	}
	public boolean isDoor(DiscreteCoordinates coord) {
		return (((Tuto2Cell)getCell(coord.x, coord.y)).isDoor());
	}
	
	
	public class Tuto2Cell extends Cell{
		private Tuto2CellType type;
		public Tuto2Cell(int x, int y, Tuto2CellType type) {
			super(x, y);
			this.type = type;
		}
		
		protected boolean canLeave(Interactable entity) {
			return true;
		}
		
		protected boolean canEnter(Interactable entity) {
			return type.isWalkable;
		}
		
		@Override
		public boolean isCellInteractable() {
			return true;
		}
		@Override
		public boolean isViewInteractable() {
			return false;
		}
		
		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {}
		
		public boolean isDoor() {
			return (this.type == Tuto2CellType.DOOR);
		}
	
	}
	
}
