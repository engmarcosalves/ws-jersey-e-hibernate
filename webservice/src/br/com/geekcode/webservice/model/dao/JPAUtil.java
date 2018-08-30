package br.com.geekcode.webservice.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	/**
	 * Este atributo é privado e estático asseguramos que haverá apenas 1 atributo
	 * deste tipo para toda a aplicação.
	 */
	private static EntityManagerFactory emf;
	
	/**
	 * Este método é estático porém publico para que possamos acessar de qualquer lugar
	 * sem precisar instanciar a classe JPAUtil.
	 * Este método tem o objetivo de retornar o entityManager (interface que utilizamos
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
