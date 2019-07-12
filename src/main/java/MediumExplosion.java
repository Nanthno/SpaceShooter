package src.main.java;

public class MediumExplosion extends Explosion {

    public MediumExplosion(int x, int y, int catalistSeperation) {
        xPos = x;
        yPos = y;
        maxStage = 11;
        maxDuration = 40;
        duration = maxDuration;
        radius = 24;
        expType = 2;
        this.catalystSeparation = catalistSeperation;
    }
}