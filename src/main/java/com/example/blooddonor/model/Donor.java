package com.example.blooddonor.model;

public class Donor {
    private String id;      // server-generated
    private String name;
    private String bloodGroup; // e.g. A+, O-
    private double latitude;
    private double longitude;
    private String phone;
    private String city;
    private long lastDonatedEpoch; // epoch millis (optional)

    public Donor() {}

    // getters and setters
    // ... generate all standard getters/setters
    // Example:
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public long getLastDonatedEpoch() { return lastDonatedEpoch; }
    public void setLastDonatedEpoch(long lastDonatedEpoch) { this.lastDonatedEpoch = lastDonatedEpoch; }
}
