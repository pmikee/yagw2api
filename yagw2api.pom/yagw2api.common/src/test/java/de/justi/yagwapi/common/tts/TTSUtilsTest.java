package de.justi.yagwapi.common.tts;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Locale;

import javafx.application.Platform;

import com.sun.javafx.tk.Toolkit;

import de.justi.yagwapi.common.TTSUtils;

public class TTSUtilsTest {

	public static void main(final String[] args) throws IOException {

		TTSUtils.readOut(
				"The rule of the Jagiellon dynasty spanned the late Middle Ages and early Modern Era of Polish history. Beginning with the Lithuanian Grand Duke Jogaila (Władysław II Jagiełło), the Jagiellon dynasty (1386–1572) formed the Polish–Lithuanian union. The partnership brought vast Lithuania-controlled Rus' areas into Poland's sphere of influence and proved beneficial for the Poles and Lithuanians, who coexisted and cooperated in one of the largest political entities in Europe for the next four centuries. In the Baltic Sea region Poland's struggle with the Teutonic Knights continued and included the Battle of Grunwald (1410), where a Polish-Lithuanian army inflicted a decisive defeat on the Teutonic Knights, both countries' main adversary, allowing Poland's and Lithuania's territorial expansion into the far north region of Livonia.[19] In 1466, after the Thirteen Years' War, King Casimir IV Jagiellon gave royal consent to the milestone Peace of Thorn, which created the future Duchy of Prussia, a Polish vassal. The Jagiellons at one point also established dynastic control over the kingdoms of Bohemia (1471 onwards) and Hungary.[20][21] In the south Poland confronted the Ottoman Empire and the Crimean Tatars (by whom they were attacked on 75 separate occasions between 1474 and 1569),[22] and in the east helped Lithuania fight the Grand Duchy of Moscow. Some historians estimate that Crimean Tatar slave-raiding cost Poland one million of its population from 1494 to 1694.[23]",
				Locale.ITALIAN);
		checkState(Platform.isImplicitExit());
		Toolkit.getToolkit().exit();
		Platform.exit();
		System.exit(-1);
	}

}
