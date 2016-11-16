package edu.nd.jsiva.boincclient;

/**
 * Created by Josh Siva on 10/11/2016.
 */

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BoincManager {

    static Context thisContext;

    //util
    private static void copyFile(String assetPath, String localPath) {
        try {
            InputStream in = thisContext.getAssets().open(assetPath);
            FileOutputStream out = new FileOutputStream(localPath);
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String run_cmd(boolean wait, String thingToRun)
    {
        String nativeOutput = new String("");
        Process nativeApp;
        try {
            nativeApp = Runtime.getRuntime().exec(thingToRun);


            BufferedReader reader = new BufferedReader(new InputStreamReader(nativeApp.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(nativeApp.getErrorStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            if (wait) {
                nativeApp.waitFor();
                nativeOutput = Integer.toString(nativeApp.exitValue());
                if (0 != nativeApp.exitValue())
                {
                    while ((read = errReader.read(buffer)) > 0) {
                        output.append(buffer, 0, read);
                    }
                    errReader.close();
                }
            }

            nativeOutput += "> " + output.toString();
            Log.v("abc", nativeOutput);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return nativeOutput;
    }

    public BoincManager (Context context)
    {
        thisContext = context;
    }

    public void init ()
    {
       // copyFile ("libs/librebound.so", "/data/data/edu.nd.jsiva.boincclient/librebound.so");
        //run_cmd (true, false, "/system/bin/chmod 777 /data/data/edu.nd.jsiva.boincclient/librebound.so");
        copyFile ("rebound", "/data/data/edu.nd.jsiva.boincclient/rebound");
        run_cmd (true, "/system/bin/chmod 755 /data/data/edu.nd.jsiva.boincclient/rebound");
        copyFile ("sample.txt", "/data/data/edu.nd.jsiva.boincclient/sample.txt");
        run_cmd (true, "/system/bin/chmod 755 /data/data/edu.nd.jsiva.boincclient/sample.txt");
    }

    public String start ()
    {
        return run_cmd(true, "/data/data/edu.nd.jsiva.boincclient/rebound /data/data/edu.nd.jsiva.boincclient");
    }

    public String check ()
    {
        return run_cmd(true, "ls -l /data/data/edu.nd.jsiva.boincclient/");//res*");
    }

    //exec client

    //lots o functions for interacting with client
}
