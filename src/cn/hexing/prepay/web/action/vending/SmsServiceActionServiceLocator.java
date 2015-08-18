/**
 * SmsServiceActionServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.hexing.prepay.web.action.vending;

public class SmsServiceActionServiceLocator extends org.apache.axis.client.Service implements cn.hexing.prepay.web.action.vending.SmsServiceActionService {

    public SmsServiceActionServiceLocator() {
    }


    public SmsServiceActionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SmsServiceActionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SmsServiceAction
    private java.lang.String SmsServiceAction_address = "http://180.211.136.109/prepay/services/SmsServiceAction";

    public java.lang.String getSmsServiceActionAddress() {
        return SmsServiceAction_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SmsServiceActionWSDDServiceName = "SmsServiceAction";

    public java.lang.String getSmsServiceActionWSDDServiceName() {
        return SmsServiceActionWSDDServiceName;
    }

    public void setSmsServiceActionWSDDServiceName(java.lang.String name) {
        SmsServiceActionWSDDServiceName = name;
    }

    public cn.hexing.prepay.web.action.vending.SmsServiceAction getSmsServiceAction() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SmsServiceAction_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSmsServiceAction(endpoint);
    }

    public cn.hexing.prepay.web.action.vending.SmsServiceAction getSmsServiceAction(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.hexing.prepay.web.action.vending.SmsServiceActionSoapBindingStub _stub = new cn.hexing.prepay.web.action.vending.SmsServiceActionSoapBindingStub(portAddress, this);
            _stub.setPortName(getSmsServiceActionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSmsServiceActionEndpointAddress(java.lang.String address) {
        SmsServiceAction_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.hexing.prepay.web.action.vending.SmsServiceAction.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.hexing.prepay.web.action.vending.SmsServiceActionSoapBindingStub _stub = new cn.hexing.prepay.web.action.vending.SmsServiceActionSoapBindingStub(new java.net.URL(SmsServiceAction_address), this);
                _stub.setPortName(getSmsServiceActionWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SmsServiceAction".equals(inputPortName)) {
            return getSmsServiceAction();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://vending.action.web.prepay.hexing.cn", "SmsServiceActionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://vending.action.web.prepay.hexing.cn", "SmsServiceAction"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SmsServiceAction".equals(portName)) {
            setSmsServiceActionEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
