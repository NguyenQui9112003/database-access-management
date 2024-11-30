package org.example.connectionManager;

public class connectionManagerSingleton {
    private static connectionManagerSingleton instance;
    private connectionManagerSingleton() {
        try {

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm để đảm bảo trình quản lý kết nối chỉ có một cái duy nhất
    public static connectionManagerSingleton getInstance() {
        if (instance == null) {
            instance = new connectionManagerSingleton();
        }
        return instance;
    }
}
