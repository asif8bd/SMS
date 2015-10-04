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
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

class ReadMessagesOld {
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

	public void myDriver() {

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, SMSProperties.USERNAME,
					SMSProperties.PASSWORD);
		} catch (ClassNotFoundException cnf) {
			System.out.println(cnf);
		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public boolean isMeterTaggedWithMobile(String mobileNo, String meterNo) {

		if (meterNo.length() != 12) {
			return false;
		}

		String query = "select sjhm from da_yhlxr where hh=(select hh from da_bj where bjjh='"
				+ meterNo + "')";

		try {
			stm = con.createStatement();
			rs = stm.executeQuery(query);
			String db_mobileNo = "";
			while (rs.next()) {
				db_mobileNo = rs.getString("sjhm").trim();
			}
			rs.close();

			if (mobileNo.equals("88" + db_mobileNo)) {

				// logger.info("Mobile matched " + mobileNo);

				return true;

			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}

	}

	public class InboundNotification implements IInboundMessageNotification {

		@Override
		public void process(AGateway gateway, MessageTypes msgType,
				InboundMessage msg) {
			if (msgType == MessageTypes.INBOUND) {
				System.out
						.println(">>> New	Inbound message detected from Gateway: "
								+ gateway.getGatewayId());
			} else if (msgType == MessageTypes.STATUSREPORT) {
				System.out
						.println(">>> New Inbound Status Report message detected 	from Gateway: "
								+ gateway.getGatewayId());
			}
			System.out.println("From: "+msg.getOriginator());
			System.out.println("Text: "+msg.getText());
			System.out.println("---------------------");
			//"01745221760"
			OutboundMessage out = new OutboundMessage(msg.getOriginator(),"Tnx for RND");
			out.setFrom("USA");
			
			
			try {
				Service.getInstance().sendMessage(out);
				Service.getInstance().deleteMessage(msg);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GatewayException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doIt() throws Exception {
		SerialModemGateway gateway = new SerialModemGateway("ibcs", port,
				boudRate, mfr, model);
		InboundNotification inboundNotification = new InboundNotification();
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setProtocol(Protocols.PDU);
		gateway.getATHandler().setStorageLocations("SM");
		Service.getInstance().addGateway(gateway);
		Service.getInstance().setInboundMessageNotification(inboundNotification);
		Service.getInstance().startService();
		
		/*
		// SmsServiceActionProxy sm = new SmsServiceActionProxy();
		while (port.equals("abc")) {//true
			Service.getInstance().readMessages(msgList, MessageClasses.ALL);
			myDriver();
			for (InboundMessage msg : msgList) {
				String sender = msg.getOriginator();
				String text_msg = msg.getText();
				text_msg = text_msg.trim().replaceAll(" +", " ");
				System.out.println("\nFrom: " + sender);
				System.out.println("Text: " + text_msg);

				String[] test = text_msg.split(" ");
				for (int i = 0; i < test.length; i++) {
					// System.out.println(test[i]);
				}

				String meterNo = test[0];

				if (meterNo.matches("[0-9]+")) {
					if (meterNo.length() == 11) {
						meterNo = "0" + meterNo;
					}
				} else {

					meterNo = "";
					System.out.println("Skipping other sms....");
					continue;
				}
				if (isMeterTaggedWithMobile(sender, meterNo)) {
					System.out.println("Congrats! Mobile no: " + sender
							+ " is registerd with Meter: " + meterNo
							+ ". Wait for token.");
				} else {
					System.out.println("Sorry!!! Mobile no: " + sender
							+ " is not registerd with Meter: " + meterNo);
				}
				// if(test[0].length()<)
				// String Token= sm.getToken(test[0], test[1]);
				// System.out.println(Token);
				// Service.getInstance().deleteMessage(msg);
			}
			msgList.clear();
			System.out.println("============================");
			Thread.sleep(5000);
		}
		*/
	}
/*
	public void sendPostRequest() {
		try {

			String url = "https://selfsolve.apple.com/wcResults.do";
			// String url = "http://localhost:8088/ussd/home.html";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// String urlParameters = "responseString=1&step=S_1";
			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get the response
			int responseCode = con.getResponseCode();

			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			System.out
					.println("Response message : " + con.getResponseMessage());

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Print result
			System.out.println(response.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	public static void main(String[] a) throws Exception {

		ReadMessagesOld app = new ReadMessagesOld();

		try {
			// send data via HTTP post method
			//app.sendPostRequest();

			// Read all SMS from inbox and send Token
			 app.doIt();

		} catch (Exception e) {
			logger.error(e);
		}

	}
}