package com.infotech.locationbox.servlet;

public class DataNetwork {
    protected String prefix = null;
    protected String network = null;
    protected String host = null;
    protected int port = 0;
    protected String[] hosts = null;
    protected int[] ports = null;
    protected int inx = 0;

    protected static DataNetwork[] networks = null;

    public DataNetwork() {
    }

    public DataNetwork(String network, String host, int port) {
        this.network = network;
        this.host = host;
        this.port = port;
    }

    public DataNetwork(String prefix) {
        this.prefix = prefix;
        this.network = Utils.getParameter(prefix + ".network");
        String links = Utils.getParameter(prefix + ".links");
        String[] info = Utils.splitString(links, ",");
        hosts = new String[info.length];
        ports = new int[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], ":");
            hosts[i] = data[0];
            ports[i] = Integer.parseInt(data[1]);
        } // for()
    }

    //-----------------------------------------------------------------------------

    public synchronized DataNetwork getNext() {
        DataNetwork dn = new DataNetwork(network, hosts[inx], ports[inx]);
        inx++;
        if (inx >= hosts.length)
            inx = 0;
        return dn;
    } // getNext()

    //-----------------------------------------------------------------------------

    public static synchronized DataNetwork getNext(String prefix) {
        DataNetwork dn = null;
        for (int i = 0; i < networks.length; i++) {
            if (networks[i].prefix.equalsIgnoreCase(prefix)) {
                dn = networks[i];
                break;
            }
        } // for()

        if (dn == null)
            return null;

        return dn.getNext();
    } // getNext()

}
