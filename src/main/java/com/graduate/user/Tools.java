package com.graduate.user;

import com.graduate.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import static com.graduate.Utils.RSAUtils.myDecrypt;
import static com.graduate.Utils.RSAUtils.myEncrypt;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年04月17日 21:05
 */

public class Tools {

    @Autowired
    UserMapper userMapper;

    // 对字节数组字符串进行Base64解码并生成图片
    //imgFilePath 待保存的本地路径
    public static boolean GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            //====
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean base64ToImageOutput(String base64, OutputStream out) throws IOException {
        if (base64 == null) { // 图像数据为空
            return false;
        }
        try {
            // Base64解码
            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            out.write(bytes);
            out.flush();
            return true;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Tools tools;

    @PostConstruct
    public void init() {
        tools = this;
        tools.userMapper = this.userMapper;
    }

    @PostConstruct
    public static Boolean judge(String id) throws Exception {
        System.out.println(id);
        try {
            int i = new Integer(myDecrypt(id));
            if(tools.userMapper.selectById(myDecrypt(id)) == null)
                return false;
            else
                return true;
        }
        catch (BadPaddingException e){
            return false;
        }
    }

    public static Boolean judgeAuthority(String id) throws Exception {
        return judge(id);
    }




}
