package ftp.server;

import ftp.server.messages.Message;
import ftp.server.messages.QUIT;
import ftp.server.states.DfaState;
import ftp.server.states.NotAuthenticated;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.nio.file.Paths;

public class FtpServer extends Thread {

    public Socket socket;
    public Socket dataSocket;
    private BufferedReader in;
    private PrintWriter out;
    public DfaState state;
    private Logger logger;

    FtpServer(Socket socket, Logger logger) {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            this.logger = logger;
            this.state = new NotAuthenticated(Paths.get(""));


            writeOutput("220 Welcome!");

            start();
        } catch (IOException e) {


            e.printStackTrace();

            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    public void run() {

        boolean quit = false;

        while (!quit) {
            // Receive a message
            try {

                String line;
                StringBuilder input = new StringBuilder();
                if ((line = in.readLine()) != null) {
                    input.append(line);
                }

                logger.writeLog(input.toString(), socket);

                // Process Message
                String[] tokens = input.toString().split(" ");

                // Create (parse) a message
                Class<?> class1 = Class.forName("ftp.server.messages." + tokens[0].toUpperCase());
                Constructor constructor = class1.getConstructor();
                Message message = (Message) constructor.newInstance();

                state.processTransition(message, this);


                message.server = this;
                message.getArguments(tokens);
                String msg = message.process(state);

                writeOutput(msg);


                if (message.getClass().equals(QUIT.class))
                    quit = true;


            } catch (Exception e) {

                writeOutput(e.getMessage());

            }

        }





    }

    private void writeOutput(String msg) {

        String message = "";
        message += msg;
        byte b = 0x0d;
        message += (char)b;
        b = 0x0a;
        message += (char)b;

        out.print(message);
        out.flush();

    }

    public void setState(DfaState state) {
        this.state = state;
    }

    public Socket getDataSocket() {


        return null;
    }

}
