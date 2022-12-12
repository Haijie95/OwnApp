import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main{

    public static void main(String[] args){
        
        String usage="""
            //Usage Server
            //<program><server><port><username>
            //Usage Client
            //<program><client><localhost><port>
                """;

        if((args.length)!=3){ //check for correct input
            System.out.println("Incorrect Inputs. Please check the following usage.");
            System.out.println(usage);
            return;
        }

        String type=args[0]; //this will determine the server side or client side
        if(type.equalsIgnoreCase("server")){
            int port=Integer.parseInt(args[1]);
            String filename=args[2];
            StartServer(port,filename);
        }else if (type.equalsIgnoreCase("client")){
            String hostname=args[1];
            int port =Integer.parseInt(args[2]);
            StartClient(hostname,port);
        }else{
            System.out.println("Incorrect Argument!!!");
        }

    }

    public static void StartServer(int port, String username){ //server side application
        ServerSocket newServer;
        
        try{
            newServer = new ServerSocket(port);
            Socket newSocket=newServer.accept();
            //In
            DataInputStream dis=new DataInputStream(new BufferedInputStream(newSocket.getInputStream()));
            //Out
            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(newSocket.getOutputStream()));
            
            //dos.writeUTF("Welcome to My Mart"); //welcome user after they connected
            //dos.flush();
            System.out.println("Server Initialised!!!");

            while(true){
                
                String fromClient=dis.readUTF();
                if(fromClient.equalsIgnoreCase("exit")){
                    //exit
                    System.out.println("Server Closing");
                    break;
                }
                
                String[] command=fromClient.strip().split(" ");
                
                //there should be add list delete exit
                if(command[0].equalsIgnoreCase("add")){
                    if (command.length<=1){
                        dos.writeUTF("Fruit missing!!!");
                        dos.flush();    
                        break;
                    }else{
                        // System.out.println(command[1]);
                        // dos.writeUTF(command[1]);
                        // dos.flush();
                        Cart cart=new Cart(command[1],username);
                        String summary=cart.addAction();
                        dos.writeUTF(summary);
                        dos.flush();
                    }
                }else if(command[0].equalsIgnoreCase("list")){
                    Cart cart=new Cart(username);
                    String summary=cart.listAction();
                    dos.writeUTF(summary);
                    dos.flush();
                }else if(command[0].equalsIgnoreCase("delete")){
                    if (command.length<=1){ //
                        dos.writeUTF("Fruit missing!!!");
                        dos.flush();    
                        break;
                    }else{
                        Cart cart=new Cart(command[1],username);
                        String summary = cart.deleteAction();
                        dos.writeUTF(summary);
                        dos.flush();
                    }
                }else{
                    //invalid command
                    dos.writeUTF("From Server: Invalid Command");
                    dos.flush();
                }
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }

        
    }
    public static void StartClient(String host,int port) {
            String menu="""
                Menu
                1.Add
                2.List
                3.Delete
                4.Exit
                """;
            try{
                Socket socket=new Socket(host,port);

                //In
                DataInputStream dis=new DataInputStream((new BufferedInputStream(socket.getInputStream())));
                //Out
                DataOutputStream dos=new DataOutputStream((new BufferedOutputStream(socket.getOutputStream())));

                
                Scanner sc = new Scanner(System.in);
                boolean stop =false;

                System.out.println("Welcome to the mini mart!");
                while(!stop){
                    System.out.print(menu);
                    String line=sc.nextLine();
                    if(line.equalsIgnoreCase("exit")){
                        dos.writeUTF("exit");
                        dos.flush();
                        stop=true;
                        break;
                    }

                    dos.writeUTF(line);
                    dos.flush();

                    String fromServer=dis.readUTF();
                    System.out.println((fromServer));

                }
            }
            catch(UnknownHostException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
    }
}
