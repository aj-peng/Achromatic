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

        hitbox = new Rectangle(12,16,24,32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setValues();
        setImages();
        input.connect(this::move, this::interact);
    }

    public void update() {
        if (input.moving) {
            idle = false;

            // CHECK COLLISION
            collision = false;
            gp.collision.checkTile(this);

            // OBJECT COLLISION
            int objIndex = gp.collision.checkObject(this, true);
            pickup(objIndex);

            if (!collision) {
                switch (direction) {
                    case Direction.UP -> worldY -= speed;
                    case Direction.DOWN -> worldY += speed;
                    case Direction.LEFT -> worldX -= speed;
                    case Direction.RIGHT -> worldX += speed;
                }

                spriteCounter++;
                if (spriteCounter > interval) {
                    spriteNum = (spriteNum >= 4) ? 1 : spriteNum + 1;
                    spriteCounter = 0;
                }
            }
        }
        else if (!idle) {
            idle = true;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getImage();
        g2.drawImage(image, screenX, screenY, null);
        if (gp.debug) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
        }
    }

    public void speak(int index) {
        if (index == 999 || gp.npcs[index] == null) {
            gp.gameState = GamePanel.GameState.PLAY;
            return;
        }

        switch (gp.gameState) {
            case PLAY -> {
                gp.gameState = GamePanel.GameState.DIALOGUE;
                gp.npcs[index].dialogueIndex = 0;
                gp.npcs[index].speak();
                gp.playSound(1);
                idle = true;
            }
            case DIALOGUE -> {
                gp.npcs[index].speak();
                gp.playSound(1);
            }
        }
    }

    public void move() {
        direction = input.direction;
    }

    public void interact() {
        // NPC INTERACTION
        int npcIndex = gp.collision.checkEntity(this, gp.npcs);
        if (npcIndex != 999 && gp.npcs[npcIndex] != null) {
            speak(npcIndex);
        }
        else {
            gp.event.checkEvent();
        }
    }

    public boolean visible(int x, int y) {
        return x + gp.tileSize > worldX - screenX && x - gp.tileSize < worldX + screenX &&
                y + gp.tileSize > worldY - screenY && y - gp.tileSize < worldY + screenY;
    }

    private void pickup(int index) {
        if (index != 999 && gp.objs[index] != null) {
            switch (gp.objs[index].name) {
                case "Dice"-> {
                    gp.playSound(2);
                    gp.objs[index] = null;
                    gp.event.gamble();
                }
                case "Door" -> {
                    gp.playSound(2);
                    gp.objs[index] = null;
                    gp.ui.addMessage("-1 Door");
                }
            }
        }
    }

    private void setValues() {
        worldX = gp.tileSize * 7;
        worldY = gp.tileSize * 2;
        speed = 4;
        direction = Direction.DOWN;

        // PLAYER STATUS
        maxHealth = 5;
        health = maxHealth;
    }

    private void setImages() {
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
}
