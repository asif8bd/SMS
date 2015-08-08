package com.sms.send;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SMSProperties {
	public final static String PORT;
	public final static String BAUDRATE;
	public final static String MANUFACTURER;
	public final static String MODEL;	
	public final static String USERNAME;
	public final static String PASSWORD;
	public final static String DRIVER;
	public final static String URL;
	public final static String UTILITY;
	
	static{
		Properties p = new Properties();
	    try {
			p.load(new FileInputStream("application.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PORT= p.getProperty("modem.comPort");
	    BAUDRATE= p.getProperty("modem.baudRate");
	    MANUFACTURER= p.getProperty("modem.manufacturer");
	    MODEL= p.getProperty("modem.model");
	    UTILITY= p.getProperty("bpdb.utilty");
	    
	    USERNAME= p.getProperty("database.connection.username");
	    PASSWORD= p.getProperty("database.connection.password");
	    DRIVER= p.getProperty("database.connection.driver_class");
	    URL= p.getProperty("database.connection.url");   
		
	}
	public static void main(String[] args) {
		System.out.println(256>>8);
		
	}
}
