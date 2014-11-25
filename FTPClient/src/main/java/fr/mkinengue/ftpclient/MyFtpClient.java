package fr.mkinengue.ftpclient;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class MyFtpClient {

	public static void main(String[] args) {
		final FTPClient ftp = new FTPClient();
		// final FTPClientConfig config = new FTPClientConfig();
		final String username = "freebox";
		final String password = "xxI188EY";
		// config.setXXX(YYY); // change required options
		// ftp.configure(config );
		boolean error = false;
		try {
			int reply;
			final String server = "hd1.freebox.fr";
			ftp.connect(server, 21);
			// System.out.println("Connected to " + server + ".");
			ftp.login(username, password);
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify
			// success.
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			// ... // transfer files
			for (final FTPFile ftpFile : ftp.listDirectories("/")) {
				System.out.println("File : " + ftpFile.getName());
			}
			for (final String fileName : ftp.listNames("/")) {
				System.out.println("Name : " + fileName);
			}

			String fileName;
			fileName = "E:\\VIDEO\\Mandela Multi 1080p.mkv";
			fileName = "E:\\VIDEO\\Sculp & tone.avi";
			final FileInputStream in = new FileInputStream(fileName);
			final boolean stored = ftp.storeUniqueFile(in);
			if (!stored) {
				System.err.println("File not stored");
			}

			ftp.logout();
		} catch (final IOException e) {
			error = true;
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.logout();
					ftp.disconnect();
				} catch (final IOException ioe) {
					// do nothing
				}
			}
			System.exit(error ? 1 : 0);
		}
	}
}
