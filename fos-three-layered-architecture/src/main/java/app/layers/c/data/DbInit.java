package app.layers.c.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import app.layers.c.data.entities.Patient;
import app.layers.c.data.repositories.PatientsRepository;

@Component
public class DbInit implements ApplicationRunner {

	@Autowired
	PatientsRepository patientsRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Patient p1 = new Patient("Walter", "Paganini");
		Patient p2 = new Patient("Mario", "Rossi");
		Patient p3 = new Patient("Luigi", "Rossi");
		
		patientsRepo.saveAll(List.of(p1, p2, p3));		
	}
}
