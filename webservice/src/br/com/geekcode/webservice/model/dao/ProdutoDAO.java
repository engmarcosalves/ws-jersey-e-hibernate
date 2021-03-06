package br.com.geekcode.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.geekcode.webservice.exceptions.DAOException;
import br.com.geekcode.webservice.exceptions.ErrorCode;
import br.com.geekcode.webservice.model.domain.Produto;

public class ProdutoDAO {
	
	public List<Produto> getAll() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Produto> produtos = null;

		try {
			produtos = em.createQuery("select p from Produto p", Produto.class).getResultList();
			
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao recuperar todos os produtos do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		
		return produtos;
	}
	
	public Produto getById(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		Produto produto = null;
		
		if (id <= 0) {
			throw new DAOException("O id precisa ser maior que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			produto = em.find(Produto.class, id);
			
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao buscar produto por id no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		
		if (produto == null) {
			throw new DAOException("Produto de id " + id + " não existe.", ErrorCode.NOT_FOUND.getCode());
		}
		
		return produto;
	}
	
	public Produto save(Produto produto) {
		EntityManager em = JPAUtil.getEntityManager();
		
		if (!produtoIsValid(produto)) {
			throw new DAOException("Produto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			em.getTransaction().begin();
			em.persist(produto);
			em.getTransaction().commit();
			
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao salvar produo no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		
		return produto;		
	}
	
	public Produto update(Produto produto) {
		EntityManager em = JPAUtil.getEntityManager();
		Produto produtoManaged = null;
		
		if (produto.getId() <= 0) {
			throw new DAOException("O id precisa ser maior que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		if (!produtoIsValid(produto)) {
			throw new DAOException("Produto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		try {
			em.getTransaction().begin();
			produtoManaged = em.find(Produto.class, produto.getId());
			produtoManaged.setNome(produto.getNome());
			produtoManaged.setQuantidade(produto.getQuantidade());
			em.getTransaction().commit();
			
		} catch (NullPointerException e) {
			em.getTransaction().rollback();
			throw new DAOException("Produto informado para atualização não existe: " + e.getMessage(), ErrorCode.NOT_FOUND.getCode());
			
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar produto no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		
		return produtoManaged;
	}
	

	public Produto delete(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		Produto produto = null;
		
		if (id <= 0) {
			throw new DAOException("O id precisa ser maior que 0.", ErrorCode.BAD_REQUEST.getCode());
		}
				
		try {
			em.getTransaction().begin();
			produto = em.find(Produto.class, id);
			em.remove(produto);
			em.getTransaction().commit();
			
		} catch (IllegalArgumentException e) {
			em.getTransaction().rollback();
			throw new DAOException("Produto informado para a remoção não existe: " + e.getMessage(), ErrorCode.NOT_FOUND.getCode());
			
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover produto do banco de dados." + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
				
		return produto;
	}

	private boolean produtoIsValid(Produto produto) {
		
		try {
			if ((produto.getNome() == null || produto.getNome().isEmpty() || produto.getQuantidade() < 0)) {
				return false;
			}
			
		} catch (NullPointerException e) {
			throw new DAOException("Produto com dados incompletos. " + e.getMessage(), ErrorCode.BAD_REQUEST.getCode());
		}		

		return true;
	}
	
	public List<Produto> getByPagination(int firstResult, int maxResults) {
		List<Produto> produtos;
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			produtos = em.createQuery("select p from Produto p", Produto.class)
					.setFirstResult(firstResult -1)
					.setMaxResults(maxResults)
					.getResultList();
			
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao buscar produtos no banco de dados: " + e.getMessage(), ErrorCode.NOT_FOUND.getCode());
						
		} finally {
			em.close();
		}
		
		if (produtos.isEmpty()) {
			throw new DAOException("Página com produtos vazia.", ErrorCode.NOT_FOUND.getCode());
		}
		
		return produtos;
	}
	
	public List<Produto> getByName(String name) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Produto> produtos = null;
		
		try {
			produtos = em.createQuery("select p from Produto p where p.nome like :name", Produto.class)
					.setParameter("name", "%" + name + "%")
					.getResultList();			
			
		} catch (RuntimeException e) {
			throw new DAOException("Erro ao buscar produtos por nome no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		
		if (produtos.isEmpty()) {
			throw new DAOException("A consulta não encontrou produtos.", ErrorCode.NOT_FOUND.getCode());
		}
		
		return produtos;
	}
}