package model;

import java.io.Serializable;

import java.math.BigDecimal;

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
@NamedQueries({ @NamedQuery(name = "Country.findAll", query = "select o from Country o") })
@Table(name = "COUNTRIES")
public class Country implements Serializable {

    private static final long serialVersionUID = 5909353595776535289L;

    @Id
    @Column(name = "COUNTRY_ID", nullable = false)
    private String countryId;

    @Column(name = "COUNTRY_NAME", length = 40)
    private String countryName;

    @ManyToOne
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @OneToMany(mappedBy = "country", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Location> LOCATIONSSet;

    public Country() {}

    public Country(String countryId, String countryName, Region region) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.region = region;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Location> getLOCATIONSSet() {
        return LOCATIONSSet;
    }

    public void setLOCATIONSSet(Set<Location> LOCATIONSSet) {
        this.LOCATIONSSet = LOCATIONSSet;
    }

    public Location addLocation(Location location) {
        getLOCATIONSSet().add(location);
        location.setCountry(this);
        return location;
    }

    public Location removeLocation(Location location) {
        getLOCATIONSSet().remove(location);
        location.setCountry(null);
        return location;
    }
}
