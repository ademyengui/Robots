import java.util.Scanner;

public class RobotLivraison extends RobotConnecte {
    public static final int ENERGIE_LIVRAISON = 15;
    public static final int ENERGIE_CHARGEMENT = 5;
    
    protected  String colisActuel;
    protected  String destination;
    protected  boolean enLivraison;
    
    public RobotLivraison(String id, int x, int y) {
        super(id, x, y);
        this.colisActuel = null;
        this.destination = null;
        this.enLivraison = false;
    }
    
    @Override
    public void effectuerTache() throws RobotException {
        if (!enMarche) {
            throw new RobotException("Le robot doit être démarré pour effectuer une tâche");
        }
        
        Scanner scanner = new Scanner(System.in);
        
        if (enLivraison) {
            System.out.println("Entrez les coordonnées de destination (x y):");
            int destX = scanner.nextInt();
            int destY = scanner.nextInt();
            faireLivraison(destX, destY);
        } else {
            System.out.println("Voulez-vous charger un nouveau colis? (oui/non)");
            String reponse = scanner.next().toLowerCase();
            
            if (reponse.equals("oui")) {
                System.out.println("Entrez la description du colis:");
                String colis = scanner.next();
                System.out.println("Entrez la destination:");
                String dest = scanner.next();
                chargerColis(colis, dest);
            } else {
                ajouterHistorique("En attente de colis");
            }
        }
    }
    
    public void faireLivraison(int destX, int destY) throws RobotException {
        try {
            deplacer(destX, destY);
            colisActuel = null;
            enLivraison = false;
            ajouterHistorique("Livraison terminée à " + destination);
            destination = null;
        } catch (RobotException e) {
            throw new RobotException("Échec de la livraison: " + e.getMessage());
        }
    }
    
    @Override
    public void deplacer(int x, int y) throws RobotException {
        try {
            // Calcul de la distance
            double distance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
            
            if (distance > 100) {
                throw new RobotException("Distance trop grande (max 100 unités)");
            }
            
            verifierEnergie((int) (distance * 0.3));
            verifierMaintenance();
            
            // Consommation d'énergie
            consommerEnergie((int) (distance * 0.3));
            
            // Mise à jour des heures d'utilisation
            heuresUtilisation += (int) distance / 10;
            
            // Déplacement effectif
            this.x = x;
            this.y = y;
            
            ajouterHistorique(String.format("Déplacement vers (%d,%d), distance: %.2f", x, y, distance));
        } catch (EnergieInsuffisanteException | MaintenanceRequiseException e) {
            throw new RobotException("Échec du déplacement: " + e.getMessage());
        }
    }
    
    public void chargerColis(String colis, String destination) throws RobotException {
        try {
            if (enLivraison) {
                throw new RobotException("Le robot est déjà en livraison");
            }
            if (colisActuel != null) {
                throw new RobotException("Le robot a déjà un colis");
            }
            
            verifierEnergie(ENERGIE_CHARGEMENT);
            
            colisActuel = colis;
            this.destination = destination;
            enLivraison = true;
            consommerEnergie(ENERGIE_CHARGEMENT);
            
            ajouterHistorique("Chargement du colis: " + colis + " pour " + destination);
        } catch (EnergieInsuffisanteException e) {
            throw new RobotException("Échec du chargement: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Colis : %s, Destination : %s", 
                colisActuel != null ? colisActuel : "Aucun", 
                destination != null ? destination : "Aucune");
    }
   
}