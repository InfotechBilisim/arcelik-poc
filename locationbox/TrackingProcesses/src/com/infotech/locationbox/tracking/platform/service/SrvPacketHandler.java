package com.infotech.locationbox.tracking.platform.service;

import com.infotech.locationbox.tracking.platform.base.*;

import java.io.*;

public class SrvPacketHandler extends Object {
  private SystemObject so = null;
  private PrintStream out = null;
  private SrvConnectionThread cnn = null;

  public SrvPacketHandler() {
  }

  public SrvPacketHandler(PrintStream out, SrvConnectionThread cnn, SystemObject so) {
    this.out = out;
    this.cnn = cnn;
    this.so = so;
  }

//-----------------------------------------------------------------------------

  public void destroy() {
    this.out = null;
    this.cnn = null;
    this.so = null;
    return;
  }  // destroy()

//-----------------------------------------------------------------------------

  public boolean packetReceived(String packet) throws Exception {
    if( packet.length() == 0 ) {
     // Nothing to do for an empty line
     return true;
    }

    if( packet.equalsIgnoreCase("QUIT") ) {
      this.out = null;
      this.cnn = null;
      this.so = null;
      return false;
    }

    if( packet.equalsIgnoreCase("TEST") ) {
      try {
        out.println("Diagnostics...");
        out.flush();
      }
      catch (Exception ex) {
        Log.showTextForService("Exception while sending packet: " + ex.getMessage());
      }
      this.out = null;
      this.cnn = null;
      this.so = null;
      return false;
    }

    return true;
  } // packetReceived()

//-----------------------------------------------------------------------------

  public void sendPacket(String packet) {
    try {
      out.println(packet);
    }
    catch (Exception ex) {
      Log.showTextForService("Exception while sending packet: " + ex.getMessage());
    }
    return;
  } // sendString();

}
