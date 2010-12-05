package net.morlhon.wall;

import java.net.MalformedURLException;

import net.morlhon.wall.common.PivotWall;

import org.apache.pivot.wtk.DesktopApplicationContext;

/**
 * Startups a default http wall server.
 *
 * @author jlf
 */
public class BuildRadiator {

	public static void main(String[] args) throws MalformedURLException {
		BuildRadiator radiator = new BuildRadiator();
		radiator.startup(args);
	}

	public void startup(String[] args) {
		DesktopApplicationContext.main(PivotWall.class, args);
	}
}
