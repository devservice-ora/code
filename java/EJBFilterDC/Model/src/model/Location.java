package model;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Location.findAll", query = "select o from Location o") })
@Table(name = "LOCATIONS")
public class Location implements Serializable {

    private static final long serialVersionUID = -258389643348329957L;

    @Column(nullable = false, length = 30)
    private String city;


    @Id
    @Column(name = "LOCATION_ID", nullable = false)
    private Integer locationId;

    @Column(name = "POSTAL_CODE", length = 12)
    private String postalCode;

    @Column(name = "STATE_PROVINCE", length = 25)
    private String stateProvince;

    @Column(name = "STREET_ADDRESS", length = 40)
    private String streetAddress;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    @OneToMany(mappedBy = "location", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Department> DEPARTMENTSSet;

    public Location() {}

    public Location(String city, Country country, Integer locationId, String postalCode, String stateProvince,
                    String streetAddress) {
        this.city = city;
        this.country = country;
        this.locationId = locationId;
        this.postalCode = postalCode;
        this.stateProvince = stateProvince;
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Department> getDEPARTMENTSSet() {
        return DEPARTMENTSSet;
    }

    public void setDEPARTMENTSSet(Set<Department> DEPARTMENTSSet) {
        this.DEPARTMENTSSet = DEPARTMENTSSet;
    }

    public Department addDepartment(Department department) {
        getDEPARTMENTSSet().add(department);
        department.setLocation(this);
        return department;
    }

    public Department removeDepartment(Department department) {
        getDEPARTMENTSSet().remove(department);
        department.setLocation(null);
        return department;
    }
}
