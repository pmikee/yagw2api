package de.justi.yagw2api.analyzer.wvw.entities.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.google.common.base.Objects;

@MappedSuperclass
abstract class AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).toString();
	}
}
