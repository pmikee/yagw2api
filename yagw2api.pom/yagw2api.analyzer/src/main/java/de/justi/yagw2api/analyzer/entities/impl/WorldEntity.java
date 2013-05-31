package de.justi.yagw2api.analyzer.entities.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;

@Entity(name="world")
class WorldEntity extends AbstractEntity implements IWorldEntity{
	@Column(name="name_de", unique=true, nullable=false)
	private String nameDE;
	
	@Column(name="origin_id", unique=true, nullable=false)
	private Integer originWorldId;

	public WorldEntity() {
		this.nameDE = null;
		this.originWorldId = null;
	}
	
	public void setNameDE(String nameDE) {
		checkNotNull(nameDE);
		checkArgument(nameDE.trim().length() > 0);
		this.nameDE = nameDE;
	}
	
	public void setOriginId(int originId) {
		checkArgument(originId > 0);
		this.originWorldId = originId;
	}
	
	@Override
	public Optional<String> getNameDE() {
		return Optional.fromNullable(this.nameDE);
	}	
	
	public String toString() {
		return Objects.toStringHelper(this).add("super", super.toString()).add("nameDE", this.getNameDE()).add("origin", this.getOriginWorldId()).toString();
	}

	@Override
	public Optional<Integer> getOriginWorldId() {
		return Optional.fromNullable(this.originWorldId);
	}
	
	@Override
	public int hashCode() {		
		return Objects.hashCode(this.getClass().getName(),this.originWorldId);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || ! (obj instanceof IWorldEntity)) {
			return false;
		}else{
			final IWorldEntity worldEntity = (IWorldEntity)obj;
			return Objects.equal(this.originWorldId, worldEntity.getOriginWorldId().orNull());
		}
	}

}
