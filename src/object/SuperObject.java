package object;

import main.GamePanel;
import main.Utility;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SuperObject {
    Utility utility = new Utility();
    public BufferedImage image;
    public String name;
    public int worldX, worldY;
    public boolean collision = false;
    public Rectangle hitbox = new Rectangle(0,0,48,48);
    public int hitboxDefaultX = 0, hitboxDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (gp.player.onScreen(worldX, worldY)) {
            g2.drawImage(image, screenX, screenY, null);
        }
    }
}
