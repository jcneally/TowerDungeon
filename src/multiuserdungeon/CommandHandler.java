/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiuserdungeon;

import java.util.ArrayList;

/**
 *
 * @author james
 *
 * CommandHandler's job is to take a valid command from the user and deal with it accordingly
 */
public class CommandHandler {


  // Update this variable to extend valid commands
  private static String[] validCommands = { "say", "tell", "yell", "north", "west", "south", "east", "up", "down"};

  private Server myServer;
  private Player myPlayer; // player invoking the command
  private Tower myTower;

  public CommandHandler(Server s, Player p, Tower t){
    this.myServer = s;
    this.myPlayer = p;
    this.myTower = t;
  }

  // Process the command from the user and do the correct thing!
    public void processCommand(String command){

      if(commandValid(command)){
        // extend further valid commands by adding to static list and creating a method for it
        String[] arguments = command.split(" ");
        String commandName = arguments[0].toLowerCase();

        if(commandName.equals("say")){
          say(command.split(" ", 2)); // basically, split at first space and keep the rest
        }
        else if(commandName.equals("tell")){
          tell(command.split(" ", 3));
        }
        else if(commandName.equals("yell")){
          yell(command.split(" ", 2));
        }
        else if(commandName.equals("north")){
          // move player and send them the new room description
          myPlayer.getCurrentRoom().removePlayer(myPlayer);
          myPlayer.getCurrentRoom().north().addPlayer(myPlayer);
          myPlayer.getServer().sendData(myPlayer.getCurrentRoom().getRoomInfo());
        }
        else if(commandName.equals("south")){
          myPlayer.getCurrentRoom().removePlayer(myPlayer);
          myPlayer.getCurrentRoom().south().addPlayer(myPlayer);
          myPlayer.getServer().sendData(myPlayer.getCurrentRoom().getRoomInfo());
        }
        else if(commandName.equals("east")){
          myPlayer.getCurrentRoom().removePlayer(myPlayer);
          myPlayer.getCurrentRoom().east().addPlayer(myPlayer);
          myPlayer.getServer().sendData(myPlayer.getCurrentRoom().getRoomInfo());
        }
        else if(commandName.equals("west")){
          myPlayer.getCurrentRoom().removePlayer(myPlayer);
          myPlayer.getCurrentRoom().west().addPlayer(myPlayer);
          myPlayer.getServer().sendData(myPlayer.getCurrentRoom().getRoomInfo());
        }


      } else{
        myServer.sendData("Invalid Command.");
      }
    }

    // Command methods are of this format:
    public void say(String[] args){

      // get the list of players in this player's room, and send them a (hopefully) friendly message
      ArrayList<Player> playersInRoom = myPlayer.getCurrentRoom().getPlayers();
      for(Player p: playersInRoom){
        p.getServer().sendData(myPlayer.getName() + " says: " + args[1]);
      }
    }

    public void tell(String[] args){

      // get the list of players in the tower and see if the player is there
      ArrayList<Player> playersInTower = myTower.getPlayers();
      for(Player p: playersInTower){
        if(p.getName().equals(args[1])){
          p.getServer().sendData(myPlayer.getName() + " whispers: " + args[2]);
          break;
        }
      }
      // also post the message to you that was just sent
      myPlayer.getServer().sendData(myPlayer.getName() + " whispers: " + args[2]);
    }

    public void yell(String[] args){

      // get the list of players in this tower, and send them a (hopefully) friendly message
      ArrayList<Player> playersInTower = myTower.getPlayers();
      for(Player p: playersInTower){
        p.getServer().sendData(myPlayer.getName() + " yells: " + args[1]);
      }
    }

    // Checks if command sent by user is valid
    private boolean commandValid(String command){
      // Split the input on spaces
      String[] arguments = command.split(" ");
      String commandName = arguments[0].toLowerCase();

      for(int i = 0; i < validCommands.length; i++){
        if(validCommands[i].equals(commandName)){
          return true;
        }
      }

      return false;

    }

}
