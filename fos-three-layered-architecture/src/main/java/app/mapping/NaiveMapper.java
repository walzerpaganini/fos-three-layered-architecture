package app.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * In alcuni rari casi, può essere che vi troviate costretti a scrivere un algoritmo basato
 * sulla reflection di Java per automatizzare il mapping tra oggetti di classi diverse (per
 * esempio tra le entità del data layer e i modelli del service layer). In questa classe ho
 * scritto un algoritmo di esempio per darvi un'idea di come funzioni la reflection e di come
 * si possa applicare a questo scopo.
 * 
 * ATTENZIONE: Si tratta di algoritmo estremamente semplificato, realizzato SOLO a scopo
 * illustrativo. In generale, è molto difficile progettare algoritmi così generali, dato 
 * che bisogna tener conto di molti possibili casi e prevedere un comportamento sensato e
 * corretto per ogni situazione (cosa che questo algoritmo NON fa). A meno che non siate
 * assolutamente costretti a fare qualcosa del genere, vi consiglio piuttosto di affidarvi
 * a librerie di mapping già pronte. Per esempio:
 * 
 * https://modelmapper.org/getting-started/
 * https://mapstruct.org/
 * 
 * Detto ciò, il meccanismo pensato per questo algoritmo didattico è il seguente: riceviamo
 * in ingresso un certo oggetto (inObject) da convertire in una nuova istanza di una certa
 * classe, anch'essa ricevuta come parametro (outClass). Usiamo il costruttore di default
 * della classe di uscita per creare la nuova istanza; dopodiché, per ogni setter pubblico
 * presente all'interno della classe di uscita, cerchiamo un getter pubblico corrispondente
 * (es. setName() --> getName()) all'interno della classe dell'oggetto ricevuto in ingresso;
 * se il getter viene trovato, lo invochiamo per ottenere il valore da passare al setter 
 * dell'oggetto in uscita; infine, restituiamo il nuovo oggetto così costruito.
 * 
 * Riuscite a immaginare alcuni dei possibili casi in cui questo codice non funzionerebbe?
 */

public class NaiveMapper {
	public static <In, Out> Out map(In inObject, Class<Out> outClass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Class<?> inClass = inObject.getClass();
		
		String inClassName = inClass.getSimpleName();
		String outClassName = outClass.getSimpleName();
		
		Out outObject = invokeDefaultConstructorOf(outClass);
		
		for(Method outSetter : settersOf(outClass)) {			
			
			String outSetterName = outSetter.getName();
			System.out.println("Found setter in class " + outClassName + ": " + outSetterName);

			String inGetterName = "get" + outSetterName.substring(3);
			System.out.print("Searching getter in class " + inClassName + ": " + inGetterName + "... ");
			
			try {
				Method inGetter = outClass.getMethod(inGetterName);
				System.out.println("FOUND");
				
				Object inValue = inGetter.invoke(inObject); // --> inObject.inGetter();
				
				outSetter.invoke(outObject, inValue); // --> outObject.outSetter(inValue);
				
			} catch(NoSuchMethodException e) {
				System.out.println("NOT FOUND");
			}
			
			System.out.println();
		}
		
		return outObject;
	}
	
	private static <T> T invokeDefaultConstructorOf(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Constructor<T> defaultConstructor = clazz.getConstructor();
		return defaultConstructor.newInstance(); // --> new Class<T>();
	}
	
	private static List<Method> settersOf(Class<?> clazz) {
		List<Method> setters = new LinkedList<>();
		
		for(Method method : clazz.getMethods()) {
			if(method.getName().startsWith("set")) {
				setters.add(method);
			}
		}
		
		return setters;
	}
}
