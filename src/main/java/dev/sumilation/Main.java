package dev.sumilation;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationEngine;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.view.RendererSwing;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // 1) создаём конфиг с параметрами
        SimulationConfig cfg = new SimulationConfig();

        // 2) создаём карту с конфигом
        SimulationMap sim = new SimulationMap(cfg.mapWidth, cfg.mapHeight, cfg);

        // 3) создаём движок как раньше
        SimulationEngine engine = new SimulationEngine(sim);

        // 4) создаём визуализатор
        RendererSwing panel = new RendererSwing(sim, 36);

        // 5) создаём окно и запускаем тики
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simulation");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            int delayMs = 15; // скорость обновления
            new Timer(delayMs, e -> {
                engine.makeTurn();
                panel.repaint();
            }).start();
        });
    }
}