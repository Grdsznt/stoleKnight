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
    private int radiusX, radiusY;

    public SimpleHitbox(Actor actor, int radiusX, int radiusY) {
        this.actor = actor;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public boolean intersectsOval(Actor other) {
        if (other instanceof Hero1) {
            Hero1 hero = (Hero1) other;
            SimpleHitbox otherHitbox = hero.getHitbox();
            int dx = other.getX() - actor.getX();
            int dy = other.getY() - actor.getY();
    
            double angle = Math.atan2(dy, dx);
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
    
            double distance = Math.sqrt(dx * dx + dy * dy);
    
            double overlapX = radiusX * cosAngle + otherHitbox.getRadiusX() * cosAngle;
            double overlapY = radiusY * sinAngle + otherHitbox.getRadiusX() * sinAngle;
    
            return distance < Math.sqrt(overlapX * overlapX + overlapY * overlapY);
        } else {
            return false;
        }
    }
    
    public void drawHitbox() {
        GreenfootImage image = actor.getImage();
        image.setColor(new Color(255, 0, 0));
        image.drawOval(actor.getX() - radiusX, actor.getY() - radiusY, radiusX * 2, radiusY * 2);
        actor.setImage(image);
    }
    
    public int getRadiusX() {
        return radiusX;
    }

    public int getRadiusY() {
        return radiusY;
    }
}
