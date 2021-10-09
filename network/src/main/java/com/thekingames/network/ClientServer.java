package com.thekingames.network;

import java.io.IOException;
import java.net.Socket;

public class ClientServer {

    private static final String LOG_TAG = "myServerApp";
    //ip-адрес сервера, который принимает соединения
    private String mServerName = "192.168.41.148";
    //порт, который принимает соединения
    private int mServerPort = 6970;
    //сокет, через который приложения общаются с сервером
    private Socket mSocket = null;

    //пустой конструктор красса
    public ClientServer() {
    }

    //функция открытия нового соединения, если сокет уже открыт, то закрываем его/
    public void openConnection() throws Exception {
        //освобождаем ресурсы: закрываем сокет
        closeConnection();

        try {
            //создание нового сокета, с указанием адреса сервера, и порта процесса
            mSocket = new Socket(mServerName, mServerPort);
        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: " + e.getMessage());
        }
    }

    //функция отправления данных по сокету/////////////////////////////////////////
    //переменная data - данные, которые отправляем/////////////////////////////////
    public void sendData(byte[] data) throws Exception {
        if (mSocket == null || mSocket.isClosed()) {
            throw new Exception("Невозможно отправить данные. Сокет не создан или закрыт");
        }
        try {
            //отправка данных
            mSocket.getOutputStream().write(data);
            mSocket.getOutputStream().flush();
        } catch (IOException e) {
            throw new Exception("Невозможно отправить данные: " + e.getMessage());
        }
    }

    //функция закрытия соединения//////////////////////////////////////////////////
    public void closeConnection() {
        //проверяем сокет, если не закрыт, то закрываем его
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                System.out.println("client_server" + "Невозможно закрыть сокет: " + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
    }

    //функция получения сообщений от других клиентов////////////////////////////////
    public void getData() throws IOException {
        //создание буффера для данных
        byte[] buffer = new byte[1024 * 4];
        while (true) {
            //получаем входящие сообщения
            try {
                int count = mSocket.getInputStream().read(buffer, 0, buffer.length);
                String enterMessage = new String(buffer, 0, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
//переопределяем метод finalize и освобождаем ресурсы
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }
}
