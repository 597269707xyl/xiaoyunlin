package com.zdtech.platform.framework.encrypt;

import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * encrypt
 *
 * @author panli
 * @date 2017/10/27
 */
public class Encrypt {
    private static String ALGORITHM = "RSA";
    private static Integer KEYSIZE = 1024;
    private static String jarPath = Encrypt.class.getProtectionDomain().getCodeSource().getLocation().getFile();

    enum KeyType {
        Public, Private
    }

    public static byte[] encryptOrDecrypt(Key key, int model, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(model, key);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends Key> T getKeyFromFile(String filePath, Class<T> t) {

        try  {
            URL url=new URL("jar:file:" + jarPath + "!" + filePath);
            InputStream fis = url.openStream();
            ObjectInputStream ois = new ObjectInputStream(fis);
            return t.cast(ois.readObject());
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return null;
    }


    private static void writeKeyToFile(Key key, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Key parseKey(byte[] keyBytes, KeyType keyType) {
        PKCS8EncodedKeySpec pkcsKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            if (keyType == KeyType.Private) {
                return keyFactory.generatePrivate(pkcsKeySpec);
            } else if (keyType == KeyType.Public) {
                return keyFactory.generatePublic(pkcsKeySpec);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean check(){
        long expireMil = 0L;
        String pubKeyPath = "/pubKey";
        PublicKey pubKey = Encrypt.getKeyFromFile(pubKeyPath, PublicKey.class);
        try {
//            FileInputStream fis = new FileInputStream(new File(""));
            URL url=new URL("jar:file:" + jarPath + "!/time");
            InputStream fis = url.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read = -1;
            while ((read = fis.read(buf)) > 0) {
                bos.write(buf,0,read);
            }
            String base64Str = new String(bos.toByteArray());
            BASE64Decoder decoder= new BASE64Decoder();
            byte[] toDecrypt = decoder.decodeBuffer(base64Str);

            byte[] decripted = Encrypt.encryptOrDecrypt(pubKey,Cipher.DECRYPT_MODE,toDecrypt);
            String timeStr = new String(decripted);
            String timeStr1 = timeStr.substring(2);
            System.out.println("软件过期时间：" + timeStr1);
            expireMil = Long.valueOf(timeStr1);
            Date cur = new Date();
            if (cur.getTime()> expireMil){
                return false;
            }
        } catch (IOException e) {

        }
        return true;

    }
    public static void main(String[] args) {
        String priKeyPath = "D:/priKey";
        String pubKeyPath = "conf/datamessage/pubKey";
        PrivateKey priKey = Encrypt.getKeyFromFile(priKeyPath, PrivateKey.class);
        PublicKey pubKey = Encrypt.getKeyFromFile(pubKeyPath, PublicKey.class);

        try {
            FileInputStream fis = new FileInputStream(new File("conf/datamessage/time"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read = -1;
            while ((read = fis.read(buf)) > 0) {
                bos.write(buf,0,read);
            }
            String base64Str = new String(bos.toByteArray());
            BASE64Decoder decoder= new BASE64Decoder();
            byte[] toDecrypt = decoder.decodeBuffer(base64Str);

            byte[] decripted = Encrypt.encryptOrDecrypt(pubKey,Cipher.DECRYPT_MODE,toDecrypt);
            String timeStr = new String(decripted);
            String timeStr1 = timeStr.substring(2);
            long expireMil = Long.valueOf(timeStr1);

            Date cur = new Date();
            System.out.println(expireMil);
            System.out.println(cur.getTime());
            if (cur.getTime()> expireMil){
                System.out.println("软件已过期，不能再使用...");
                System.exit(1);
            }
        } catch (IOException e) {

        }

    }
}
