/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
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
public class Server {

    /**
     * @param args the command line arguments
     */
    private static Cipher encrypt;
    private static Cipher decrypt;
    private static final byte[] initialization_vector = { 22, 33, 11, 44, 55, 99, 66, 77 };
    public static void main(String[] args) {
        // TODO code application logic here
        String decryptedFile = "C:\\Users\\Rah Desta\\Documents\\GitHub\\SekuritasJaringan\\server\\decrypted.txt";
        try {
                /*
                 * variable initialization
                 */
                SecretKey secret_key = KeyGenerator.getInstance("DES").generateKey();
                AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(initialization_vector);
                ServerSocket serverSocket;            
                Socket clientSocket;
                DataInputStream inputStream;
                DataOutputStream outputStream;
                String message;

                /*
                 * create socket server, accept client, preparing input stream
                 * receive message, and print to screen
                 */
                serverSocket = new ServerSocket(5000);
                clientSocket = serverSocket.accept();
                inputStream = new DataInputStream(clientSocket.getInputStream());

                message = inputStream.readUTF();
                System.out.println("client" + message);
                
                // set decryption mode
                decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
                decrypt.init(Cipher.DECRYPT_MODE, secret_key, alogrithm_specs);
                // decrypt file
                decrypt(new FileInputStream(message), new FileOutputStream(decryptedFile));
                System.out.println("End of Encryption/Decryption procedure!");
                

                /*
                 * preparing output stream, send message back to client
                 */
                outputStream = new DataOutputStream(clientSocket.getOutputStream());
                outputStream.writeUTF(message);

                /*
                 * close input stream, output stream
                 * close client socket and server socket
                 */
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                serverSocket.close();
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
                System.out.println("Listen: " + e.getMessage());
                e.printStackTrace();
        }
    }
   private static void decrypt(InputStream input, OutputStream output)throws IOException {
        input = new CipherInputStream(input, decrypt);
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
