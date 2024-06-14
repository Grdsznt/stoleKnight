import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot, and MouseInfo)

/**
 * Overlay class for debugging
 * 
 * @author Edwin
 * @version 1
 */
public class Overlay extends Actor {
    private Actor target;
    private SimpleHitbox hitbox;

    public Overlay(Actor target, SimpleHitbox hitbox) {
        this.target = target;
        this.hitbox = hitbox;
        setImage(new GreenfootImage(target.getImage().getWidth(), target.getImage().getHeight()));
        drawHitbox();
    }

    public void act() {
        setLocation(target.getX(), target.getY()); // set location to match actor
    }
    /**
     * Method to draw the oval hitbox
     */
    private void drawHitbox() {
        GreenfootImage image = getImage();
        image.clear();
        image.setColor(new Color(0, 255, 0, 128)); // Green color with transparency
        int centerX = target.getImage().getWidth() / 2;
        int centerY = target.getImage().getHeight() / 2;
        int radiusX = hitbox.getRadiusX();
        int radiusY = hitbox.getRadiusY();
        int offsetX = hitbox.getOffsetX();
        int offsetY = hitbox.getOffsetY();
        // draw oval with a x and y radius and factor in an offset
        image.drawOval(centerX - radiusX + offsetX, centerY - radiusY + offsetY, radiusX * 2, radiusY * 2);
    }
}