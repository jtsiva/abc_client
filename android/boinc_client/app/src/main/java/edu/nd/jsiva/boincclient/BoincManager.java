package edu.nd.jsiva.boincclient;

/**
 * Created by Josh Siva on 10/11/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
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
        try {
            Process nativeApp = Runtime.getRuntime().exec(thingToRun);

            BufferedReader reader = new BufferedReader(new InputStreamReader(nativeApp.getInputStream()));
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
            }

            nativeOutput = output.toString();
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
        copyFile ("armeabi-v7a/boinc_client", "/data/data/edu.nd.jsiva.boincclient/boinc_client");
        run_cmd (true, "/system/bin/chmod 777 /data/data/edu.nd.jsiva.boincclient/boinc_client");
        copyFile ("armeabi-v7a/boinccmd", "/data/data/edu.nd.jsiva.boincclient/boinccmd");
        run_cmd (true, "/system/bin/chmod 777 /data/data/edu.nd.jsiva.boincclient/boinccmd");
    }

    public String start ()
    {
        return run_cmd(true, "/system/bin/ls -l /data/data/edu.nd.jsiva.boincclient/");//"/data/data/edu.nd.jsiva.boincclient/boinc_client --help");
    }

    public String check ()
    {
        return run_cmd(true, "/data/data/edu.nd.jsiva.boincclient/boinccmd --help 2>&1");
    }

    //exec client

    //lots o functions for interacting with client
}
