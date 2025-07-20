package com.uas.klique.db;

import com.uas.klique.config.DatabaseConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Seeder {
    public static void seed() {
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            // Baca file schema.sql dari resource
            String schemaSql = new BufferedReader(
                    new InputStreamReader(
                            Seeder.class.getResourceAsStream("/database/schema.sql")
                    )
            ).lines().collect(Collectors.joining("\n"));

            // Pisahkan per statement dengan delimiter semicolon (;)
            String[] statements = schemaSql.split(";");

            for (String raw : statements) {
                String sql = raw.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }

            System.out.println("Database seeded successfully (split).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
