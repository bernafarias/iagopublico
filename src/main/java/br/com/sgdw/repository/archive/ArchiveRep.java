package br.com.sgdw.repository.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public interface ArchiveRep {

	/**
	 * @author Wilker
	 * @param datasetName
	 * @param fileName
	 * @return
	 * @throws JSchException
	 * @throws SftpException
	 * @throws IOException
	 */
	
	InputStream readArchive(String datasetName, String fileName) throws SftpException, IOException;
	
	/**
	 * @author Wilker
	 * @param datasetName
	 * @param filaname
	 * @param content
	 * @throws JSchException
	 */
	
	void writeArchive(String datasetName, List<String> filenames) throws SftpException, IOException;
	
	void createTempFiles(String filename, String content);

	void removeFolder(String datasetName);
	
}
