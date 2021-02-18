/**
 * NanChangShuangShikeTangServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ry.sskt.core.wsdl;

import org.springframework.stereotype.Service;

public class NanChangShuangShikeTangServiceServiceLocator extends org.apache.axis.client.Service implements NanChangShuangShikeTangServiceService {

    public NanChangShuangShikeTangServiceServiceLocator() {
    }


    public NanChangShuangShikeTangServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NanChangShuangShikeTangServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NanChangShuangShikeTangService
    private String NanChangShuangShikeTangService_address = "http://172.17.2.161:12030/AvconWebService/services/NanChangShuangShikeTangService";

    public String getNanChangShuangShikeTangServiceAddress() {
        return NanChangShuangShikeTangService_address;
    }

    // The WSDD service name defaults to the port name.
    private String NanChangShuangShikeTangServiceWSDDServiceName = "NanChangShuangShikeTangService";

    public String getNanChangShuangShikeTangServiceWSDDServiceName() {
        return NanChangShuangShikeTangServiceWSDDServiceName;
    }

    public void setNanChangShuangShikeTangServiceWSDDServiceName(String name) {
        NanChangShuangShikeTangServiceWSDDServiceName = name;
    }

    public NanChangShuangShikeTangService_PortType getNanChangShuangShikeTangService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NanChangShuangShikeTangService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNanChangShuangShikeTangService(endpoint);
    }

    public NanChangShuangShikeTangService_PortType getNanChangShuangShikeTangService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ry.sskt.core.wsdl.NanChangShuangShikeTangServiceSoapBindingStub _stub = new com.ry.sskt.core.wsdl.NanChangShuangShikeTangServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getNanChangShuangShikeTangServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNanChangShuangShikeTangServiceEndpointAddress(String address) {
        NanChangShuangShikeTangService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (NanChangShuangShikeTangService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ry.sskt.core.wsdl.NanChangShuangShikeTangServiceSoapBindingStub _stub = new com.ry.sskt.core.wsdl.NanChangShuangShikeTangServiceSoapBindingStub(new java.net.URL(NanChangShuangShikeTangService_address), this);
                _stub.setPortName(getNanChangShuangShikeTangServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
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
        String inputPortName = portName.getLocalPart();
        if ("NanChangShuangShikeTangService".equals(inputPortName)) {
            return getNanChangShuangShikeTangService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://172.17.2.161:12030/AvconWebService/services/NanChangShuangShikeTangService", "NanChangShuangShikeTangServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://172.17.2.161:12030/AvconWebService/services/NanChangShuangShikeTangService", "NanChangShuangShikeTangService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("NanChangShuangShikeTangService".equals(portName)) {
            setNanChangShuangShikeTangServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
