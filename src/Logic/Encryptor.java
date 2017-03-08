package Logic;


import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Encryptor {
  File file;
  SecretKeySpec secretKeySpec;
  public void setFile(String filePath) throws Exception {
    this.file = new File(filePath);
    if(!file.isFile()){
      throw new Exception("The file you choosed is not valid");
    }
  }
  public void setKey(String keyword){
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      sha.update(keyword.getBytes("UTF-8"));
      byte[] key = sha.digest();
      secretKeySpec = new SecretKeySpec(key, "AES");
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  public void encrypt(){
    byte[] bFile = new byte[(int) file.length()];
    try {
      //adding portocol bytes to the file bytes
      //String portcol = "encryptor portocol";
      //byte[] decPortocol = portcol.getBytes();

      //convert file into array of bytes
      BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
      bufferedInputStream.read(bFile);
      bufferedInputStream.close();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      //outputStream.write(decPortocol);
      outputStream.write(bFile);

      byte[] cryptedFileBytes = outputStream.toByteArray();
      //Cipher and encrypting
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] encryptedBytes = cipher.doFinal(cryptedFileBytes);

      //Write Encrypted File
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file,false));
      bufferedOutputStream.write(encryptedBytes);
      bufferedOutputStream.flush();
      bufferedOutputStream.close();
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  public BufferedImage decryptImage(BufferedImage input){
        
        BufferedImage img = null;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(input, "png", baos);
            byte[] rawImage = baos.toByteArray();
            
            byte k[] = "NiTh5252".getBytes();
            SecretKeySpec key = new SecretKeySpec(k, "DES");
            Cipher dec = Cipher.getInstance("DES");
            dec.init(Cipher.DECRYPT_MODE, key);
            ByteArrayOutputStream decImage = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(decImage,dec);
            cos.write(rawImage);
//            byte[] buf = new byte[1024];
//            int read;
////            while((read=rawImage.read(buf))!=-1){
////                cos.write(buf,0,read);
////            }
            cos.close();
            byte[] data = decImage.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            img = ImageIO.read(bais);
            
            
        }
        catch(Exception e){JOptionPane.showMessageDialog(null,e);}
        
        return img;
    }

}