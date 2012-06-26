package org.genshin.warehouse.settings;

import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;

public class BuildInfo {
	public int majorVersion;
	public int minorVersion;
	public int lastBuild;
	public String versionCode;
	
	public BuildInfo(int majorVersion, int minorVersion, String versionCode, int lastBuild) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.lastBuild = lastBuild;
		this.versionCode = versionCode;
	}
	
	public BuildInfo() {
		this.majorVersion = 0;
		this.minorVersion = 0;
		this.lastBuild = 0;
		this.versionCode = "";
	}
	
	public String getBuildInfoLine() {
		return majorVersion + "." + minorVersion + " " + versionCode  + " [" + Warehouse.getContext().getString(R.string.build) + lastBuild + "]";
	}
}
