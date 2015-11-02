package kg.apc.jmeter.functions;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;




/** 
 * 类说明 
 * @author  samy.zhh
 * @version V1.0 2015年10月28日 下午3:36:36 
 */
public class AESUtil {
    private static final String  SHA1PRNG  = "SHA1PRNG";
    private static final String  AES       = "AES";
    
    
    public static void main(String args[]) throws Exception{
        
//        Cipher clientCookieDecryptCipher = getCipher(clientKey, false);
        String clientKey = "563a7b80-bc18-45cd-a8d6-3022dcbe4b4b";
        
        long times = 1000 * 60 * 2;
        String text = (System.currentTimeMillis()+times)+"";
        
//        text = "1446038682184";
        String enc = encodeText(clientKey, text);
        System.out.println("encrypt: "+enc+"\r\n");
        
        //F82675ED55C490D1AB7F75BCB17A0278
        enc= "AFE9CC4EE8CF084AD11A3A99D9EE6B5F";
        String dec = decodeText(clientKey, enc);
        System.out.println("dec:"+dec);

    }

    public static String encodeText(String clientKey, String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = getCipher(clientKey, true);
        
        return encodeText(cipher, text, null);
    }
    
    public static String encodeText(Cipher cipher, String text, String charset) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (text == null)
            return null;
        byte[] resultBytes = null;
        String result = null;
        byte[] byteContent = null;
        if (charset==null || charset.length()==0) {
            byteContent = text.getBytes();
        }
        else
            byteContent = text.getBytes(charset);
        resultBytes = cipher.doFinal(byteContent);
        result = bytes2Hex(resultBytes);
        return result;
    }
    
    public static String decodeText(String clientKey, String cipherText) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = getCipher(clientKey, false);
        return decodeText(cipher, cipherText, null);
    }
    
    public static String decodeText(Cipher cipher, String cipherText, String charset) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (cipherText == null)
            return null;
        byte[] resultBytes = null;
        byte[] byteContent = hex2Bytes(cipherText);
        resultBytes = cipher.doFinal(byteContent);
        if (charset==null || charset.length()==0) {
            return new String(resultBytes);
        }
        else
            return new String(resultBytes, charset);
        
    }
    
    public static Cipher getCipher(String seed, boolean isEncrypt) {
        Cipher cipher = null;
        try {
            KeyGenerator clientkgen = KeyGenerator.getInstance(AES);
            SecureRandom clientSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            clientSecureRandom.setSeed(seed.getBytes());
            clientkgen.init(128, clientSecureRandom);
    
            SecretKey clientSecretKey = clientkgen.generateKey();
            byte[] clientEnCodeFormat = clientSecretKey.getEncoded();
            SecretKeySpec clientKeySpec = new SecretKeySpec(clientEnCodeFormat, AES);
            cipher = Cipher.getInstance(AES);
            if (isEncrypt)
                cipher.init(Cipher.ENCRYPT_MODE, clientKeySpec);
            else
                cipher.init(Cipher.DECRYPT_MODE, clientKeySpec);
        }catch(Exception e) {
            System.out.println("happens error when getCipher :"+e.getMessage());
        }
        return cipher;
    }
    
    /**
     * 字符转换成16进制
     * @param bytes
     * @return
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null) return null;
        StringBuilder result = new StringBuilder();
        String tmp = "";
        for (int n = 0; n < bytes.length; n++) {
            tmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
            if (tmp.length() == 1) result.append("0").append(tmp);
            else result.append(tmp);
        }
        return result.toString().toUpperCase();
    }
    
    /**
     * 16进制转换成字符
     * @param hexStr
     * @return
     */
    public static byte[] hex2Bytes(String hexStr) {
        byte[] b = hexStr.getBytes();
        if ((b.length % 2) != 0) throw new IllegalArgumentException("length need to be even");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
