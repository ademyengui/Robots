public class TestRobotLivraison {
    public static void main(String[] args) {
        try {
            // Création d'un robot de livraison
            RobotLivraison robot = new RobotLivraison("R202", 0, 0);
            
            // Démarrage du robot
            robot.demarrer();
            System.out.println(robot);
            
            // Connexion à un réseau
            robot.connecter("Réseau_INSA");
            System.out.println(robot);
            
            // Chargement d'un colis
            robot.chargerColis("PC Portable", "23 rue de la paix, Ariana");
            System.out.println(robot);
            
            // Effectuer la tâche (livraison)
            robot.effectuerTache(); // Simuler l'entrée utilisateur dans la console
            
            // Affichage de l'historique
            System.out.println("\nHistorique des actions:");
            System.out.println(robot.getHistorique());
            
        } catch (RobotException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}