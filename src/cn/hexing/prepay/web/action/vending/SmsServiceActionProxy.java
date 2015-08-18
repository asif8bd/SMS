package cn.hexing.prepay.web.action.vending;

public class SmsServiceActionProxy implements cn.hexing.prepay.web.action.vending.SmsServiceAction {
  private String _endpoint = null;
  private cn.hexing.prepay.web.action.vending.SmsServiceAction smsServiceAction = null;
  
  public SmsServiceActionProxy() {
    _initSmsServiceActionProxy();
  }
  
  public SmsServiceActionProxy(String endpoint) {
    _endpoint = endpoint;
    _initSmsServiceActionProxy();
  }
  
  private void _initSmsServiceActionProxy() {
    try {
      smsServiceAction = (new cn.hexing.prepay.web.action.vending.SmsServiceActionServiceLocator()).getSmsServiceAction();
      if (smsServiceAction != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)smsServiceAction)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)smsServiceAction)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (smsServiceAction != null)
      ((javax.xml.rpc.Stub)smsServiceAction)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.hexing.prepay.web.action.vending.SmsServiceAction getSmsServiceAction() {
    if (smsServiceAction == null)
      _initSmsServiceActionProxy();
    return smsServiceAction;
  }
  
  public java.lang.String getToken(java.lang.String meterNo, java.lang.String vendingAmount) throws java.rmi.RemoteException{
    if (smsServiceAction == null)
      _initSmsServiceActionProxy();
    return smsServiceAction.getToken(meterNo, vendingAmount);
  }
  
  
}