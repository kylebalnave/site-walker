/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.balnave.io;

import com.balnave.rambler.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author kyleb2
 */
public class GZip {

    /**
     * GZips a file
     *
     * @param fileIn
     * @param fileOut
     * @param deleteIn
     * @return
     */
    public static boolean zip(String fileIn, String fileOut, boolean deleteIn) {
        byte[] buffer = new byte[1024];
        GZIPOutputStream gzos = null;
        boolean success = true;
        try {
            gzos = new GZIPOutputStream(new FileOutputStream(fileOut));
            FileInputStream in = new FileInputStream(fileIn);
            int len;
            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
            }
            in.close();
        } catch (IOException ex) {
            Logger.log(ex.getMessage(), Logger.WARNING);
            success = false;
        } finally {
            try {
                if (gzos != null) {
                    gzos.finish();
                    gzos.close();
                }
            } catch (IOException ex) {
                Logger.log(ex.getMessage(), Logger.WARNING);
                success = false;
            }
            if (deleteIn) {
                new File(fileIn).delete();
            }
        }
        return success;
    }
}
