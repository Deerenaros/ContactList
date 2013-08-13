/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <uvesnin@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Vesnin Yuriy
 * ----------------------------------------------------------------------------
 */

package com.ru.reksoft.contacts;

import javax.persistence.*;
import java.util.*;

/*******************************************************
 * Simply wrapper of ObjectDB classes
 ******************************************************/
class SimplyServer {
	private EntityManagerFactory emf;
	private EntityManager em;

	SimplyServer ( String path ) throws Exception {
		emf = Persistence.createEntityManagerFactory ( path );
		em = emf.createEntityManager ();
		em.getTransaction().begin();
	}
	
	List < Contact > getAll () throws Exception {
		return em.createQuery ( "SELECT c FROM Contact c", Contact.class ).getResultList ();
	}
	
	void put ( Contact c ) throws Exception {
		try {
			em.persist ( c );
		} catch ( Exception e ) {
			System.out.println ( e.getMessage () );
		}
	}
	
	boolean remove ( String n ) throws Exception {
		return ( em.createQuery ( "DELETE FROM Contact c WHERE c.myName = :n" ).setParameter ( "n", n ).executeUpdate () >= 1
			? true : false );
	}
	
	void commit () throws Exception {
		em.getTransaction ().commit ();
	}
}