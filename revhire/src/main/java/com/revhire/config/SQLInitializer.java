package com.revhire.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class SQLInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection()) {

            if (!tableExists(conn, "USERS")) {
                System.out.println("Initializing database...");
                executeSQLScript(conn);
                System.out.println("Database initialized successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean tableExists(Connection conn, String tableName) {
        try {
            ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private static void executeSQLScript(Connection conn) throws Exception {

        InputStream input = SQLInitializer.class
                .getClassLoader()
                .getResourceAsStream("schema.sql");

        if (input == null) {
            throw new RuntimeException("schema.sql not found!");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        StringBuilder command = new StringBuilder();
        String line;
        boolean insideTrigger = false;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty() || line.startsWith("--")) {
                continue;
            }

            if (line.toUpperCase().startsWith("CREATE OR REPLACE TRIGGER")) {
                insideTrigger = true;
            }

            command.append(line).append("\n");

            if (insideTrigger && line.equalsIgnoreCase("END;")) {

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(command.toString());
                }

                command.setLength(0);
                insideTrigger = false;
                continue;
            }

            if (!insideTrigger && line.endsWith(";")) {

                String sql = command.toString();
                sql = sql.substring(0, sql.length() - 2); // remove last ";\n"

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                }

                command.setLength(0);
            }
        }
    }



}
