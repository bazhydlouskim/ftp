package ftp.client.commands;

import ftp.client.FtpMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HelpCommand extends Command {

    public HelpCommand () {
        this.hasArguments = false;
        this.arguments = null;
    }

    public HelpCommand (String arguments)
    {
        this.hasArguments = true;
        this.arguments = arguments;
    }

    public boolean hasArguments;
    public String arguments;


    public void execute() {

        String message;

        if (hasArguments && arguments != null) {
            message = "LIST " + arguments + "/r/n";
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

            for (String line : (Iterable<String>) in.lines()::iterator) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        hasArguments = false;
        arguments = null;

    }


}
