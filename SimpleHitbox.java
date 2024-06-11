import greenfoot.*;
import java.util.ArrayList;
/**
 * Write a description of class simpleHitbox here.
 * 
 * @author Edwin Dong
 * @version (a version number or a date)
 * 
 * Edited by Andy Feng
 */
public class SimpleHitbox  
{
    private Actor actor;
    private int radiusX, radiusY, offsetY, offsetX;
    protected static ArrayList<SimpleHitbox> allHitboxesInWorld = new ArrayList<SimpleHitbox>();

    public SimpleHitbox(Actor actor, int radiusX, int radiusY, int offsetY, int offsetX) {
        this.actor = actor;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.offsetY = offsetY;
        this.offsetX = offsetX;
    }

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

    public boolean intersectsOval(Actor other) {
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
        } else if(actor instanceof Enemy){
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
        return false;
    }
    
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