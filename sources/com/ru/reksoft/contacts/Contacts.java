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

public class Contacts {
	private String myFilename;
	private List < Contact > myContacts;

	public Contacts ( String filename ) throws Throwable {
		File file = new File ( myFilename = filename );
		if ( !file.createNewFile () ) {
			try {
				myContacts = (List)( new ObjectInputStream ( new FileInputStream ( myFilename ) ) ).readObject ();
			} catch ( Exception e ) {
				if ( e instanceof java.io.InvalidClassException ) {
					System.out.println ( "Sorry my friend, but i see, that you use too new version of this program..." );
					System.exit ( 42 );
				} else {
					throw e;
				}
			}
		} else {
			myContacts = new LinkedList ();
		}
		
		Runtime.getRuntime ().addShutdownHook ( new Thread () {
			@Override
			public void run () {
				if ( Main.needSave ) {
					System.out.println ( "Saving..." );
					onfinalize ();
				}
			}
		} );
	}
	
	public Contact addContact ( String name, String phones ) {
		Contact c = new Contact ( name, phones );
		myContacts.add ( c );
		return c;
	}
	
	public void delete ( int i ) {
		myContacts.remove ( i );
	}
	
	public void delete ( Contact c ) {
		myContacts.remove ( c );
	}
	
	public Contact[] search ( String namePattern ) {
		Pattern pattern =  Pattern.compile ( namePattern );
		List< Contact > lst = new ArrayList ();

		for ( Contact c: myContacts ) {
			Matcher matcher = pattern.matcher ( c.getName() );
			if ( matcher.find () ) {
				lst.add ( c );
			}
		}
		
		return ( lst.size() > 0 ? lst.toArray ( new Contact[0] ) : null );
	}
	
	public void onfinalize () {
		try {
			new ObjectOutputStream ( new FileOutputStream ( myFilename, false ) ).writeObject ( myContacts );
		} catch ( Throwable e ) {
			e.getMessage();
		}
	}
	
	@Override
	public void finalize () {
		onfinalize ();
	}
	
	@Override
	public String toString () {
		System.out.println ( "Human's name: phone #1; phone #2\n-----------------" );	
		StringBuilder sb = new StringBuilder ( "" );
		
		boolean first = true;
		for ( Contact c: myContacts ) {
			if ( first ) first = false; else sb.append ( "\n" );
			sb.append ( c.getName () + ": " );
			for ( String phone: c.getPhones () ) {
				sb.append ( phone + "; " );
			}
		}
		
		return sb.toString();
	}
}