package br.com.geekcode.webservice.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	/**
	 * Este atributo � privado e est�tico asseguramos que haver� apenas 1 atributo
	 * deste tipo para toda a aplica��o.
	 */
	private static EntityManagerFactory emf;
	
	/**
	 * Este m�todo � est�tico por�m publico para que possamos acessar de qualquer lugar
	 * sem precisar instanciar a classe JPAUtil.
	 * Este m�todo tem o objetivo de retornar o entityManager (interface que utilizamos
	 * para nos comunicar com o banco de dados)
	 * @return
	 */
	public static EntityManager getEntityManager() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("produtos");
		}
		return emf.createEntityManager();
	}

}
