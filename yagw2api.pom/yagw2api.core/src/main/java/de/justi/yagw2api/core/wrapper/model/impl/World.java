package de.justi.yagw2api.core.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.core.wrapper.model.IUnmodifiable;
import de.justi.yagw2api.core.wrapper.model.IWorld;


class World implements IWorld {
	
	class UnmodifiableWorld implements IUnmodifiable, IWorld{

		@Override
		public int getId() {
			return World.this.getId();
		}

		@Override
		public String getName() {
			return World.this.getName();
		}

		@Override
		public void setName(String name) {
			throw new UnsupportedOperationException(this.getClass().getSimpleName()+" is instance of "+IUnmodifiable.class.getSimpleName()+" and therefore can not be modified.");
		}

		@Override
		public IWorld createUnmodifiableReference() {
			return this;
		}
		
	}
	
	private final int id;	
	private String name;
	
	public World(int id, String name) {
		checkArgument(id > 0);
		checkNotNull(name);
		this.id = id;
		this.name = name;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
	}
	@Override
	public void setName(String name) {
		checkNotNull(name);
		this.name = name;
	}
	

	@Override
	public IWorld createUnmodifiableReference() {
		return this;
	}

	@Override
	public int hashCode() {		
		return Objects.hashCode(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || ! (obj instanceof IWorld)) {
			return false;
		}else{
			final IWorld world = (IWorld)obj;
			return Objects.equal(this.id, world.getId());
		}
	}

}
