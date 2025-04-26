public abstract class RobotConnecte extends Robot implements Connectable {
    protected boolean connecte;
    protected String reseauConnecte;
    
    public RobotConnecte(String id, int x, int y) {
        super(id, x, y);
        this.connecte = false;
        this.reseauConnecte = null;
    }
    
    @Override
    public void connecter(String reseau) throws RobotException {
        try {
            verifierEnergie(5);
            verifierMaintenance();
            
            this.connecte = true;
            this.reseauConnecte = reseau;
            consommerEnergie(5);
            ajouterHistorique("Connexion au réseau: " + reseau);
        } catch (EnergieInsuffisanteException | MaintenanceRequiseException e) {
            throw new RobotException("Échec de la connexion: " + e.getMessage());
        }
    }
    
    @Override
    public void deconnecter() {
        if (this.connecte) {
            this.connecte = false;
            ajouterHistorique("Déconnexion du réseau: " + this.reseauConnecte);
            this.reseauConnecte = null;
        }
    }
    
    @Override
    public void envoyerDonnees(String donnees) throws RobotException {
        if (!this.connecte) {
            throw new RobotException("Le robot n'est pas connecté à un réseau");
        }
        
        try {
            verifierEnergie(3);
            verifierMaintenance();
            
            consommerEnergie(3);
            ajouterHistorique("Envoi de données: " + donnees + " via " + this.reseauConnecte);
        } catch (EnergieInsuffisanteException | MaintenanceRequiseException e) {
            throw new RobotException("Échec de l'envoi des données: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Connecté : %s", connecte ? "Oui (" + reseauConnecte + ")" : "Non");
    }
}