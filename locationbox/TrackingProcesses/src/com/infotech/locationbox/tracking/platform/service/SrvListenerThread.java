package com.infotech.locationbox.tracking.platform.service;

import com.infotech.locationbox.tracking.platform.base.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class SrvListenerThread extends Thread {
  private SystemObject so = null;
  private ArrayList connections = null;
  private ServerSocket socket = null;
  private int  port = -1;
  private boolean running = false;

  public SrvListenerThread() {
  }

  public SrvListenerThread(int  port, SystemObject so) throws IOException {
    this.connections = new ArrayList();
    this.port = port;
    this.socket = new ServerSocket(this.port);
    Log.showTextForService("*** Service Listener Started on port " + this.port);
    this.so = so;
  }

  public void run() {
    running = true;
    while( running ) acceptConnections();
    return;
  } // run()

//-----------------------------------------------------------------------------

  private void acceptConnections() {
    try {
      compactConnections();

      Socket accepted = socket.accept();
      accepted.setSoTimeout(10000);
      accepted.setKeepAlive(true);
      accepted.setTcpNoDelay(true);
      Log.showTextForService("Service Socket accepted. Address: " + accepted.getInetAddress());
      SrvConnectionThread cnn = new SrvConnectionThread(accepted, so);
      cnn.start();
      connections.add(cnn);
      Log.showTextForService("Service Connection count: " + connections.size() + ", alive count: " + countAliveCnn());
    }
    catch(InterruptedIOException ex) {
      ;
    }
    catch (Exception ex) {
      running = false;
      Log.showTextForService("Exception in acceptConnections() : " + ex.getMessage());
      ex.printStackTrace();
      return;
    }

    return;
  } // acceptConnections()

//-----------------------------------------------------------------------------

  public void cnnAdd(SrvConnectionThread cnn) {
    connections.add(cnn);
    return;
  } // cnnAdd()

  public void cnnRemove(SrvConnectionThread cnn) {
    connections.remove(cnn);
    cnn.stopThread();
    return;
  } // cnnAdd()

//-----------------------------------------------------------------------------

  public int countAliveCnn() {
    int count = 0;
    for( int i = 0; i < connections.size(); i++ ) {
      SrvConnectionThread cnn = (SrvConnectionThread)connections.get(i);
      if( cnn != null && cnn.isAlive() ) count++;
    } // for()
    return( count );
  } // countAliveCnn()

//-----------------------------------------------------------------------------

  public void compactConnections() {
    for( int i = connections.size(); i > 0; i-- ) {
      SrvConnectionThread cnn = (SrvConnectionThread)connections.get(i - 1);
      if( cnn == null ) connections.remove(i - 1);
      else
      if( !cnn.isAlive() ) {
        cnn.stopThread();
        connections.remove(i - 1);
      }
    } // for()
    return;
  } // compactConnections()

//-----------------------------------------------------------------------------

  public void sendStringToAll(String txt) {
    for( int i = 0; i < connections.size(); i++ ) {
      SrvConnectionThread cnn = (SrvConnectionThread)connections.get(i);
      if( cnn.isAlive() ) cnn.sendPacket(txt);
    } // for()
    return;
  } // sendStringToAll()

}
