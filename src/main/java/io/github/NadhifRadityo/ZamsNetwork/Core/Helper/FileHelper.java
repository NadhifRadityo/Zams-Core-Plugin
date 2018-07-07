package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;

public class FileHelper {
	public String defaultFileName = null;
	
	public static final String JAVA_CLASS_PATH_PROPERTY = "java.class.path";
	public static final String CUSTOM_CLASS_PATH_PROPERTY = "custom.class.path";

	public final char[] forbiddenCharWin = { 
		'<',
		'>',
		':',
		'"',
		'|',
		'?',
		'*'
	};
	public final String[] forbiddenCharWinReserved = {
		"CON", "PRN", "AUX", "NUL",
		"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
		"LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
	};
	public final char[] forbiddenCharWinIgnored = {
		'/',
		'\\' // '\'
	};
	public final char[] forbiddenCharWinEnd = {
		'.'
	};
	
	public final char[] forbiddenCharLinux = { //Unix
		
	};
	public final char[] forbiddenCharLinuxIgnored = { //Unix
		'/'
	};
	
	//Get all files
	public File[] getAllFiles(String dir) throws FileException {
		try {
			return getAllFiles(this.getFile(dir));
		} catch (FileException e) {
			throw new FileException("Can not get all files!", e);
		}
	}
	public File[] getAllFiles(String path, String fileName) throws FileException {
		try {
			return getAllFiles(this.getFile(path, fileName));
		} catch (FileException e) {
			throw new FileException("Can not get all files!", e);
		}
	}
	public File[] getAllFiles(File folder){
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}
	
	
	//Count File
	public int countFile(String dir) throws FileException {
		if(!this.checkName(dir)) {
			throw new FileException("Dir name contains forbidden character.");
		}
		try {
			return this.countFile(this.getAllFiles(dir));
		} catch (FileException e) {
			throw new FileException("Can not count file!", e);
		}
	}
	public int countFile(String path, String fileName) throws FileException {
		if(!this.checkName(path)) {
			throw new FileException("Path name contains forbidden character.");
		}
		if(!this.checkName(fileName)) {
			throw new FileException("File name contains forbidden character.");
		}
		try {
			return this.countFile(this.getAllFiles(path, fileName));
		} catch (FileException e) {
			throw new FileException("Can not count file!", e);
		}
	}
	public int countFile(File[] files) {
		int result = 0;
		for(File file : files) {
			if(file.isFile()) {
				result++;
			}
		}
		return result;
	}
	
	//Count Directory
	public int countDirectory(String dir) throws FileException {
		if(!this.checkName(dir)) {
			throw new FileException("Dir name contains forbidden character.");
		}
		try {
			return this.countDirectory(this.getAllFiles(dir));
		} catch (FileException e) {
			throw new FileException("Can not count file!", e);
		}
	}
	public int countDirectory(String path, String fileName) throws FileException {
		if(!this.checkName(path)) {
			throw new FileException("Path name contains forbidden character.");
		}
		if(!this.checkName(fileName)) {
			throw new FileException("File name contains forbidden character.");
		}
		try {
			return this.countDirectory(this.getAllFiles(path, fileName));
		} catch (FileException e) {
			throw new FileException("Can not count file!", e);
		}
	}
	public int countDirectory(File[] files) {
		int result = 0;
		for(File file : files) {
			if(file.isDirectory()) {
				result++;
			}
		}
		return result;
	}
	
	//List all files
//	public void aw(File curDir) {
//		File[] filesList = curDir.listFiles();
//        for(File f : filesList){
//            if(f.isDirectory())
//                aw(f);
//            if(f.isFile()){
//                System.out.println(f.getName());
//            }
//        }
//	}
//	
	public File[] getListAllFiles(File curDir) throws FileException {
		List<File> result = new ArrayList<File>();
		
		File[] filesList = this.getAllFiles(curDir);
		if(filesList == null) {
			throw new FileException("Directory / file is not available / unreachable");
		}
		for(File f : filesList){
			if(f.isDirectory()) {
				File[] filesChild = this.getListAllFiles(f);
				for(File file : filesChild) {
					result.add(file);
				}
			}else if(f.isFile()){
				result.add(f);
			}
		}
		return result.toArray(new File[result.size()]);
	}
	public File[] getListAllFiles(String dir) throws FileException{
		dir = this.fixedName(dir);
		try {
			return this.getListAllFiles(this.getFile(dir));
		} catch (FileException e) {
			throw new FileException("Can not list all files!", e);
		}
	}
	public File[] getListAllFiles(String path, String fileName) throws FileException{
		fileName = this.fixedName(fileName);
		try {
			return this.getListAllFiles(this.getFile(path, fileName));
		} catch (FileException e) {
			throw new FileException("Can not list all files!", e);
		}
	}
	
	//Get file
	public File getFile(String path, String fileName) throws FileException{
		if(!this.checkName(path)) {
			throw new FileException("Path name contains forbidden character.");
		}
		if(!this.checkName(fileName)) {
			throw new FileException("File name contains forbidden character.");
		}
		
		if(fileName != null) {
			return new File(path, fileName);
		}
		return new File(path);
	}
	public File getFile(String dir) throws FileException{
		if(!this.checkName(dir)) {
			throw new FileException("File name contains forbidden character.");
		}
		return getFile(dir, this.defaultFileName);
	}
	
	//Check if name is legal
	public boolean checkName(String fileName) {
		if(fileName != null)
		
		if(OSHelper.isWindows()) {
			ArrayList<String> CharList = new ArrayList<String>();
			for(char charc : this.forbiddenCharWin) {
				CharList.add(Character.toString(charc));
			}
			
			for(String character : fileName.split("")) {
				if(CharList.contains(character)) {
					return false;
				}
			}
			
			ArrayList<String> forbiddenWinReserver = new ArrayList<String>(Arrays.asList(this.forbiddenCharWinReserved));
			if(forbiddenWinReserver.contains(fileName.toUpperCase())) {
				return false;
			}
			
			return true;
		}
		return true;
	}
	
	//name fixing
	public String fixedName(String fileName) {
		if(OSHelper.isWindows()) {
			ArrayList<String> CharList = new ArrayList<String>();
			for(char charc : this.forbiddenCharWin) {
				// https://stackoverflow.com/questions/40246231/java-util-regex-patternsyntaxexception-dangling-meta-character-near-index-0/40246400?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
				CharList.add("\\" + Character.toString(charc));
			}
			
			for(String character : CharList) {
				fileName.replaceAll(character, "");
			}
			
			ArrayList<String> forbiddenWinReserver = new ArrayList<String>(Arrays.asList(this.forbiddenCharWinReserved));
			if(forbiddenWinReserver.contains(fileName.toUpperCase())) {
				return null;
			}
			
			return fileName;
		}
		return fileName;
	}
	
	public String[] getClassPathRoots() {
		String classPath;
		if (System.getProperties().containsKey(CUSTOM_CLASS_PATH_PROPERTY)) {
			classPath = System.getProperty(CUSTOM_CLASS_PATH_PROPERTY);
		} else {
			classPath = System.getProperty(JAVA_CLASS_PATH_PROPERTY);
		}
		String[] pathElements = classPath.split(File.pathSeparator);
		return pathElements;
	}
}
