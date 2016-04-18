package csce5013.blucrypt;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CredentialStore
{
    private List<Serializable> credentials, Keys;
    private MessageDigest md;
    private Context parent;

    public CredentialStore(boolean load, Context parent)
    {
        credentials = new ArrayList<>();
        Keys = new ArrayList<>();
        this.parent = parent;

        try
        {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e)
        {
            System.out.println("Message digest error. Quitting...");
            return;
        }

        if(load)
        {
            try
            {
                File file = new File(this.parent.getFilesDir() + "keystore");
                File file2 = new File(this.parent.getFilesDir() + "RSA hashes");

                FileInputStream f = new FileInputStream(file);
                FileInputStream f2 = new FileInputStream(file2);

                ObjectInputStream s = new ObjectInputStream(f);
                ObjectInputStream s2 = new ObjectInputStream(f2);

                credentials = (List<Serializable>)s.readObject();
                Keys = (List<Serializable>)s2.readObject();

                s.close();
                s2.close();
            } catch (IOException | ClassNotFoundException e)
            {
                System.out.println("Failed to load keystore");
            }
        }
    }

    public boolean HasCredentials()
    {
        return !credentials.isEmpty();
    }

    //0 = success, 1 = PIN already exists
    public byte[] AddCredential(int PIN)
    {
        int success;

        if(!HasCredentials())
        {
            try
            {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);

                KeyPair keys = keyGen.generateKeyPair();

                Keys.add(keys.getPrivate());
                Keys.add(keys.getPublic());
            } catch (NoSuchAlgorithmException e)
            {
                System.out.println("Failed to generate RSA keys");
            }
        }

        if(CheckCredential(PIN))
        {
            success = 1;
        }
        else
        {
            credentials.add(Hash(PIN));
            success = StoreCredentials();
        }

       if (success == 0)
           return Hash(PIN);
        else return null;
    }

    //Authenticate and return what he has access to
    public boolean CheckCredential (int PIN)
    {
        byte[] test = Hash(PIN);
        boolean passed = false;

        for (Serializable cred : credentials)
        {
            if (Arrays.equals((byte[]) cred, test))
            {
                passed = true;
            }
        }

        return passed;
    }

    public byte[] getRSAKey()
    {
        return ((PublicKey) Keys.get(1)).getEncoded();
    }

    //remove a credential from the store
    public int RemoveCredential (int PIN)
    {
        int success = 1;

        if (credentials.contains(Hash(PIN)))
        {
            credentials.remove(Hash(PIN));
            success = 0;
        }

        return success;
    }

    //Store the credentials to file
    private int StoreCredentials(){
        int success = 0;

        File file = new File(parent.getFilesDir() + "keystore");
        File file2 = new File(parent.getFilesDir() + "RSA hashes");
        FileOutputStream f, f2;

        try {
            f = new FileOutputStream(file);
            f2 = new FileOutputStream(file2);

            ObjectOutputStream s = new ObjectOutputStream(f);
            ObjectOutputStream s2 = new ObjectOutputStream(f2);

            s.writeObject(credentials);
            s2.writeObject(Keys);

            s.flush();
            s2.flush();

            s.close();
            s2.close();
        } catch (IOException e) {
            success = 1;
        }

        return success;
    }

    private byte[] Hash(int PIN)
    {
        //update the message digest
        md.update(ByteBuffer.allocate(4).putInt(PIN).array());

        //return the hash
        return md.digest();
    }

    public int ClearMemory()
    {
        credentials = new ArrayList<>();
        return StoreCredentials();
    }

    public byte[] getHash(int PIN)
    {
        return Hash(PIN);
    }

    public byte[] sign(int pin) {
        try {
            Cipher signer = Cipher.getInstance("RSA/ECB/NoPadding");
            signer.init(Cipher.ENCRYPT_MODE, (Key) Keys.get(0));

            return signer.doFinal(Hash(pin));
        } catch (Exception e) {
            return null;
        }
    }
}
