package app.layers.b.service.medicaltests;

public class TestResult {
	private String paramName; // Es. Ca++
	private Float paramValue; // Es. 16.8
	
	public TestResult(String paramName, Float paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Float getParamValue() {
		return paramValue;
	}

	public void setParamValue(Float paramValue) {
		this.paramValue = paramValue;
	}
}
