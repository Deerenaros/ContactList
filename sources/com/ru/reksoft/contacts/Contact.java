/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <uvesnin@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Vesnin Yuriy
 * ----------------------------------------------------------------------------
 */

package com.ru.reksoft.contacts;

import com.google.i18n.phonenumbers.*;
import javax.persistence.*;

import java.util.*;
import java.io.*;

/*******************************************************
 * Simply class, that storage information about person 
 * that includes:<br>                                 
 * name (could be first name with last name, or only   
 * first name, or just nickname - no difference);<br>
 * list of phone numbers (without naming ones);<br>  
 * and additional information with any data also could 
 * stored;<br>
 ******************************************************/
@Entity public class Contact implements Comparable < Contact >, Serializable {
/*******************************************************
 * Simply class to store phone number in order
 * to allow ObjectDB store instances of this class
 ******************************************************/
	@Embeddable static class PNumber {
		public long code;
		public long number;
		
		public PNumber ( Phonenumber.PhoneNumber p ) {
			code = p.getCountryCode ();
			number = p.getNationalNumber();
		}
		
		@Override
		public String toString () {
			return "+" + code + " " + number;
		}
	}
/*******************************************************
 * Defines name of person, also using as id
 ******************************************************/
	@Id
	private String myName;
/*******************************************************
 * Contains all phone number, that are associated with
 * person
 ******************************************************/
	@OneToMany
	private List < PNumber > myPhonesNumbers;

/*******************************************************
 * Construct contact from
 * @param name 		Name of person
 * @param phones 	String, contained all phones of person
 *					wroted in international style
 ******************************************************/
	Contact ( String name, String phones ) {
		PhoneNumberUtil util = PhoneNumberUtil.getInstance ();
		myName = name;
		myPhonesNumbers = new LinkedList ();
		
		Iterator < PhoneNumberMatch > i = util.findNumbers ( phones, "RU" ).iterator ();
		while ( i.hasNext () ) {
			myPhonesNumbers.add ( new PNumber ( i.next ().number () ) );
		}
	}
/*******************************************************
 * @return String, that contains name of person
 ******************************************************/
	public String getName () {
		return myName;
	}

/*******************************************************
 * @return Array of strings, contains a phone number
 * of the person, one phone number in each
 ******************************************************/
	public String[] getPhones () {
		PhoneNumberUtil util = PhoneNumberUtil.getInstance ();
		List< String > lst = new ArrayList ();
		
		for ( PNumber p: myPhonesNumbers ) {
			lst.add ( p.toString () );
		}
		
		return lst.toArray ( new String[0] );
	}

/*******************************************************
 * Comparing two contacts firstly by name, secondary
 * by phone numbers. In lexicographical order
 * @return	Negative, if aurgument lesser than self,
 *			positive, if self lesser aurgument,
 *			otherwise - 0
 ******************************************************/
	@Override
	public int compareTo ( Contact cnt ) {
		String cmp1 = myName + myPhonesNumbers,
			cmp2 = cnt.myName + cnt.myPhonesNumbers;
		return cmp1.compareTo ( cmp2 );
	}

/*******************************************************
 * Comparing in order to check equality. 
 * @return True when fully equals. Otherwise - false
 ******************************************************/
	@Override
	public boolean equals ( Object obj ) {
		if ( obj instanceof Contact ) {
			Contact cnt = (Contact)obj;
			// I really do not know, when we should take equals of two contacts, so, i think, number is primary.
			return	myPhonesNumbers.equals ( cnt.myPhonesNumbers );
		}
		
		return false;
	}
	
	@Override
	public String toString () {
		return myName + ": " + myPhonesNumbers;
	}
}