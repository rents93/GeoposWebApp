package com.positionWebApp;

public class Position {
    private double latitude;
    private double longitude;
    private long timestamp;

    public Position(double lat, double lon, long ts){
        this.latitude = lat;
        this.longitude = lon;
        this.timestamp = ts;
        /*ciao*/
        /*ciao*/
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean hasValidCoord(){
        return (latitude>=-90.00 && latitude<=90.00 && longitude>=-180.00 && longitude<=180.00);
    }

    public boolean isValidPos(Position prec){
        return ((this.timestamp>prec.timestamp) && ((distance(prec.latitude, this.latitude,
                prec.longitude, this.longitude))/((this.timestamp-prec.timestamp)/1000))<100);
    }

    public double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }
}
