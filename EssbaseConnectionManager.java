package com.hyperion.planning;

import com.essbase.api.base.EssException;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.domain.IEssDomain;
import com.essbase.api.metadata.IEssCubeOutline;
import com.essbase.api.session.IEssbase;

public class EssbaseConnectionManager {
	private static String userName = "admin";
	private static String password = "password";
	//private static String server = "localhost";
	private static String server = "den02ahj.us.oracle.com";
	private static String provider = "Embedded";
	
	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static String getServer() {
		return server;
	}

	public static String getProvider() {
		return provider;
	}

	public static void setUserName(String userName) {
		EssbaseConnectionManager.userName = userName;
	}

	public static void setPassword(String password) {
		EssbaseConnectionManager.password = password;
	}

	public static void setServer(String server) {
		EssbaseConnectionManager.server = server;
	}

	public static void setProvider(String provider) {
		EssbaseConnectionManager.provider = provider;
	}

	public static void comeOutClean(EssbaseCube cube) {
		IEssbase ess = cube.getEss();
		IEssOlapServer olapServer = cube.getOlapServer();
		IEssCubeOutline cubeOutline = cube.getCubeOutline();
		try{
			olapServer.clearClientCache();
			cubeOutline.clearClientCache();
			System.out.println("Cache cleared");
			if (cubeOutline != null && cubeOutline.isOpen()) 
				cubeOutline.close();
		}catch (EssException x) {
			System.err.println("Error: " + x.getMessage());
		}
		
		try {
			if (olapServer != null && olapServer.isConnected() == true)
				olapServer.disconnect();
		} catch (EssException x) {
			System.err.println("Error: " + x.getMessage());
		}

		try {
			if (ess != null && ess.isSignedOn() == true)
				ess.signOff();
		} catch (EssException x) {
			System.err.println("Error: " + x.getMessage());
		}
		
		System.out.println("Resources cleaned");
	}

	private static void getOlapConnection(EssbaseCube cube) throws EssException {
		IEssbase ess = cube.getEss();
		IEssDomain domain = ess.signOn(userName, password, false, null, provider);
		IEssOlapServer olapServer = domain.getOlapServer(server);
		ess.setGlobalClientCachingEnabled(false);
		olapServer.connect();
		olapServer.setClientCachingEnabled(false);
		cube.setOlapServer(olapServer);
	}

	private static IEssbase getJAPIVersion() throws EssException {
		return IEssbase.Home.create(IEssbase.JAPI_VERSION);
	}

	public static void connectToCube(EssbaseCube cube) throws EssException {
		cube.setEss(getJAPIVersion());
		getOlapConnection(cube);
	}
}
