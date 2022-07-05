package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_FIREBALL extends Projectile {
	
	GamePanel gp;
	
	public OBJ_FIREBALL(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Fireball";
		speed = 5;
		maxLife = 80;
		life = maxLife;
	}
}
