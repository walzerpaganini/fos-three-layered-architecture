package app.layers.c.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.layers.c.data.entities.MedicalTest;

public interface MedicalTestsRepository extends CrudRepository<MedicalTest, Long> {
	<T> List<T> findBy(Class<T> projection);
	<T> T findById(long id, Class<T> projection);
	<T> List<T> findAllByPatientId(long patientId, Class<T> projection);
}