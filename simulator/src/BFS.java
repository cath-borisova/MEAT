package simulator.src;
import java.util.*;
public class BFS implements Algo{
    Queue<Coords> q = new LinkedList<>();
    boolean [] [] visited = new int [grid.length][grid.length[0]];

    public int [] nextMove(int [] grid, int curX, int curY, int desX, int desY){
        breadthFirstSearch(curX, curY, desX, desY);
        int [] coords = new int[2];
        return coords;
    }

    public void breadthFirstSearch(int startX, int startY, int desX, int desY){
        q.add(new Coords(startX, startY));
        visited[startX][startY] = true;

        while(!q.isEmpty()){
            Coords curr = q.remove();
            int x = curr.x;
            int y = curr.y;
            if(curr.x == startX && curr.y == startY){
                break;
            }

            if(inBounds(visited, x + 1, y)){
                q.add(new Coords(x+1, y));
            }
            if(inBounds(visited, x - 1, y)){
                q.add(new Coords(x-1, y));
            }
            if(inBounds(visited, x, y+1)){
                q.add(new Coords(x, y+1));

            }
            if(inBounds(visited, x, y-1)){
                q.add(new Coords(x, y-1));
            }
        }
    }

    public boolean inBounds(boolean [][] visited, int x, int y){ //check for obstacles and rubbles
        if(x < 0 || y < 0 || x >= visited.length || y >= visited[0].length){
            return false;
        }
        if(visited[x][y]){
            return false;
        }
        return true;
    }
}