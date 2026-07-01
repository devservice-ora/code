package model;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Region.findAll", query = "select o from Region o") })
@Table(name = "REGIONS")
public class Region implements Serializable {

    private static final long serialVersionUID = -5099995966925031234L;

    @Id
    @Column(name = "REGION_ID", nullable = false)
    private BigDecimal regionId;

    @Column(name = "REGION_NAME", length = 25)
    private String regionName;

    @OneToMany(mappedBy = "region", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Country> COUNTRIESSet;

    public Region() {}

    public Region(BigDecimal regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public BigDecimal getRegionId() {
        return regionId;
    }

    public void setRegionId(BigDecimal regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Set<Country> getCOUNTRIESSet() {
        return COUNTRIESSet;
    }

    public void setCOUNTRIESSet(Set<Country> COUNTRIESSet) {
        this.COUNTRIESSet = COUNTRIESSet;
    }

    public Country addCountry(Country country) {
        getCOUNTRIESSet().add(country);
        country.setRegion(this);
        return country;
    }

    public Country removeCountry(Country country) {
        getCOUNTRIESSet().remove(country);
        country.setRegion(null);
        return country;
    }
}
