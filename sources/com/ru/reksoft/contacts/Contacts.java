/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <uvesnin@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Vesnin Yuriy
 * ----------------------------------------------------------------------------
 */

package com.ru.reksoft.contacts;

import java.util.*;
import java.io.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*******************************************************
 * Contacts manager class. It's organaze connect with
 * server, providing methods to store, search and
 * delete contacts
 ******************************************************/
public class Contacts {
/*******************************************************
 * Private member type of SimplyServer, what using in
 * order to connect with server
 ******************************************************/
	private SimplyServer myServer;

/*******************************************************
 * Construct contact manager
 ******************************************************/
	public Contacts ( String filename ) throws Exception {
		try {
			myServer = new SimplyServer ( filename );
		} catch ( Exception e ) {
			System.out.println ( e.getMessage () );
		}
	}

/*******************************************************
 * Create method, works like factory
 * @param name Name is personal identificator in our
 * life, so it's identificator in this programm ;)
 * @param phones String, that must contains all phones
 * connected with named person
 ******************************************************/
	public Contact create ( String name, String phones ) throws Exception {
		Contact c = new Contact ( name, phones );
		myServer.put ( c );
		myServer.commit ();
		return c;
	}
/*******************************************************
 * Delete contact from database by name (id)
 * @param name Personal id - name
 ******************************************************/	
	public boolean delete ( String name ) throws Exception {
		if ( myServer.remove ( name ) ) {
			myServer.commit ();
			return true;
		} return false;
	}
/*******************************************************
 * Search all contacts by name pattern
 * @param namePattern Pattern of the name with
 * java's regex sintax
 ******************************************************/
	public Contact[] search ( String namePattern ) throws Exception {
		Pattern pattern =  Pattern.compile ( namePattern );
		List< Contact > myContacts = myServer.getAll (),
			lst = new LinkedList ();

		for ( Contact c: myContacts ) {
			Matcher matcher = pattern.matcher ( c.getName() );
			if ( matcher.find () ) {
				lst.add ( c );
			}
		}
		
		return lst.toArray ( new Contact[ 0 ] );
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ( "" );
		
		return sb.toString();
	}
}