package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.compagnia.*;
import it.prova.gestioneimpiegatojdbc.impiegato.*;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class TestGestioneImpiegato {

	public static void main(String[] args) {
		ImpiegatoDAOImpl impiegatoDAOInstance = null;
		CompagniaDAOImpl compagniaDAOInstance = null;
		
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			
			// ecco chi 'inietta' la connection: il chiamante
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);
			compagniaDAOInstance = new CompagniaDAOImpl(connection);

			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			////////////////// COMPAGNIE
			//inserisco
			testInsertCompagnia(compagniaDAOInstance);
			System.out.println("In tabella Compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
			
			//elimino in modo da non avere duplicati
			testDeleteCompagnia(compagniaDAOInstance);
			System.out.println("In tabella Compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
			
			testFindByExampleCompagnia(compagniaDAOInstance);
			System.out.println("In tabella Compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
			
			testFindAllByRagioneSocialeContiene(compagniaDAOInstance);
			System.out.println("In tabella Compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
			
			////////////////// IMPIEGATI
			//inserisco
			testInsertImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella Impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
			
			//elimino in modo da non avere duplicati
			testDeleteImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella Compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
			
			testFindByExampleImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella Impiegato ci sono " + compagniaDAOInstance.list().size() + " elementi.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//test compagnia
	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInsertCompagnia inizio.............");
		int quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("Compagnia1", 1000000, new Date()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertCompagnia : FAILED");

		System.out.println(".......testInsertCompagnia fine: PASSED.............");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testDeleteCompagnia inizio.............");
		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, non ci sono voci sul DB");

		Compagnia ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		compagniaDAOInstance.delete(ultimoDellaLista);
		
		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = compagniaDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteCompagnia fine: PASSED.............");
	}
	
	private static void testFindByExampleCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindByExampleCompagnia inizio.............");
		Compagnia example = new Compagnia();
		
		example.setRagioneSociale("co");
		//example.setFatturatoAnnuo(1000);
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		example.setDataFondazione(dataCreazione);
		
		List<Compagnia> elencoExample = compagniaDAOInstance.findByExample(example);
		for (Compagnia userItem : elencoExample) {
			System.out.println(userItem.toString());
		}
		System.out.println(".......testFindByExampleCompagnia fine: PASSED.............");
	}
	
	private static void testFindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindAllByRagioneSocialeContiene inizio.............");
		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindAllByCognome : FAILED, non ci sono voci sul DB");

		// ora provo ad estrarli e devono avere tutti data successiva a quella scelta
		List<Compagnia> elencoRagione = compagniaDAOInstance.findAllByRagioneSocialeContiene("co");
		for (Compagnia userItem : elencoRagione) {
			System.out.println(userItem.toString());
		}
		System.out.println(".......testFindAllByRagioneSocialeContiene fine: PASSED.............");
	}
	
	//test Impiegato
	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInsertImpiegato inizio.............");
		int quantiElementiInseriti = impiegatoDAOInstance
				.insert(new Impiegato("Mario", "Rossi", "MRRSS594029181XD", new Date(), new Date()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertImpiegato : FAILED");

		System.out.println(".......testInsertImpiegato fine: PASSED.............");
	}
	
	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testDeleteImpiegato inizio.............");
		List<Impiegato> elencoVociPresenti = impiegatoDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteImpiegato : FAILED, non ci sono voci sul DB");

		Impiegato ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		impiegatoDAOInstance.delete(ultimoDellaLista);
		
		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = impiegatoDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteImpiegato : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteImpiegato fine: PASSED.............");
	}
	
	private static void testFindByExampleImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testFindByExampleImpiegato inizio.............");
		Impiegato example = new Impiegato();
		
		example.setNome("ma");
		example.setCognome("ro");
		//example.setCodiceFiscale("M");
		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		example.setDataNascita(dataCreazione);
		Date dataCreazione2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		//example.setDataAssunzione(dataCreazione);
		
		List<Impiegato> elencoExample = impiegatoDAOInstance.findByExample(example);
		for (Impiegato userItem : elencoExample) {
			System.out.println(userItem.toString());
		}
		System.out.println(".......testFindByExampleImpiegato fine: PASSED.............");
	}
}
