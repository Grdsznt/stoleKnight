import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map extends Actor
{
    private ArrayList<Image> usedImages;
    public Map() {
        GreenfootImage image = new GreenfootImage(144, 144);
        image.setColor(new Color(100, 100, 100));
        image.drawRect(0, 0, 143, 143);
        image.drawRect(1, 1, 141, 141);
        image.setColor(new Color(100, 100, 100, 100));
        image.fillRect(2, 2, 139, 139);
        
        setImage(image);
        
        
        usedImages = new ArrayList<Image>();
        usedImages.add(new Image(16, 16));
        usedImages.get(0).getImage().setColor(new Color(255, 255, 255, 250));
        usedImages.get(0).getImage().fillRect(0, 0, 16, 16);
    }
    
    /**
     * Draws the room on the map, centered on a 5x5 grid
     *
     * @param row The row
     * @param col The column
     * @param type The type of room being drawn
     */
    public void drawRoom(int row, int col, int type) {
        // 0, 0, 15, 15
        getImage().setColor(new Color(180, 180, 180));
        getImage().fillRect(16+24*col, 16+24*row, 16, 16);
        // Special room types will have special icons
        if ((type & 16) != 0) {
            Image newImage = new Image("icons/house.png");
            usedImages.add(newImage);
            getWorld().addObject(newImage, 24+24*col+getX()-getImage().getWidth()/2, 24+24*row+getY()-getImage().getHeight()/2);
        }
        
        if ((type & 32) != 0) {
            Image newImage = new Image("icons/portal.png");
            usedImages.add(newImage);
            getWorld().addObject(newImage, 24+24*col+getX()-getImage().getWidth()/2, 24+24*row+getY()-getImage().getHeight()/2);
        }
        
        if ((type & 64) != 0) {
            Image newImage = new Image("icons/chest.png");
            usedImages.add(newImage);
            getWorld().addObject(newImage, 24+24*col+getX()-getImage().getWidth()/2, 24+24*row+getY()-getImage().getHeight()/2);
        }
        
        if ((type & 128) != 0) {
            Image newImage = new Image("icons/eventroom.png");
            usedImages.add(newImage);
            getWorld().addObject(newImage, 24+24*col+getX()-getImage().getWidth()/2, 24+24*row+getY()-getImage().getHeight()/2);
        }
    }
    
    /**
     * Draws the paths on the map, centered on a 5x5 grid
     *
     * @param row The row
     * @param col The column
     * @param type The type of room being drawn
     */
    public void drawPaths(int row, int col, int type) {
        getImage().setColor(new Color(140, 140, 140));
        if ((type & 1) != 0) {
            getImage().fillRect(32+24*col, 20+24*row, 8, 8);
        }
        
        if ((type & 2) != 0) {
            getImage().fillRect(8+24*col, 20+24*row, 8, 8);
        }
        
        if ((type & 4) != 0) {
            getImage().fillRect(20+24*col, 32+24*row, 8, 8);
        }
        
        if ((type & 8) != 0) {
            getImage().fillRect(20+24*col, 8+24*row, 8, 8);
        }
    }
    
    /**
     * Sets the active room on the map (where the player is)
     *
     * @param row The row the player is in
     * @param col The column the player is in
     */
    public void setActiveRoom(int row, int col) {
        if (usedImages.get(0).getWorld() == null) {
            getWorld().addObject(usedImages.get(0), 0, 0);
        }
        usedImages.get(0).setLocation(24+24*col+getX()-getImage().getWidth()/2, 24+24*row+getY()-getImage().getHeight()/2);
        
    }
    
    /**
     * Resets the map to nothing
     *
     */
    public void resetMap() {
        for (Image image : usedImages) {
            getWorld().removeObject(image);
        }
        Image holder = usedImages.get(0);
        usedImages.clear();
        usedImages.add(holder);
        
        GreenfootImage image = new GreenfootImage(144, 144);
        image.setColor(new Color(100, 100, 100));
        image.drawRect(0, 0, 143, 143);
        image.drawRect(1, 1, 141, 141);
        image.setColor(new Color(100, 100, 100, 100));
        image.fillRect(2, 2, 139, 139);
        
        setImage(image);
    }
}
