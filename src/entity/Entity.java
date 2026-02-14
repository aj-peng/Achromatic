package entity;

import main.GamePanel;
import main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {
    Utility utility = new Utility();
    GamePanel gp;

    public int worldX, worldY;
    public int speed;
    public boolean idle = false;

    public int spriteNum = 1;
    public int spriteCounter = 0;
    public final int interval = 8;

    public String direction;
    public BufferedImage up1, up2, up3, down1, down2, down3,
            left1, left2, left3, right1, right2, right3;

    public Rectangle hitbox = new Rectangle(0,0,48,48);
    public int hitboxDefaultX, hitboxDefaultY;
    public boolean collision = false;

    public int lockCounter = 0;
    public int dialogueIndex = 0;
    String dialogues[] = new String[10];

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {}

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.dialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    public void update() {
        setAction();

        collision = false;
        gp.collision.checkTile(this);
        gp.collision.checkObject(this, false);
        gp.collision.checkPlayer(this);

        if (!collision && gp.gameState == gp.playState) {
            idle = false;
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }
        else if (!idle) {
            idle = true;
        }

        spriteCounter++;
        if (spriteCounter > interval) {
            spriteNum = (spriteNum >= 4) ? 1 : spriteNum + 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = switch (direction) {
                case "up" -> (idle) ? up2 : (spriteNum == 1) ? up1 : (spriteNum == 3) ? up3 : up2;
                case "down" -> (idle) ? down2 : (spriteNum == 1) ? down1 : (spriteNum == 3) ? down3 : down2;
                case "left" -> (idle) ? left1 : (spriteNum == 1) ? left1 : (spriteNum == 3) ? left3 : left2;
                case "right" -> (idle) ? right1 : (spriteNum == 1) ? right1 : (spriteNum == 3) ? right3 : right2;
                default -> null;
            };
            g2.drawImage(image, screenX, screenY, null);
        }
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = utility.scaleImage(image, width, height);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  image;
    }
}
