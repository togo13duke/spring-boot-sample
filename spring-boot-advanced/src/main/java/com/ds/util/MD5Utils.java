package com.ds.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String code(String str){

        try{

            var md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            var byteDigest = md.digest();

            int i;
            var buf = new StringBuffer("");

            for (var offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            // 32bit暗号化
            return buf.toString();

            // 16bit暗号化
            //return buf.toString().substring(8, 24);

        }catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
