import java.util.List;


public class RobotLivraisonEco extends RobotLivraison {
    protected  DeliveryMap deliveryMap;
    protected  double totalEmissions; // in kg CO2
    protected  static final double EMISSION_PER_UNIT = 0.01; // kg CO2 per unit distance
    protected double totalDistance;

    public RobotLivraisonEco(String id, int x, int y, DeliveryMap deliveryMap) {
        super(id, x, y);
        this.deliveryMap = deliveryMap;
        this.totalEmissions = 0;
    }
    
    
    @Override
    public void deplacer(int x, int y) throws RobotException {
        double distance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        super.deplacer(x, y);
        updateEmissions(distance);
    }
    
    // New method for graph-based navigation
    public void deliverToLocation(String destinationName) throws RobotException {
        if (destination == null || !destination.equals(destinationName)) {
            throw new RobotException("No delivery scheduled for " + destinationName);
        }
        
        Location currentLoc = findClosestLocation();
        Location destLoc = deliveryMap.getLocation(destinationName);
        
        if (destLoc == null) {
            throw new RobotException("Destination inconnue: " + destinationName);
        }
        
        List<Location> path = deliveryMap.findShortestPath(currentLoc.name, destinationName);
        
        if (path == null || path.size() < 2) {
            throw new RobotException("Aucun chemin trouvé vers " + destinationName);
        }
        
        // Follow the path
        for (int i = 1; i < path.size(); i++) {
            Location next = path.get(i);
            System.out.println("En route vers " + next.name + "...");
            deplacer(next.x, next.y);
        }
        
        // Delivery complete
        colisActuel = null;
        enLivraison = false;
        ajouterHistorique("Livraison terminée à " + destination);
        destination = null;
    }
    
    protected  Location findClosestLocation() {
        Location closest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Location loc : deliveryMap.locations.values()) {
            double distance = Math.sqrt(Math.pow(loc.x - this.x, 2) + Math.pow(loc.y - this.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = loc;
            }
        }
        
        return closest;
    }
    
    protected  void updateEmissions(double distance) {
        double emissions = distance * EMISSION_PER_UNIT;
        totalEmissions += emissions;
        ajouterHistorique(String.format("Émissions CO2: +%.2f kg (Total: %.2f kg)", emissions, totalEmissions));
    }
    
        
    public double getTotalEmissions() {
        return totalEmissions; // Should already exist
    }

    
        
    @Override
    public String toString() {
        return super.toString() + String.format(", Émissions CO2: %.2f kg", totalEmissions);
    }
}