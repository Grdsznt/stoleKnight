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

    // public void updateHitbox() {
        // GreenfootImage image = actor.getImage();
        // this.radiusX = image.getWidth() / 2;
        // this.radiusY = image.getHeight() / 2;
    // }

    public boolean intersectsOval(Actor other) {
        int centerX = actor.getX();
        int centerY = actor.getY();

        GreenfootImage otherImage = other.getImage();
        int otherWidth = otherImage.getWidth();
        int otherHeight = otherImage.getHeight();
        int otherLeft = other.getX() - otherWidth / 2;
        int otherRight = other.getX() + otherWidth / 2;
        int otherTop = other.getY() - otherHeight / 2;
        int otherBottom = other.getY() + otherHeight / 2;

        return ellipseContains(centerX, centerY, radiusX, radiusY, otherLeft, otherTop)
            || ellipseContains(centerX, centerY, radiusX, radiusY, otherRight, otherTop)
            || ellipseContains(centerX, centerY, radiusX, radiusY, otherLeft, otherBottom)
            || ellipseContains(centerX, centerY, radiusX, radiusY, otherRight, otherBottom);
    }

    private boolean ellipseContains(int centerX, int centerY, int radiusX, int radiusY, int x, int y) {
        int dx = x - centerX;
        int dy = y - centerY;

        return (dx * dx) / (double) (radiusX * radiusX) + (dy * dy) / (double) (radiusY * radiusY) <= 1.0;
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
