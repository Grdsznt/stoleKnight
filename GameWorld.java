import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

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
        int spawnRow = Greenfoot.getRandomNumber(2)+1;
        int spawnCol = Greenfoot.getRandomNumber(2)+1;
        generateRooms(spawnRow, spawnCol);
    }
    
    private void generateRooms(int startRow, int startCol) {
        int currRow = startRow;
        int currCol = startCol;
        for (int i = 0; i < 4; i++) {
            boolean valid = false;
            while (!valid) {
                int direction = Greenfoot.getRandomNumber(4);
                if (direction == 0) {
                    if (currRow-1 >= 0 && worldGrid[currRow-1][currCol] == 0) {
                        worldGrid[currRow][currCol] += 8;
                        worldGrid[currRow-1][currCol] += 4;
                        currRow--;
                        valid = true;
                    }
                } else if (direction == 1) {
                    if (currRow+1 < 4 && worldGrid[currRow+1][currCol] == 0) {
                        worldGrid[currRow][currCol] += 4;
                        worldGrid[currRow+1][currCol] += 8;
                        currRow++;
                        valid = true;
                    }
                } else if (direction == 2) {
                    if (currCol-1 >= 0 && worldGrid[currRow][currCol-1] == 0) {
                        worldGrid[currRow][currCol] += 2;
                        worldGrid[currRow][currCol-1] += 1;
                        currCol-=1;
                        valid = true;
                    }
                } else if (direction == 3) {
                    if (currCol+1 < 4 && worldGrid[currRow][currCol+1] == 0) {
                        worldGrid[currRow][currCol] += 1;
                        worldGrid[currRow][currCol+1] += 2;
                        currCol++;
                        valid = true;
                    }
                }
            }
        }
        
        for (int[] a : worldGrid) {
            System.out.println(Arrays.toString(a));
        }
        System.out.println();
        
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
