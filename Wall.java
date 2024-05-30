import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Tile
{
    Wall() {
         GreenfootImage image = new GreenfootImage(75, 75);
       image.setColor(new Color(0, 0, 0));
       image.drawRect(0, 0, 74, 74);

       setImage(image);
    }
    /**
     * Act - do whatever the Wall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
       
    }
}

