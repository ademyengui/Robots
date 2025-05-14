import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RobotGUI extends JFrame {
    protected  RobotLivraison robot;
    protected  JTextArea infoArea;
    protected  JTextArea historiqueArea;
    
    public RobotGUI() {
        super("Gestion de Robot de Livraison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        // Initialisation du robot
        robot = new RobotLivraison("R202", 50, 50);
        
        // Panel d'information
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoPanel.add(new JLabel("État du Robot:"), BorderLayout.NORTH);
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        
        // Panel d'historique
        JPanel histPanel = new JPanel(new BorderLayout());
        historiqueArea = new JTextArea();
        historiqueArea.setEditable(false);
        histPanel.add(new JLabel("Historique:"), BorderLayout.NORTH);
        histPanel.add(new JScrollPane(historiqueArea), BorderLayout.CENTER);
        
        // Panel de contrôle
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        
        JPanel basicControls = new JPanel(new GridLayout(1, 4));
        basicControls.add(createButton("Démarrer", e -> demarrerRobot()));
        basicControls.add(createButton("Arrêter", e -> arreterRobot()));
        basicControls.add(createButton("Recharger", e -> rechargerRobot()));
        basicControls.add(createButton("Connecter", e -> connecterRobot()));
        
        JPanel livraisonControls = new JPanel(new GridLayout(1, 3));
        livraisonControls.add(createButton("Charger Colis", e -> chargerColis()));
        livraisonControls.add(createButton("Livrer", e -> livrerColis()));
        livraisonControls.add(createButton("Effectuer Tâche", e -> effectuerTache()));
        
        controlPanel.add(basicControls);
        controlPanel.add(livraisonControls);
        
        // Ajout des panels à la fenêtre
        add(infoPanel, BorderLayout.NORTH);
        add(histPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        updateDisplay();
    }
    
    protected  JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }
    
    protected  void demarrerRobot() {
        try {
            robot.demarrer();
            updateDisplay();
        } catch (RobotException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected  void arreterRobot() {
        robot.arreter();
        updateDisplay();
    }
    
    protected  void rechargerRobot() {
        String input = JOptionPane.showInputDialog(this, "Quantité à recharger (%):");
        if (input != null) {
            try {
                int quantite = Integer.parseInt(input);
                robot.recharger(quantite);
                updateDisplay();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    protected  void connecterRobot() {
        String reseau = JOptionPane.showInputDialog(this, "Nom du réseau:");
        if (reseau != null && !reseau.isEmpty()) {
            try {
                robot.connecter(reseau);
                updateDisplay();
            } catch (RobotException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    protected  void chargerColis() {
        String colis = JOptionPane.showInputDialog(this, "Description du colis:");
        if (colis != null && !colis.isEmpty()) {
            String destination = JOptionPane.showInputDialog(this, "Destination:");
            if (destination != null && !destination.isEmpty()) {
                try {
                    robot.chargerColis(colis, destination);
                    updateDisplay();
                } catch (RobotException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    protected  void livrerColis() {
        String inputX = JOptionPane.showInputDialog(this, "Coordonnée X de destination:");
        String inputY = JOptionPane.showInputDialog(this, "Coordonnée Y de destination:");
        
        if (inputX != null && inputY != null) {
            try {
                int x = Integer.parseInt(inputX);
                int y = Integer.parseInt(inputY);
                robot.faireLivraison(x, y);
                updateDisplay();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Coordonnées invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (RobotException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    protected  void effectuerTache() {
        try {
            robot.effectuerTache();
            updateDisplay();
        } catch (RobotException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected  void updateDisplay() {
        infoArea.setText(robot.toString());
        historiqueArea.setText(robot.getHistorique());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RobotGUI gui = new RobotGUI();
            gui.setVisible(true);
        });
    }
}