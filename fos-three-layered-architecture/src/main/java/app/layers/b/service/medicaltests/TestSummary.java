package app.layers.b.service.medicaltests;

import java.time.LocalDateTime;

public interface TestSummary {
	Long getId();
	Long getPatientId();
	String getDescription();
	LocalDateTime getDateTime();
}
