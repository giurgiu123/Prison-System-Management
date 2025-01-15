package com.example.penitenciarv1.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteSQLFileClass {

    public static void main(String[] args) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String sqlFilePath = "allData.sql"; // Path to the SQL file

        Connection connection = null;
        Statement statement = null;

        try {
            // Step 1: Establish the connection
            connection = databaseConnector.getConnection();

            // Step 2: Create a statement
            statement = connection.createStatement();

            // Step 3: Read the SQL file
            File file = new File("../src");
            for(String fileNames : file.list()) System.out.println(fileNames);


            if (!file.exists()) {
                System.out.println("The specified SQL file does not exist: " + sqlFilePath);
                return;
            }


            String sql = readSQLFile(sqlFilePath);

            // Step 4: Execute the SQL script
            String[] sqlStatements = sql.split(";");
            for (String sqlStatement : sqlStatements) {
                if (sqlStatement.trim().length() > 0) {
                    statement.execute(sqlStatement);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Step 5: Close the resources
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String readSQLFile(String filePath) {
        StringBuilder sqlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sqlContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sqlContent.toString();
    }
}
