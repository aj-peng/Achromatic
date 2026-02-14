package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Dice extends SuperObject {
    public OBJ_Dice() {
        name = "Dice";
        collision = false;
        try {
            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/object/dice.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
