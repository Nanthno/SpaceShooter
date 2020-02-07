package src.main.java.enemy;

import java.util.HashMap;

class EnemyMaster {

    private static final HashMap<Integer, Assignment> pilotedAttacking = new HashMap<>();
    private static int lastID = 0;

    static int requestID() {
        lastID++;
        return lastID;
    }

    static Assignment requestAssignment(EnemyType type) {
        if (type == EnemyType.PILOTED) {
            int maxPilotedLockOn = 3;
            if (pilotedAttacking.keySet().size() < maxPilotedLockOn) {
                return Assignment.ATTACK;
            }
            return Assignment.STRAFE;
        }
        return Assignment.NONE;
    }

}
