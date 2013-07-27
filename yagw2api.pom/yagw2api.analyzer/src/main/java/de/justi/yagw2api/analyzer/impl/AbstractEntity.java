package de.justi.yagw2api.analyzer.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.google.common.base.Objects;

import de.justi.yagw2api.analyzer.IEntity;

@MappedSuperclass
abstract class AbstractEntity implements IEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long id;

	@Override
	public final long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).toString();
	}
}
