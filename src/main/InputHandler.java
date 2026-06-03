package main;

import entity.Entity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputHandler implements KeyListener {
    GamePanel gp;

    private Runnable wasd, space;
    private final Set<Integer> debounce = new HashSet<>();

    public Entity.Direction direction;
    public boolean moving = false;

    public InputHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void connect(Runnable wasd, Runnable space) {
        this.wasd = wasd;
        this.space = space;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (debounce.contains(code)) return;
        debounce.add(code);

        switch (gp.gameState) {
            case TITLE -> titleState(code);
            case PLAY  -> playState(code);
            case PAUSE -> pauseState(code);
            case DIALOGUE -> dialogueState(code);
        }

        if (code == KeyEvent.VK_BACK_QUOTE) {
            gp.debug = !gp.debug;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        debounce.remove(code);

        if ((code == KeyEvent.VK_W && direction == Entity.Direction.UP) ||
                (code == KeyEvent.VK_S && direction == Entity.Direction.DOWN) ||
                (code == KeyEvent.VK_A && direction == Entity.Direction.LEFT) ||
                (code == KeyEvent.VK_D && direction == Entity.Direction.RIGHT)) {
            direction = Entity.Direction.NONE;
            onWASD(false);
        }
    }

    private void titleState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum - 1, 0 , 2);
                gp.playSound(1);
            }
            case KeyEvent.VK_S -> {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum + 1, 0 , 2);
                gp.playSound(1);
            }
            case KeyEvent.VK_SPACE -> {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = GamePanel.GameState.PLAY;
                    gp.playMusic(0);
                    gp.playSound(1);
                } else if (gp.ui.commandNum == 1) {
                    System.out.println("NEW GAME");
                } else if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
    }

    private void playState(int code) {
        // Handle movement keys
        switch (code) {
            case KeyEvent.VK_W -> {
                direction = Entity.Direction.UP;
                onWASD(true);
            }
            case KeyEvent.VK_S ->{
                direction = Entity.Direction.DOWN;
                onWASD(true);
            }
            case KeyEvent.VK_A -> {
                direction = Entity.Direction.LEFT;
                onWASD(true);
            }
            case KeyEvent.VK_D -> {
                direction = Entity.Direction.RIGHT;
                onWASD(true);
            }
            case KeyEvent.VK_SPACE -> onSpace();
            case KeyEvent.VK_ESCAPE -> {
                gp.ui.commandNum = 0;
                gp.gameState = GamePanel.GameState.PAUSE;
            }
        }
    }

    private void pauseState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum - 1, 0 , 1);
                gp.playSound(1);
            }
            case KeyEvent.VK_S -> {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum + 1, 0 , 1);
                gp.playSound(1);
            }
            case KeyEvent.VK_SPACE -> {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = GamePanel.GameState.PLAY;
                }
                else if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
            case KeyEvent.VK_ESCAPE -> gp.gameState = GamePanel.GameState.PLAY;
        }
    }

    private void dialogueState(int code) {
        switch (code) {
            case KeyEvent.VK_SPACE -> onSpace();
            case KeyEvent.VK_ESCAPE -> gp.gameState = GamePanel.GameState.PLAY;
        }
    }

    private void onWASD(boolean active) {
        if (active) wasd.run();
        moving = active;
    }

    private void onSpace() {
        space.run();
    }
}
