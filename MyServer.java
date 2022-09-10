import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;


public class MyServer extends Thread {
    private HashMap<String, DataOutputStream> dout;
    private DataInputStream din;
    private String user;
    private String to;
    private Socket socket;
    public MyServer(String user, HashMap<String, DataOutputStream> dout, DataInputStream din, Socket socket){
        this.dout = dout;
        this.din = din;
        this.user = user;
        this.socket = socket;
    }

    public void updateOutputs(HashMap<String, DataOutputStream> dout){
        this.dout = dout;
    }

    public void run() {
        try {
            dout.get(user).writeUTF("To? ");
            to = din.readUTF();
            while(!dout.containsKey(to)){
                dout.get(user).writeUTF("To? ");
                to = din.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            String message;
            try{
                message = din.readUTF();
                if (message.equals("new chat")){
                    dout.get(user).writeUTF("To? ");
                    to = din.readUTF();
                    while(!dout.containsKey(to)){
                        dout.get(user).writeUTF("To? ");
                        to = din.readUTF();
                    }
                }else if (message.equals("exit")){
                    din.close();
                    dout.get(user).close();
                    socket.close();
                }else{
                    dout.get(to).writeUTF(user + ": " + message);
                    dout.get(to).flush();
                }
            }catch (Exception e){
                System.out.println(user + " has left the server!");
                break;
            }
        }
    }
}


