import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;



public class EnhancedRobotGUI extends JFrame {
    protected  RobotLivraisonEco robot;
    protected  DeliveryMap deliveryMap;
    protected  JTextArea infoArea;
    protected  JTextArea historiqueArea;
    
    public EnhancedRobotGUI() {
        super("Système Éco-Robot de Livraison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        
        // Initialize delivery map
        deliveryMap = DeliveryMap.createDefaultMap();
        
        // Initialize robot at warehouse location
        Location warehouse = deliveryMap.getLocation("Entrepôt Principal");
        robot = new RobotLivraisonEco("ECO-001", warehouse.x, warehouse.y, deliveryMap);
        
        // Create main panels
        JPanel mapPanel = createMapPanel();
        JPanel controlPanel = createControlPanel();
        
        // Add components to frame
        add(mapPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        updateDisplay();
    }
    
    protected  JPanel createMapPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Info display
        infoArea = new JTextArea(5, 40);
        infoArea.setEditable(false);
        
        // History display
        historiqueArea = new JTextArea(10, 40);
        historiqueArea.setEditable(false);
        
        // Map visualization
        JPanel visualMap = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };
        visualMap.setPreferredSize(new Dimension(800, 400));
        
        // Add components to panel
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(new JScrollPane(infoArea));
        textPanel.add(new JScrollPane(historiqueArea));
        
        panel.add(textPanel, BorderLayout.NORTH);
        panel.add(visualMap, BorderLayout.CENTER);
        
        return panel;
    }
    
    protected  void drawMap(Graphics g) {
        // Draw locations
        for (Location loc : deliveryMap.locations.values()) {
            // Draw node
            g.setColor(Color.BLUE);
            g.fillOval(loc.x * 5, loc.y * 5, 20, 20);
            
            // Draw connections
            g.setColor(Color.GRAY);
            for (Location neighbor : loc.adjacentLocations) {
                g.drawLine(loc.x * 5 + 10, loc.y * 5 + 10, 
                          neighbor.x * 5 + 10, neighbor.y * 5 + 10);
            }
            
            // Draw location name
            g.setColor(Color.BLACK);
            g.drawString(loc.name, loc.x * 5, loc.y * 5 - 5);
        }
        
        // Draw robot
        g.setColor(Color.RED);
        g.fillOval(robot.x * 5, robot.y * 5, 20, 20);
        g.drawString("Robot", robot.x * 5, robot.y * 5 - 5);
    }
    
    protected  JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        
        // Basic controls
        JPanel basicControls = new JPanel(new GridLayout(1, 4));
        basicControls.add(createButton("Démarrer", e -> demarrerRobot()));
        basicControls.add(createButton("Arrêter", e -> arreterRobot()));
        basicControls.add(createButton("Recharger", e -> rechargerRobot()));
        basicControls.add(createButton("Connecter", e -> connecterRobot()));
        
        // Delivery controls
        JPanel deliveryControls = new JPanel(new GridLayout(1, 3));
        deliveryControls.add(createButton("Charger Colis", e -> chargerColis()));
        
        JComboBox<String> locationCombo = new JComboBox<>(
            deliveryMap.locations.keySet().toArray(new String[0]));
        deliveryControls.add(locationCombo);
        deliveryControls.add(createButton("Livrer à", e -> {
            String destination = (String) locationCombo.getSelectedItem();
            livrerADestination(destination);
        }));
        
        // Eco controls
        JPanel ecoControls = new JPanel(new GridLayout(1, 2));
        ecoControls.add(createButton("Statistiques Éco", e -> afficherStatistiques()));
        ecoControls.add(createButton("Optimiser Trajet", e -> optimiserTrajet()));
        
        panel.add(basicControls);
        panel.add(deliveryControls);
        panel.add(ecoControls);
        
        return panel;
    }
    
    protected  void livrerADestination(String destination) {
        try {
            if (robot.colisActuel == null) {
                JOptionPane.showMessageDialog(this, "Aucun colis chargé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            robot.destination = destination;
            robot.deliverToLocation(destination);
            updateDisplay();
            repaint();
        } catch (RobotException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected  void afficherStatistiques() {
        String stats = String.format(
            "Statistiques Écologiques:\n" +
            "Émissions CO2 totales: %.2f kg\n" +
            "Distance parcourue: %.2f unités\n" +
            "Énergie restante: %d%%",
            robot.getTotalEmissions(),
            //robot.getTotalDistance(),
            robot.energie
        );
        JOptionPane.showMessageDialog(this, stats, "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected  void optimiserTrajet() {
        if (robot.destination == null) {
            JOptionPane.showMessageDialog(this, "Aucune destination programmée", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Location current = robot.findClosestLocation();
        List<Location> path = deliveryMap.findShortestPath(current.name, robot.destination);
        
        if (path != null) {
            StringBuilder sb = new StringBuilder("Trajet optimisé:\n");
            for (Location loc : path) {
                sb.append("→ ").append(loc.name).append("\n");
            }
            sb.append(String.format("Distance: %.2f unités", calculatePathDistance(path)));
            JOptionPane.showMessageDialog(this, sb.toString(), "Trajet Optimisé", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Aucun trajet trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected  double calculatePathDistance(List<Location> path) {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Location a = path.get(i);
            Location b = path.get(i + 1);
            distance += Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
        }
        return distance;
    }
    // debuuuuuuuuuuuuuuggggggggggggggggggggggg

    protected  JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    protected void updateDisplay() {
        // Update robot info
        infoArea.setText(robot.toString());
        
        // Update history
        historiqueArea.setText(robot.getHistorique());
        
        // Redraw the map visualization
        repaint(); 
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
                JOptionPane.showMessageDialog(this, "Entrez un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
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
    
    // ... (keep all other methods from previous GUI implementation)
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedRobotGUI gui = new EnhancedRobotGUI();
            gui.setVisible(true);
        });
    }
}