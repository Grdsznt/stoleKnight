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
    }

    private void drawHitbox() {
        GreenfootImage image = getImage();
        image.clear();
        image.setColor(new Color(0, 255, 0, 128)); // Red color with transparency
        int centerX = target.getImage().getWidth() / 2;
        int centerY = target.getImage().getHeight() / 2;
        int radiusX = hitbox.getRadiusX();
        int radiusY = hitbox.getRadiusY();
        int offsetX = hitbox.getOffsetX();
        int offsetY = hitbox.getOffsetY();
        
        image.drawOval(centerX - radiusX + offsetX, centerY - radiusY + offsetY, radiusX * 2, radiusY * 2);
    }
}