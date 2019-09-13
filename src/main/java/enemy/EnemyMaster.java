package src.main.java.enemy;

import java.util.HashMap;
import java.util.HashSet;

public class EnemyMaster {

    static int maxPilotedLockOn = 3;

    static HashMap<Integer, Assignment> pilotedAttacking = new HashMap<>();
    static int lastID = 0;

    protected static int requestID() {
        lastID++;
        return lastID;
    }

    protected static void idKilled(int id) {
        if (pilotedAttacking.containsKey(id)) {
            pilotedAttacking.remove(id);
        }

    }

    protected static Assignment requestAssignment(int id, EnemyType type) {
        if (type == EnemyType.PILOTED) {
            if (pilotedAttacking.keySet().size() < maxPilotedLockOn) {
                return Assignment.ATTACK;
            }
            return Assignment.STRAFE;
        }
        return Assignment.NONE;
    }

}
