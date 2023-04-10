package app.layers.c.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.layers.c.data.entities.MedicalTestResult;

public interface MedicalTestResultsRepository extends CrudRepository<MedicalTestResult, Long> {
	<T> List<T> findAllByMedicalTestId(long medicalTestId, Class<T> projection);
}
