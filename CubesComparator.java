package com.hyperion.planning;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.essbase.api.base.EssException;

public class CubesComparator {

	public static void main(String[] args) throws IOException {
		EssbaseCube sourceCube = new EssbaseCube();
		sourceCube.setApplicationName("Sample_U");
		sourceCube.setCubeName("Basic");
//		sourceCube.setApplicationName("SampCopy");
//		sourceCube.setCubeName("East");
//		sourceCube.setApplicationName("ASOsamp");
//		sourceCube.setCubeName("Sample");
		EssbaseCube targetCube = new EssbaseCube();
		targetCube.setApplicationName("SampCopy");
		targetCube.setCubeName("East");
//		targetCube.setApplicationName("Sample_U");
//		targetCube.setCubeName("Basic");
		
		//Writer.setFilePath("D:\\diff.txt");
//		Writer.setFilePath("D:\\diff.xlsx");
//		Writer.init();
		ExcelUtil.setFilePath("D:\\sheets\\diff7.xlsx");
		ExcelUtil.init();
		
		try{
			EssbaseConnectionManager.connectToCube(sourceCube);
			sourceCube.load();
			
			EssbaseConnectionManager.connectToCube(targetCube);
			targetCube.load();
			
			//EssbaseUtil.cubeComparator(sourceCube, targetCube);
			TreeComparator.writeSummary(sourceCube, targetCube);
			//EssbaseTreeComparator.printComparison(sourceCube, targetCube);
		} catch (EssException e) {
			System.out.println("Error : "+e.getMessage());
			e.printStackTrace();
		} /*catch (IOException e) {
			System.out.println("Error : "+e.getMessage());
			e.printStackTrace();
		}*/
		finally{
			EssbaseConnectionManager.comeOutClean(sourceCube);
			EssbaseConnectionManager.comeOutClean(targetCube);
			//Writer.closeWriter();
			ExcelUtil.closeWorkbook();
		}
		Desktop dt = Desktop.getDesktop();
		dt.open(new File("D:\\sheets\\diff7.xlsx"));
		System.out.println("done");
	}
}
