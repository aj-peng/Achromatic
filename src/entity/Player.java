package entity;

import main.GamePanel;
import main.InputHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    InputHandler input;
    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, InputHandler input) {
        super(gp);
        this.input = input;

        screenX = (gp.screenWidth - gp.tileSize) / 2;
        screenY = (gp.screenHeight - gp.tileSize) / 2;

        setDefaultValues();
        getPlayerImages();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 7;
        worldY = gp.tileSize * 2;
        speed = 4;
        direction = "down";

        hitbox = new Rectangle();
        hitbox.x = 12;
        hitbox.y = 16;
        hitbox.width = 24;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public void getPlayerImages() {
        up1 = setup("/player/grey_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/grey_up_2", gp.tileSize, gp.tileSize);
        up3 = setup("/player/grey_up_3", gp.tileSize, gp.tileSize);

        down1 = setup("/player/grey_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/grey_down_2", gp.tileSize, gp.tileSize);
        down3 = setup("/player/grey_down_3", gp.tileSize, gp.tileSize);

        left1 = setup("/player/grey_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/grey_left_2", gp.tileSize, gp.tileSize);
        left3 = setup("/player/grey_left_3", gp.tileSize, gp.tileSize);

        right1 = setup("/player/grey_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/grey_right_2", gp.tileSize, gp.tileSize);
        right3 = setup("/player/grey_right_3", gp.tileSize, gp.tileSize);
    }

    public void update() {
        if (input.upHeld || input.downHeld || input.leftHeld || input.rightHeld) {
            idle = false;

            if (input.upHeld) { direction = "up"; }
            else if (input.downHeld) { direction = "down"; }
            else if (input.leftHeld) { direction = "left"; }
            else if (input.rightHeld) { direction = "right"; }

            // CHECK COLLISION
            collision = false;
            gp.collision.checkTile(this);

            // OBJECT COLLISION
            int objIndex = gp.collision.checkObject(this, true);
            pickUpObject(objIndex);

            if (!collision) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > interval) {
                spriteNum = (spriteNum >= 4) ? 1 : spriteNum + 1;
                spriteCounter = 0;
            }
        }
        else if (!idle) {
            idle = true;
        }

        if (input.spaceHeld) {
            // NPC INTERACTION
            int npcIndex = gp.collision.checkEntity(this, gp.npcs);
            interactNPC(npcIndex);
        }
    }

    public void pickUpObject(int index) {
        if (index != 999) {
            String objName = gp.objs[index].name;

            switch (objName) {
                case "Dice":
                    gp.playSound(2);
                    gp.objs[index] = null;
                    gp.ui.addMessage("+1 Dice");
                    break;
                case "Door":
                    gp.playSound(2);
                    gp.objs[index] = null;
                    gp.ui.addMessage("-1 Door");
                    break;
            }
        }
    }

    public void interactNPC(int index) {
        if (index != 999) {
            gp.gameState = gp.dialogueState;
            input.spaceHeld = false;
            idle = true;
            gp.npcs[index].speak();
            gp.playSound(1);
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = switch (direction) {
            case "up" -> (idle) ? up2 : (spriteNum == 1) ? up1 : (spriteNum == 3) ? up3 : up2;
            case "down" -> (idle) ? down2 : (spriteNum == 1) ? down1 : (spriteNum == 3) ? down3 : down2;
            case "left" -> (idle) ? left1 : (spriteNum == 1) ? left1 : (spriteNum == 3) ? left3 : left2;
            case "right" -> (idle) ? right1 : (spriteNum == 1) ? right1 : (spriteNum == 3) ? right3 : right2;
            default -> null;
        };

        g2.drawImage(image, screenX, screenY, null);
        if (input.debug) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        }
    }

    public boolean onScreen(int x, int y) {
        return x + gp.tileSize > worldX - screenX && x - gp.tileSize < worldX + screenX &&
                y + gp.tileSize > worldY - screenY && y - gp.tileSize < worldY + screenY;
    }
}
