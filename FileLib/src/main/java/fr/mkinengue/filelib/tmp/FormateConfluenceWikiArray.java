/*
 * Copyright (c) 2011, Eurostar - www.eurostar.com All rights reserved. Usage of this software, in source or binary form, partly
 * or in full, and of any application developed with this software, is restricted to the customer.s employees in accordance with
 * the terms of the agreement signed with Eurostar.
 */
package fr.mkinengue.filelib.tmp;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marcel_Kinengue
 */
public class FormateConfluenceWikiArray {

    private static final String INPUT_FILE_NAME = "src/main/java/temp.xml";

    private static final String XSLT_MAPPING_INPUT_FILE_NAME = "src/main/java/mapping_input_wdi.xsl";

    private static final String XSLT_MAPPING_OUTPUT_FILE_NAME = "src/main/java/mapping_output_wdi.xsl";

    private static final String TEMP_TEXT_FILE_NAME = "src/main/java/temp.txt";

    private static final String OUTPUT_FILE_NAME = "src/main/java/outputFile.txt";

    private static final String REGEX_TO_DEL1 = "[a-zA-Z]+[0-9]*:";

    private static final String REGEX_TO_DEL2 = "^[\\s]{2}\\|[\\s]/";

    private static final Set<String> LIST_TO_DELETE = new HashSet<String>();
    static {
        LIST_TO_DELETE.add("/Envelope/Body/getQuotationResponse/out/");
        LIST_TO_DELETE.add("/Envelope/Body/getQuotation/in0/");
        LIST_TO_DELETE.add("/Envelope/Body/cancel/in0/");
        LIST_TO_DELETE.add("/Envelope/Body/cancelResponse/out/");
        LIST_TO_DELETE.add("/Envelope/Body/finalize/in0/");
        LIST_TO_DELETE.add("/Envelope/Body/finalizeResponse/out/");
        LIST_TO_DELETE.add("/Envelope/Body/book/in0/");
        LIST_TO_DELETE.add("/Envelope/Body/bookResponse/out/");
        LIST_TO_DELETE.add("/Envelope/Body/searchPnrResponse/SearchPnrOutput/");
    }

    private static final Map<String, String> MAP_REPLACEMENT_BY_REGEX = new HashMap<String, String>();
    static {
        MAP_REPLACEMENT_BY_REGEX.put(REGEX_TO_DEL1, "");
        MAP_REPLACEMENT_BY_REGEX.put(REGEX_TO_DEL2, "| /");
    }

    private static final Map<String, String> MAP_CHAINES_ATTENDUES = new HashMap<String, String>();
    static {
        MAP_CHAINES_ATTENDUES.put("LinkedMicrocondition", "microconditions");
        MAP_CHAINES_ATTENDUES.put("BsgInformations", "bsgsInformations");
        MAP_CHAINES_ATTENDUES.put("BookedApPnr", "bookedApPnr");
        MAP_CHAINES_ATTENDUES.put("History", "histories");
        MAP_CHAINES_ATTENDUES.put("BasicTelephone", "telephones");
        MAP_CHAINES_ATTENDUES.put("BookedPassenger", "passengers");
        MAP_CHAINES_ATTENDUES.put("PnrIdentification", "relatedPnrIdentifications");
        MAP_CHAINES_ATTENDUES.put("QuoteResultByTCN", "quoteResultByTCN");
        MAP_CHAINES_ATTENDUES.put("QuoteResultByTCN", "quoteByTCN");
        MAP_CHAINES_ATTENDUES.put("BookedPnr", "pnrs");
        MAP_CHAINES_ATTENDUES.put("MicroCondition", "linkedMicroConditions");
    }

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

    /**
     * Supprime les occurences des éléments de la liste LIST_TO_DELETE dans la châine de caractère str
     * 
     * @param str
     * @return String : chaîne épurée
     */
    private static String deleteStrings(final String str) {
        if (str == null) {
            return null;
        }
        String ret = str;
        for (final String delElt : LIST_TO_DELETE) {
            ret = ret.replaceAll(delElt, "");
        }
        return ret;
    }

    /**
     * Returns a list of Strings that verify the regex regex in the text text
     * 
     * @param text
     * @param regex
     * @param replacement
     * @return String
     */
    private static String getTextReplaced(final String text, final String regex, final String replacement) {
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(text);
        return m.replaceAll(replacement);
    }

    /************************************************************************************
     * Codes de formatage pour ajout des index de liste dans les xpath
     ************************************************************************************/

    private static final String[] MAP_INDEX = new String[] { "i", "j", "k", "l", "m", "n", "o", "p", "q" };

    private static final Set<String> LIST_STRING = new HashSet<String>();
    static {
        LIST_STRING.add("int");
        LIST_STRING.add("string");
        LIST_STRING.add("LocalityCode");
    }

    private static boolean isPluriel(final String str1, final String pluriel) {
        if (str1 == null || pluriel == null) {
            return false;
        }
        return (str1 + "s").toLowerCase().equals(pluriel.toLowerCase())
                || (str1 + "es").toLowerCase().equals(pluriel.toLowerCase());
    }

    private static String getIndex(final int idx) {
        return "\\[" + MAP_INDEX[idx] + "\\]";
    }

    private static String ajouteIndex(final String str) {
        if (str == null) {
            return null;
        }

        String ret = "";
        final String[] split = str.split("/");
        final int nbFld = split.length;
        int idx = 0;
        int cptFld = 0;
        for (final String fld : split) {
            // Le champ courant est dans la map des chaînes de caractères attendus pour pluriel
            if ((cptFld > 0) && (MAP_CHAINES_ATTENDUES.get(fld) != null)
                    && MAP_CHAINES_ATTENDUES.get(fld).equals(split[cptFld - 1])) {
                // Chaine attendue trouvée, on ajoute l'index à la prochaine itération
                ret = ret + fld + getIndex(idx);
                idx++;
            }
            // On vérifie si le champ courant est pluriel du champ précédent sauf pour le premier élément
            else if ((cptFld > 0) && isPluriel(fld, split[cptFld - 1])) {
                // On ajoute un index au champ et on augmente l'index
                ret = ret + fld + getIndex(idx);
                idx++;
            }
            // On vérifie que le champ courant est l'une des chaînes de la liste
            else if (LIST_STRING.contains(fld)) {
                // On ajoute un index et on augmente l'index
                ret = ret + fld + getIndex(idx);
                idx++;
            }
            // Aucune particularité,, on ajoute le champ courant dans la chaîne finale
            else {
                ret = ret + fld;
            }

            cptFld++;
            if (cptFld < nbFld) {
                // Le compteur de champs traités, additionné de un, est encore strictement inférieur au nombre total
                // d'éléments
                ret = ret + "/";
            }
        }

        return ret;
    }

    /**
     * @param row
     *            expected format of the row => | xxx | | |
     * @return String
     */
    private static String formateRowAndAddIndex(final String row) {
        if (row == null) {
            return null;
        }
        final String[] rowsplit = row.split("\\|");
        String indexAdded = "";
        boolean treated = false;
        for (final String s : rowsplit) {
            if (!treated && !s.trim().isEmpty()) {
                indexAdded = ajouteIndex(s.trim());
                treated = true;
            }
        }
        final StringBuilder ret = new StringBuilder("| ").append(indexAdded).append(" |");
        for (int i = 1; i < rowsplit.length - 1; i++) {
            ret.append(" |");
        }
        return ret.toString();
    }

    /************************************************************************************
     * Transformation XSLT
     ************************************************************************************/

    /**
     * From http://java.developpez.com/faq/xml/?page=xslt#creerHtmlXslt<br />
     * Pour transformer en utilisant XSLT
     * 
     * @param xml
     * @param xsl
     * @param text
     */
    private static void creerText(final String xml, final String xsl, final String text) {
        // Création de la source DOM
        final DocumentBuilderFactory fabriqueD = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructeur;
        try {
            constructeur = fabriqueD.newDocumentBuilder();
            final File fileXml = new File(xml);
            final Document document = constructeur.parse(fileXml);
            final Source source = new DOMSource(document);

            // Création du fichier de sortie
            final File fileText = new File(text);
            final Result resultat = new StreamResult(fileText);

            // Configuration du transformer
            final TransformerFactory fabriqueT = TransformerFactory.newInstance();
            final StreamSource stylesource = new StreamSource(xsl);
            final Transformer transformer = fabriqueT.newTransformer(stylesource);
            transformer.setOutputProperty(OutputKeys.METHOD, "text");

            // Transformation
            transformer.transform(source, resultat);
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copierFichier(final String source, final String dest) {
        try {
            final File fSrc = new File(source);
            if (!fSrc.exists()) {
                throw new RuntimeException("Le fichier " + source + " n'existe pas");
            } else if (!fSrc.isFile()) {
                throw new RuntimeException(source + " n'est pas un fichier");
            }

            final File fDst = new File(dest);
            if (!fDst.exists()) {
                // Création du ficheir de destination
                fDst.createNewFile();
            }

            FileUtils.copyFile(fSrc, fDst);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /************************************************************************************
     * Exécution du main
     ************************************************************************************/

    /**
     * {@inheritDoc}
     */
    public static void main(final String[] args) {
        InputStream in = null;
        Writer out = null;
        final boolean isInput = false;
        final long debut = System.currentTimeMillis();
        final String destDirName = "C:\\Documents and Settings\\Marcel_Kinengue\\Mes documents\\VSCT-ERSTAR\\Developpeurs";
        final String dstFileNameInput = "re_book_input.txt";
        final String dstFileNameOutput = "ase_getQuotation_output.txt";
        String dstFileName = "";
        try {
            // Parsing du fichier XML pour transformation en fichier text via XSLT
            if (isInput) {
                creerText(INPUT_FILE_NAME, XSLT_MAPPING_INPUT_FILE_NAME, TEMP_TEXT_FILE_NAME);
                dstFileName = dstFileNameInput;
            } else {
                creerText(INPUT_FILE_NAME, XSLT_MAPPING_OUTPUT_FILE_NAME, TEMP_TEXT_FILE_NAME);
                dstFileName = dstFileNameOutput;
            }
            System.out.println((System.currentTimeMillis() - debut) + " ms pour transfomration XSLT");

            // Opening the file to write in
            out = new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE_NAME), "utf-8");
            // Création du fichier de sortie
            final File f = new File(OUTPUT_FILE_NAME);
            f.delete();
            f.createNewFile();

            in = new FileInputStream(TEMP_TEXT_FILE_NAME);
            final DataInputStream dat = new DataInputStream(in);
            final BufferedReader br = new BufferedReader(new InputStreamReader(dat));
            String strLine = null;
            boolean firstLine = true;
            try {
                // Read File Line By Line and replacements
                while ((strLine = br.readLine()) != null) {
                    String newText = deleteStrings(strLine);
                    if (newText != null && !newText.trim().isEmpty()) {
                        if (!firstLine) {
                            newText = getTextReplaced(newText, REGEX_TO_DEL1,
                                    MAP_REPLACEMENT_BY_REGEX.get(REGEX_TO_DEL1));
                            newText = getTextReplaced(newText, REGEX_TO_DEL2,
                                    MAP_REPLACEMENT_BY_REGEX.get(REGEX_TO_DEL2));
                            newText = formateRowAndAddIndex(newText);
                        } else {
                            firstLine = false;
                        }
                        out.write(newText);
                        out.write("\n");
                    }
                }
            } finally {
                out.flush();
            }

            // Création du dossier de destination si non existant
            if (!(new File(destDirName)).exists()) {
                (new File(destDirName)).mkdirs();
            }
            // Copie du fichier créé dans le fichier de destination
            copierFichier(OUTPUT_FILE_NAME, destDirName + File.separator + dstFileName);

            System.out.println("Traitements effectués en " + (System.currentTimeMillis() - debut) + " ms");
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
}
