package simulator.src;
public interface Algo {
    public class Coords{
        int x;
        int y;
        public Coords(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    public int [] nextMove(int [] grid, int curX, int curY, int desX, int desY);
}
