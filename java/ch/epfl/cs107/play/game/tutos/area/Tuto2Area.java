package ch.epfl.cs107.play.game.tutos.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

abstract public class Tuto2Area extends Area {
	private Window window;
	private Tuto2Behavior behavior;
	/**
     * Create the area by adding all its actors
     * called by the begin method, when the area starts to play
     */
	protected abstract void createArea();
	
	    public boolean begin(Window window, FileSystem fileSystem) {
		 	this.window = window;
	        if (super.begin(window, fileSystem)) {
	            // Set the behavior map
	        	behavior = new Tuto2Behavior(window, getTitle());
	        	setBehavior(behavior);
	            createArea();	            
	            return true;
	        }
	        return false;
	    }
	public float getCameraScaleFactor() {
		return 10.f;
	}
	public boolean isDoor(DiscreteCoordinates c) {
		return (behavior.isDoor(c));
	}
}
