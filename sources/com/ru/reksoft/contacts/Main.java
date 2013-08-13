/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <uvesnin@gmail.com> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return Poul-Henning Kamp
 * ----------------------------------------------------------------------------
 */

package com.ru.reksoft.contacts;

import com.google.i18n.phonenumbers.*;
import com.beust.jcommander.*;


import java.util.*;
import java.io.File;

class Main {
	static private interface Functor < R, A > {
		public R calc ( A arg ) throws Exception;
	}

	@Parameter ( names = { "-I", "--interactive" }, description = "Launchs in interactive mode" )
	static private boolean isInteractive = false;
	@Parameter ( names = { "-s", "--serverpath" }, description = "Path to the server" )
	static private String serverPath = "home:\\contact-list\\contacts.odb";
	@Parameter ( names = { "-?", "--help" }, description = "Show help" )
	static private boolean help = false;
	
	static private Map < String, Functor < Object, String[] > >  commands;
	
	static private HashMap getCommands ( Object obj ) {
		final Contacts contacts = (Contacts)obj;
		HashMap < String, Functor < Object, String[] > > cmds = new HashMap ();
		cmds.put ( "search", new Functor < Object, String[] > () {
			@Override
			public Object calc ( String[] arg ) throws Exception {
				if ( arg.length != 1 ) {
					throw new Exception ( "Search: Wrong arguments!" );
				}
				for ( Contact c: contacts.search ( arg[ 0 ] ) ) {
					System.out.println ( c.getName () );
					for ( String p: c.getPhones () ) {
						System.out.println ( String.format ( "\t%s", p ) );
					}
				}
				return null;
			}
		} );
		
		cmds.put ( "delete", new Functor < Object, String[] > () {
			@Override
			public Object calc ( String[] arg ) throws Exception {
				if ( arg.length != 1 ) {
					throw new Exception ( "Search: Wrong arguments!" );
				}
				
				if ( contacts.delete ( arg[ 0 ] ) ) {
					System.out.println ( String.format ( "%s has been deleted", arg[ 0 ] ) );
				} else {
					System.out.println ( String.format ( "%s has NOT been deleted", arg[ 0 ] ) );
				}
				return null;
			}
		} );
		
		cmds.put ( "create", new Functor < Object, String[] > () {
			@Override
			public Object calc ( String[] arg ) throws Exception {
				if ( arg.length != 1 ) {
					throw new Exception ( "Search: Wrong arguments!" );
				}
				
				System.out.println ( String.format ( "Enter %s's phones number", arg[ 0 ] ) );
				contacts.create ( arg[ 0 ], new Scanner ( System.in ).nextLine () );
				return null;
			}
		} );
		
		cmds.put ( "exit", new Functor < Object, String[] > () {
			@Override
			public Object calc ( String[] arg ) throws Exception {
				System.exit ( 0 );
				return null;
			}
		} );
		
		cmds.put ( "help", new Functor < Object, String[] > () {
			@Override
			public Object calc ( String[] arg ) throws Exception {
				return null;
			}
		} );
		
		return cmds;
	}
	
	static public void main ( String[] args ) {
		try {
			new JCommander ( new Main (), args );
		} catch ( Exception e ) {
			new JCommander ( new Main () ).usage ();
			System.out.println ( "But: %s".format ( e.getMessage () ) );
			System.exit ( 1 );
		} finally {
			if ( help ) {
				new JCommander ( new Main () ).usage ();
				System.exit ( 0 );
			}
			if ( serverPath.indexOf ( "home:" ) == 0 ) {
				serverPath = System.getProperty ( "user.home" ) + serverPath.substring ( "home:".length() );
				File serv = new File ( serverPath );
				if ( !serv.exists () &&
				!serv.getParentFile ().exists () &&
				!serv.getParentFile ().mkdirs () ) {
					System.out.println ( "Illegal server filename! Exiting..." );
					System.exit ( 1 );
				}
			}
		}
		
		
		try {
			Contacts contacts = new Contacts ( serverPath );
			Scanner cin = new Scanner ( System.in );
			Map commands = getCommands ( contacts );
			if ( isInteractive ) {
				for ( ;; ) {
					try {
						String[] cmd = cin.nextLine ().split ( " " );
						Functor f = (Functor) commands.get ( cmd[ 0 ] );
						f.calc ( Arrays.copyOfRange ( cmd, 1, cmd.length ) );
					} catch ( Exception e ) {
						System.out.println ( String.format ( "ERROR: %s ", e.getMessage () ) );
					}
				}
			} else {
				//to do
			}
		} catch ( Throwable e ) {
			System.out.println ( String.format ( "FATAL ERROR: %s", e.getMessage () ) );
		}
	}
}