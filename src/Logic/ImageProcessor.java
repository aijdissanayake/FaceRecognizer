/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Achala PC
 */
public class ImageProcessor {
    
    public void saveImage(ByteArrayOutputStream img, String filePath)
    {
        try {
//            System.out.println("Creating file");
//            File outputfile = new File(filePath);
//            System.out.println("saving File");
//            ImageIO.write(img, "png", outputfile);
                byte[] data = img.toByteArray();
                FileOutputStream encImage = new FileOutputStream(filePath);
                encImage.write(data);
            } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }    
    }
    
    public FileInputStream loadImage(String filePath)
    {
        FileInputStream img = null;
        try 
        {
        System.out.println("setting path");
        img = new FileInputStream(filePath);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return img;
    }
    
    public  boolean compareImages(File userFile, BufferedImage authImage) {        
    try {
        // take buffer data from userinput file and create the data buffer 
        BufferedImage userImage = ImageIO.read(userFile);
        DataBuffer userImageDB = userImage.getData().getDataBuffer();
        int userImageSize = userImageDB.getSize();
        // take buffer data from saved file and create the data buffer 
        DataBuffer authImageDB = authImage.getData().getDataBuffer();
        int authImageSize = authImageDB.getSize();
        // compare data-buffers of the two Images
        if(userImageSize == authImageSize) {
            for(int i=0; i<userImageSize; i++) { 
                if(userImageDB.getElem(i) != authImageDB.getElem(i)) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    } 
    catch (Exception e) { 
        System.out.println("Failed to compare image files ...");
        return  false;
    }
}
    public ByteArrayOutputStream encryptImage(FileInputStream rawImage){
//        BufferedImage img = null;
        ByteArrayOutputStream  encImage = null;
        try
        {
        //setting up encryption method,key etc.
        byte k[] = "NiTh5252".getBytes();
        SecretKeySpec key = new SecretKeySpec(k, "DES");
        Cipher enc = Cipher.getInstance("DES");
        enc.init(Cipher.ENCRYPT_MODE, key);
        //create Byte Array to hold the encrypted byte stream
        encImage = new ByteArrayOutputStream();
        //Byte array is filled by Cipher output stream while encrypting using enc 
        CipherOutputStream cos = new CipherOutputStream(encImage,enc);
        byte[] buf = new byte[1024];
        int read;
        while((read=rawImage.read(buf))!=-1){
            cos.write(buf,0,read);
        }
        //close streams and file
        rawImage.close();
        cos.close();
        //get the output Byte Array  stream to a byte array to cretae a buffered image
//        byte[] data = encImage.toByteArray();
//            ByteArrayInputStream input = new ByteArrayInputStream(data);
//            img = ImageIO.read(input);
        }
        catch(Exception e){JOptionPane.showMessageDialog(null,e);}
        return encImage;
    }
    public BufferedImage decryptImage(FileInputStream rawImage){
        
        BufferedImage img = null;
        try{
            //same process as encryptImage
            byte k[] = "NiTh5252".getBytes();
            SecretKeySpec key = new SecretKeySpec(k, "DES");
            Cipher dec = Cipher.getInstance("DES");
            dec.init(Cipher.DECRYPT_MODE, key);
            ByteArrayOutputStream decImage = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(decImage,dec);
            byte[] buf = new byte[1024];
            int read;
            while((read=rawImage.read(buf))!=-1){
                cos.write(buf,0,read);
            }
            rawImage.close();
            decImage.flush();
            cos.close();
            byte[] data = decImage.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            img = ImageIO.read(input);
            
            
        }
        catch(Exception e){JOptionPane.showMessageDialog(null,e);}
        
        return img;
    }
}
