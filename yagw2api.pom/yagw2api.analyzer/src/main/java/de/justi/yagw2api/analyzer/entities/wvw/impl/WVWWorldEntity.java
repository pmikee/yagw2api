package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.google.common.base.Objects;

import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEntity;

@Entity(name="world")
class WVWWorldEntity extends AbstractEntity implements IWVWWorldEntity{
	@Column(name="name_de", unique=true)
	private String nameDE;

	@SuppressWarnings("unused")
	private WVWWorldEntity() {}
	
	public WVWWorldEntity(String nameDE) {
		checkNotNull(nameDE);
		checkArgument(nameDE.trim().length() > 0);
		this.nameDE = nameDE;
	}
	
	@Override
	public String getNameDE() {
		return this.nameDE;
	}	
	
	public String toString() {
		return Objects.toStringHelper(this).add("super", super.toString()).add("nameDE", this.nameDE).toString();
	}
}
