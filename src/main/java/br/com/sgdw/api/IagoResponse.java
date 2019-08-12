package br.com.sgdw.api;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import br.com.sgdw.service.exception.IagoException;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.SystemMsg;

public class IagoResponse {
	
	static final Logger log = Logger.getLogger(IagoResponse.class);
	
	private static final String CONTENTDISPOSION = "Content-Disposition";
	private static final String ATTACHMENT = "attachment; filename=\"";
	private static final String CSVHEADER = ".csv\"";
	
	private IagoResponse() {
		
	}

    public static void setResponse(HttpServletResponse response, IagoException e){
    	try {
			response.sendError(e.getStatus(), e.getMessage());
		} catch (IOException e1) {
			log.error(e1);
		}
    }
    
    public static void getResponseDownload(String format, HttpServletResponse response, String collection, InputStream stream){
		response.setHeader(CONTENTDISPOSION, ATTACHMENT +collection+"."+format+"\"");
		try {
			IOUtils.copy(stream, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			log.error(SystemMsg.OPEN_FILE_ERROR.valor(), e);
		}
	}
    
    public static void getResponse(String format, HttpServletResponse response, String collection, InputStream is){
		Formats formatEnum = Formats.getFormat(format);

		if(response != null) {
			
			switch(formatEnum){
			
			case JSON: 
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					break;

			case CSV: 
					response.setHeader(CONTENTDISPOSION, ATTACHMENT +collection+ CSVHEADER);
					break;

			case XML:
					response.setContentType(MediaType.APPLICATION_XML_VALUE);
					break;

			default:
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					break;	
			}
			
			if(is != null) {
				try {
					IOUtils.copy(is, response.getOutputStream());
				} catch (IOException e) {
					log.error(SystemMsg.OPEN_FILE_ERROR.valor(), e);
				}
			}
		}
	}
}
