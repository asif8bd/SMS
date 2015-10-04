package com.ibcs.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.IUSSDNotification;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.USSDResponse;
import org.smslib.modem.SerialModemGateway;

class TestUSSD {
	static Logger logger = Logger.getLogger(ReadMessages.class.getName());
	static List<InboundMessage> msgList = new ArrayList<InboundMessage>();

	String driver = SMSProperties.DRIVER;
	String url = SMSProperties.URL;
	static String port = SMSProperties.PORT;
	static int boudRate = Integer.parseInt(SMSProperties.BAUDRATE);
	static String mfr = SMSProperties.MANUFACTURER;
	static String model = SMSProperties.MODEL;
	String utilty = SMSProperties.UTILITY;
	Connection con;
	Statement stm;
	ResultSet rs;



	public class USSDNotification implements IUSSDNotification
	   {
	      public void process(AGateway gateway, USSDResponse response) {
	                        System.out.println("USSD handler called from Gateway: " + gateway.getGatewayId());
	                        System.out.println(response);
	      }
	   }

	public void doIt() throws Exception {
		SerialModemGateway gateway = new SerialModemGateway("ibcs", port,
				boudRate, mfr, model);
		 System.out.println("Test \nstart");
		USSDNotification ussdNotification = new USSDNotification();
		//InboundNotification inboundNotification = new InboundNotification();
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setProtocol(Protocols.PDU);
		gateway.getATHandler().setStorageLocations("SM");
		Service.getInstance().addGateway(gateway);
		//Service.getInstance().setInboundMessageNotification(inboundNotification);
		//Service.getInstance().setUSSDNotification(ussdNotification);
		Service.getInstance().startService();
		
		String resp = gateway.sendCustomATCommand("AT+CUSD=1,\"*2#\",15\n"); // working
		//String resp =gateway.sendUSSDCommand("*2#", true);
	      System.out.println(resp);
		
		
	}

	public static void main(String[] a) throws Exception {

		TestUSSD app = new TestUSSD();

		try {
			
			// Read all SMS from inbox and send Token
			 app.doIt();

		} catch (Exception e) {
			logger.error(e);
		}

	}
}