import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
/**
 * This is the StartWorld where the user will see first.
 * 
 * @author Jean P
 * @author Edwin Dong
 * @version June 2024
 */
public class StartWorld extends World
{
    //Worlds
    private GameWorld gWorld;
    private InstructionWorld instructionsWorld;
    
    //Image
    private GreenfootImage cover = new GreenfootImage("soul-knight-cover.jpg");
    
    //Game title
    private SuperTextBox title = new SuperTextBox("Stole Knight", new Color(150,75,0,0), Color.BLACK, new Font(70), true, 400, 0, Color.WHITE);
    
    //Two boxes
    private SuperTextBox instructions = new SuperTextBox("Instructions", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox start = new SuperTextBox("Start", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox continueGame = new SuperTextBox("Continue Game", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private int floor;
    
    /**
     * Constructor for objects of class StartWorld.
     */
    public StartWorld()
    {    
        // Create a new world with 1200x720 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        setBackground(cover);
        
        
        //Add title
        addObject(title, 600, 250);
        
        //Add instruction and start boxes
        addObject(instructions, 600, 400);
        addObject(start, 600, 500);
        addObject(continueGame, 600, 600);
        
        ArrayList<String> al = readData();
        if (al.size() > 0) {
            floor = Integer.parseInt(al.get(0));
        } else {
            floor = 1;
        }
    }
    
    public StartWorld(int floor) {
        super(1200, 720, 1);
        setBackground(cover);
        
        //Add title
        addObject(title, 600, 250);
        
        //Add instruction and start boxes
        addObject(instructions, 600, 400);
        addObject(start, 600, 500);
        this.floor = floor;
        ArrayList<String> data = new ArrayList<String>();
        data.add(Integer.toString(floor));
        try {
            FileWriter out = new FileWriter("Data.txt");
            PrintWriter output = new PrintWriter (out);
            for (String s: data) {
                output.println(s); // for every line in the arraylist, write it to the output file
            }
            output.close();
        } catch(IOException e) {
            System.out.println("Error: " + e); // otherwise, if there is an IOException, let the user know
        }
        data = readData(); // read data
        floor = Integer.parseInt(data.get(0));  // set floor to saved floor      
    }
    
    static {
        Hero.damageSoundPlayer();
    }
    
    public void act(){
        //Animate the options while hovering on them
        if(Greenfoot.mouseMoved(instructions)) {
            instructions.updateFont(new Font(40), 450);
        } else if(Greenfoot.mouseMoved(start)) {
            start.updateFont(new Font(40), 450);
        } else if (Greenfoot.mouseMoved(continueGame)) {
            continueGame.updateFont(new Font(40), 450);
        }
        
        //Return to normal if not hovering on them
        if(Greenfoot.mouseMoved(this)) {
            instructions.updateFont(new Font(30), 400);
            start.updateFont(new Font(30), 400);
            continueGame.updateFont(new Font(30), 400);
        }
        
        //Go to each world if pressed
        if(Greenfoot.mousePressed(instructions)) {
            instructionsWorld = new InstructionWorld(this);
            Greenfoot.setWorld(instructionsWorld);
        }
        if(Greenfoot.mousePressed(start)) {
            gWorld = new GameWorld(1);
            Greenfoot.setWorld(gWorld);
        }
        if (Greenfoot.mousePressed(continueGame)) {
            gWorld = new GameWorld(floor);
            Greenfoot.setWorld(gWorld);
        }
    }
    /*
     * Method to read data into an arraylist
     */
    public ArrayList<String> readData() {
        ArrayList<String> d = new ArrayList<String>();
        try {
            Scanner s = new Scanner(new File("Data.txt")); // create a scanner for the file
            boolean moreLines = true;
            while (moreLines) { // while there are more lines, add the line to the arraylist as a string
                try {
                    d.add(s.nextLine());
                } catch (NoSuchElementException e) {
                    moreLines = false; // if reached end, stop looping
                }
            }
            s.close(); // close the scanner
        } catch (FileNotFoundException e) { // if the file is not found, let the user know
            System.out.println("File not found.");
        }
        return d;
    }
    
    public int getFloor(){
        return floor;
    }
}
