package org.example;

import org.example.Crud.*;
import org.example.Models.*;
import org.example.Private.Sensitive;
import org.example.Services.*;
import org.example.SwingComponents.MainPage;
import org.example.Validation.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public class App 
{
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                MainPage mainPage = new MainPage();
                mainPage.setVisible(true);
            });
        }
}

