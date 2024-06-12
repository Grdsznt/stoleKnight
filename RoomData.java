import java.util.Arrays;
import java.util.ArrayList;
import greenfoot.*;
/**
 * This class is used to store all the rooms
 * 
 * @author Felix Zhao
 * @version (a version number or a date)
 */
public class RoomData  
{
    protected Tile[][] roomGrid;
    protected ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    // 0000 - up, down, left, right
    // add unique room shapes later - special rooms e.x chest/shop/special room should be all the same
    public RoomData(int roomType) {
        
        roomGrid = new Tile[15][21]; 
        for (int i = 0; i < 21; i++) {
            if (i > 8 && i < 12) {
                if ((roomType & 8) == 0) {
                    roomGrid[0][i] = new Wall(this, 0, i);
                } else {
                    roomGrid[0][i] = new RoomExit(this, 0, i, "up");
                }
                
                if ((roomType & 4) == 0) {
                    roomGrid[14][i] = new Wall(this, 14, i);
                } else {
                    roomGrid[14][i] = new RoomExit(this, 14, i, "down");
                }
                continue;
            } 

            roomGrid[0][i] = new Wall(this, 0, i);
            roomGrid[14][i] = new Wall(this, 14, i);
        }
        
        for (int i = 0; i < 15; i++) {
            if (i > 5 && i < 9) {
                if ((roomType & 2) == 0) {
                    roomGrid[i][0] = new Wall(this, i, 0);
                } else {
                    roomGrid[i][0] = new RoomExit(this, i, 0, "left");
                }
                
                if ((roomType & 1) == 0) {
                    roomGrid[i][20] = new Wall(this, i, 24);
                } else {
                    roomGrid[i][20] = new RoomExit(this, i, 21, "right");
                }
                continue;
            } 
            roomGrid[i][0] = new Wall(this, i, 0);
            roomGrid[i][20] = new Wall(this, i, 21);
        }
        
        if ((roomType & 16) != 0) {

        } else if ((roomType & 32) != 0) {
            roomGrid[7][10] = new Portal(this, 7, 10);
        } else if ((roomType & 64) != 0) {
            roomGrid[7][10] = new Chest(this, 7, 10);
        }
        String[][] innerTiles = getRandomRoom(roomType);
        // (r, c) pairs
        ArrayList<int[]> possibleLocations = new ArrayList<int[]>();
        if (innerTiles != null) {
            for (int i = 0; i < innerTiles.length; i++) {
                for (int j = 0; j < innerTiles[i].length; j++) {
                    if (innerTiles[i][j] == null) {
                        
                        possibleLocations.add(new int[] {(i+3)*48+24, (j+7)*48+24});
                        continue;
                    }
                    switch (innerTiles[i][j]) {
                        case "Wall":
                            roomGrid[i+3][j+3] = new Wall(this, i+3, j+3);
                            break;
                        case "Void":
                            roomGrid[i+3][j+3] = new Void(this, i+3, j+3);
                            break;
                        
                    }
                    
                    
                }
            }
        }
        
        
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                if (roomGrid[i][j] instanceof Wall) {
                    
                    int count = 0;
                    if (i != 0 && roomGrid[i-1][j] instanceof Wall) {
                        count += 8;
                    }
                    if (i+1 < 15 && roomGrid[i+1][j] instanceof Wall) {
                        count+=4;
                    }
                    if (j != 0 && roomGrid[i][j-1] instanceof Wall) {
                        count+=2;
                    }
                    if (j+1 < 21 && roomGrid[i][j+1] instanceof Wall) {
                        count+=1;
                    }
                    roomGrid[i][j].setImage("Tiles/wall" + count + ".png");
                    
                    roomGrid[i][j].getImage().scale(48, 48);
                    if (i == 0 && j == 0) {
                        roomGrid[i][j].setImage("Tiles/cornertopleft.png");
                    } else if (i == 14 && j == 0) {
                        roomGrid[i][j].setImage("Tiles/cornerbottomleft.png");
                    } else if (i == 14 && j == 20) {
                        roomGrid[i][j].setImage("Tiles/cornerbottomright.png");
                    } else if (i == 0 && j == 20) {
                        roomGrid[i][j].setImage("Tiles/cornertopright.png");
                    } else if (j == 0) {
                        roomGrid[i][j].setImage("Tiles/edgeleft.png");

                    } else if (j == 20) {
                        roomGrid[i][j].setImage("Tiles/edgeright.png");
                    }
                    roomGrid[i][j].getImage().scale(48, 48);
                } else if (roomGrid[i][j] instanceof Void) {
                    if (i > 0 && !(roomGrid[i-1][j] instanceof Void)) {
                        roomGrid[i][j].setImage("Tiles/voidedge.png");
                        roomGrid[i][j].getImage().scale(48, 48);

                    }
                } 
                
            }
        }
        if (roomType < 16 && possibleLocations.size() > 0) {
            int numberOfEnemies = 5;
            for (int i = 0; i < numberOfEnemies; i++) {
                int[] location = possibleLocations.get(Greenfoot.getRandomNumber(possibleLocations.size()));
                int random = Greenfoot.getRandomNumber(3);
                if (random == 0) enemyList.add(new Ogre(location[1], location[0]));
                else if (random == 1) enemyList.add(new Imp(location[1], location[0]));
                else enemyList.add(new Wizard(location[1], location[0]));
            }
        }
        
    }
    
    public Tile[][] getTileData() {
        return roomGrid;
    }
    
    /**
     * Returns the enemy list
     *
     * @return Returns the enemy list
     */
    public ArrayList<Enemy> getEnemies() {
        return enemyList;
    }
    
    // 0000 - special rooms - start - end - loot/shop - special
    public static String[][] getRandomRoom(int type) {
        if (type >= 16) {
            return null;
        }
        //return innerRooms[10];
        return innerRooms[Greenfoot.getRandomNumber(innerRooms.length)];
    }
    
    
    // for the non-special shapes
    // 1-15 - 16 start - 32 end -64 chest - 128 - special
    // 19*9 (25-5, 15-4)
    // start from (3,3) end at (21, 11) 0-indexed
    private static String[][][] innerRooms = new String[][][] {
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            
        },
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            
        },
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        },
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", "Wall", "Wall", "Wall", null, null, null, null, null, "Wall", "Wall", "Wall", "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        },
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, "Wall", "Wall", "Wall", null, null, null, null, null, null, null, "Wall", "Wall", "Wall", null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, "Wall", null, null, null, null, null, null, null, null, null, null, null, "Wall", null},
            {null, "Wall", "Wall", "Wall", null, null, null, null, null, null, null, "Wall", "Wall", "Wall", null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            
        },
        
        {
            {"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
            {"Wall", null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {"Wall", null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {"Wall", null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"},
            {"Wall", null, null, "Wall", null, null, null, null, null, null, null, null, null, null, "Wall"},
            {"Wall", null, null, "Wall", null, null, null, null, null, null, null, null, null, null, "Wall"},
            {"Wall", null, null, null, null, null, null, null, "Wall", null, null, null, null, null, "Wall"},
            {"Wall", null, null, null, null, null, null, null, "Wall", null, null, null, null, null, "Wall"},
            {"Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall"}
            
        },
        
        {
            {null, null, null, null, null, null, "Wall", "Wall", "Wall", null, null, null, null, null, null},
            {null, null, null, null, null, null, "Wall", "Void", "Wall", null, null, null, null, null, null},
            {null, null, null, null, null, "Wall", "Wall", "Void", "Wall", "Wall", null, null, null, null, null},
            {null, null, null, null, null, "Wall", "Void", "Void", "Void", "Wall", null, null, null, null, null},
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            
        },
        
        {
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
        },
        
        {
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null},
            
        },
        
        {
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null,  null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, "Wall", "Wall", "Wall", "Wall", null, null, null, null, null, null, null},
            
        },
        
        {
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", null, null, null, null, null, "Wall", null, null, null, null},
            {null, null, null, null, "Wall", "Wall", null, null, null, "Wall", "Wall", null, null, null, null},
            {null, null, null, null, null, "Wall", null, null, null, "Wall", null, null, null, null, null},
            {null, null, null, null, null, "Wall", "Wall", null, "Wall",  "Wall", null, null, null, null, null},
            {null, null, null, null, null, null,  "Wall", null,  "Wall", null, null, null, null, null, null},
            {null, null, null, null, null, null,  "Wall",  "Wall",  "Wall", null, null, null, null, null, null},
            {null, null, null, null, null, null, null,  "Wall", null, null, null, null, null, null, null},
            
        },
    };
}
