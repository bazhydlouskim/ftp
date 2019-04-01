package ftp.client.commands;

import ftp.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserCommand extends Command {

    public String username;

    public void execute() {

        String message = "USER " + username + "/r/n";
        Socket socket = FtpMain.socket;

        try {
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                out.print(message);
            out.flush();

            String line;
            //while (!in.ready());

            if ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


}
