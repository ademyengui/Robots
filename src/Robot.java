import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Robot {
    // Attributs avec visibilité protected pour permettre l'accès aux classes filles
    protected String id;
    protected int x, y;
    protected int energie;
    protected int heuresUtilisation;
    protected boolean enMarche;
    protected ArrayList<String> historiqueActions;
    
    // Constructeur
    public Robot(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.energie = 100; // Par défaut à 100%
        this.heuresUtilisation = 0;
        this.enMarche = false;
        this.historiqueActions = new ArrayList<>();
        ajouterHistorique("Robot créé");
    }
    
    // Méthodes
    protected void ajouterHistorique(String action) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        historiqueActions.add(date + " " + action);
    }
    
    protected void verifierEnergie(int energieRequise) throws EnergieInsuffisanteException {
        if (this.energie < energieRequise) {
            throw new EnergieInsuffisanteException("Énergie insuffisante pour cette action. Énergie disponible: " + this.energie + "%, nécessaire: " + energieRequise + "%");
        }
    }
    
    protected void verifierMaintenance() throws MaintenanceRequiseException {
        if (this.heuresUtilisation >= 100) {
            throw new MaintenanceRequiseException("Maintenance requise après " + this.heuresUtilisation + " heures d'utilisation");
        }
    }
    
    public void demarrer() throws RobotException {
        try {
            verifierEnergie(10);
            this.enMarche = true;
            ajouterHistorique("Démarrage du robot");
        } catch (EnergieInsuffisanteException e) {
            ajouterHistorique("Échec du démarrage: " + e.getMessage());
            throw new RobotException("Impossible de démarrer le robot: " + e.getMessage());
        }
    }
    
    public void arreter() {
        this.enMarche = false;
        ajouterHistorique("Arrêt du robot");
    }
    
    protected void consommerEnergie(int quantite) {
        this.energie = Math.max(0, this.energie - quantite);
    }
    
    public void recharger(int quantite) {
        this.energie = Math.min(100, this.energie + quantite);
        ajouterHistorique("Rechargement de " + quantite + "%");
    }
    
    // Méthodes abstraites
    public abstract void deplacer(int x, int y) throws RobotException;
    public abstract void effectuerTache() throws RobotException;
    
    public String getHistorique() {
        StringBuilder sb = new StringBuilder();
        for (String action : historiqueActions) {
            sb.append(action).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("%s [ID : %s, Position : (%d,%d), Énergie : %d%%, Heures : %d]",
                            this.getClass().getSimpleName(), id, x, y, energie, heuresUtilisation);
    }
    
        
}