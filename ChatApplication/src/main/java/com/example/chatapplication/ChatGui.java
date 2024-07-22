package com.example.chatapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatGui {
    @FXML
    protected Label welcomeText;

    @FXML
    protected TextField inputName;

    @FXML
    protected TextArea messageArea;

    @FXML
    protected TextField inputField;

    protected String name;

    public void setWelcomeText(String text) {
        welcomeText.setText(text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputName() {
        return inputName.getText();
    }

    public String getInputField() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.clear();
    }
}