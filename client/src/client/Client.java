/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.net.*;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
/**
 *
 * @author Rah Desta
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    private static Cipher encrypt;
    private static final byte[] initialization_vector = { 22, 33, 11, 44, 55, 99, 66, 77 };
    public static void main(String[] args) {
        String clearFile = "C:\\Users\\Rah Desta\\Documents\\GitHub\\SekuritasJaringan\\client\\input.txt";
        String encryptedFile = "C:\\Users\\Rah Desta\\Documents\\GitHub\\SekuritasJaringan\\client\\encrypted.txt";
        // TODO code application logic here
        try {
                /*
                 * variable initialization
                 */
                Socket clientSocket;
                DataInputStream inputStream;
                DataOutputStream outputStream;
                String message;

                /*
                 * create socket, preapring data output stream, send to server
                 */
                clientSocket = new Socket("localhost", 5000);
                
                SecretKey secret_key = KeyGenerator.getInstance("DES").generateKey();
                AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(initialization_vector);
                // set encryption mode ...
                encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
                encrypt.init(Cipher.ENCRYPT_MODE, secret_key, alogrithm_specs);
                // encrypt file
                encrypt(new FileInputStream(clearFile), new FileOutputStream(encryptedFile));
                System.out.println("End of Encryption/Decryption procedure!");
                
                outputStream = new DataOutputStream(clientSocket.getOutputStream());
                outputStream.writeUTF(encryptedFile);

                /*
                 * preparing data input stream, receive message from server, print
                 */
                inputStream = new DataInputStream(clientSocket.getInputStream());
                message = inputStream.readUTF();
                System.out.println("From server: " + message);

                /*
                 * close data output stream, data input stream, and client socket
                 */
                outputStream.close();
                inputStream.close();
                clientSocket.close();
            }
            catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
                e.printStackTrace();
            }
        }
    
   private static void encrypt(InputStream input, OutputStream output)throws IOException {
       output = new CipherOutputStream(output, encrypt);
       writeBytes(input, output);
   }
   
   private static void writeBytes(InputStream input, OutputStream output)throws IOException {
        byte[] writeBuffer = new byte[512];
        int readBytes = 0;
        while ((readBytes = input.read(writeBuffer)) >= 0) {
            output.write(writeBuffer, 0, readBytes);
        }
        output.close();
        input.close();
   }
}