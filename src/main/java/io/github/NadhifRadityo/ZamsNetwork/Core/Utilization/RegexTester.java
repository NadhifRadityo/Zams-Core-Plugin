package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexTester {
	private String regex;
	private String description;
	
	public RegexTester(String regex) {
		this.regex = regex;
	}
	
	public String getRegex() {
		return regex;
	}
	
    public boolean isValid() {
        try {
            Pattern.compile(this.regex);
        } catch (PatternSyntaxException exception) {
            this.description = exception.getDescription();
            return false;
        }
        return true;
    }
    
    public String getError() {
    	return description;
    }
}
