import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * This is where the main games is held. It generates and controls the floor
 * </p>
 * <br>
 * 
 * 
 * <h2> List of Features </h2>
 * <p>
 * You control a player and fight through many floors of dungeons. 
 * Currently, there are 3 different enemies with different behaviours.
 * Additionally, there's 3 different weapons in the game with different mechanics. (Sword, Bow, Wand)
 * The player has 2 different "hitpoints" - HP and Shield. Shield can regenerate but HP cannot without other factors
 * The layout of the floor are randomized will the rooms themselves are layouts/premade.
 * There are speical rooms throughout a floor. A chest room, A ! room and a portal room.
 * For the player, every third floor completed you can pick a buff which buffs the player
 * </p>
 * 
 * <h2> References </h2>
 * <h3> Art </h3>
 * https://0x72.itch.io/dungeontileset-ii - GrafxKid <br>
 * https://www.deviantart.com/kristyglas/art/Pixel-Art-Dungeon-Entrance-Door-835889504 - kristyglas <br>
 * https://whatoplay.com/post/prequel-soul-knight/ - Chilly Room <br>
 * <h3> Sound </h3>
 * 
 * <h3> Code </h3>
 * SuperStatbar, SuperTextBox and SuperSmoothMover is taken from Jordan Cohen <br>
 * 
 * 
 * <h2> Bugs </h2>
 * <ul>
 *  <li> Sometimes enemies can get stuck on walls</li>
 *  <li> Enemies won't properly target somtimes but this due to fact because the pathfinding is at lower accuracy for performance</li>
 * </ul>
 * 
 * Edited by Andy Feng
 * @author Felix Zhao 
 * @version 0.1
 * 
 * 
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
        // addObject(new Ogre(500, 500), 500, 500);
        addObject(new Hero1(), 800, 600);
        // addObject(new Wizard(800, 200), 800, 200);
        // addObject(new Imp(400, 400), 400, 400);
        
        mouseHold = false;
        setPaintOrder(Weapon.class, Hero.class, Overlay.class, Projectile.class, BallProjectile.class, Enemy.class);
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
        
        // for (RoomData[] a : roomGrid) {
            // System.out.println(Arrays.toString(a));
        // }
        
        // System.out.println(startRow + " " + startCol);
        // for (int[] a : worldGrid) {
            // System.out.println(Arrays.toString(a));
        // }
        
        // System.out.println("");
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
        ArrayList<Enemy> enemyList = roomGrid[row][col].getEnemies();
        
        for (Enemy enemy : enemyList) {
            addObject(enemy, 500, 500);
        }
        
        
        if (enemyList.size() != 0) {
            for (Hero hero: getObjects(Hero.class)) {
                hero.addHitboxList(RoomExit.class);
                
            }
            canChangeRooms = false;
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                if (room[i][j] == null) {
                    continue;
                }
                // add 192 because there's 4 tiles of extra size to the left
                addObject(room[i][j], j*BLOCK_SIZE+BLOCK_SIZE/2+192, i*BLOCK_SIZE+BLOCK_SIZE/2);
                if (enemyList.size() > 0 && room[i][j] instanceof RoomExit) {
                    ((RoomExit)room[i][j]).setState(true);
                }
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
        
        // enemy things
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
       
        // just in case
        for (Enemy enemy : roomGrid[row][col].getEnemies()) {
            if (enemy instanceof Wizard) {
                Wizard wizard = (Wizard) enemy;
                removeObject(wizard.getWand());
            }
            Overlay overlay = enemy.getOverlay();
            removeObject(overlay);
            removeObject(enemy);
        }
        
        for (Actor actor : getObjects(Actor.class)) {
            if (actor instanceof Projectile) {
                removeObject(actor);
            }
            if (actor instanceof BallProjectile) {
                removeObject(actor);
            }
            if (actor instanceof Weapon) {
                if (getObjects(Hero.class).get(0).getWeapons().contains(actor)) {
                    continue;
                }
                removeObject(actor);
            }
            if (actor instanceof BallProjectileHero) {
                removeObject(actor);
            }
        }
    }
    
    public void enemyDied(Enemy enemy) {
        roomGrid[currentRoomRow][currentRoomCol].getEnemies().remove(enemy);
        if (roomGrid[currentRoomRow][currentRoomCol].getEnemies().size() == 0) {
            canChangeRooms = true;
            for (Hero hero: getObjects(Hero.class)) {
                hero.removeHitboxList(RoomExit.class);
            }
            for (RoomExit tile : getObjects(RoomExit.class)) {
                tile.setState(false);
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
        if (floor == 10) {
            Greenfoot.setWorld(new EndWorld(1));
            return;
        }
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
        Greenfoot.setWorld(new BuffWorld(this, hero));
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
    
    /**
     * Returns the floor number
     *
     * @return Returns the floor number
     */
    public int getFloor() {
        return floor;
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
