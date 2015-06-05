package de.justi.yagw2api.common.math;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Math {
	private Math(){
		throw new AssertionError("no instance");
	}

	@SuppressWarnings("unchecked")
	public static final <V extends Number> V clamp(final V toClamp, final V min, final V max){
		checkNotNull(toClamp,"missing toClamp");
		checkNotNull(min, "missing min");
		checkNotNull(max,"missing max");

		if(toClamp instanceof Integer){
			return (V)(Integer)clamp(toClamp.intValue(), min.intValue(), max.intValue());
		}else if(toClamp instanceof Long){
			return (V)(Long)clamp(toClamp.longValue(), min.longValue(), max.longValue());
		}else if(toClamp instanceof Double){
			return (V)(Double)clamp(toClamp.doubleValue(), min.doubleValue(), max.doubleValue());
		}else if(toClamp instanceof Float){
			return (V)(Float)clamp(toClamp.floatValue(), min.floatValue(), max.floatValue());
		}else{
			throw new UnsupportedOperationException("unsupported number type: "+toClamp.getClass());
		}
	}

	public static final int clamp(final int toClamp, final int min, final int max){
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final long clamp(final long toClamp, final long min, final long max){
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final double clamp(final double toClamp, final double min, final double max){
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final float clamp(final float toClamp, final float min, final float max){
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

}
