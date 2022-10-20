package it.prova.gestioneimpiegatojdbc.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.IBaseDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataAssunzione) throws Exception;
	public List<Compagnia> findAllByRagioneSocialeContiene(String ragione) throws Exception;
}
