package com.fbb.jjzprocess.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by fengbb on 2018/1/2.
 */

public class SignUtil {
    public static String createSign(String text){
        String sign = "";
        try {
            Socket socket = new Socket("127.0.0.1", 9000);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(text);
            sign = dis.readUTF();
            System.out.println("sign:"+sign);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

//    public static String createSign(String text){
//        String sign = "";
//        try {
//            Socket socket = new Socket("127.0.0.1", 9000);
//            DataInputStream dis = new DataInputStream(socket.getInputStream());
//            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//            dos.writeUTF(text);
//            sign = dis.readUTF();
//            System.out.println("sign:"+sign);
//            socket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sign;
//    }
}
