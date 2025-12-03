package domain;

import java.util.List;


public class RideContainer {
private Ride ride;
private List<RideRequest> rideRequestList;
private Driver driver;
public RideContainer(Ride r) {
	this.ride=r;
	this.driver=r.getDriver();
	this.rideRequestList=r.getEskakizunak();
}
public RideContainer() {
	this.ride=null;
	this.driver=null;
	this.rideRequestList=null;
}
public Ride getRide() {
	return ride;
}
public List<RideRequest> getRideRequestList() {
	return rideRequestList;
}
public Driver getDriver() {
	return driver;
}
public String toString() {
	return ride+"/"+rideRequestList+"/"+driver;
}
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	RideContainer other = (RideContainer) obj;
	if (!this.getRide().equals(other.getRide()))
		return false;
	return true;
}
}
