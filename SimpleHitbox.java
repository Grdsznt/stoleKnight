import greenfoot.*;
import java.util.ArrayList;

/**
 * Oval hitbox class used for collision detection.
 * This class represents an oval-shaped hitbox attached to an actor.
 * It provides methods for collision detection and hitbox rotation.
 * 
 * Authors: Edwin Dong, Andy Feng
 * Version: 1
 */
public class SimpleHitbox  
{
    private Actor actor; // The actor associated with this hitbox
    private int radiusX, radiusY; // Radii of the hitbox (half width and half height)
    private int offsetY, offsetX; // Offset of the hitbox relative to the actor
    protected static ArrayList<SimpleHitbox> allHitboxesInWorld = new ArrayList<SimpleHitbox>(); // List to keep track of all hitboxes in the world

    /**
     * Constructor to initialize the hitbox.
     * 
     * @param actor The actor to which the hitbox is attached.
     * @param radiusX Half of the width of the hitbox (x-axis).
     * @param radiusY Half of the height of the hitbox (y-axis).
     * @param offsetY Vertical offset of the hitbox relative to the actor.
     * @param offsetX Horizontal offset of the hitbox relative to the actor.
     */
    public SimpleHitbox(Actor actor, int radiusX, int radiusY, int offsetY, int offsetX) {
        this.actor = actor;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.offsetY = offsetY;
        this.offsetX = offsetX;
    }

    /**
     * Method to rotate the hitbox around the actor by a specified angle.
     * 
     * @param angleInDegrees The angle (in degrees) by which to rotate the hitbox.
     */
    public void rotateHitbox(double angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);
        double cosTheta = Math.cos(angleInRadians);
        double sinTheta = Math.sin(angleInRadians);
    
        // Calculate center point of the hitbox relative to actor's position
        int centerX = actor.getX() + offsetX;
        int centerY = actor.getY() + offsetY;
    
        // Rotate each corner of the hitbox around the center point
        int rotatedOffsetX = (int) (offsetX * cosTheta - offsetY * sinTheta);
        int rotatedOffsetY = (int) (offsetX * sinTheta + offsetY * cosTheta);
    
        // Update hitbox position based on rotated coordinates
        offsetX = rotatedOffsetX;
        offsetY = rotatedOffsetY;
    }

    /**
     * Method to check if this hitbox intersects with another oval-shaped hitbox.
     * 
     * @param other The other Actor whose hitbox to check against.
     * @return true if the hitboxes intersect, false otherwise.
     */
    public boolean intersectsOval(Actor other) {
        // Check if the other actor is Hero
        if (other instanceof Hero) {
            Hero1 hero = (Hero1) other;
            SimpleHitbox otherHitbox = hero.getHitbox();
            int dx = (other.getX() + otherHitbox.getOffsetX()) - (actor.getX() + offsetX);
            int dy = (other.getY() + otherHitbox.getOffsetY()) - (actor.getY() + offsetY);
    
            double angle = Math.atan2(dy, dx);
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
    
            double distance = Math.sqrt(dx * dx + dy * dy);
    
            double overlapX = radiusX * cosAngle + otherHitbox.getRadiusX() * cosAngle;
            double overlapY = radiusY * sinAngle + otherHitbox.getRadiusX() * sinAngle;
    
            return distance < Math.sqrt(overlapX * overlapX + overlapY * overlapY);
        } 
        // Check if the other actor is Enemy
        else if (other instanceof Enemy){
            Enemy e = (Enemy) other;
            SimpleHitbox otherHitbox = e.getHitbox();
            int dx = (other.getX() + otherHitbox.getOffsetX()) - (actor.getX() + offsetX);
            int dy = (other.getY() + otherHitbox.getOffsetY()) - (actor.getY() + offsetY);
    
            double angle = Math.atan2(dy, dx);
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
    
            double distance = Math.sqrt(dx * dx + dy * dy);
    
            double overlapX = radiusX * cosAngle + otherHitbox.getRadiusX() * cosAngle;
            double overlapY = radiusY * sinAngle + otherHitbox.getRadiusX() * sinAngle;
    
            return distance < Math.sqrt(overlapX * overlapX + overlapY * overlapY);
        }
        return false; // Default case: no intersection
    }
    
    /**
     * Method to check if this hitbox intersects with another hitbox.
     * 
     * @param other The other SimpleHitbox object to check against.
     * @return true if the hitboxes intersect, false otherwise.
     */
    public boolean isHitBoxesIntersecting(SimpleHitbox other) {
        int dx = (other.getActor().getX() + other.getOffsetX()) - (actor.getX() + offsetX);
        int dy = (other.getActor().getY() + other.getOffsetY()) - (actor.getY() + offsetY);

        double angle = Math.atan2(dy, dx);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
    
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        double overlapX = radiusX * cosAngle + other.getRadiusX() * cosAngle;
        double overlapY = radiusY * sinAngle + other.getRadiusX() * sinAngle;
        
        return distance < Math.sqrt(overlapX * overlapX + overlapY * overlapY);
    }
    
    // Getters for radiusX, radiusY, offsetX, offsetY, and actor
    public int getRadiusX() {
        return radiusX;
    }

    public int getRadiusY() {
        return radiusY;
    }
    
    public int getOffsetY() {
        return offsetY;
    }
    
    public int getOffsetX() {
        return offsetX;
    }
    
    public Actor getActor() {
        return actor;
    }
}
