/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiuserdungeon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author james
 * Some client/server code used from http://edn.embarcadero.com/article/31995
 */
public class Server implements Runnable {

  // Can be abstracted to take user input but static for now
  static int port = 3333;

  static ServerSocket socket;
  static String TimeStamp;
  private Socket connection;
  static StringBuffer process;
  private int ID;

  private static Tower tower;

  private Player myPlayer;
  private Tower myTower;

  public static void main(String [] args){
    // Server code froman
    int count = 0;
    try{
      ServerSocket socket = new ServerSocket(port);
      System.out.println("Server Initialized");

      // Create a new Tower Dungeon
      tower = new Tower();
      tower.generateLevel();

      while (true) {
        Socket connection = socket.accept();
        Runnable runnable = new Server(connection, ++count, tower);
        Thread thread = new Thread(runnable);
        thread.start();

      }
    }
    catch (Exception e) {System.out.println(e.getMessage());}
  }

  Server(Socket s, int i, Tower t) {
    this.connection = s;
    this.ID = i;
    this.myTower = t;
  }

  public Tower getTower(){
    return this.myTower;
  }

  public Player getPlayer(){
    return this.myPlayer;
  }

  public int getID(){
    return ID;
  }

  public void run() {
    System.out.println("Client Connected Successfully.");

    try {
      // Create a new player object upon successful connect
      // Name can obviously be changed but this just gives them all a unique one
      Player player = new Player("James" + ID, this);
      tower.addPlayer(player);
      this.myPlayer = player;

      // Initially tell the player about the room they're in, and to enter a command
      sendData(player.getCurrentRoom().getRoomInfo() + "\nEnter a Command: ");

      // create a thread to handle input to server
      Runnable runnable = new ServerInput();
      Thread thread = new Thread(runnable);
      thread.start();

      while(true){

        // anything here that would require the server to send data to clients
        // that is not a response to client input

      }
    }
    catch (Exception e) {
      System.out.println(e);
    }
    finally {
      try {
        connection.close();
        System.out.println("Client has disconnected.");
     }
      catch (IOException e){}
    }
  }

  // sends data to Client. Feedback about command, chat message, etc
  public void sendData(String data){
    // add eof
    String message = data + (char) 13;

    try{
      BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
      OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
      osw.write(message);
      osw.flush();
    } catch (Exception e){
      // better error handling for serious project
      System.out.println(e);
    }
  }


  // deals with input to the server asynchonously
  // Would need to use mutexes or synchronus methods to prevent race conditions
  class ServerInput implements Runnable {

    public void run(){

      try{
        CommandHandler handler = new CommandHandler(Server.this, Server.this.getPlayer(), Server.this.getTower());

        BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
        InputStreamReader isr = new InputStreamReader(is);

        int character;

        StringBuffer command = new StringBuffer();

        // read input and print out to server
        while(true){
          while((character = isr.read()) != 13) {
            command.append((char)character);
          }

          handler.processCommand(command.toString());
          command.setLength(0);
        }
       }
      catch (Exception e) {
        System.out.println(e);
      }

    }
  }


}
