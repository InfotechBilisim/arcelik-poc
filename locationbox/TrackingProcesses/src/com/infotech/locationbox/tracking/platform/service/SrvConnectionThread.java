package com.infotech.locationbox.tracking.platform.service;

import com.infotech.locationbox.tracking.platform.base.*;

import java.io.*;
import java.net.*;

public class SrvConnectionThread extends Thread {
  private SystemObject so = null;
  private Socket socket = null;
  private SrvPacketHandler packetHandler = null;
  private InputStream inp = null;
  private PrintStream out = null;
  private boolean running = false;

  private String ipAddress = null;

  public SrvConnectionThread() {
  }

  public SrvConnectionThread(Socket socket, SystemObject so) throws IOException {
    this.socket = socket;
    ipAddress = socket.getInetAddress().getHostAddress();
//    this.inp = new BufferedReader( new InputStreamReader(this.socket.getInputStream()) );
    this.inp = this.socket.getInputStream();
    this.out = new PrintStream(this.socket.getOutputStream());
    this.so = so;
    packetHandler = new SrvPacketHandler(out, this, so);
  }

  public void run() {
    Log.showTextForService("Connection request accepted : " + ipAddress);

    running = true;
    while( running ) processConnection();

    packetHandler.destroy();
    packetHandler = null;
    try { socket.shutdownInput(); } catch (Exception e) {;}
    try { socket.shutdownOutput(); } catch (Exception e) {;}
    try { socket.close(); } catch (Exception e) {;}
    this.socket = null;
    this.inp = null;
    this.out = null;
    this.so = null;

    Log.showTextForService("Disconnected !");
    return;
  } // run()

//-----------------------------------------------------------------------------

  public void stopThread() {
    running = false;
    return;
  } // stopThread()

//-----------------------------------------------------------------------------

  private void processConnection() {
    String s = "";

    boolean binaryData = false;
    int binaryPrefixLength = 0;

    try {
      while( true ) {
        int ch = inp.read();
        if( ch < 0 ) { // End of file
          Log.showTextForService("Disconnected : " + socket.getInetAddress());
          running = false;
          return;
        }

        if( s.startsWith("@@") && ch == ',' && !binaryData ) {
          binaryData = true;
          binaryPrefixLength = s.length() + 1;
        }
        else {
          if( binaryData ) {
            s += Utils.convNumberToHexString((long)ch, 2);
            if( s.length() >= (binaryPrefixLength + 34) ) break;

            continue;
          }
        }

        if( ch == 0x0a ) {
          if( s.length() > 0 ) break;

          continue; // Skip
        }

        if( ch == 0x0d ) {
          if( s.length() > 0 ) break;

          continue; // Skip
        }

        if( s.startsWith("$P") && ch == '*' ) {
          ch = inp.read(); // Skip checksum data of WaveOn
          ch = inp.read();
          break; // End of line
        }

        s += (char)ch;

        if( ch == '>' ) {
          if( s.length() == 6 && s.equals("<ACK/>") )
            break;
        }

      } // while()

      try {
        if( ! packetHandler.packetReceived(convTurkishToUnicode(s)) ) {
          running = false;
          Log.showTextForService("Completed : " + socket.getInetAddress());
          return;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

    }
    catch(InterruptedIOException ex) {
      return;
    }

    catch(SocketException ex) {
      Log.showTextForService("Socket Exception : " + ex.getMessage());
      running = false;
      return;
    }

    catch(IOException ex) {
      ex.printStackTrace();
      running = false;
      return;
    }

    return;
  } // processConnection()

//-----------------------------------------------------------------------------

  public void sendPacket(String packet) {
    if( packetHandler == null ) {
      Log.showTextForService("Connection already disconnected !");
      return;
    }

    packetHandler.sendPacket(convTurkishFromUnicode(packet));
    return;
  } // sendPacket();

  public String getSourceIp() {
    return socket.getInetAddress().getHostAddress();
  }

//-----------------------------------------------------------------------------

  public String convTurkishToUnicode(String txt) {
    String str = "";

    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      switch( ch ) {
      case 0xD0 : str += (char)0x11E; break;
      case 0xF0 : str += (char)0x11F; break;
      case 0xDE : str += (char)0x15E; break;
      case 0xFE : str += (char)0x15F; break;
      case 0xDD : str += (char)0x130; break;
      case 0xFD : str += (char)0x131; break;
      default :
        str += (char)ch;
        break;
      } // switch()
    } // for()
    return( str );
  } // convTurkishToUnicode()

//-----------------------------------------------------------------------------

  public String convTurkishFromUnicode(String txt) {
    String str = "";

    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      switch( ch ) {
      case 0x11E : str += (char)0xD0; break;
      case 0x11F : str += (char)0xF0; break;
      case 0x15E : str += (char)0xDE; break;
      case 0x15F : str += (char)0xFE; break;
      case 0x130 : str += (char)0xDD; break;
      case 0x131 : str += (char)0xFD; break;
      default :
        str += (char)ch;
        break;
      } // switch()
    } // for()
    return( str );
  } // convTurkishFromUnicode()

//-----------------------------------------------------------------------------

  public String getIpAddress() {
    return ipAddress;
  }

}
