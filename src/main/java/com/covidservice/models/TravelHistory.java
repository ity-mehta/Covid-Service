package com.covidservice.models;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection="travelHistory")
@CompoundIndexes({
    @CompoundIndex(name = "timefrom_timeto", def = "{'timefrom' : 1, 'timeto': 1}")
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TravelHistory {
	
	@Indexed(unique = true)
	private String _cn6ca;
	
	private String accuracylocation;
	private String address;
	private String datasource;
	private String latlong;
	private String modeoftravel;
	private String pid;
	private String placename;
	private Date timefrom;
	private Date timeto;
	private String type;
	
	public String get_cn6ca() {
		return _cn6ca;
	}
	public void set_cn6ca(String _cn6ca) {
		this._cn6ca = _cn6ca;
	}
	public String getAccuracylocation() {
		return accuracylocation;
	}
	public void setAccuracylocation(String accuracylocation) {
		this.accuracylocation = accuracylocation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getLatlong() {
		return latlong;
	}
	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}
	public String getModeoftravel() {
		return modeoftravel;
	}
	public void setModeoftravel(String modeoftravel) {
		this.modeoftravel = modeoftravel;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPlacename() {
		return placename;
	}
	public void setPlacename(String placename) {
		this.placename = placename;
	}
	
	public Date getTimefrom() {
		return timefrom;
	}
	public void setTimefrom(Date timefrom) {
		this.timefrom = timefrom;
	}
	public Date getTimeto() {
		return timeto;
	}
	public void setTimeto(Date timeto) {
		this.timeto = timeto;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
