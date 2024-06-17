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
import java.util.HashSet;
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
    private GreenfootImage title = new GreenfootImage("gameTitle.png");
    private int titleX = (getWidth() - title.getWidth())/2;
    private int titleY = 140;
    
    // three boxes
    private SuperTextBox instructions = new SuperTextBox("Instructions", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox start = new SuperTextBox("New Game", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox continueGame = new SuperTextBox("Continue Game", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private int floor, health, energy, slot1, slot2, gold;// 0 for sword, 1 for bow, 2 for wand
    private ArrayList<String> buffs;
    
    /**
     * Constructor for objects of class StartWorld.
     */
    public StartWorld()
    {    
        // Create a new world with 1200x720 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        setBackground(cover);
        
        
        //Add title
        getBackground().drawImage(title, titleX, titleY);
        boolean hasGameData = false;
        //Add instruction and start boxes
        addObject(instructions, 600, 420);
        addObject(start, 600, 520);
        
        
        ArrayList<String> al = readData();
        buffs = new ArrayList<String>();
        if (al.size() > 5) {
            floor = Integer.parseInt(al.get(0));
            health = Integer.parseInt(al.get(1));
            energy = Integer.parseInt(al.get(2));
            slot1 = Integer.parseInt(al.get(3));
            slot2 = Integer.parseInt(al.get(4));
            gold = Integer.parseInt(al.get(5));
            for (int i = 6; i < al.size(); i++) {
                buffs.add(al.get(i));
            }
            hasGameData = true;
        } else {
            floor = 1;
            health = 10;
            energy = 100;
            slot1 = 0;
            slot2 = 3; // no weapon
            gold = 10;
        }
        
        if (hasGameData) {
            addObject(continueGame, 600, 620);
        }
    }
    
    public StartWorld(ArrayList<String> saveData) { // constructor to load past game
        super(1200, 720, 1);
        setBackground(cover);
        
        //Add title
        getBackground().drawImage(title, titleX, titleY);
        
        //Add instruction and start boxes
        addObject(instructions, 600, 420);
        addObject(start, 600, 520);
        addObject(continueGame, 600, 620);
        this.floor = Integer.parseInt(saveData.get(0));
        this.health = Integer.parseInt(saveData.get(1));
        this.energy = Integer.parseInt(saveData.get(2));
        this.slot1 = Integer.parseInt(saveData.get(3));
        this.slot2 = Integer.parseInt(saveData.get(4));
        this.gold = Integer.parseInt(saveData.get(5));
        this.buffs = new ArrayList<String>();
        ArrayList<String> data = new ArrayList<String>();
        data.add(saveData.get(0));
        data.add(saveData.get(1));
        data.add(saveData.get(2));
        data.add(saveData.get(3));
        data.add(saveData.get(4));
        data.add(saveData.get(5));
        for (int i = 6; i < saveData.size() ; i++) {
            buffs.add(saveData.get(i));
        }
        for (String buff : buffs) {
            data.add(buff);
        }
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
            BuffWorld.resetUnselectedList();
            gWorld = new GameWorld();
            Greenfoot.setWorld(gWorld);
        }
        if (Greenfoot.mousePressed(continueGame) && (floor != 1 || energy != 100 || slot1 != 0 || slot2 != 3)) {
            BuffWorld.resetUnselectedList();
            BuffWorld.removeUnselectedList(buffs);
            gWorld = new GameWorld(floor, health, energy, slot1, slot2, gold, buffs);
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
    
    public int getHealth(){
        return health;
    }
    
    public int getEnergy(){
        return energy;
    }
    
    public int getFirstSlot(){
        return slot1;
    }
    public int getSecondSlot(){
        return slot2;
    }
    
    /**
     * Returns gold
     *
     * @return Returns the amount of gold
     */
    public int getGold() {
        return gold;
    }
    
    /**
     * Returns buffs
     *
     * @return Returns all the saved buffs of the player
     */
    public ArrayList<String> getBuffs() {
        return buffs;
    }


}
