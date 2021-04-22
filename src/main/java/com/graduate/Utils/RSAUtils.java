package com.graduate.Utils;


import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA安全编码组件
 *
 * @version 1.0
 * @desc 公钥和私钥存放在properties文件的时候每行的末尾加上“\r\n\” <br/>
 * “\r\n” 起到换行的作用,最后的“\”在properties在里表示连接
 *
 * @author Neo
 * @date 2018-4-15 22:23:19
 * @since 1.0
 */
public class RSAUtils extends Base64Utils {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final String MY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAb9+0HGbMZTXsZtHwFI6x9Mfin89fkJpvjlVx\n" +
            "dNmTHwPJN0g6dJLH/PC63TSAjJq7Y+7/Wtzn3ulj5ai0bG0k8QtDXlSuHRAxq0tBig93Ou069On7\n" +
            "Sdl/xqTJxk709luba4IHLhL5JMWJiFDoDiic+3ptzzeKq3zgFmkPhIHhiwIDAQAB";
    private static final String MY_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIBv37QcZsxlNexm0fAUjrH0x+Kf\n" +
            "z1+Qmm+OVXF02ZMfA8k3SDp0ksf88LrdNICMmrtj7v9a3Ofe6WPlqLRsbSTxC0NeVK4dEDGrS0GK\n" +
            "D3c67Tr06ftJ2X/GpMnGTvT2W5trggcuEvkkxYmIUOgOKJz7em3PN4qrfOAWaQ+EgeGLAgMBAAEC\n" +
            "gYB7uMj6vF0+ScTPDCDxryviOxUNtrpRqX9LNQYTu+cXNMo1274FKWR3p63Ro+K+bjNjjhmc/E9O\n" +
            "QUn8BDAmVUgw5qhBUxy9FiNCUkZvtqaisvuxof1WRlQT/bTC4HczgLupnHPMWV2yNVdyQDHf9lp6\n" +
            "2FV3fihW3f6BfF7rCvXSIQJBAOLZVrH//dFyb5ogLIOH7ARBSWSpg28iMsPsawy2xLjKExJkl3UE\n" +
            "7TsZ/HCPhrNWBN8QuncioqjCNMkRfaY5dfECQQCQ8RJusZKzuXXbr+S8zIf3jZS0Mi1U8j5u/cYp\n" +
            "GJ3K0mAsla+cE8UbcofRkNtXm/avESJ7jU0JfdM4ezARn+M7AkEAh4tjoPIbwCVHh+lHj8QMgzJb\n" +
            "XzlmXmYpvSWrhSpD+0JLCQdv9CzgbxsE2mD4FHoGaR6+u+bHGF0KW0YHNOZKkQJAYxT5dcRcNhJF\n" +
            "OF3grxPequMVUa2vgY9sELHYwgU68QipiDD3cGmvZ9j+cctltlk+5GSBPxbUH/Xs64KC1LmJOwJA\n" +
            "UIduQ7SE/5Bkr2BeBfjsqkVz39vvh2bjbnC8KDYj1khbBN4/ow77aLaekZyr4VvecsFEuM0ZHmwC\n" +
            "5Y+Z8eMLjA==";

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        return sign(data.getBytes(), privateKey);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }


    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(), publicKey, sign);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }


    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        return new String(decryptByPrivateKey(Base64Utils.decryptBASE64(data), key));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) throws Exception {
        return new String(decryptByPublicKey(Base64Utils.decryptBASE64(data), key));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        return Base64Utils.encryptBASE64(encryptByPublicKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        return Base64Utils.encryptBASE64(encryptByPrivateKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static String myEncrypt(String Data) throws Exception {
        return RSAUtils.encryptByPublicKey(Data, MY_PUBLIC_KEY);
    }

    public static String myDecrypt(String EncryptData) throws Exception {
        return RSAUtils.decryptByPrivateKey(EncryptData, MY_PRIVATE_KEY);
    }


    public static void main(String[] args) {
        try {
            Map<String, Object> map = RSAUtils.initKey();
            String publicKey = RSAUtils.getPublicKey(map);
            String privateKey = RSAUtils.getPrivateKey(map);
            System.out.println("公钥：" + publicKey);
            System.out.println("私钥：" + privateKey);
            String data = "12";
            String encryptData = RSAUtils.encryptByPublicKey(data, publicKey);
            System.out.println("加密后：" + encryptData);
            String decryptData = RSAUtils.decryptByPrivateKey(encryptData, privateKey);
            System.out.println("解密后：" + decryptData);
            System.out.println(RSAUtils.myDecrypt(RSAUtils.myEncrypt("12")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
