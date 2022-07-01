package ch.epfl.cs107.play.game.tutos.actor;
import ch.epfl.cs107.play.game.actor.*;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;

public class SimpleGhost extends Entity{
	private Sprite sprite;
	private float energyLevel;
	private TextGraphics hpText;
	
	public SimpleGhost(Vector position, String spriteName) {
		super(position);
		this.sprite = new Sprite(spriteName, 1, 1.f, this);
		this.energyLevel = 10;
		this.hpText = new TextGraphics(Integer.toString((int)energyLevel), 0.4f, Color.BLUE);
		this.hpText.setParent(this);
		this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
	}
	
	public boolean isWeak() {
		if(energyLevel <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void strengthen() {
		this.energyLevel = 10.f;
	}
	
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
		hpText.draw(canvas);
	}
	
	public void update(float deltaTime) {
		if (energyLevel > 0) {
			energyLevel-=deltaTime;
			hpText.setText(Integer.toString((int)energyLevel));
		}
		else if(energyLevel < 0) {
			energyLevel = 0;
		}
	}
	
	public void moveUp(float delta) {
		setCurrentPosition(getPosition().add(0.f, delta));
	}
	public void moveDown(float delta) {
		setCurrentPosition(getPosition().add(0.f, -delta));
	}
	public void moveLeft(float delta) {
		setCurrentPosition(getPosition().add(-delta, 0.f));
	}
	public void moveRight(float delta) {
		setCurrentPosition(getPosition().add(delta, 0.f));
	}
	

}
