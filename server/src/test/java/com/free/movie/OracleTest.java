package com.free.movie;

import org.junit.Test;

import java.sql.*;
import java.util.concurrent.*;

public class OracleTest {

    @Test
    public void testColumn() throws InterruptedException {



        ExecutorService executorService = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1));
        int i = 1;
        while (i-- > 0) {
            executorService.execute(() -> {

                ResultSet rs = null;
                PreparedStatement stmt = null;
                Connection conn = null;
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    String dbURL = "jdbc:oracle:thin:@192.168.99.156:1521:helowin";
                    conn = DriverManager.getConnection(dbURL, "test", "test");

                    System.out.println("连接成功");

                    stmt = conn.prepareStatement("select * from \"user\"",
//                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    stmt.setFetchSize(1);

                    ResultSet resultSet = stmt.executeQuery();

                    while (resultSet.next()) {
                        System.out.println(Thread.currentThread().getName() + " -" + resultSet.getString("id"));
                        System.out.println(Thread.currentThread().getName() + " -" + resultSet.getString("name"));
                        System.out.println("++++++++++++++++++++++++++++++++");
                        Thread.sleep(2000);
                    }

//            int i = 306;
//            while(i++ < 10_0000) {
//                PreparedStatement preparedStatement = conn.prepareStatement(
//                        "INSERT INTO \"user\" (\"id\", \"name\", \"code\", \"create_time\", \"update_time\") VALUES " +
//                                "('" + i + "', 'name" +
//                                i + "', NULL, NULL, NULL)");
//                preparedStatement.execute();
//                preparedStatement.close();
//            }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                            rs = null;
                        }
                        if (stmt != null) {
                            stmt.close();
                            stmt = null;
                        }
                        if (conn != null) {
                            conn.close();
                            conn = null;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

        executorService.awaitTermination(1000, TimeUnit.HOURS);
    }

}
