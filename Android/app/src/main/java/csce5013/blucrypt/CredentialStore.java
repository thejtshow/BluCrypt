package csce5013.blucrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jtdav on 3/5/2016.
 */
public class CredentialStore
{
    private List<Serializable> credentials;
    private MessageDigest md;

    public CredentialStore(boolean load)
    {
        credentials = new ArrayList<>();
        try {
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
                File file = new File("keystore");
                FileInputStream f = new FileInputStream(file);
                ObjectInputStream s = new ObjectInputStream(f);
                credentials = (List<Serializable>)s.readObject();
                s.close();
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
    public int AddCredential(int PIN)
    {
        int success;

        byte[] hashedPIN = Hash(PIN);

        if(credentials.contains(hashedPIN))
        {
            success = 1;
        }
        else
        {
            credentials.add(hashedPIN);
            success = StoreCredentials();
        }

        return success;
    }

    //Authenticate and return what he has access to
    public boolean CheckCredential (int PIN)
    {
        return credentials.contains(Hash(PIN));
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

        File file = new File("keystore");
        FileOutputStream f;

        try {
            f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(credentials);
            s.flush();
            s.close();
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
}
