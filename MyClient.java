
import java.io.*;
import java.lang.reflect.Executable;
import java.net.*;
import java.util.Scanner;

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

