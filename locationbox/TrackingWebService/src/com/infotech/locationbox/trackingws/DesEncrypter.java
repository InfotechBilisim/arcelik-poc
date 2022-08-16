package com.infotech.locationbox.trackingws;


import com.infotech.locationbox.utils.Utils;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DesEncrypter {
  private static Cipher ecipher = null;
  private static Cipher dcipher = null;

  // 8-byte Salt
  private static byte[] salt = {
    (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
    (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
  };

  // Iteration count
  private static int iterationCount = 19;
  private static String passPhrase = "MatchUpPSWX";

  public DesEncrypter() {
  }

//-----------------------------------------------------------------------------

  public static void initialize() {
    try {
      // Create the key
      KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
      SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
      ecipher = Cipher.getInstance(key.getAlgorithm());
      dcipher = Cipher.getInstance(key.getAlgorithm());
      // Prepare the parameter to the ciphers
      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
      // Create the ciphers
      ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
      dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return;
  } // initialize()

//-----------------------------------------------------------------------------

  public static String encrypt(String str) {
    if( ecipher == null ) initialize();
    try {
      // Encode the string into bytes using utf-8
      byte[] utf8 = str.getBytes("UTF8");
      // Encrypt
      byte[] enc = ecipher.doFinal(utf8);
      // Encode bytes to base64 to get a string
      return new sun.misc.BASE64Encoder().encode(enc);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // encrypt()

//-----------------------------------------------------------------------------

  public static String decrypt(String str) {
    if( dcipher == null ) initialize();
    try {
      // Decode base64 to get bytes
      byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
      // Decrypt
      byte[] utf8 = dcipher.doFinal(dec);
      // Decode using utf-8
      return new String(utf8, "UTF8");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // decrypt()

//-----------------------------------------------------------------------------

  public static String encryptSimple(String str) {
    if( ecipher == null ) initialize();
    try {
      byte[] utf8 = str.getBytes("UTF8");
      byte[] bytes = ecipher.doFinal(utf8);
      StringBuffer sbuf = new StringBuffer();
      for( int i = 0; i < bytes.length; i++ ) {
        sbuf.append(Utils.convNumberToHexString(bytes[i] & 0xff, 2));
      } // for()
      return sbuf.toString();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // encryptSimple()

//-----------------------------------------------------------------------------

  public static String decryptSimple(String str) {
    if( dcipher == null ) initialize();
    try {
      byte[] bytes = new byte[str.length() / 2];
      int offs = 0;
      for( int i = 0; i < bytes.length; i++ ) {
        bytes[i] = (byte) Utils.convHexStringToNumber(str.substring(offs, offs + 2), 2);
        offs += 2;
      } // for()
      byte[] utf8 = dcipher.doFinal(bytes);
      return new String(utf8, "UTF8");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // decryptSimple()

}
