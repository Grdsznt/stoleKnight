import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Felix Zhao 
 * @version (a version number or a date)
 * 
 * Edited by Andy Feng
 */
public class GameWorld extends World
{
    // for testing purposes
    public final static int BLOCK_SIZE = 48;
    // The number represents the openings. 0000 - up, down, left, rigth - 1-15
    // 0000 0000 - special rooms | directions
    // 0001 - start | 0010 - end | 0100 - shop/loot | 1000 - special place
    // 4x4 max
    int[][] worldGrid = new int[5][5];
    RoomData[][] roomGrid = new RoomData[5][5];
    private int level;
    private int currentRoomRow = -1;
    private int currentRoomCol = -1;
    private boolean canChangeRooms = true;
    
    ArrayList<Tile> grid;
    ArrayList<Wall> obstacles;
    private boolean mainPathDone;
    private Map map;
    private Label floorLabel;
    private int floor;
    
    private static boolean mouseHold;
    
    private static MouseInfo mouse;
    /**
     * Constructor for objects of class GameWorld.
     * 
     */
    
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        level = 1;
        // int spawnRow = Greenfoot.getRandomNumber(2)+1;
        // int spawnCol = Greenfoot.getRandomNumber(2)+1;
        map = new Map();
        
        addObject(map, 104, 590);
        generateRooms(2, 2);
        setActOrder(Hero.class);
        // setPaintOrder(Wall.class, Chest.class, Weapon.class, Hero.class);
        
        // testing
        addObject(new Ogre(500, 500), 500, 500);
        addObject(new Hero1(), 800, 600);
        
        mouseHold = false;
        setPaintOrder(Hero.class, Weapon.class, Overlay.class, SightlineOverlay.class, Enemy.class);
        // background stuff
        GreenfootImage background = new GreenfootImage(1200, 720);
        background.setColor(new Color(34, 34, 34));
        background.fillRect(0, 0, 1200, 720);
        GreenfootImage stats = new GreenfootImage("statbar.png");
        stats.scale(183, 123);
        background.setColor(Color.WHITE);
        
        background.drawImage(stats, 15, 15);
        setBackground(background);
        floor = 1;
        floorLabel = new Label("Floor 1", 30);
        addObject(floorLabel, 104, 690);
    }
    
    public ArrayList<Wall> getObstacles() {
        obstacles = (ArrayList<Wall>) getObjects(Wall.class);
        return obstacles;
    }
    
    private void generateRooms(int startRow, int startCol) {
        worldGrid[startRow][startCol] += 16;
        ArrayList<int[]> path = new ArrayList<int[]>();
        path.add(new int[]{startRow, startCol});
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
            
            path.add(new int[]{currRow, currCol});
        }
        worldGrid[currRow][currCol] += 32;
        
        
        boolean hasShop = false;
        boolean hasSpecial = false;
        for (int i = 1; i < path.size()-1; i++) {
            int row = path.get(i)[0];
            int col = path.get(i)[1];
            if (row != 0 && (worldGrid[row][col] & 8) == 0 && (worldGrid[row-1][col] & 48) == 0) {
                worldGrid[row][col] += 8;
                if (worldGrid[row-1][col] == 0 && !hasShop && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row-1][col] += 64;
                    hasShop = true;
                } else if (worldGrid[row-1][col] == 0 && !hasSpecial && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row-1][col] += 128;
                    hasSpecial = true;
                }
                worldGrid[row-1][col] += 4;
            }
            
            if (row < 4 && (worldGrid[row][col] & 4) == 0 && (worldGrid[row+1][col] & 48) == 0) {
                worldGrid[row][col] += 4;
                if (worldGrid[row+1][col] == 0 && !hasShop && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row+1][col] += 64;
                    hasShop = true;
                } else if (worldGrid[row+1][col] == 0 && !hasSpecial && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row+1][col] += 128;
                    hasSpecial = true;
                }
                worldGrid[row+1][col] += 8;
            }
            
            if (col != 0 && (worldGrid[row][col] & 2) == 0 && (worldGrid[row][col-1] & 48) == 0) {
                worldGrid[row][col] += 2;
                if (worldGrid[row][col-1] == 0 && !hasShop && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row][col-1] += 64;
                    hasShop = true;
                } else if (worldGrid[row][col-1] == 0 && !hasSpecial && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row][col-1] += 128;
                    hasSpecial = true;
                }
                worldGrid[row][col-1] += 1;
            }
            
            if (col < 4 && (worldGrid[row][col] & 1) == 0 && (worldGrid[row][col+1] & 48) == 0) {
                worldGrid[row][col] += 1;
                if (worldGrid[row][col+1] == 0 && !hasShop && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row][col+1] += 64;
                    hasShop = true;
                } else if (worldGrid[row][col+1] == 0 && !hasSpecial && Greenfoot.getRandomNumber(2) == 0) {
                    worldGrid[row][col+1] += 128;
                    hasSpecial = true;
                }
                worldGrid[row][col+1] += 2;
            }
        }
        
        
        for (int i = 0; i < 5; i++) {
            for (int j  = 0; j < 5; j++) {
                if (worldGrid[i][j] == 0) {
                    continue;
                }
                
                roomGrid[i][j] = new RoomData(worldGrid[i][j]);
            }
        }
        
        for (RoomData[] a : roomGrid) {
            System.out.println(Arrays.toString(a));
        }
        
        System.out.println(startRow + " " + startCol);
        for (int[] a : worldGrid) {
            System.out.println(Arrays.toString(a));
        }
        
        System.out.println("");
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                
                // add 192 because there's 4 tiles of extra size to the left
                addObject(new Floor(null, i, j), j*BLOCK_SIZE+BLOCK_SIZE/2+192, i*BLOCK_SIZE+BLOCK_SIZE/2);
            }
        }
        loadRoom(startRow, startCol);
    }
    
    // Loads the room at the specific place
    private void loadRoom(int row, int col) {
        if (currentRoomRow >= 0 && currentRoomCol >= 0) {
            unloadRoom(currentRoomRow, currentRoomCol);
        }
        currentRoomRow = row;
        currentRoomCol = col;
        Tile[][] room = roomGrid[row][col].getTileData();
        // The world is 21x15 - 48 pixels per block - adjust properly
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                if (room[i][j] == null) {
                    continue;
                }
                // add 192 because there's 4 tiles of extra size to the left
                addObject(room[i][j], j*BLOCK_SIZE+BLOCK_SIZE/2+192, i*BLOCK_SIZE+BLOCK_SIZE/2);
            }
        }
        
        map.setActiveRoom(row, col);
        map.drawRoom(row, col, worldGrid[row][col]);
        map.drawPaths(row, col, worldGrid[row][col]);
        
        if ((worldGrid[row][col] & 1) != 0) {
            map.drawRoom(row, col+1, worldGrid[row][col+1]);
        }
        
        if ((worldGrid[row][col] & 2) != 0) {
            map.drawRoom(row, col-1, worldGrid[row][col-1]);
        }
        
        if ((worldGrid[row][col] & 4) != 0) {
            map.drawRoom(row+1, col, worldGrid[row+1][col]);
        }
        
        if ((worldGrid[row][col] & 8) != 0) {
            map.drawRoom(row-1, col, worldGrid[row-1][col]);
        }
        
    }
    
    private void unloadRoom(int row, int col) {
        Tile[][] room = roomGrid[row][col].getTileData();
        // The world is 25x15 - 48 pixels per block - adjust properly
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                if (room[i][j] == null) {
                    continue;
                }
                removeObject(room[i][j]);
            }
        }
    }

    /**
     * Switches to a different room
     *
     * @param row The row
     * @param col The column
     * @return Returns if the changing the rooms worked
     */
    public boolean changeRooms(int row, int col) {
        // checks if it's in bound of the 5x5 thing or if changing rooms is valid
        // should probably check if the room actually exsits
        if (!canChangeRooms || row < 0 || row > 4 || col < 0 || col > 4) {
            return false;
        }
        loadRoom(row, col);
        return true;
    }
    
    public void nextMap(Hero hero) {
        unloadRoom(currentRoomRow, currentRoomCol);
        map.resetMap();
        worldGrid = new int[5][5];
        roomGrid = new RoomData[5][5];
        currentRoomRow = 2;
        currentRoomCol = 2;
        generateRooms(2, 2);
        // 500, 500 temp numbers - change later
        hero.setLocation(500, 500);
        
        floor++;
        floorLabel.setValue("Floor " + floor);
    }
    
    /**
     * Switches to a different room
     *
     * @param pos The position of the new room (row, col)
     * @return Returns if the changing the rooms worked
     */
    public boolean changeRooms(int[] pos) {
        return changeRooms(pos[0], pos[1]);
    }
    
    /**
     * Returns the position of the room on the map
     *
     * @return Returns the position of the room on the map
     */
    public int[] getRoomPosition() {
        return new int[]{currentRoomRow, currentRoomCol};
    }
    
    public void act() {
        mouse = Greenfoot.getMouseInfo();
    }
    
    public static int getMouseX() {
        if(mouse != null) return mouse.getX();
        return -1;
    }
    
    public static int getMouseY() {
        if(mouse != null) return mouse.getY();
        return -1;
    }
    
    public static boolean isMousePressed(Actor obj) {
        if(mouse != null) return Greenfoot.mousePressed(obj);
        return false;
    }
    
    public static boolean isMouseDragged(Actor obj) {
        if(mouse != null) return Greenfoot.mouseDragged(obj);
        return false;
    }
    
    public static boolean hasMouseDragEnded(Actor obj) {
        if(mouse != null) return Greenfoot.mouseDragEnded(obj);
        return false;
    }
    
    public static boolean isMouseClicked(Actor obj) {
        if(mouse != null) return Greenfoot.mouseClicked(obj);
        return false;
    }
    
    public static boolean isMouseHolding(){
        if(mouseHold && (hasMouseDragEnded(null) || isMouseClicked(null))) mouseHold = false;
        if(!mouseHold && isMousePressed(null)) mouseHold = true;
        return mouseHold;
    }
}
