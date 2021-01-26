package Server;// A Java program for a Server
//import com.sun.security.ntlm.Server;
import Util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JavaSocketServer {
    //initialize socket and input stream
    final int NUMBER_OF_CONNECTIONS = 5;
    int socketIndex = 0;
    private Socket[] socketArray = new Socket[NUMBER_OF_CONNECTIONS];
    private ServerSocket server = null;
    private DataInputStream in = null;




    // constructor with port
    public JavaSocketServer(int port) {

            // starts server and waits for a connection
            try {
                server = new ServerSocket(port);
                System.out.println("Server started");

                System.out.println("Waiting for a client ...");


                for (int i = 0; i < socketArray.length; i++) {
                   socketArray[i] = null;

                }

                for (int i = 0; i < socketArray.length; i++) {
                    socketArray[i] = server.accept();
                    ClientChatThread chatThread = new ClientChatThread(socketArray, socketArray[i], i);
                    chatThread.start();
                    socketIndex++;

                }


            } catch (IOException i) {
                System.out.println(i);
            }
            catch(StringIndexOutOfBoundsException s){
                System.out.println("Inside SocketServer");
                System.out.println(s);
            }

            finally
            {
                try {

                    for (int i = 0; i < socketIndex; i++) {
                        socketArray[i].close();
                    }
                } catch (IOException i) {
                    System.out.println(i);
                }
            }
        }


        public static void main (String args[])
        {
            JavaSocketServer server = new JavaSocketServer(50000);
        }


}



class ClientChatThread extends Thread{

    private Socket socket;
    private int index;
    private DataOutputStream out;
    private DataInputStream in;
    private String helloMessage = "Hello from server";
    private BufferedReader socketBR = null;
    private BufferedWriter socketWR = null;
    private Socket[] socketArray = null;
    private ColorClass colorCodes = new ColorClass();
    public ArrayList<String> users = new ArrayList<String>();




    public ClientChatThread(Socket[] socketArray,Socket socket, int index)throws IOException{
        this.socket = socket;
        this.index = index;
        this.socketArray = socketArray;


    }
    protected void finalize(){
        try{
            out.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    protected void broadcast(byte[] byteArray, int nob){

        try{
            for(int i = 0; i < socketArray.length; i++){

                if(socketArray[i] == null){break;}
                DataOutputStream out1 = new DataOutputStream(socketArray[i].getOutputStream());
                out1.write(byteArray,0,nob);
                out1.flush();
                //out1.close();
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        catch(StringIndexOutOfBoundsException s){
            System.out.println("Inside Broadcast");
            System.out.println(s);
        }
    }
    protected String getUserName(String message){
        int index = message.indexOf(":");
        if(index == -1){
            return "";
        }
        else{
            return message.substring(0,index);
        }
    }

    public void run(){
        try
        {
            //BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

            System.out.println("Client #" + String.valueOf(index) + " connected");
           // out.writeUTF(helloMessage);
           // out.flush();
            DataOutputStream out1 = new DataOutputStream(socket.getOutputStream());
            DataInputStream sin1 = new DataInputStream(socket.getInputStream());
            String dataString = "";

            byte[] mb1 = new byte[1000];
            while(true){


                dataString = "";
                int nob = 0;
                nob = sin1.read(mb1);

                dataString += new String(mb1, 0, nob);
                String sender = getUserName(dataString);
               /* if(!users.contains(sender)){
                    users.add(sender);
                    System.out.println(sender);
                }*/

                System.out.print(colorCodes.ANSI_GREEN);
                System.out.println(dataString);
                System.out.print(colorCodes.ANSI_RESET);
                //out1.write(mb1,0,nob);
               // out1.flush();
                //out1.close();
                broadcast(mb1, nob);


            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        catch(StringIndexOutOfBoundsException s){
            System.out.println("Inside Run");
            System.out.println(s);
        }



    }
}