package org.tbwork.anole.loader.core.impl;
 
import java.net.MalformedURLException;
import java.net.URL;

import org.tbwork.anole.loader.core.Anole;
import org.tbwork.anole.loader.core.ConfigManager;
import org.tbwork.anole.loader.exceptions.OperationNotSupportedException;
import org.tbwork.anole.loader.util.AnoleLogger;
import org.tbwork.anole.loader.util.ProjectUtil;
import org.tbwork.anole.loader.util.StringUtil;
import org.tbwork.anole.loader.util.AnoleLogger.LogLevel; 

public class AnoleClasspathLoader extends AnoleFileSystemLoader{ 
	
	private AnoleLogger logger;
			
	private AnoleConfigFileParser acfParser = AnoleConfigFileParser.instance(); 
	  
	public AnoleClasspathLoader(){
		super();
	}
	
	public AnoleClasspathLoader(ConfigManager cm){
		super(cm);
	}
	
	@Override
	public void load(LogLevel logLevel) { 
		AnoleLogger.anoleLogLevel = logLevel; 
		load(logLevel, "*.anole"); 
	}
	
	@Override
	public void load(LogLevel logLevel, String... configLocations) { 
		String currentClassPath;
		try {
			URL classPathUrl = Thread.currentThread().getContextClassLoader().getResource(""); 
			currentClassPath = ProjectUtil.getUrlLocalPath(classPathUrl); 
			configLocations = StringUtil.prefixString(configLocations, currentClassPath);
			super.load(logLevel, configLocations);
			Anole.initialized = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  
}