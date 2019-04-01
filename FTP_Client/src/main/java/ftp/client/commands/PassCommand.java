package ftp.client.commands;

import ftp.client.FtpMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PassCommand extends Command {


    public String password;

    public void execute() {

        String message = "PASS " + password + "/r/n";
        Socket socket = FtpMain.socket;

        try {
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            out.print(message);
            out.flush();

            for (String line: (Iterable<String>) in.lines()::iterator) {
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