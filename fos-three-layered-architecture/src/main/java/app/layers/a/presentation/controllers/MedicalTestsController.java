package app.layers.a.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.layers.b.service.medicaltests.MedicalTestsService;
import app.layers.b.service.medicaltests.TestDetails;
import app.layers.b.service.medicaltests.TestSummary;

@RestController
@RequestMapping("medical-tests")
public class MedicalTestsController {

	private MedicalTestsService medicalTestsService;
	
	@Autowired
	public MedicalTestsController(MedicalTestsService medicalTestsService) {
		this.medicalTestsService = medicalTestsService;
	}
	
	@GetMapping
	public List<TestSummary> getMedicalTests() {
		return medicalTestsService.getAllTests();
	}
	
	@GetMapping("{id}")
	public TestDetails getMedicalTestDetails(@PathVariable long id) {
		return medicalTestsService.getTestDetails(id);
	}
	
	/**
	 * {
	 * 	  description: "Analisi del sangue",
	 *    patientId: 1,
	 *    results: [
	 *    	{
	 *         paramName: "Ca++",
	 *         paramValue: 15.6
	 *      },
	 *      {
	 *         paramName: "K",
	 *         paramValue: 66.7
	 *      }
	 *    ]
	 * }
	 * @throws Exception 
	 */
	
	@PostMapping
	public TestDetails createNewTest(@RequestBody TestDetails testDetails) throws Exception {
		return medicalTestsService.saveTest(testDetails);
	}
	
	@ExceptionHandler({ UsernameNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private String handleUserNotFound(UsernameNotFoundException e) {
		return "Utente non trovato.";
	}
}
