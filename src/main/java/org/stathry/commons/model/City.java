/*
 * Copyright https://github.com/stathry/generator All rights reserved.
 */
package org.stathry.commons.model;

import java.io.Serializable;

/**
 * @author Auto-generated by FreeMarker
 * @date 2018-10-19 11:14
 */

public class City implements Serializable {

    private Integer id;
    /**
     * 城市名称
     */
    private String name;

    private String countryCode;

    private String district;

    private Integer population;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "City[" + "id = " + id
                + ", name = " + name
                + ", countryCode = " + countryCode
                + ", district = " + district
                + ", population = " + population
                + "]";
    }
}  