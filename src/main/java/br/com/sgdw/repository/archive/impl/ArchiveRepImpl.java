package br.com.sgdw.repository.archive.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.util.IagoCfg;
import br.com.sgdw.util.constantes.SystemMsg;

@Repository
public class ArchiveRepImpl implements ArchiveRep{
	
	static final Logger log = Logger.getLogger(ArchiveRepImpl.class); 
		
	@Override
	public InputStream readArchive(String datasetName, String fileName) throws SftpException, IOException{
		File file;
		InputStream out = null;
		String remoteFile = IagoCfg.getPath();
		
		if(IagoCfg.getIsSsh()){
			ChannelSftp channel;
			
			try {
				channel = initChanel();
				channel.connect();
				log.info("Navegando para "+ remoteFile);
				channel.cd(remoteFile);
				log.info("Navegando para "+ datasetName);
				channel.cd(datasetName);
				log.info("Buscando arquivo "+ fileName);
				out = channel.get(fileName);
			} catch (JSchException e) {
				log.error(e);
			}
			
		}else{
			verifyDir();
			file = new File(remoteFile+"/" +datasetName + "/" + fileName);
			out = FileUtils.openInputStream(file);
		}
		
		return out;
		
	}
	
	@Override 
	public void removeFolder(String datasetName) {
		File file;
		String remoteFile = IagoCfg.getPath();
		
		if(IagoCfg.getIsSsh()) {
			ChannelSftp channel;
			
			try {
				channel = initChanel();
				channel.connect();
				removeFolderFiles(channel, datasetName);
				channel.cd(remoteFile);
				channel.rmdir(datasetName);
			} catch (JSchException | SftpException e) {
				log.error(e);
			}
			
		}else {
			file = new File(remoteFile+"/"+datasetName);
			FileUtils.deleteQuietly(file);
		}
		
	}
	
	private void removeFolderFiles(ChannelSftp channel, String datasetName) {
		String remoteFile = IagoCfg.getPath();
		
		try {
			channel.cd(remoteFile);
			channel.cd(datasetName);
			List<?> fileList = channel.ls(remoteFile+"/"+datasetName);
			for(int i=0; i<fileList.size(); i++) {
				LsEntry entry = (LsEntry) fileList.get(i);
				channel.rm(entry.getFilename());
			}
			
		} catch (SftpException e) {
			log.error(e);
		}
	}
	
	@Override
	public void writeArchive(String datasetName, List<String> filenames) throws SftpException, IOException{
		if(IagoCfg.getIsSsh()){
			this.writeArchiveSSH(datasetName, filenames);
		}else{
			this.writeArchiveLocal(datasetName, filenames);
		}
	}
	
	private void writeArchiveLocal(String datasetName, List<String> filenames) throws IOException{
		verifyDir();
		
		File datasetFolder = new File(IagoCfg.getPath()+"/"+datasetName);
		if(!datasetFolder.exists()) {
			datasetFolder.mkdir();
		}
			
		for(String datasetFile : filenames){
			File to = new File(IagoCfg.getTempPath()+"/"+datasetFile);
			to.renameTo(new File(datasetFolder.getPath()+"/"+datasetFile));
		}
	}
	
	private void writeArchiveSSH(String datasetName, List<String> filenames) throws IOException, SftpException {		
		ChannelSftp channel;
		try {
			channel = initChanel();
			channel.connect();
			navigateSSH(datasetName, channel);
			
			for(String datasetFile : filenames){
				log.info("Transferindo " + datasetFile);
				
				File file = new File(IagoCfg.getTempPath()+"/"+datasetFile);
				channel.put(new FileInputStream(file), datasetFile);
				file.delete();
				
				log.info("Concluido");
			}
			channel.exit();
			channel.getSession().disconnect();
		} catch (JSchException e) {
			log.error(e);
		}
	}
	
	private void verifyDir(){
		File dir = new File(IagoCfg.getPath());
		if(!dir.exists()) {
			dir.mkdir();
		}		
	}
		
	private void navigateSSH(String dir, ChannelSftp channel) throws SftpException{
		
		try{
			channel.stat(IagoCfg.getPath()+"/"+dir);
		}catch (SftpException e) {
			log.error(e);
			
			channel.cd(IagoCfg.getPath());
			channel.mkdir(dir);
			channel.cd(dir);
		}
		
	}
	
	@Override
	public void createTempFiles(String filename, String content){
		try{
			if(new File(IagoCfg.getTempPath()+"/"+filename).exists()){
				Files.write(Paths.get(IagoCfg.getTempPath()+"/"+filename), content.getBytes(), StandardOpenOption.APPEND);
			}else{
				Files.write(Paths.get(IagoCfg.getTempPath()+"/"+filename), content.getBytes(), StandardOpenOption.CREATE_NEW);
			}
		}catch (IOException e) {
			log.error(SystemMsg.WRITE_FILE_ERROR.valor(), e);
		}	
	}
	
	private ChannelSftp initChanel() throws JSchException{
		ChannelSftp channel;

		Properties properties = new Properties();
		properties.setProperty("StrictHostKeyChecking", "no");

		JSch jsch = new JSch();
		Session session = jsch.getSession(IagoCfg.getUsername(), IagoCfg.getHost(), IagoCfg.getPort());
		session.setPassword(IagoCfg.getPassword());
		session.setConfig(properties);
		session.connect();
		log.info("session is alive:" + session.isConnected());

		channel = (ChannelSftp) session.openChannel("sftp");

		return channel;
	}
}
