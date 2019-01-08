package com.company.Messages;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class MessageDecryption {
    private String message;

    public MessageDecryption(String message, Key key) {
        try {
            byte[] cipherText = new byte[message.length()];
            char[] carr = message.toCharArray();
            for (int i = 0; i < message.length(); i++) {
                cipherText[i] = (byte) carr[i];
            }

            // Creates the Cipher object (specifying the algorithm, mode, and padding)
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // decrypt the ciphertext using the same key
            //System.out.println("\nStart decryption");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] newPlainText = cipher.doFinal(cipherText);
            //System.out.println("Finish decryption: ");

            System.out.println(new String(newPlainText, StandardCharsets.UTF_8));
            this.message = new String(newPlainText, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }
}
