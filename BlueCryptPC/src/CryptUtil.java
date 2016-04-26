import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collection;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FilenameUtils;



/**
 * 
 */

/**
 * @author Owner
 *
 */
public class CryptUtil {
	private static String encryptFolderPath=null;
	private static String keyFolderPath=null;
	private static String passWord=null;
	private static Cipher cipher=null;
	
	
	public CryptUtil(String encryptFolderPath, String keyFolderPath, String passWord) {
		this.encryptFolderPath=encryptFolderPath;
		this.keyFolderPath=keyFolderPath;
		this.passWord=passWord;
		
	}

	public static void generateKey() throws Exception {
		//Salt: used for encoding
		byte [] salt=new byte [8];
		SecureRandom secureRandom=new SecureRandom();
		secureRandom.nextBytes(salt);
		FileOutputStream saltOutputStream=new FileOutputStream(keyFolderPath+"salt.enc");
		saltOutputStream.write(salt);
		saltOutputStream.close();
		
		//AES-256 key specification
		SecretKeyFactory secretKeyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(passWord.toCharArray(), salt, 65536,256);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        
        // IV : Random adds for more secure
        FileOutputStream ivOutFile = new FileOutputStream(keyFolderPath + "iv.enc");
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        ivOutFile.write(iv);
        ivOutFile.close();	
	}
	
	public static void encryptFile() throws Exception {
		Collection<File> treeEncryptionFolder = new ArrayList<>();
        addTree(new File(encryptFolderPath), treeEncryptionFolder);
        for(File file : treeEncryptionFolder) {
            String filename = file.getAbsolutePath();

            String noExtFile = FilenameUtils.removeExtension(filename);
            String extFile = FilenameUtils.getExtension(filename);

            // File streams
            FileInputStream inFile = new FileInputStream(filename);
            FileOutputStream outFile = new FileOutputStream(noExtFile + "AES."+extFile);

            // File encryption
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();

            // Delete File
            String[] parts = file.getAbsolutePath().split(encryptFolderPath);
            if(file.delete()) {
                System.out.println("Encrypted : " + parts[parts.length-1]);
            } else {
                System.err.println("Error : " + parts[parts.length-1]);
            }
        }
		
	}
	
	 public static void loadKey() throws Exception {

	        // Reading Salt file
	        FileInputStream saltFis = new FileInputStream(keyFolderPath + "salt.enc");
	        byte[] salt = new byte[8];
	        saltFis.read(salt);
	        saltFis.close();

	        // Reading IV file
	        FileInputStream ivFis = new FileInputStream(keyFolderPath + "iv.enc");
	        byte[] iv = new byte[16];
	        ivFis.read(iv);
	        ivFis.close();

	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec keySpec = new PBEKeySpec(passWord.toCharArray(), salt, 65536, 256);
	        SecretKey tmp = factory.generateSecret(keySpec);
	        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

	        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
	        
	    }

	 public static void decryptionFile() throws Exception {

	        Collection<File> treeEncryptionFolder = new ArrayList<>();
	        addTree(new File(encryptFolderPath), treeEncryptionFolder);

	        for(File file : treeEncryptionFolder) {
	            String filename = file.getAbsolutePath();

	            String noExtFile = FilenameUtils.removeExtension(filename);
	            String extFile = FilenameUtils.getExtension(filename);

	            String extractFilename = noExtFile.substring(noExtFile.length()-3,noExtFile.length());
	            if(extractFilename.equals("AES")) {

	                // File streams
	                FileInputStream fis = new FileInputStream(filename);
	                FileOutputStream fos = new FileOutputStream(noExtFile.substring(0,noExtFile.length()-3) + "." + extFile);

	                byte[] in = new byte[64];
	                int read;
	                while ((read = fis.read(in)) != -1) {
	                    byte[] output = cipher.update(in, 0, read);
	                    if (output != null)
	                        fos.write(output);
	                }

	                byte[] output = cipher.doFinal();
	                if (output != null)
	                    fos.write(output);

	                fis.close();
	                fos.flush();
	                fos.close();

	                // Delete File
	                String[] parts = file.getAbsolutePath().split(encryptFolderPath);
	                if(file.delete()) {
	                    System.out.println("Decrypted : " + parts[parts.length-1]);
	                } else {
	                    System.err.println("Error : " + parts[parts.length-1]);
	                }
	            }
	        }
	    }
	
	public static void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if(!FilenameUtils.getExtension(child.getAbsolutePath()).equals("")) {
                    all.add(child);
                }
                addTree(child, all);
            }
        }
    }
}
