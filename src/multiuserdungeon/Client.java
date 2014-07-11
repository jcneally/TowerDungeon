/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiuserdungeon;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author james
 */
public class Client {

  // Can be abstracted to take user input but static for now
  static int port = 3333;

  static String host = "localhost";
  static Socket connection;

  public static void main(String [] args){


    // client code from
    try {
      /** Obtain an address object of the server */
      InetAddress address = InetAddress.getByName(host);
      /** Establish a socket connetion */
      connection = new Socket(address, port);
      /** Instantiate a BufferedOutputStream object */

      System.out.println("Successfully Connected To Server.");

      BufferedOutputStream bos = new BufferedOutputStream(connection.
          getOutputStream());


      /** Instantiate an OutputStreamWriter object with the optional character
       * encoding.
       */
      OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");


      // create a thread to handle input to server
      Runnable runnable = new ClientInput();
      Thread thread = new Thread(runnable);
      thread.start();

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


      while(true) {

        // Get command and send to server
        String command = "";

        try {
            command = br.readLine();
        } catch (Exception e) {
            System.out.println("error reading user input");
            System.exit(1);
        }

        command += (char) 13;

        /** Write across the socket connection and flush the buffer */
        osw.write(command);
        osw.flush();


      }

      /** Close the socket connection. */

    } catch(Exception e){
      // Should capture more specific errors generally

      System.out.println(e.getMessage());

    } finally {
      /** Close the socket connection. */
      try{
        connection.close();
      } catch(Exception e){
        // error handling
      }
    }
  }

  // deals with input to the client asynchonously
  // Would need to use mutexes or synchronus methods to prevent race conditions
  static class ClientInput implements Runnable {

    public void run(){

      try{
        /** Instantiate a BufferedInputStream object for reading
        /** Instantiate a BufferedInputStream object for reading
        * incoming socket streams.
        */

        BufferedInputStream bis = new BufferedInputStream(connection.
            getInputStream());
        /**Instantiate an InputStreamReader with the optional
        * character encoding.
        */

        StringBuffer instr = new StringBuffer();

        InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");

        /**Read the socket's InputStream and append to a StringBuffer */
        int c;

        while(true){
          while ( (c = isr.read()) != 13  && c != -1)
            instr.append( (char) c);

          if(c == -1){
            break;
          }
          System.out.println(instr);
          instr.setLength(0);
        }

       }
      catch (Exception e) {
        System.out.println(e);
      }

    }
  }
}
