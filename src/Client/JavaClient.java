package Client;// A Java program for a Client
//import com.sun.security.ntlm.Client;

import Util.ColorClass;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class JavaClient extends Thread
{
    // initialize socket and input output streams
    private Socket socket		 = null;
    private DataInputStream input = null;
    private DataOutputStream out	 = null;
    private String name = "";
    private String inputLine = "";
    private String socketLine = "";
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private BufferedReader socketBR = null;
    private BufferedWriter socketWR = null;
    private JTextPane msgArea = null;
    JTextPane roomParticipants = null;
    private ArrayList<String> USERS = new ArrayList<String>();





    public String readInputLine() throws IOException {
        // Reading data using readLine
        System.out.print("> ");
        inputLine = reader.readLine();
        return inputLine;
    }

    public String readSocketLine() throws IOException{
        socketLine = socketBR.readLine();
        return socketLine;

    }

    public void printLine(String output){
        //System.out.println("Print line has been called");
        System.out.println(output);

    }

    public void printLine(String output, String color){
        System.out.print(color);
        printLine(output);
        System.out.print(ColorClass.ANSI_RESET);
    }

    public String readName()throws IOException{
        System.out.println("Please Enter Your Name");
        name = reader.readLine();
        return name;

    }


    public JavaClient(String address, int port)
    {
        // establish a connection
        try
        {
            readName();
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            //input = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            //socketBR = new BufferedReader(new InputStreamReader(input));
            socketWR = new BufferedWriter(new OutputStreamWriter(out));

            this.start();
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }




        // keep reading until "Over" is input
        while (!inputLine.equals("Over"))
        {
            try
            {

                this.readInputLine();
                socketWR.write(name + ": " + inputLine);

                socketWR.flush();
                currentThread().sleep(100);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
            catch(InterruptedException i)
            {
                System.out.println(i);
            }
        }

        // close the connection
        try
        {
            input.close();
            this.out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }





    public JavaClient(String address, int port, boolean gui, JTextPane msgArea, String userName, JTextPane roomParticipants) {
        // establish a connection
        this.msgArea = msgArea;
        this.roomParticipants = roomParticipants;
        try {
            this.name = userName;
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            //input = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            //socketBR = new BufferedReader(new InputStreamReader(input));
            socketWR = new BufferedWriter(new OutputStreamWriter(out));

            this.start();
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }


    }



    public void sendMessage(String message){
        try{
            socketWR.write(name + ": " + message);
            socketWR.flush();
        }
        catch(IOException i){
            System.out.print(i);
        }

    }

    protected String getUsername(String message){
        int index = message.indexOf(":");
        if(index == -1){
            return "";
        }
        else{
            return message.substring(0,index);
        }
    }


    public void run(){
        System.out.println("Client Thread Started");
        try {
            DataInputStream sin1 = new DataInputStream(socket.getInputStream());

            String dataString = "";
            byte[] mb1 = new byte[1000];
            while(true) {

                try {

                    dataString = "";
                    int nob = sin1.read(mb1);
                    dataString += new String(mb1, 0, nob);
                    if(dataString == null){ continue; }
                    String sender = getUsername(dataString);
                    if(!USERS.contains(sender)){
                        USERS.add(sender);
                        if(roomParticipants != null){
                            roomParticipants.setText(roomParticipants.getText() + "\n" + sender);
                        }

                    }

                    if(dataString.startsWith(name)){
                        msgArea.setText(msgArea.getText() + "\n" + dataString);
                        //printLine(dataString, ColorClass.ANSI_GREEN);
                        /*if(dataString.substring(0, dataString.indexOf(':')) == name){
                            SimpleAttributeSet attribs = new SimpleAttributeSet();
                            StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
                            msgArea.setParagraphAttributes(attribs, true);
                        }*/
                        //Should set text to right side ^


                    }
                    else{
                        msgArea.setText(msgArea.getText() + "\n" + dataString);
                        //printLine(dataString, ColorClass.ANSI_BLUE);
                    }

                } catch (IOException i) {
                    System.out.println("Number One");
                    System.out.println(i);
                }
            }
        }
        catch(IOException e){
            System.out.println("Number Two");
            System.out.println(e);
        }
    }

    public static void main(String args[])
    {
        String ipAddress = null;

        if(args.length == 0){
           // if(args[0] == null){
                ipAddress = "192.168.1.42";
            //}

        }
        JavaClient client = new JavaClient(ipAddress, 50000);
    }
}






