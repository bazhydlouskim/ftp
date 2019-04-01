package ftp.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FtpServerMain {


    private static String logDirectory;
    private static int numLogFiles;
    private static String usernameFile;
    public static boolean portSupported;
    public static boolean pasvSupported;
    public static ArrayList<UserCredentials> users;

    private static int portNumber;
    public static Logger logger;
    //private static ServerSocket socket;


    public static void main(String[] args) {
        // Get and parse the arguments


        getArguments(args);


        // Default values
        logDirectory = Paths.get("").toAbsolutePath().toString();    // Defaults to the current directory
        numLogFiles = 5;
        usernameFile = "ftp.users";
        String logFileName = "ftp.log";
        portSupported = false;
        pasvSupported = true;

        // Read the configuration file
        readConfig();

        // Get authorized users
        users = new ArrayList<>();
        getUsers();

        if (!(portSupported || pasvSupported)) {
            System.err.println("FATAL ERROR: PORT mode and PASV mode are both turned off!");
            System.exit(-2);
        }


        logger = new Logger(logFileName, logDirectory, numLogFiles);


        try (ServerSocket socket = new ServerSocket(portNumber)) {


            while (true) {
                FtpServer server = new FtpServer(socket.accept(), logger);

            }


        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }


    private static void getArguments(String[] args) {

        if (args.length != 1) {
            System.err.println("Invalid usage! The correct usage is ftp-server <port_number>");
            System.exit(1);
        } else {

            try {
                portNumber = Integer.parseInt(args[0]);
                if (portNumber < 1 || portNumber > 65535)
                    throw new Exception();
            } catch (Exception e) {
                System.out.flush();
                System.err.println("Invalid port number!");
                System.exit(2);
            }
        }
    }


    private static void readConfig() {

        try (BufferedReader in = new BufferedReader(new FileReader("ftpserverd.conf"))) {

            String line;
            while ((line = in.readLine()) != null) {


                String[] tokens = line.trim().split("#");
                String parameter = tokens[0].trim();
                if (!parameter.isEmpty()) {

                    String[] pair = parameter.split("=");
                    if (pair.length == 2) {

                        String key = pair[0].trim().toLowerCase();
                        String value = pair[1].trim();


                        if (!key.isEmpty() && !value.isEmpty()) {


                            switch (pair[0].trim().toLowerCase()) {
                                case "logdirectory":

                                    FtpServerMain.logDirectory = value;
                                    break;
                                case "numlogfiles":
                                    try {
                                        int num = Integer.parseInt(value);
                                        if (num < 1) throw new Exception();
                                        FtpServerMain.numLogFiles = num;
                                    } catch (Exception e) {
                                        System.err.println("Invalid number of log files:\n" + line);
                                    }

                                    break;

                                case "usernamefile":

                                    FtpServerMain.usernameFile = value;

                                    break;

                                case "port_mode":

                                    if (value.toLowerCase().equals("yes"))
                                        FtpServerMain.portSupported = true;
                                    else if (value.toLowerCase().equals("no"))
                                        FtpServerMain.portSupported = false;
                                    else {
                                        System.err.println("Invalid PORT mode specification:\n" + line);
                                    }

                                    break;

                                case "pasv_mode":

                                    if (value.toLowerCase().equals("yes"))
                                        FtpServerMain.pasvSupported = true;
                                    else if (value.toLowerCase().equals("no"))
                                        FtpServerMain.pasvSupported = false;
                                    else {
                                        System.err.println("Invalid PASV mode specification:\n" + line);
                                    }

                                    break;

                                default:
                                    System.err.println("Invalid configuration line:\n" + line);
                                    break;
                            }
                        }
                    } else {
                        System.err.println("Invalid configuration line:\n" + line);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FATAL ERROR: Configuration file cannot be found!");
            System.exit(-1);
        }
    }


    private static void getUsers() {

        try (BufferedReader in = new BufferedReader(new FileReader(usernameFile))) {

            String line;
            while ((line = in.readLine()) != null) {

                // Spaces are not allowed in usernames and passwords
                String[] tokens = line.trim().split(":", 2);
                if (tokens.length != 2) {
                    System.err.println("Invalid username and password specification! One user will not be able to log in!");
                } else {

                    if (!tokens[0].isEmpty() && !tokens[1].isEmpty()) {
                        UserCredentials credentials = new UserCredentials();
                        credentials.username = tokens[0];
                        credentials.password = tokens[1];
                        users.add(credentials);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FATAL ERROR: Username file not found!");
            logger.writeLog("FATAL ERROR: Username file not found!");
            System.exit(-4);
        }

    }


}
