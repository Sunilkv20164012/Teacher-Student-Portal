package com.company.Messages;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class MessageEncryption {
    private String message;

    public MessageEncryption(String message, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        byte[] plainText = message.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // encrypt using the key and the plaintext
        //System.out.println("\nStart encryption");

        //  Initializes the Cipher object

        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Calculates the ciphertext with a plaintext string.
        byte[] cipherText = cipher.doFinal(plainText);
        String str2="";

        for (byte b:cipherText) {
            str2 +=(char)b;
        }
        this.message = str2;
        //System.out.println("Finish encryption: ");
    }

    public String getMessage() {
        return message;
    }
}