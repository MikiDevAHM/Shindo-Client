package me.miki.shindo.mobends.pack;

import me.miki.shindo.mobends.data.MoBends_EntityData;

public class BendsVar {
	public static MoBends_EntityData tempData;
	
	public static float getGlobalVar(String name){
		if(name.equalsIgnoreCase("ticks")){
			if(tempData == null)
				return 0;
			return tempData.ticks;
		}else if(name.equalsIgnoreCase("ticksAfterPunch")){
			if(tempData == null)
				return 0;
			return tempData.ticksAfterPunch;
		}
		return Float.POSITIVE_INFINITY;
	}
}
