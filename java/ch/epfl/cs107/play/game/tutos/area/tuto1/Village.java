package ch.epfl.cs107.play.game.tutos.area.tuto1;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.area.*;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;

public class Village extends SimpleArea{
	
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
