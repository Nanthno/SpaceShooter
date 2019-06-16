package src.main.java;

class EnemyBasic extends EnemyShip {


    static final double maxSpeed = 2.5;
    static final double minSpeed = 1;

    public EnemyBasic(int y, double xSpeed) {
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 0;
        radius = 8;
    }

    public EnemyBasic(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 0;
        radius = 8;
    }

}

