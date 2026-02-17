package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    GamePanel gp;

    public boolean spaceHeld, spaceBuffer;
    public boolean upHeld, downHeld, leftHeld, rightHeld;
    public boolean debug = false;

    public InputHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum - 1, 0 , 2);
                gp.playSound(1);
            }
            else if (code == KeyEvent.VK_S) {
                gp.ui.commandNum = Math.clamp(gp.ui.commandNum + 1, 0 , 2);
                gp.playSound(1);
            }
            else if (code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                    gp.playSound(1);
                }
                else if (gp.ui.commandNum == 1) {
                    System.out.println("NEW GAME");
                }
                else if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upHeld = true;
            }
            else if (code == KeyEvent.VK_A) {
                leftHeld = true;
            }
            else if (code == KeyEvent.VK_S) {
                downHeld = true;
            }
            else if (code == KeyEvent.VK_D) {
                rightHeld = true;
            }
            else if (code == KeyEvent.VK_SPACE && !spaceBuffer) {
                spaceBuffer = true;
                spaceHeld = true;
            }
            else if (code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
        }
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }
        }
        else if (code == KeyEvent.VK_SPACE && !spaceBuffer) {
            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_BACK_QUOTE) {
            debug = !debug;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upHeld = false;
        }
        else if (code == KeyEvent.VK_A) {
            leftHeld = false;
        }
        else if (code == KeyEvent.VK_S) {
            downHeld = false;
        }
        else if (code == KeyEvent.VK_D) {
            rightHeld = false;
        }
        else if (code == KeyEvent.VK_SPACE) {
            spaceBuffer = false;
            spaceHeld = false;
        }
    }
}
