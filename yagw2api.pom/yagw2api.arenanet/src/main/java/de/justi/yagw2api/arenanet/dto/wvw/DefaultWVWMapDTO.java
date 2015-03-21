package de.justi.yagw2api.arenanet.dto.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultWVWMapDTO implements WVWMapDTO {

	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;
	@Since(1.0)
	@SerializedName("objectives")
	private DefaultWVWObjectiveDTO[] objectives;
	@Since(1.0)
	@SerializedName("type")
	private String type;

	@Override
	public WVWObjectiveDTO[] getObjectives() {
		return this.objectives;
	}

	@Override
	public String getType() {
		return this.type.toUpperCase();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("redScore", this.getRedScore()).add("greenScore", this.getGreenScore()).add("blueScore", this.getBlueScore())
				.add("objectiveCount", this.objectives.length).add("type", this.getType()).toString();
	}

	@Override
	public int getRedScore() {
		return this.scores[0];
	}

	@Override
	public int getGreenScore() {
		return this.scores[2];
	}

	@Override
	public int getBlueScore() {
		return this.scores[1];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(this.objectives);
		result = (prime * result) + Arrays.hashCode(this.scores);
		result = (prime * result) + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		DefaultWVWMapDTO other = (DefaultWVWMapDTO) obj;
		if (!Arrays.equals(this.objectives, other.objectives)) {
			return false;
		}
		if (!Arrays.equals(this.scores, other.scores)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
