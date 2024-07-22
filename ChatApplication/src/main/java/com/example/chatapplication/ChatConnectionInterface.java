package com.example.chatapplication;

import javafx.scene.control.TextArea;

public interface ChatConnectionInterface {
    void initializeConnection(TextArea messageArea);
    void sendMessage(String message);
    void closeConnection();
}