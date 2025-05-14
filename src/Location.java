import java.util.ArrayList;
import java.util.List;



public class Location {
    String name;
    int x, y;
    List<Location> adjacentLocations;
    
    public Location(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.adjacentLocations = new ArrayList<>();
    }
    
    public void addNeighbor(Location neighbor) {
        this.adjacentLocations.add(neighbor);
        neighbor.adjacentLocations.add(this); // Make it bidirectional
    }
    
    @Override
    public String toString() {
        return name + " (" + x + "," + y + ")";
    }
}