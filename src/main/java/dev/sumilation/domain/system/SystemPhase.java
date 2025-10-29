package dev.sumilation.domain.system;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import java.util.Random;

public interface SystemPhase {
    void apply(SimulationMap sim, SimulationConfig cfg, Random rnd);
}