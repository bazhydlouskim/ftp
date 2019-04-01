package ftp.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class Logger {

    private PrintWriter out;
    private ReentrantLock lock;

    Logger(String fileName, String logDirectory, int numLogFiles) {


        try {

            File dir = new File(logDirectory);

            String newFileName = getFileName(dir, fileName, numLogFiles);


            File logFile = new File(dir, newFileName);

            FileWriter fw = new FileWriter(logFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);

            lock = new ReentrantLock(true);


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FATAL ERROR: Cannot create a log file!");
            System.exit(-3);
        }

    }


    private String getFileName(File dir, String filename, int maxLogFiles) {

        try {

            File[] files = dir.listFiles();


            for (int i = 0; i < maxLogFiles; i++) {

                boolean skip = false;

                for (int j = 0; j < files.length && !skip; j++) {
                    if (files[j].getName().equals(filename + "." + i)) {
                        skip = true;
                    }
                }
                if (!skip) return String.format("%s.%d", filename, i);

            }

            for (int i = 0; i < maxLogFiles; i++) {

                boolean skip = false;

                for (int j = 0; j < files.length && !skip; j++) {
                    String[] tokens = files[j].getName().split(filename + ".");
                    if  ((tokens.length == 2) && (Integer.parseInt(tokens[1]) == i)) {
                        if (i == 0) files[j].delete();
                        else {
                            files[j].renameTo(new File(String.format("%s.%d", filename, i-1)));
                        }
                    }
                }
                //if (!skip) return String.format("%s.%d", filename, i);
            }

            return String.format("%s.%d", filename, maxLogFiles-1);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("FATAL ERROR: Failed to find log files!");
            System.exit(-5);
        }


        return filename;
    }


    public void writeLog(String log) {

        try {
            lock.lock();
            out.println(LocalDateTime.now().toString() + "    " + log);
            out.flush();
        } catch (Exception e) {
            System.err.println("Error writing to the log file: ");
            System.err.println(log);
            e.printStackTrace();

        } finally {
            lock.unlock();
        }

    }

    public void writeLog(String log, Socket socket) {

        try {
            lock.lock();
            out.println(LocalDateTime.now().toString() + "    " + log + "    " + socket.getInetAddress().toString() + "/" + socket.getPort());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog(log + "        Error getting remote socket information!");
        } finally {
            lock.unlock();
        }

    }
}
