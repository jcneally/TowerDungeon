
package multiuserdungeon;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author james neally
 */
public class Tower {

  // the largest possible width/height for a level
  private int dimensions = 5;
  private Room[][] rooms;
  private ArrayList<Room[][]> levels;
  private int numLevels;

  // all players currently in the tower
  private ArrayList<Player> players;

  private Room startRoom; // all players start in this room

  public Tower(){

    // This number is incrememented every time the player goes up a staircase
    // Effectively infinite
    this.numLevels = 0;
    players = new ArrayList<Player>();
    levels = new ArrayList<Room[][]>();
  }


  // For large-scale, should be synchronized so multiple different levels aren't built
  public void generateLevel(){

    // two dimensional array tells whether a room has already been built
    rooms = new Room[dimensions][dimensions];

    // randomly chooses a square to be the starting square for that level.
    // if not level 1, should also have stairs down
    Random rand = new Random();
    startRoom = new Room();
    startRoom.x = rand.nextInt(dimensions);
    startRoom.y = rand.nextInt(dimensions);
    rooms[startRoom.x][startRoom.y] = startRoom;

    // recursive function to set up the rooms
    setupRooms(startRoom, rooms);

    this.numLevels++;

    levels.add(rooms);

  }

  // add player to tower in the start room
  public void addPlayer(Player p){
    players.add(p);
    startRoom.addPlayer(p);
    p.setCurrentRoom(startRoom);
  }

  public ArrayList<Player> getPlayers(){
    return players;
  }

  private void setupRooms(Room currentRoom, Room[][] rooms ){

    setupNorth(currentRoom, rooms);
    setupEast(currentRoom, rooms);
    setupSouth(currentRoom, rooms);
    setupWest(currentRoom, rooms);

    currentRoom.done = true;


    // Recurse on all adjacent rooms that are not done
    if(currentRoom.north() != null && currentRoom.north().done == false){
      setupRooms(currentRoom.north(), rooms);
    }

    if(currentRoom.east() != null && currentRoom.east().done == false){
      setupRooms(currentRoom.east(), rooms);
    }

    if(currentRoom.south() != null && currentRoom.south().done == false){
      setupRooms(currentRoom.south(), rooms);
    }

    if(currentRoom.west() != null && currentRoom.west().done == false){
      setupRooms(currentRoom.west(), rooms);
    }

  }

  // Functions to check the boundaries and set up rooms
  // Could be abstracted into one function with more effort
  private void setupNorth(Room currentRoom, Room[][] rooms){
    Random rand = new Random();

    // make sure it's not out of bounds
    if(currentRoom.y - 1 >= 0){

      // randomly decide whether there will be an entrance (50%)
      if(rand.nextInt(2) == 0){

        // create a room in the square above if it doesn't exist
        if(rooms[currentRoom.x][currentRoom.y - 1] == null){
          Room r = new Room();
          r.x = currentRoom.x;
          r.y = currentRoom.y - 1;
          currentRoom.setAdjacentRoom("north", r);
          rooms[r.x][r.y] = r;
        }
        // otherwise just set the entrance
        else{
          currentRoom.setAdjacentRoom("north", rooms[currentRoom.x][currentRoom.y - 1]);
        }

        // set the other room's entrance to this
        currentRoom.north().setAdjacentRoom("south", currentRoom);

      }
    }

  }

  private void setupEast(Room currentRoom, Room[][] rooms){
    Random rand = new Random();

    // make sure it's not out of bounds
    if(currentRoom.x + 1 < dimensions){

      // randomly decide whether there will be an entrance (50%)
      if(rand.nextInt(2) == 0){

        // create a room in the square above if it doesn't exist
        if(rooms[currentRoom.x + 1][currentRoom.y] == null){
          Room r = new Room();
          r.x = currentRoom.x + 1;
          r.y = currentRoom.y;
          currentRoom.setAdjacentRoom("east", r);
          rooms[r.x][r.y] = r;
        }
        // otherwise just set the entrance
        else{
          currentRoom.setAdjacentRoom("east", rooms[currentRoom.x + 1][currentRoom.y]);
        }

        // set the other room's entrance to this
        currentRoom.east().setAdjacentRoom("west", currentRoom);

      }
    }
  }

  private void setupSouth(Room currentRoom, Room[][] rooms){
    Random rand = new Random();

    // make sure it's not out of bounds
    if(currentRoom.y + 1 < dimensions){

      // randomly decide whether there will be an entrance (50%)
      if(rand.nextInt(2) == 0){

        // create a room in the square above if it doesn't exist
        if(rooms[currentRoom.x][currentRoom.y + 1] == null){
          Room r = new Room();
          r.x = currentRoom.x;
          r.y = currentRoom.y + 1;
          currentRoom.setAdjacentRoom("south", r);
          rooms[r.x][r.y] = r;
        }
        // otherwise just set the entrance
        else{
          currentRoom.setAdjacentRoom("south", rooms[currentRoom.x][currentRoom.y + 1]);
        }

        // set the other room's entrance to this
        currentRoom.south().setAdjacentRoom("north", currentRoom);

      }
    }
  }

  private void setupWest(Room currentRoom, Room[][] rooms){
    Random rand = new Random();

    // make sure it's not out of bounds
    if(currentRoom.x - 1 >= 0){

      // randomly decide whether there will be an entrance (50%)
      if(rand.nextInt(2) == 0){

        // create a room in the square above if it doesn't exist
        if(rooms[currentRoom.x - 1][currentRoom.y] == null){
          Room r = new Room();
          r.x = currentRoom.x - 1;
          r.y = currentRoom.y;
          currentRoom.setAdjacentRoom("west", r);
          rooms[r.x][r.y] = r;
        }
        // otherwise just set the entrance
        else{
          currentRoom.setAdjacentRoom("west", rooms[currentRoom.x - 1][currentRoom.y]);
        }

        // set the other room's entrance to this
        currentRoom.west().setAdjacentRoom("east", currentRoom);

      }
    }
  }
}
