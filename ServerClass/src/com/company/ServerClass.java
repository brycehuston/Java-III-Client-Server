package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClass {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4321)) { // server is on port 4321
            System.out.println("Server is listening on port #" + serverSocket.getLocalPort());
            try (Socket clientSocket = serverSocket.accept()) { // wait, listen and accept connection
                String clientHostName = clientSocket.getInetAddress().getHostName(); // client name
                int clientPortNumber = clientSocket.getLocalPort(); // port used
                System.out.println("Connected from " + clientHostName + " on #" + clientPortNumber);

                BufferedReader inStream; // input stream from client
                inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                DataOutputStream outStream; // output stream to client
                outStream = new DataOutputStream(clientSocket.getOutputStream());

                while (true) { // chatting loop
                    String inLine = inStream.readLine(); // read a line from client
                    System.out.println("From Client: " + inLine);

                    // if the client sends "quit", then stop
                    if (inLine.equalsIgnoreCase("quit")) {
                        System.out.println("Client Disconnected!!!");
                        break; // disconnect
                    }

                    int inLined;

                    // Check if you can actually make the input squared
                    try {
                        inLined = Integer.parseInt(inLine);
                    }
                    // If not, clear the client up and wait to try it all again
                    catch(Exception a) {
                        System.out.println("Cannot convert to int... cancelling");
                        outStream.write(13); // carriage return
                        outStream.write(10); // line feed
                        outStream.flush(); // flush the stream line
                        continue;
                    }

                    // Square the number and send out the result!
                    inLined = inLined * inLined;
                    String outLine = "Squared: " + inLined;
                    outStream.writeBytes(outLine); // send a message to client
                    outStream.write(13); // carriage return
                    outStream.write(10); // line feed
                    outStream.flush(); // flush the stream line
                }
                inStream.close();
                outStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException occurred" + e);
        }
    } // main
}

