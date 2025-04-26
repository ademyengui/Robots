public abstract class RobotEco extends RobotConnecte {
    protected double empreinteCarbone; // en kg CO2
    
    public RobotEco(String id, int x, int y) {
        super(id, x, y);
        this.empreinteCarbone = 0;
    }
    
    public double getEmpreinteCarbone() {
        return empreinteCarbone;
    }
    
    public void optimiserTrajet(int x, int y) {
        // Implémentation d'un algorithme d'optimisation de trajet
    }
    
    public void activerModeEco() {
        // Réduire la consommation d'énergie
    }
}

