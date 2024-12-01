package org.example.connectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionManagerSingleton {
    private static ConnectionManagerSingleton instance;
    private final ArrayList<Connection> connectionArr = new ArrayList<>(); // Biến này dùng để chứa đựng các connection
    private final int maxConnection;

    private ConnectionManagerSingleton(int maxConnection) {
        try {
            System.out.println("Creating a manager with " + maxConnection + " connections.");
            this.maxConnection = maxConnection;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm để đảm bảo trình quản lý kết nối chỉ có một cái duy nhất
    public static ConnectionManagerSingleton getInstance(int maxConnection) {
        if (instance == null) {
            instance = new ConnectionManagerSingleton(maxConnection);
        }
        return instance;
    }

    // Cần một hàm để thêm connection vào trong cái Array
    public boolean addConnection(Connection con) throws SQLException {
        // Kiểm tra vượt quá giới hạn chưa? Chưa thì thêm vào
        if (connectionArr.size() < maxConnection) {
            System.out.println("Adding connection to position " + connectionArr.size() + " in the manager");
            connectionArr.add(con);
            System.out.println("Added connection!");
            return true;
        }
        // Còn nếu vượt giới hạn rồi thì kiểm xem có cái nào bị đóng không?
        // Có thì thế vào
        for (int i = 0; i < connectionArr.size(); i++) {
            if (connectionArr.get(i).isClosed()) {
                System.out.println("Adding connection to position " + i + " in the manager");
                connectionArr.set(i, con);
                System.out.println("Added connection!");
                return true;
            }
        }

        // Không thì báo đầy
        System.out.println("The manager is full.");
        return false;
    }

    // Cần một hàm để xóa connection vào trong cái queue
}
