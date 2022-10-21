package it.prova.gestioneimpiegatojdbc.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {
	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}
	
	@Override
	public List<Impiegato> list() throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from impiegato")) {

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
				impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
				impiegatoTemp.setId(rs.getLong("ID"));

				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("NOME"));
					result.setCognome(rs.getString("COGNOME"));
					result.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					result.setDataNascita(rs.getDate("DATANASCITA"));
					result.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("UPDATE impiegato SET "
				+ "nome=?, cognome=?, codicefiscale=?, datadinascita=?, dataassunzione=? where id=?;")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione) VALUES (?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato ImpiegatoTemp = null;
		String query = "SELECT * FROM impiegato WHERE ";
		
		if(input.getNome()!= null && !input.getNome().isBlank()) {
			query += " nome LIKE '" + input.getNome() + "%' AND ";
		}
		if(input.getCognome()!= null && !input.getCognome().isBlank()) {
			query += " cognome LIKE '" + input.getCognome() + "%' AND ";
		}
		if(input.getCodiceFiscale()!= null && !input.getCodiceFiscale().isBlank()) {
			query += " codicefiscale LIKE '" + input.getCodiceFiscale() + "%' AND ";
		}
		if(input.getDataNascita()!= null) {
			query += " datanascita > '" + new java.sql.Date(input.getDataNascita().getTime()) + "' AND ";
		}
		if(input.getDataAssunzione()!= null) {
			query += " dataassunzione > '" + new java.sql.Date(input.getDataAssunzione().getTime()) + "' AND ";
		}
		query += " true=true;";
		
		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery(query)) {

			while (rs.next()) {
				ImpiegatoTemp = new Impiegato();
				ImpiegatoTemp.setNome(rs.getString("nome"));
				ImpiegatoTemp.setCognome(rs.getString("cognome"));
				ImpiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				ImpiegatoTemp.setDataNascita(rs.getDate("datanascita"));
				ImpiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				ImpiegatoTemp.setId(rs.getLong("ID"));
				result.add(ImpiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato ImpiegatoTemp = null;
		
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato "
				+ "WHERE compagnia_id = ?")) {

			ps.setLong(1, compagnia.getId());
			
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					ImpiegatoTemp = new Impiegato();
					ImpiegatoTemp.setNome(rs.getString("nome"));
					ImpiegatoTemp.setCognome(rs.getString("cognome"));
					ImpiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
					ImpiegatoTemp.setDataNascita(rs.getDate("datanascita"));
					ImpiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
					ImpiegatoTemp.setId(rs.getLong("ID"));
	
					result.add(ImpiegatoTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}

	@Override
	public int countByDataFondazioneCompagniaGreaterThan(Date data) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		int result = 0;
		
		try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM impiegato i "
				+ "INNER JOIN compagnia c ON c.id = i.compagnia_id "
				+ "WHERE datafondazione > ? "
				+ "GROUP BY i.id;")) {
			ps.setDate(1, new java.sql.Date(data.getTime()));
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = rs.getInt("COUNT(*)");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato ImpiegatoTemp = null;
		String query = "SELECT * FROM impiegato i "
				+ "INNER JOIN compagnia c ON c.id = i.compagnia_id "
				+ "WHERE dataassunzione < datafondazione;";
		
		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery(query)) {

			while (rs.next()) {
				ImpiegatoTemp = new Impiegato();
				ImpiegatoTemp.setNome(rs.getString("nome"));
				ImpiegatoTemp.setCognome(rs.getString("cognome"));
				ImpiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				ImpiegatoTemp.setDataNascita(rs.getDate("datanascita"));
				ImpiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				ImpiegatoTemp.setId(rs.getLong("ID"));
				result.add(ImpiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}

}
