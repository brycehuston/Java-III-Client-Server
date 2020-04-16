package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientClass {

    static Socket socket;
    static DataOutputStream outStream; // output stream to server
    static DataInputStream inStream; // input stream from server

    public static void main(String[] args) {

        String host = "127.0.0.1"; // get host
        int port = 4321; // get port #

        try {
            // create socket for connection
            socket = new Socket(host, port);

            // get input/output streams
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to " + host + " on port " + port);
        } catch (UnknownHostException e) {
            System.out.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        chat(); // perform chatting loop

        try { // close resources
            socket.close();
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            // handle exception here
        }
    } // main

    public static void chat() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                String lineInput = sc.nextLine();

                if (lineInput.length() > 0) { // if client types something
                    outStream.writeBytes(lineInput); // send message to server
                    outStream.write(13); // carriage return
                    outStream.write(10); // line feed
                    outStream.flush(); // flush the stream line

                    if (lineInput.equalsIgnoreCase("quit")) {
                        System.exit(0); // stop client chatting as well.
                    }

                    // print any message received from server
                    int inByte;
                    System.out.print("From Server: ");
                    while ((inByte = inStream.read()) != '\n') {
                        System.out.write(inByte);
                    }
                    System.out.println();
                }
            } catch (IOException e) {
                // handle IO exception here
            }
        }
    }
}

