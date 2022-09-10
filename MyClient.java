
import java.io.*;
import java.lang.reflect.Executable;
import java.net.*;
import java.util.Scanner;

/*

    Class MyClient
    Allows a user to enter a chat room located on local host, port 6666

    Args: 
        Arg[0] - User Name for the user to be used in the chat room
    Returns:
        Exit Code 0 - program terminated

    To leave the chat room, the user can type "exit"

*/
public class MyClient {
    public static boolean close = false;
    public static void main(String[] args) throws IOException {
        try {
            if (args.length < 1){
                System.out.println("Must provide a username!");
            }
            String name = args[0];
            Socket s = new Socket("localhost", 6666);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            DataInputStream din = new DataInputStream(s.getInputStream());
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            dout.writeUTF(name);

            Thread output = new Thread(new Runnable() {
                private String message;
                public void run() {
                    while (true){
                        try {
                            if ((message = keyboard.readLine()) != null) {
                                if (message.equals("exit")) {
                                    din.close();
                                    dout.close();
                                    s.close();
                                }
                                dout.writeUTF(message);
                                dout.flush();
                            }
                        } catch (Exception e) {
                            System.exit(0);
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread input = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String message = din.readUTF();
                            System.out.println(message);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            input.start();
            output.start();

        } catch (Exception e){
            System.out.println(e);
        }
    }
}

