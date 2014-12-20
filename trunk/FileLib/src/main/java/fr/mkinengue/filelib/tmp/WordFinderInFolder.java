/*
 * Copyright (c) 2011, Eurostar - www.eurostar.com All rights reserved. Usage of this software, in source or binary form, partly
 * or in full, and of any application developed with this software, is restricted to the customer.s employees in accordance with
 * the terms of the agreement signed with Eurostar.
 */
package fr.mkinengue.filelib.tmp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marcel_Kinengue
 */
public class WordFinderInFolder {

    private static final Set<String> FOLDERS_TO_READ_IN_PATH = new HashSet<String>();
    static {
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Logs\\Rec21SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Logs\\Rec22SBE");

        FOLDERS_TO_READ_IN_PATH
                .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2538\\Rec31");
        FOLDERS_TO_READ_IN_PATH
                .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2538\\Rec32");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMANH23SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMANH24SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMANH25SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMATH21SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMATH22SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMATH23SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMATH24SBE");
        // FOLDERS_TO_READ_IN_PATH
        // .add("C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2066\\NERMATH25SBE");
    }

    private static final String PATH_TO_SYNTHESE_FILE = "C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\Dropbox\\VSCT-ERS\\Jiras\\webtiers\\DSS-2538";

    /**
     * Close the InputStream in parameter
     * 
     * @param in
     */
    private static final void close(final InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Close the Writer in parameter
     * 
     * @param out
     */
    private static final void close(final Writer out) {
        if (out != null) {
            try {
                out.close();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String findString(final String needle, final String synthFileName) {
        final StringBuffer ret = new StringBuffer();
        InputStream in = null;
        Writer out = null;
        final long debut = System.currentTimeMillis();
        final String pathToSyntheseFile = PATH_TO_SYNTHESE_FILE + "\\" + synthFileName;
        try {
            // Création du fichier de sortie
            final File f = new File(pathToSyntheseFile);
            f.delete();
            final boolean createdFile = f.createNewFile();
            final String newLine = "\r\n";
            if (createdFile) {
                final String s = "File " + pathToSyntheseFile + " successfully created";
                ret.append(s).append(newLine);
                System.out.println(s);
            } else {
                final String s = "File " + pathToSyntheseFile + " already existed";
                ret.append(s).append(newLine);
                System.out.println(s);
            }
            // Opening the file to write in
            out = new OutputStreamWriter(new FileOutputStream(pathToSyntheseFile), "utf-8");

            File folder = null;
            for (final String folderToReadIn : FOLDERS_TO_READ_IN_PATH) {
                folder = new File(folderToReadIn);
                if (!folder.exists() || !folder.isDirectory()) {
                    // Folder does not exist or the corresponding file is not a folder. Move on to next folder
                    final String display = "Path " + folderToReadIn
                            + " ignored because it does not exist or is not a folder";
                    System.out.println(display);
                    ret.append(display).append(newLine);
                    continue;
                } else if (!folder.canRead()) {
                    final String display = "Folder " + folderToReadIn + " ignored because it cannot be read";
                    System.out.println(display);
                    ret.append(display).append(newLine);
                    continue;
                }

                // Current path was a folder, we check if there are files inside that we will process
                final File[] listFiles = folder.listFiles();
                if (listFiles == null) {
                    final String display = "Files of folder " + folderToReadIn
                            + " ignored because the files could not be retrieved";
                    System.out.println(display);
                    ret.append(display).append(newLine);
                    continue;
                }

                final StringBuffer processedFiles = new StringBuffer();
                boolean matchFound = false;
                for (final File fileFolder : listFiles) {
                    if (!fileFolder.isFile() || !fileFolder.canRead()) {
                        final String display = "File " + fileFolder.getName() + " of folder " + folderToReadIn
                                + " ignored because it does not exist or is not a file or cannot be read";
                        System.out.println(display);
                        ret.append(display).append(newLine);
                        continue;
                    }

                    processedFiles.append(fileFolder.getName()).append(" / ");
                    in = new FileInputStream(fileFolder);
                    final DataInputStream dat = new DataInputStream(in);
                    final BufferedReader br = new BufferedReader(new InputStreamReader(dat));
                    String strLine = null;
                    try {
                        // Read File Line By Line and replacements
                        int lineNumber = 1;
                        final List<Integer> matchLines = new ArrayList<Integer>();
                        while ((strLine = br.readLine()) != null) {
                            if (strLine.contains(needle)) {
                                // The line contains the searched string, we write in the synthese file and display it as well
                                matchLines.add(lineNumber);
                            }
                            lineNumber++;
                        }

                        if (!matchLines.isEmpty()) {
                            final String display = "Match in file " + fileFolder.getPath() + " - lines : " + matchLines;
                            System.out.println(display);
                            ret.append(display).append(newLine);
                        }

                        // Set Match found to true in case a match was found in current file
                        matchFound = matchFound || !matchLines.isEmpty();
                    } finally {
                        close(in);
                    }
                }

                if (!matchFound) {
                    final String display = "No Match found in all processed files";
                    System.out.println(display);
                    ret.append(newLine).append(display).append(newLine);
                }

                final String ss = "Processed files in folder " + folder.getName() + " are : "
                        + processedFiles.toString();
                System.out.println(ss);
                ret.append(ss).append(newLine);
            }

            out.write(ret.toString());
            out.flush();
            System.out.println("Traitements effectués en " + (System.currentTimeMillis() - debut) + " ms");

            return ret.toString();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(in);
            close(out);
            System.out.println("Fin");

        }
    }

    /************************************************************************************
     * Exécution du main
     ************************************************************************************/

    /**
     * {@inheritDoc}
     */
    public static void main(final String[] args) {
        findString("QZPTDF", "QZPTDF_search_results.txt");
        // findString("QLEOTJ", "QLEOTJ_search_results.txt");
        // findString("QLHZHB", "QLHZHB_search_results.txt");
    }
}
