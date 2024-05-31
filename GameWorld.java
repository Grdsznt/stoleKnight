import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{
    
    ArrayList<Tile> grid;
    MouseInfo mouse;
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1280, 720, 1); 
        
    }
    
    public void act() {
        mouse = Greenfoot.getMouseInfo();
    }
    
    public int getMouseX() {
        return mouse.getX();
    }
    
    public int getMouseY() {
        return mouse.getY();
    }
    
    public boolean isMousePressed(Actor obj) {
        return Greenfoot.mousePressed(obj);
    }
    
    public boolean isMouseDragged(Actor obj) {
        return Greenfoot.mouseDragged(obj);
    }
    
    public boolean hasMouseDragEnded(Actor obj) {
        return Greenfoot.mouseDragEnded(obj);
    }
    
    public boolean isMouseClicked(Actor obj) {
        return Greenfoot.mouseClicked(obj);
    }
}
