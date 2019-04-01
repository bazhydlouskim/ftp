package ftp.client.commands;

import ftp.client.FtpMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ListCommand extends Command {

    public boolean hasPath;
    public String path;

    public ListCommand()
    {
        hasPath = false;
        path = null;
    }

    public ListCommand(String path)
    {
        hasPath = true;
        this.path = path;
    }


    public void execute() {

        String message;

        if (hasPath && path != null) {
            message = "LIST " + path + "/r/n";
        }
        else {
            message = "LIST/r/n";
        }
        Socket socket = FtpMain.socket;

        try {
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            out.print(message);
            out.flush();

            //for (String line : (Iterable<String>) in.lines()::iterator) {
            //    System.out.println(line);
            //}
            String line;
            //while (!in.ready());

            if ((line = in.readLine()) != null) {
                System.out.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        hasPath = false;
        path = null;


    }

}
