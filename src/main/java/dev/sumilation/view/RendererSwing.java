package dev.sumilation.view;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RendererSwing extends JPanel {
    private final SimulationMap map;
    private final int cell;
    private final int pad = 2; // внутренние поля клетки

    // эмодзи + цвета подложки
    private final Map<Class<?>, String> emoji = Map.of(
            Predator.class,  "🐺",
            Herbivore.class, "🐑",
            Grass.class,     "🌿",
            Tree.class,      "🌳",
            Rock.class,      "⛰"
    );
    private final Map<Class<?>, Color> bg = Map.of(
            Predator.class,  new Color(255, 235, 238),
            Herbivore.class, new Color(255, 249, 196),
            Grass.class,     new Color(232, 245, 233),
            Tree.class,      new Color(200, 230, 201),
            Rock.class,      new Color(238, 238, 238)
    );

    public RendererSwing(SimulationMap map, int cellSize) {
        this.map = map;
        this.cell = cellSize;
        setPreferredSize(new Dimension(map.getWidth() * cell, map.getHeight() * cell));
        setBackground(new Color(247, 247, 247)); // общий фон чуть теплее белого
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // сглаживание текста/эмодзи
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // сетка
        g2.setColor(new Color(210, 210, 210));
        for (int x = 0; x <= map.getWidth(); x++) g2.drawLine(x * cell, 0, x * cell, map.getHeight() * cell);
        for (int y = 0; y <= map.getHeight(); y++) g2.drawLine(0, y * cell, map.getWidth() * cell, y * cell);

        // подготовим шрифт (эмодзи)
        // если Segoe UI Emoji нет — система подставит ближайший
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cell - pad * 2));
        FontMetrics fm = g2.getFontMetrics();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Position p = new Position(x, y);
                Entity e = map.getEntityAt(p);

                // подложка клетки
                int cx = x * cell, cy = y * cell;
                Color c = (e == null) ? Color.WHITE : bg.getOrDefault(e.getClass(), Color.WHITE);
                g2.setColor(c);
                g2.fillRect(cx + 1, cy + 1, cell - 1, cell - 1); // чуть не доходя до линии сетки

                // эмодзи по центру
                String s = emojiOf(e);
                if (s != null && !s.isBlank()) {
                    int textW = fm.stringWidth(s);
                    int textH = fm.getAscent() - fm.getDescent(); // «видимая» высота
                    int tx = cx + (cell - textW) / 2;
                    int ty = cy + (cell + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setColor(Color.BLACK); // цвет не влияет на цвет эмодзи, но нужен для fallback-символов
                    g2.drawString(s, tx, ty);
                }
            }
        }
    }

    private String emojiOf(Entity e) {
        if (e == null) return ""; // пустая клетка
        if (e instanceof Predator)  return emoji.get(Predator.class);
        if (e instanceof Herbivore) return emoji.get(Herbivore.class);
        if (e instanceof Grass)     return emoji.get(Grass.class);
        if (e instanceof Tree)      return emoji.get(Tree.class);
        if (e instanceof Rock)      return emoji.get(Rock.class);
        return "?";
    }
}