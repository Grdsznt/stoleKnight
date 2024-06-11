import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot, and MouseInfo)

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
        
        setLocation(target.getX(), target.getY());
        drawHitbox();
    }

    private void drawHitbox() {
        GreenfootImage image = getImage();
        image.clear();
        image.setColor(new Color(255, 0, 0, 128)); // Red color with transparency
        int centerX = target.getImage().getWidth() / 2;
        int centerY = target.getImage().getHeight() / 2;
        int radiusX = hitbox.getRadiusX();
        int radiusY = hitbox.getRadiusY();
        
        image.drawOval(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);
    }
}