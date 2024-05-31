import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Felix Zhao 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{
    // The number represents the openings. 0000 - up, down, left, rigth - 1-15
    // 4x4 max
    int[][] worldGrid = new int[4][4];
    private int level;
    private boolean mainPathDone;
    
    MouseInfo mouse;
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        level = 1;
        String[][] tempGrid = new String[4][4];
        int spawnRow = Greenfoot.getRandomNumber(2)+1;
        int spawnCol = Greenfoot.getRandomNumber(2)+1;
        int direction = Greenfoot.getRandomNumber(4);
        String directions = "";
        for (int i = 0; i < 4; i++) {
            if (i == direction) {
                directions += "1";
            } else {
                directions += "0";
            }
        }
        tempGrid[spawnRow][spawnCol] = directions;
        System.out.println(tempGrid[spawnRow][spawnCol]);
        mainPathDone = false;
        
    }
    
    private void generateRooms(int startRow, int startCol, String[][] grid) {
        Queue<int[]> queue = new LinkedList<int[]>();
        queue.add(new int[]{startRow, startCol});
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
