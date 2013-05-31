package de.justi.yagw2api.core.arenanet.dto.impl;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.core.arenanet.dto.IWorldNameDTO;

class WorldNameDTO implements IWorldNameDTO{
	private static final transient Pattern PATTERN = Pattern.compile("([^\\[]*)(("+Pattern.quote("[")+"[A-Z]*"+Pattern.quote("]")+")?)");
	@Since(1.0)
	@SerializedName("id")
	int id;
	@Since(1.0)
	@SerializedName("name")
	String name;
	
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).add("locale",this.getWorldLocale()).add("nameWithoutLocale", this.getNameWithoutLocale()).toString();
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		final Matcher matcher = PATTERN.matcher(this.getName());
		if(matcher.find()) {
			if(matcher.group(3)!=null) {
				String localeTag = matcher.group(3).replaceAll("\\[", "").replaceAll("\\]","").trim().toLowerCase();
				switch(localeTag) {
					case "sp":
						localeTag = "es";
						break;
				}
				return Optional.of(Locale.forLanguageTag(localeTag));
			}else {
				return Optional.absent();
			}
		}else {
			return Optional.absent();
		}
	}

	@Override
	public String getNameWithoutLocale() {
		final Matcher matcher = PATTERN.matcher(this.getName());
		if(matcher.find()) {
			return matcher.group(1).trim();
		}else {
			return this.getName();
		}
	}

	@Override
	public boolean isEurope() {
		return this.id >= 1000 && this.id <= 2000;
	}

	@Override
	public boolean isNorthAmerica() {
		return this.id >= 2000 && this.id <= 3000;
	}
}
