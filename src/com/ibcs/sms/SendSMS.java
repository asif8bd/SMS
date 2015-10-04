package com.ibcs.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

public class SendSMS {

	static Logger logger = Logger.getLogger(SendSMS.class.getName());

	String driver = SMSProperties.DRIVER;
	String url = SMSProperties.URL;
	String port = SMSProperties.PORT;
	int boudRate = Integer.parseInt(SMSProperties.BAUDRATE);
	String mfr = SMSProperties.MANUFACTURER;
	String model = SMSProperties.MODEL;
	String utilty = SMSProperties.UTILITY;
	Connection con;
	Statement stm;
	ResultSet rs;
	ResultSet rs1;
	ResultSet rs2;
	ResultSet rs3;
	ResultSet rs4;

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

	// To change Token pattern like 4088-8808-8085-5840-0860
	public static String changeTokenPattern(String rawToken, String pattern) {
		rawToken = rawToken.replaceAll(" ", "");
		rawToken = rawToken.trim();
		int tokLength = rawToken.length();
		String rturnToken = "";
		for (int i = 0; i < tokLength; i += 4) {
			if (i != 0)
				rturnToken += pattern;
			rturnToken += rawToken.substring(i, (i + 4));
		}
		return rturnToken;
	}

	// To format a list of token 4088-8808-8085-5840-0860,
	// 4088-8808-8085-5840-0860
	public static String formatToTokenList(String rawToken) {
		String[] tokenList = rawToken.split(",");
		StringBuilder formattedTokens = new StringBuilder();
		// System.out.println(Arrays.toString(tokenList));
		for (int i = 0; i < tokenList.length; i++) {
			formattedTokens.append(changeTokenPattern(tokenList[i], "-"));
			if (i != tokenList.length - 1) {
				formattedTokens.append(",\n");
			}
		}
		return formattedTokens.toString();
	}

	public void doIt() throws Exception {

		SerialModemGateway gateway = new SerialModemGateway("id", port,
				boudRate, mfr, model);
		gateway.setInbound(true);
		gateway.setOutbound(true);
		Service.getInstance().addGateway(gateway);
		Service.getInstance().startService();

		int smsCounter = 0;
		while (true) {
			String query = "select * from ORDER_MASTER_STATUS where status is null";
			myDriver();
			try {
				stm = con.createStatement();
				rs = stm.executeQuery(query);
				while (rs.next()) {
					String accountNo = rs.getString("ACCOUNTNO");
					String orderId = rs.getString("ORDERSID");
					String query1 = "select decode(SJHM,null,'pappu',' ','pappu',SJHM) as mobile_number from DA_YHLXR  where HH='"
							+ accountNo + "'";
					rs1 = stm.executeQuery(query1);

					while (rs1.next()) {
						String mobileNumber = rs1.getString("mobile_number")
								.trim();
						if (mobileNumber.equals("pappu")) {
							// status update without sms

							logger.warn("Mobile Not Found");
							String query4 = "update ORDER_MASTER_STATUS set status='2' where ORDERSID='"
									+ orderId + "'";

							stm.executeUpdate(query4);

						} else if (!mobileNumber.equals("pappu")) {

							if (mobileNumber.length() == 11) {
								if (mobileNumber.matches("[0-9]+")) {
									// checking valid digit in mobile no
									String query2 = "select  distinct meterno as meterno from Order_token where ordersid='"
											+ orderId + "'";
									rs2 = stm.executeQuery(query2);
									while (rs2.next()) {
										String meterNo = rs2
												.getString("meterno");
										String query3 = "select  LISTAGG (token, ', ') WITHIN GROUP (ORDER BY token) as token from Order_token where ordersid='"
												+ orderId
												+ "' order by meterseq asc";
										rs3 = stm.executeQuery(query3);
										String tokenNumber = "";
										while (rs3.next()) {

											tokenNumber = formatToTokenList(rs3
													.getString("token"));
										}
										rs3.close();

										// Added code to stop customization SMS
										String meterCustomization = "";
										String queryCustomization = "select hh from da_bj where bjjh='"
												+ meterNo
												+ "' and to_char(ZCRQ, 'yyyy-mm-dd')=(select to_char(op_time, 'yyyy-mm-dd') from order_master where ordersid='"
												+ orderId + "')";
										rs4 = stm
												.executeQuery(queryCustomization);
										while (rs4.next()) {
											meterCustomization = rs4
													.getString("hh");
										}
										rs4.close();

										if (meterCustomization
												.equals(accountNo)) {
											String query6 = "update ORDER_MASTER_STATUS set status='0' where ORDERSID='"
													+ orderId + "'";
											stm.executeUpdate(query6);
											logger.info("Token not sent due to meter("
													+ meterNo
													+ ") customization.");

										} else {

											OutboundMessage msg = new OutboundMessage(
													mobileNumber, "Meter no:\n"
															+ meterNo
															+ "\nTokens:\n"
															+ tokenNumber
															+ "\nFrom: "
															+ utilty);
											Service.getInstance().sendMessage(
													msg);

											String query4 = "update ORDER_MASTER_STATUS set status='1' where ORDERSID='"
													+ orderId + "'";

											stm.executeUpdate(query4);
											smsCounter++;
											logger.info("Message sent("
													+ smsCounter + ") to: "
													+ mobileNumber
													+ " Meter no: " + meterNo
													+ " Token:" + tokenNumber
													+ " From: " + utilty);
											Thread.sleep(1000);
										}
									}
									rs2.close();
								} else {
									// status update without SMS send

									String query4 = "update ORDER_MASTER_STATUS set status='3' where ORDERSID='"
											+ orderId + "'";
									stm.executeUpdate(query4);
									logger.warn("Mobile Not Valid");
								}

							} else {
								// status update without SMS send

								String query4 = "update ORDER_MASTER_STATUS set status='3' where ORDERSID='"
										+ orderId + "'";
								stm.executeUpdate(query4);
								logger.warn("Mobile Not Valid");
							}
						}

					}
					rs1.close();
				}
				rs.close();
			} catch (Exception e) {
				logger.error(e);
			}
			con.close();
			Thread.sleep(5000);

		}

	}

	public static void main(String args[]) {
		SendSMS app = new SendSMS();

		try {
			app.doIt();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}