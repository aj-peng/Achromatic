package main;

import object.OBJ_Heart;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class UserInterface {
    GamePanel gp;
    Graphics2D g2;

    public Font maruMonica;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> counters = new ArrayList<>();

    public String dialogue = "";
    public int commandNum = 0;

    BufferedImage heart;

    public UserInterface(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf"));
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // HUD
        SuperObject heart = new OBJ_Heart(gp);
        this.heart = heart.image;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        if (gp.gameState == gp.titleState) {
            drawTitleMenu();
        }
        else if (gp.gameState == gp.playState) {
            drawMessage();
            drawPlayerHealth();
        }
        else if (gp.gameState == gp.pauseState) {
            drawPauseMenu();
            drawPlayerHealth();
        }
        else if (gp.gameState == gp.dialogueState) {
            drawDialogueMenu();
            drawPlayerHealth();
        }
    }

    public void drawDelta(long delta) {
        int x = gp.tileSize / 4;
        int y = gp.screenHeight - x;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.WHITE);
        g2.drawString("Draw: " + delta, x, y);
    }

    public void addMessage(String text) {
        messages.add(text);
        counters.add(0);
    }

    public void removeMessage(int index) {
        messages.remove(index);
        counters.remove(index);
    }

    private void drawMessage() {
        int messageY = gp.tileSize * 5;
        int messageX = gp.tileSize / 4;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.white);

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                g2.drawString(messages.get(i), messageX, messageY);
                messageY += gp.tileSize / 2;

                int count = counters.get(i) + 1;
                counters.set(i, count);
                if (counters.get(i) > 180) {
                    removeMessage(i);
                }
            }
        }
    }

    private void drawPlayerHealth() {
        int x = gp.tileSize / 4;
        int y = gp.tileSize / 2;
        g2.drawImage(heart, x, y, null);

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(gp.player.health + "/" + gp.player.maxHealth,
                x + (gp.tileSize * 2 / 3),
                y + (heart.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent()
        );
    }

    private void drawTitleMenu() {
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64F));

        String text = "ACHROMATIC";
        int x = getCenterTextX(text);
        int y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        String[] entries = {"PLAY", "NEW GAME", "EXIT"};
        for(int i = 0; i < entries.length; i++) {
            text = entries[i];
            x = getCenterTextX(text);
            y += gp.tileSize * 3/2;
            g2.drawString(text, x, y);
            if (commandNum == i) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }
    }

    private void drawPauseMenu() {
        String text = "PAUSE";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(Color.YELLOW);

        int x = getCenterTextX(text);
        int y = gp.screenHeight / 4;
        g2.drawString(text, x, y);
    }

    private void drawDialogueMenu() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 2;
        int width = gp.screenWidth - 2*x;
        int height = gp.tileSize * 3;
        drawSubWindow(x, y, width, height);

        int padding = 8;
        x += gp.tileSize / 4 + padding;
        y += gp.tileSize / 2 + padding;

        g2.setFont(g2.getFont().deriveFont(24F));
        for (String line : dialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += gp.tileSize / 2 + padding;
        }
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        int arc = 25;
        int stroke = 4;

        Color alpha = new Color(0,0,0,210);
        g2.setColor(alpha);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(stroke));
        g2.drawRoundRect(x + stroke, y + stroke,
                width - 2*stroke, height - 2*stroke, arc, arc);
    }

    private int getCenterTextX(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth - length) / 2;
    }
}
