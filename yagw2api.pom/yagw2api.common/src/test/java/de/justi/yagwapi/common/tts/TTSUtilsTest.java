package de.justi.yagwapi.common.tts;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Locale;

import javafx.application.Platform;

import com.sun.javafx.tk.Toolkit;

import de.justi.yagwapi.common.utils.TTSUtils;

public class TTSUtilsTest {

	public static void main(String[] args) throws IOException {
		TTSUtils.readOut(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
				Locale.GERMANY);
		checkState(Platform.isImplicitExit());
		Toolkit.getToolkit().exit();
		Platform.exit();
		System.exit(-1);
	}

}
