package object;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_CHEST extends Entity {
	
	
	public OBJ_CHEST(GamePanel gp) {
		super(gp);
		name = "Chest";
		down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
	}
}