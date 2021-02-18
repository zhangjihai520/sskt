/**
 * NanChangShuangShikeTangService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ry.sskt.core.wsdl;

public interface NanChangShuangShikeTangService_PortType extends java.rmi.Remote {
    public String createConf(String sSubject, String sConfid, String sDeviceId) throws java.rmi.RemoteException;
    public String getdeviceList() throws java.rmi.RemoteException;
    public String queryConfRecordFilesRequest(String sConfid) throws java.rmi.RemoteException;
    public String updateConfInfo(String sSubject, String sConfid, String sDeviceId) throws java.rmi.RemoteException;
}
