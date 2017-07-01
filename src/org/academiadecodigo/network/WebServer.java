package org.academiadecodigo.network;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.regex.Pattern;

/**
 * Created by codecadet on 29/06/2017.
 */
public class WebServer {
    private ServerSocket serverSocket;
    private BufferedReader reciveCommands;
    private DataOutputStream sendData;
    private String header;


    public void statServer() {

        try {


            webServerConnection();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void webServerConnection() throws IOException {
        int port = 5000;
        createServerSocket(port);
        System.out.println("Waiting for a browser connection...");
        while (true) {

            Socket sSocket = serverSocket.accept();
            sendData = new DataOutputStream(sSocket.getOutputStream());
            reciveCommands = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
            extractInformationFromHeader();

            sSocket.close();


        }


    }

    private void extractInformationFromHeader() throws IOException {

        String[] arryString;
        if ((this.header = this.reciveCommands.readLine()) != null) {

            arryString = header.split(" ");
            handleVerbs(arryString[0]);

            String resourcesPath = arryString[1];

            for (String word : arryString) {
                System.out.println(word);
            }
            if (resourcesPath.equals("/")) {
                resourcesPath = "www/index.html";
            }
            if (resourcesPath.equals("/image")) {
                resourcesPath = "www/pirata-simples_17-1111184221.jpg";
            }
            if (resourcesPath.equals("/energia")) {
                resourcesPath = "www/energia.jpg";
            }

            File resource = new File(resourcesPath);

            if (!resource.exists()) {
                resource = new File("www/Error.html");
                sendData.writeBytes(createErrorHeader((resource)));
                sendData.write(Files.readAllBytes(resource.toPath()));
            } else {
                sendData.writeBytes(createHeader(resource));
                sendData.write(Files.readAllBytes(resource.toPath()));
            }
        }
        System.out.println(header);
        reciveCommands.close();
        sendData.close();
    }


    private String createHeader(File file) throws IOException {
        String[] fileType = file.getName().split("\\.");
        System.out.println(file.length());
        System.out.println(file.getName());

        return "HTTP/1.0 200 OK\r\n" +
                "Content-Type: " + fileType[1] + "\r\n" +
                "Content-Length: " + file.length() + " \r\n" +
                "\r\n";
    }

    private String createErrorHeader(File file) throws IOException {
        String[] fileType = file.getName().split("\\.");
        return "HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: " + fileType[1] + "; charset=UTF-8\r\n" +
                "Content-Length: " + file.length() + " \r\n" +
                "\r\n";
    }

    private void createServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void handleVerbs(String verbType)  {
        switch (verbType) {
            case "GET":
                sendPacket();
            case "PUT":
                //changeFile();
            default:
                sendPacket();
        }
    }

    private void sendPacket() {
        String[] verbType;
        verbType = header.split(" ");
        String verb = verbType[0];

        switch (verb) {
            case "GET":
                //
            default:
                //sendTrash();
        }

    }
}
