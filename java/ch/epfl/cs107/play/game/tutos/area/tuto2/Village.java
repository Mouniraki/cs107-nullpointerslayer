package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.Vector;

public class Village extends Tuto2Area{
	
	public void createArea() {
		SimpleGhost fantome = new SimpleGhost(new Vector(18, 7), "ghost.2");
		registerActor(fantome);
		registerActor(new Background(this));
		registerActor(new Foreground(this));
	}
	@Override
	public String getTitle() {
		return "zelda/Village";
	}
	
}
