package com.thegamecommunity.excite.modding.game.file;

import java.util.Arrays;
import java.util.HashSet;

public enum AssetType {
	art, // gui image
	bwl, // boundary wall
	bin, // mcbanner
	can, // animation?
	fnf, // faninfo
	gnd, // ground
	gtx, // font?
	ili, // pathfinding lines?
	imp, // only one 0 byte asset defined in the entire game with this extension, seem unused.
	img, // image/texture
	ild, // seems to deal with how the race track is defined. Either a circleTrack (closed loop with laps) or a boundWallTrack (start and finish line, no laps and no loop)
	lng, // localization/language file
	lyt, // gui layout
	mat, // material
	mmp, // material map
	mod, // 3d model
	msh, // 3d mesh
	pth, // "path" (either for where to put the racing path on the track, the minimap, or AI pathing)
	sfx, // Sound Effect
	sf0, // Sound effect/song?
	sng, // Song
	sn0, // Song?
	sol, // track/map data
	tab, // Gui element?
	ter, // ?? terrain maybe?
	tex, // texture
	tm0, // Unknown, seems to always be one of the following: 1: be an empty file, 2: QuickLZ compressed data, or 3: null bytes. 
	trl, // tree list
	tro, // track objects
	val, // values
	
	UNRECOGNIZED(); //Unknown asset type
	
	HashSet<String> extensions = new HashSet<>();
	
	private AssetType(String... extensions) {
		this.extensions.add(this.name());
		this.extensions.addAll(Arrays.asList(extensions));
	}
	
	public static AssetType getAssetType(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if(dotIndex == -1) {
			return UNRECOGNIZED;
		}
		String extension = fileName.substring(dotIndex + 1);
		switch(extension) {
			case "art": return art;
			case "bwl": return bwl;
			case "bin": return bin;
			case "can": return can;
			case "fnf": return fnf;
			case "gnd": return gnd;
			case "gtx": return gtx;
			case "ili": return ili;
			case "imp": return imp;
			case "img": return img;
			case "ild": return ild;
			case "lng": return lng;
			case "lyt": return lyt;
			case "mat": return mat;
			case "mmp": return mmp;
			case "mod": return mod;
			case "msh": return msh;
			case "pth": return pth;
			case "sfx": return sfx;
			case "sf0": return sf0;
			case "sng": return sng;
			case "sn0": return sn0;
			case "sol": return sol;
			case "tab": return tab;
			case "tex": return tex;
			case "ter": return ter;
			case "tm0": return tm0;
			case "trl": return trl;
			case "tro": return tro;
			case "val": return val;
			default: return UNRECOGNIZED;
		}
	}
	
}
