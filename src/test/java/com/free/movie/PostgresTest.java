package com.free.movie;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PostgresTest {

    @Test
    public void insertBatch() throws Exception {

        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String dbURL = "jdbc:postgresql://192.168.99.156:5432/postgres";
        conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

        // 关闭事务自动提交 ,这一行必须加上
        conn.setAutoCommit(false);
        stmt = conn.prepareStatement("insert into \"user\" (id, unique_key) "
                + "values (?,?)");

        for (int j = 1000_0000; j < 10000_0000; j++){
            stmt.setString(1, String.valueOf(j));
            stmt.setString(2, UUID.randomUUID().toString().substring(0, 16));
            stmt.addBatch();

            if (j % 500 == 0) {
                stmt.executeBatch();
                stmt = conn.prepareStatement("insert into \"user\" (id, unique_key) "
                        + "values (?,?)");
            }
            if (j % 5000 == 0) {
                System.out.println("已经创建到：" + j);
            }
        }

        stmt.executeBatch();
        conn.commit();
        stmt.close();
        conn.close();



    }
}
