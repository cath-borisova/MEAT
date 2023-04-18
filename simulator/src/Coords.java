package simulator.src;

public class Coords {
    int x, y;
    
    public Coords(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        Coords otherCoords = (Coords) other;
        return this.x == otherCoords.x && this.y == otherCoords.y;
    }

}
