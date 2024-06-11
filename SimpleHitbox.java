import greenfoot.*;
/**
 * Write a description of class simpleHitbox here.
 * 
 * @author Edwin Dong
 * @version (a version number or a date)
 */
public class SimpleHitbox  
{
    private Actor actor;
    private int radiusX, radiusY, offsetY, offsetX;

    public SimpleHitbox(Actor actor, int radiusX, int radiusY, int offsetY, int offsetX) {
        this.actor = actor;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.offsetY = offsetY;
        this.offsetX = offsetX;
    }

    public boolean intersectsOval(Actor other) {
        if (other instanceof Hero1) {
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
        } else {
            return false; // edit here
        }
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
