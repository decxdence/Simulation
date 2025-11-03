package dev.sumilation.domain.system;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

import java.util.*;

public final class VegetationSystem implements SystemPhase {
    @Override
    public void apply(SimulationMap sim, SimulationConfig cfg, Random rnd) {
        List<Entity> snapshot = sim.snapshotEntities();
        List<Grass> grasses = new ArrayList<>();
        for (Entity entity : snapshot) if (entity instanceof Grass grass) grasses.add(grass);

        Set<Position> toPlant = new HashSet<>();

        // локальное распространение
        for (Grass grass : grasses) {
            Position p = grass.getPosition();
            int planted = 0;
            for (Direction dir : Direction.values()) {
                if (planted >= cfg.grassMaxNeighborPlants) break;
                int nx = p.x() + dir.dx, ny = p.y() + dir.dy;
                if (!sim.inBounds(nx, ny)) continue;

                Position pos = new Position(nx, ny);
                if (sim.getEntityAt(pos) != null) continue;

                if (rnd.nextDouble() < cfg.grassSpreadProb) {
                    toPlant.add(pos);
                    planted++;
                }
            }
        }

        // случайные посевы
        for (int i = 0; i < cfg.grassRandomSeeds; i++) {
            int x = rnd.nextInt(sim.getWidth());
            int y = rnd.nextInt(sim.getHeight());
            Position pos = new Position(x, y);
            if (sim.getEntityAt(pos) == null && rnd.nextDouble() < cfg.grassRandomProb) {
                toPlant.add(pos);
            }
        }

        for (Position pos : toPlant) {
            if (sim.getEntityAt(pos) == null) {
                sim.putAt(pos, new Grass(pos));
            }
        }
    }
}