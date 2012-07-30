package de.stefanteitge.mtmx.core;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.stefanteitge.mtmx.core.file.FileLoadException;
import de.stefanteitge.mtmx.core.file.FileStoreException;
import de.stefanteitge.mtmx.core.file.pod.PodDataFormat;
import de.stefanteitge.mtmx.core.file.pod.PodFile;
import de.stefanteitge.mtmx.core.file.pod.PodLstFile;

public class PodFileTest {
	
	private static final String LST_FILE_NAME = "cockpit.lst";
	
	private static final String POD_FILE_NAME = "cockpit.pod";
	
	private static final String NEW_POD_FILE_NAME = "cockpit2.pod";
	
	private PodFile getMtm2PodFile() {
		File file = new File(ITestConfig.MTM2_DIR, POD_FILE_NAME);
		
		Assert.assertTrue("Test POD file does not exist.", file.exists() && file.isFile());
		
		return new PodFile(file);
	}
	
	@Test
	public void testCreateFromLst() throws FileLoadException, FileStoreException {
		PodFile podFile = getMtm2PodFile();

		PodLstFile plf = PodLstFile.fromPodData(podFile.getData());
		File outDir = new File(ITestConfig.OUT_DIR);
		File file = new File(outDir, LST_FILE_NAME);
		plf.toFile(file);
		
		
		podFile.getData().extractAll(outDir);
		
		PodFile newPodFile = PodFile.createFromLst(
				outDir, 
				PodLstFile.fromFile(file), 
				new File(outDir, NEW_POD_FILE_NAME),
				PodDataFormat.POD1);
		
		Assert.assertEquals(
				"Mismatching entry count.", 
				podFile.getData().getEntryCount(), 
				newPodFile.getData().getEntryCount());
	}
}
