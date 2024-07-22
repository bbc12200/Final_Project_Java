package com.example.chatapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatController extends ChatGui implements ChatConnectionInterface {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5001;

    @FXML
    protected void initialize() {
        initializeConnection(messageArea);
    }

    @Override
    public void initializeConnection(TextArea messageArea) {
        connectToServer();
        setupCloseRequest();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Platform.runLater(() -> messageArea.appendText("Welcome to the ChatApp\n"));

                readMessagesFromServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Platform.runLater(() -> messageArea.appendText("Unknown host: " + SERVER_IP + "\n"));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> messageArea.appendText("I/O Error: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void readMessagesFromServer() {
        try {
            String serverMessage;
            while (!socket.isClosed() && (serverMessage = in.readLine()) != null) {
                String finalServerMessage = serverMessage;
                Platform.runLater(() -> messageArea.appendText(finalServerMessage + "\n"));
            }
        } catch (SocketException e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
                Platform.runLater(() -> messageArea.appendText("Socket error: " + e.getMessage() + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> messageArea.appendText("I/O Error: " + e.getMessage() + "\n"));
        }
    }

    private void setupCloseRequest() {
        Platform.runLater(() -> {
            Stage stage = (Stage) messageArea.getScene().getWindow();
            stage.setOnCloseRequest((WindowEvent event) -> {
                closeConnection();
            });
        });
    }

    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void closeConnection() {
        if (out != null) {
            out.println("Someone has left the chat.");
            out.close();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        name = getInputName();
        if (name.isEmpty()) {
            setWelcomeText("Welcome to JavaFX Application!");
        } else {
            setWelcomeText("Welcome, " + name + "!");
        }
    }

    @FXML
    protected void onSendButtonClick() {
        String message = getInputField();
        String sendingMessage;

        if (name != null && !name.isEmpty()) {
            sendingMessage = name + ": " + message;
        } else {
            sendingMessage = "Anonymous User: " + message;
        }

        sendMessage(sendingMessage);
        clearInputField();
    }

}