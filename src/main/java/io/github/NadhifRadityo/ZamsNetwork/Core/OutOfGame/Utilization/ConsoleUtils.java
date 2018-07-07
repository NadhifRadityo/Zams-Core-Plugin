package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Utilization;

import java.io.PrintStream;

public class ConsoleUtils {
	private static PrintStream originalStdout = System.out;
	private static PrintStream originalErrorOut = System.err;
	
	public static void setOutputToDefault() {
		System.setOut(originalStdout);
	}
	public static void setErrorOutputToDefault() {
		System.setErr(originalErrorOut);
	}
	
	public static void setOuput(PrintStream in) {
		System.setOut(in);
	}
	public static void setError(PrintStream err) {
		System.setErr(err);
	}
}
