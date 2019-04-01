package ftp.client;

import ftp.client.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class FtpMain {

    private static String serverHost;
    private static String logFileName;
    private static int portNumber;
    public static Socket socket;


    public static void main(String[] args) {

        // Get and parse the arguments


        if ((args.length < 2) || (args.length > 3)) {
            System.err.println("Invalid usage! The correct usage is ftp <ftp_server_host> <log_filename> [<port_number>]");
            System.exit(1);
        } else {

            serverHost = args[0];
            logFileName = args[1];

            if (args.length == 3) {
                try {
                    portNumber = Integer.parseInt(args[2]);
                    if (portNumber < 1 || portNumber > 65535)
                        throw new Exception();
                } catch (Exception e) {
                    System.out.flush();
                    System.err.println("Invalid port number!");
                    System.exit(2);
                }

            } else {
                portNumber = 21;
            }


            if (!connect(serverHost, portNumber)) {
                System.out.flush();
                System.err.println("Connection to server failed! \nPlease check the arguments and try again!");
                System.exit(2);
            }


            if (!login()) {
                System.out.flush();
                System.err.println("Login failed!");
                System.exit(3);
            }

            boolean finished = false;

            while (!finished) {
                try {
                    String input = (new Scanner(System.in)).nextLine();
                    Command comm = parseInput(input);
                    if (comm != null) {
                        finished = comm.getClass().equals(ExitCommand.class);
                        comm.execute();
                    }

                }
                //catch (EOFException eof) {}
                catch (Exception e) {
                    System.out.flush();
                    System.err.println("An exception occurred:\n" + e.getMessage());
                }
                System.out.flush();
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private static Command parseInput(String input) {

        String[] tokens = input.split(" ");

        if (tokens.length < 1)
            return null;

        Command newCommand = null;

        try {
            switch (tokens[0].toLowerCase()) {

                case "dir":
                case "ls":
                    if (tokens.length == 2) {
                        newCommand = new ListCommand(tokens[1]);
                    } else if (tokens.length == 1) {
                        newCommand = new ListCommand();
                    } else {
                        throw new Exception();
                    }

                    break;

                case "cd":
                    if (tokens.length == 2) {
                        newCommand = new CwdCommand(tokens[1]);
                    } else throw new Exception();


                    break;

                case "cdup":
                case "cd..":
                    if (tokens.length == 1) {
                        newCommand = new CdupCommand();
                    } else {
                        throw new Exception();
                    }

                    break;

                case "pwd":
                    if (tokens.length == 1) {
                        newCommand = new PwdCommand();
                    } else {
                        throw new Exception();
                    }

                    break;

                case "help":
                    if (tokens.length == 2) {
                        newCommand = new HelpCommand(tokens[1]);
                    } else if (tokens.length == 1) {
                        newCommand = new HelpCommand();
                    } else {
                        throw new Exception();
                    }
                    break;

                case "get":
                    if (tokens.length == 2) {
                        newCommand = new RetrCommand(tokens[1]);
                    } else {
                        throw new Exception();
                    }
                    break;

                case "passive":

                    break;

                case "exit":
                case "quit":
                case "bye":
                    newCommand = new ExitCommand();
                    break;

                default:
                    System.out.flush();
                    System.err.println("Unrecognized command!");

                    break;


            }
        } catch (Exception e) {
            System.out.flush();
            System.err.println("Invalid usage!");
        }

        return newCommand;
    }


    public static void runAll() {


    }


    public static boolean connect(String serverHost, int portNumber) {

        // Socket Magic Here!
        try {
            socket = new Socket(serverHost, portNumber);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            while (!in.ready()) ;
            String line;
            if ((line = in.readLine()) != null)
                System.out.println(line);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    static boolean login() {
        System.out.print("user: ");
        boolean done = false;
        int i = 0;
        while (!done && (i < 3)) {
            Scanner scan = new Scanner(System.in);
            String user = scan.nextLine();
            if (user.split(" ").length != 1) {
                System.err.println("Invalid username!");
            } else {
                UserCommand comm = new UserCommand();
                comm.username = user;
                comm.execute();
                System.out.print("Password: ");
                String pass = scan.nextLine();
                PassCommand passCommand = new PassCommand();
                passCommand.password = pass;
                passCommand.execute();
                done = true;

            }
            i++;

        }

        return true;
    }


}
