import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Hitbox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Hitbox extends SuperSmoothMover
{
    protected SuperSmoothMover parent;
    protected double xOffset;
    protected double yOffset;
    // block list - blocks the movement while hitlist is just detection
    
    public Hitbox(SuperSmoothMover parent, int xOffset, int yOffset) {
        this.parent = parent;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    public abstract int checkBlockHitX();
    public abstract int checkBlockHitY();
    
    public void act() {
        double xTotalDistance = parent.getPreciseX() - getPreciseX()+xOffset;
        double yTotalDistance = parent.getPreciseY() - getPreciseY()+yOffset;
        double totalMovement = Math.pow(yTotalDistance, 2) + Math.pow(xTotalDistance, 2);
        int times = (int)(totalMovement / 400)+1;
        double xDistance = xTotalDistance/times;
        double yDistance = yTotalDistance/times;
        for (int i = 0; i < times; i++) {
            setLocation(getPreciseX()+xDistance, getPreciseY());
            int val = checkBlockHitX();
            if (val == 2) {
                xDistance = 0;
                if (parent instanceof Hero) {
                   ((Hero)parent).resetXVelocity(); 
                }
                
            }
            setLocation(getPreciseX(), getPreciseY()+yDistance);
            val = checkBlockHitY();
            if (val == 2) {
                yDistance = 0;
                if (parent instanceof Hero) {
                   ((Hero)parent).resetYVelocity(); 
                }
            }
        }
        parent.setLocation(getPreciseX()-xOffset, getPreciseY()-yOffset);
    }
    
}
