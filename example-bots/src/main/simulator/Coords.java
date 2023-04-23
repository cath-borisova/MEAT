package simulator;
import java.util.Objects;

import simulator.*;
public class Coords {
    public int x, y;
    
    public Coords(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(new int[]{this.x, this.y});
    }

    @Override
    public boolean equals(Object other) {
        Coords otherCoords = (Coords) other;
        return this.x == otherCoords.x && this.y == otherCoords.y;
    }

}
