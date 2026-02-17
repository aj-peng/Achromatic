package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Dice extends SuperObject {
    GamePanel gp;

    public OBJ_Dice(GamePanel gp) {
        this.gp = gp;

        name = "Dice";
        collision = false;
        try {
            image = utility.scaleImage(
                    ImageIO.read(Objects.requireNonNull(getClass().getResource("/object/dice.png"))),
                    gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
