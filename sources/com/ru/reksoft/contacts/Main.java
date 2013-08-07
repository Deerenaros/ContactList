/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <uvesnin@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Poul-Henning Kamp
 * ----------------------------------------------------------------------------
 */

package com.ru.reksoft.contacts;

import com.beust.jcommander.*;

public class Main {
	@Parameter ( names = { "-I", "--interactive" }, description = "Launchs in interactive mode" )
	static private boolean isInteractive = false;
	
	static private Contact[] lastSearch;
	static private java.util.Scanner cin;
	static private Contacts contacts;
	
	static public boolean needSave = true;
	
	static private void printHelp () {
		System.out.println ( "This is interactive mode of **best** contact-list application!" );
		System.out.println ( "To use one you have to use this commnds:" );
		System.out.println ( "create\n\tThis one create a new contact, than you give this command, program say that to do." );
		System.out.println ( "print all\n\tJust prints all contacts previously added." );
		System.out.println ( "search\n\tSearchs all contacts with name like inputed (program will ask)." );
		System.out.println ( "delete\n\tDeletes contact. Need search previously." );
		System.out.println ( "exit\n\tSave & Exit the program." );
		System.out.println ( "quit\n\tJust exit the program." );
	}
	
	static private void create () {
		lastSearch = null;
		System.out.println ( "Enter name of contact:" );
		String name = cin.nextLine ();
		System.out.println ( "Enter all phones numbers in one line spliting by something like comma:" );
		String phones = cin.nextLine ();
		Contact c = contacts.addContact ( name, phones );
		System.out.println ( "%s has been added!".format ( c.getName() ) );
	}
	
	static private void printall () {
		System.out.println ( contacts );
	}

	static private void search () {
		System.out.println ("As i promised - please, enter the name (or pattern) of a" +
							" human (or a robot?), whom phone number you want to find.");
		lastSearch = contacts.search ( cin.nextLine () );
		if ( lastSearch != null ) {
			System.out.println ( "#) Human's name (robot's?):\n\tphone #1;\n\tphone #2\n-----------------" );
			int i = 0;
			for ( Contact contact: lastSearch ) {
				System.out.println ( String.format ( "%d) %s", new Integer ( i++ ), contact.getName () ) );
				for ( String s: contact.getPhones () ) {
					System.out.println ( "\t" + "%s".format ( s ) );
				}
			}
		} else {
			System.out.println ( "I think you should firstly though one contact like that you want to find... I mean, no matches." );
		}
	}
	
	static private void delete () {
		if ( lastSearch != null ) {
			System.out.println ( "Input number from last SEARCH operation" );
		} else {
			System.out.println ( "Firstly - use search command in order to select applicant to be removed." );
		}
	}

	static public void main ( String[] args ) {		
		new JCommander ( new Main (), args );

		try {
			contacts = new Contacts ( System.getProperty ( "user.home" ) + "\\test.bin" );
			
			if ( isInteractive ) {
				cin = new java.util.Scanner ( System.in );
				String input;
				System.out.println ( "Enter help, if you need one." );
				while ( !( input = cin.nextLine () ).equals ( "exit" ) ) { //hm... i'm tired
					switch ( input ) {
						default: case "help":
							printHelp ();
							break;
						case "create":
							create ();
							break;
						case "print all":
							printall ();
							break;
						case "search":
							search ();
							break;
						case "delete":
							delete ();
							break;
						case "quit":
							needSave = false;
							System.exit ( 0 );
							break;
					}
				}
			}
		} catch ( Throwable e ) {
			e.getMessage();
		}
	}
}