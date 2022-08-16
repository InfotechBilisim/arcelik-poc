package com.infotech.locationbox.servlet;

import com.infotech.locationbox.stromberglabs.cluster.Cluster;
import com.infotech.locationbox.stromberglabs.cluster.ClusterUtils;


public class DataClusterPoint {

    protected Cluster clusters[] = null;

    public DataClusterPoint() {

    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        double latitude = 0.0;
        double longitude = 0.0;
        String alias = "";
        double distance = 0.0;
        float[] clusterLoc = null;

        for (int i = 0; clusters.length > i; i++) {
            clusterLoc = clusters[i].getLocation();
            latitude = clusterLoc[0];
            longitude = clusterLoc[1];
            if (i > 0)
                s += ",\n";
            s += "  { ";
            s += "\n" + indent + "\"id\": " + (i + 1) + ",";
            s += "\n" + indent + "\"latitude\": " + latitude + ",";
            s += "\n" + indent + "\"longitude\": " + longitude + ",";
            s += "\n" + indent + "\"items\": [";

            for (int j = 0; clusters[i].getItems().size() > j; j++) {
                clusterLoc = clusters[i].getItems().get(j).getLocation();
                alias = clusters[i].getItems().get(j).getAlias();
                if (j > 0)
                    s += ",";
                latitude = clusterLoc[0];
                longitude = clusterLoc[1];
                distance = (ClusterUtils.getEuclideanDistance(clusters[i].getItems().get(j), clusters[i])) * (100000); // converting to meter approximately
                s += "\n";
                s += indent + "  {";
                s += "\n" + indent + indent + "\"itemid\": " + (j + 1) + ",";
                s += "\n" + indent + indent + "\"alias\": " + alias + ",";
                s += "\n" + indent + indent + "\"latitude\": " + latitude + ",";
                s += "\n" + indent + indent + "\"longitude\": " + longitude + ",";
                s += "\n" + indent + indent + "\"distance\": " + distance;
                s += "\n";
                s += indent + "  }";

            }
            s += "\n";
            s += indent + "]";
            s += "\n";
            s += "  }";
        }
        s += "\n";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        double latitude = 0.0;
        double longitude = 0.0;
        String alias = null;
        double distance = 0.0;
        float[] clusterLoc = null;

        for (int i = 0; clusters.length > i; i++) {
            clusterLoc = clusters[i].getLocation();
            latitude = clusterLoc[0];
            longitude = clusterLoc[1];
            if (i > 0)
                s += "\n";
            s += "      <id>" + (i + 1) + "</id>\n";
            s += "      <latitude>" + latitude + "</latitude>\n";
            s += "      <longitude>" + longitude + "</longitude>\n";
            s += "      <items>";

            for (int j = 0; clusters[i].getItems().size() > j; j++) {
                clusterLoc = clusters[i].getItems().get(j).getLocation();
                latitude = clusterLoc[0];
                longitude = clusterLoc[1];
                alias = clusters[i].getItems().get(j).getAlias();
                distance = (ClusterUtils.getEuclideanDistance(clusters[i].getItems().get(j), clusters[i])) * (100000); // converting to meter approximately
                s += "      <item>\n";
                s += "      <itemid>" + (j + 1) + "</itemid>\n";
                s += "      <alias>" + alias + "</alias>\n";
                s += "      <latitude>" + latitude + "</latitude>\n";
                s += "      <longitude>" + longitude + "</longitude>\n";
                s += "      <distance>" + distance + "</distance>\n";
                s += "      </item>\n";
            }
            s += "      </items>\n";
            s += "\n";
        }
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------


    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }

    public Cluster[] getClusters() {
        return clusters;
    }

}
