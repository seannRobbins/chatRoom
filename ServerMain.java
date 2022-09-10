import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class ServerMain {
    public static void main(String[] args) throws IOException {
        HashMap<String, DataInputStream> userInputs = new HashMap<>();
        HashMap<String, DataOutputStream> userOutputs = new HashMap<>();
        ArrayList<String> users = new ArrayList<>();
        ArrayList<Socket> sockets = new ArrayList<>();
        ArrayList<MyServer> servers = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(6666)){
            //Wait for new client
            while(true) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                ////////////////////////////////////////////////////////////////////////////////////////////////
                // Add the new client
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                String from = dis.readUTF();
                userInputs.put(from, dis);
                userOutputs.put(from, dout);
                users.add(from);
                System.out.println(from + " has joined the server!");
                MyServer server = new MyServer(from, userOutputs, userInputs.get(from), socket);
                servers.add(server);
                for (MyServer s : servers){
                    s.updateOutputs(userOutputs);
                }
                server.start();
                //////////////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
