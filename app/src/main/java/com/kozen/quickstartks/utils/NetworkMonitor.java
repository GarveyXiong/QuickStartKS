package com.kozen.quickstartks.utils;

import java.util.ArrayList;
import java.util.List;

public class NetworkMonitor {
    private static volatile NetworkMonitor instance;
    private final List<NetworkListener> listeners = new ArrayList<>();
    private NetworkMonitor() {

    }

    // 获取单例实例
    public static NetworkMonitor getInstance() {
        if (instance == null) {
            synchronized (NetworkMonitor.class) {
                if (instance == null) {
                    instance = new NetworkMonitor();
                }
            }
        }
        return instance;
    }

    // 注册监听器
    public void registerListener(NetworkListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    // 注销监听器
    public void unregisterListener(NetworkListener listener) {
        listeners.remove(listener);
    }
    public void notifyListener() {
        for (NetworkListener listener : listeners) {
            listener.onDataReceived(true);
        }
//        listeners.get(0).onNetworkChanged(true);
    }


    // 监听器接口
    public interface NetworkListener {
        void onDataReceived(boolean isConnected);
    }
}