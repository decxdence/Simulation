package dev.sumilation.app;

import dev.sumilation.domain.creature.Carnivore;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SimulationMap {
    private final int height;
    private final int width;
    private final Map<Position, Entity> WorldMap = new HashMap<>();

    public SimulationMap(int height, int width) {
        this.height = height;
        this.width = width;

        initMap();

    }

    public Map<Position, Entity> getWorldMap() {
        return WorldMap;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Entity getEntityAt(Position p) {
        return WorldMap.get(p);
    }

    public void initMap() {
        for  (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Random rand = new Random();
                int number = rand.nextInt(100) + 1;
                if (number > 1 && number < 40) {

                }
                else if (number >= 40 && number < 70) {
                    WorldMap.put(new Position(x,y), new Grass(new Position(x,y)));
                }
                else if (number >= 70 && number < 84) {
                    WorldMap.put(new Position(x,y), new Herbivore(new Position(x,y), 2, 10));
                }
                else if (number >= 84 && number < 91) {
                    WorldMap.put(new Position(x,y), new Tree(new Position(x,y)));
                }
                else if (number >= 91 && number < 96) {
                    WorldMap.put(new Position(x,y), new Carnivore(new Position(x,y), 2, 10, 3));
                }
                else if (number >= 96 && number < 100) {
                    WorldMap.put(new Position(x,y), new Rock(new Position(x,y)));
                }

                }
            }
        }
    }