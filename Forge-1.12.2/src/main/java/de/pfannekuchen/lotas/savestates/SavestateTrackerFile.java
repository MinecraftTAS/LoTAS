package de.pfannekuchen.lotas.savestates;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Used to keep track of savestates and rerecordings
 * @author ScribbleLP
 *
 */
public class SavestateTrackerFile {
	
	private final File saveLocation;
	
	private final String top="#This file was generated by LoTAS and diplays info about the usage of savestates!\n\n";
	
	private int savestatecount;
	
	private int loadstatecount;

	public SavestateTrackerFile(File saveLocation) throws IOException {
		this.saveLocation=saveLocation;
		if(!saveLocation.exists()) {
			savestatecount=0;
			loadstatecount=0;
		} else {
			loadFile();
		}
	}
	
	private void loadFile() throws IOException {
		List<String> lines=FileUtils.readLines(saveLocation, Charset.defaultCharset());
		
		lines.forEach(line->{
			if(line.startsWith("Total Savestates")) {
				savestatecount=Integer.parseInt(line.split("=")[1]);
			}else if(line.startsWith("Total Rerecords")){
				loadstatecount=Integer.parseInt(line.split("=")[1]);
			}
		});
	}

	public void increaseSavestates() {
		savestatecount++;
	}
	
	public void increaseRerecords() {
		loadstatecount++;
	}
	
	public void saveFile() throws IOException {
		List<String> lines= new ArrayList<String>();
		
		lines.add(top);
		
		lines.add("Total Savestates="+savestatecount+"\nTotal Rerecords="+loadstatecount);
		
		FileUtils.writeLines(saveLocation, lines);
	}
}
