package ufl.mc.nightscout.nightscoutpro.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Result {
	
	 private String formatted_address;
	 
	 private boolean partial_match;
	 
	 
	 
	 @JsonIgnore
	 private Object address_components;
	 
	 @JsonIgnore
	 private Object types;

	 public String getFormatted_address() {
	  return formatted_address;
	 }

	 public void setFormatted_address(String formatted_address) {
	  this.formatted_address = formatted_address;
	 }

	 public boolean isPartial_match() {
	  return partial_match;
	 }

	 public void setPartial_match(boolean partial_match) {
	  this.partial_match = partial_match;
	 }

	 

	 public Object getAddress_components() {
	  return address_components;
	 }

	 public void setAddress_components(Object address_components) {
	  this.address_components = address_components;
	 }

	 public Object getTypes() {
	  return types;
	 }

	 public void setTypes(Object types) {
	  this.types = types;
	 }
}
