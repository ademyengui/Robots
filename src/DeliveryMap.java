import java.util.*;

public class DeliveryMap {

    protected  Map<String, Location> locations;
    
    public DeliveryMap() {
        this.locations = new HashMap<>();
    }
    
    public void addLocation(Location location) {
        locations.put(location.name, location);
    }
    
    public Location getLocation(String name) {
        return locations.get(name);
    }
    
    // BFS to find shortest path between two locations
    public List<Location> findShortestPath(String start, String end) {
        Location startLoc = locations.get(start);
        Location endLoc = locations.get(end);
        
        if (startLoc == null || endLoc == null) {
            return null;
        }
        
        Map<Location, Location> parentMap = new HashMap<>();
        Queue<Location> queue = new LinkedList<>();
        Set<Location> visited = new HashSet<>();
        
        queue.add(startLoc);
        visited.add(startLoc);
        
        while (!queue.isEmpty()) {
            Location current = queue.poll();
            
            if (current.equals(endLoc)) {
                // Reconstruct path
                List<Location> path = new ArrayList<>();
                Location node = endLoc;
                while (node != null) {
                    path.add(0, node);
                    node = parentMap.get(node);
                }
                return path;
            }
            
            for (Location neighbor : current.adjacentLocations) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        return null; // No path found
    }
    
    // Initialize with some default locations
    public static DeliveryMap createDefaultMap() {
        DeliveryMap map = new DeliveryMap();
        
        // Create locations
        Location warehouse = new Location("Entrepôt Principal", 0, 0);
        Location ariana = new Location("Ariana", 30, 40);
        Location tunis = new Location("Tunis Centre", 20, 10);
        Location lac = new Location("Lac", 15, 25);
        Location marsa = new Location("La Marsa", 40, 5);
        
        // Add connections
        warehouse.addNeighbor(ariana);
        warehouse.addNeighbor(tunis);
        tunis.addNeighbor(lac);
        lac.addNeighbor(marsa);
        ariana.addNeighbor(lac);
        
        // Add to map
        map.addLocation(warehouse);
        map.addLocation(ariana);
        map.addLocation(tunis);
        map.addLocation(lac);
        map.addLocation(marsa);
        
        return map;
    }
}

