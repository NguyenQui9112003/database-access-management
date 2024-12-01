package org.example.connectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public class connectionManagerSingleton {
    private static connectionManagerSingleton instance;
    private ArrayList<Connection> connectionArr = new ArrayList<>(); // Biến này dùng để chứa đựng các connection
    private int connectionLimit; // Biến này để set số lượng connection tối đa có thể nhận được

    private connectionManagerSingleton(int maxConnection) {
        try {
            connectionLimit = maxConnection;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm để đảm bảo trình quản lý kết nối chỉ có một cái duy nhất
    public static connectionManagerSingleton getInstance() {
        if (instance == null) {
            instance = new connectionManagerSingleton(10);
        }
        return instance;
    }

    // Cần một hàm để thêm connection vào trong cái queue
    public boolean addConnection(Connection con) {
        if (connectionArr.size() < connectionLimit) {
            connectionArr.add(con);
            System.out.println("Connection is added to the array.");
            return true;
        } else {
            System.out.println("The array is full.");
            return false;
        }
    }

    // Cần một hàm để xóa connection vào trong cái queue
}
