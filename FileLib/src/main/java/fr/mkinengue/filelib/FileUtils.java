package fr.mkinengue.filelib;

import java.io.File;

/**
 * Classe statique d'utilitaires de gestion de fichiers
 * 
 * @author MKINENGU
 * 
 */
public final class FileUtils {

	/**
	 * Constructeur privé pour empêcher l'instanciation de la classe
	 */
	private FileUtils() {
	}

	/**
	 * Supprimer dans le répertoire donné par dossier le fichier ou répertoire de nom nomFichierASupprimer
	 * 
	 * @param dossier
	 * @param nomFichierASupprimer
	 * @return true/false
	 */
	public static boolean supprimerFichier(File dossier, String nomFichierASupprimer) {
		if ((nomFichierASupprimer == null) || nomFichierASupprimer.trim().isEmpty()) {
			throw new RuntimeException("Le nom du fichier à supprimer doit être non nul et non vide");
		}

		if (dossier == null) {
			return false;
		} else if (!dossier.isDirectory()) {
			return false;
		} else if (!dossier.canWrite()) {
			throw new RuntimeException(
					"Le répertoire contenant probablement le fichier à supprimer n'est pas accessible. Répertoire : "
							+ dossier.getPath());
		}

		File[] listFiles = dossier.listFiles();
		File aSupprimer = null;
		for (File f : listFiles) {
			if (nomFichierASupprimer.equalsIgnoreCase(f.getName())) {
				aSupprimer = f;
				break;
			}
		}

		boolean deleted = false;
		if (aSupprimer != null) {
			deleted = org.apache.commons.io.FileUtils.deleteQuietly(aSupprimer);
		}
		return deleted;
	}
}
