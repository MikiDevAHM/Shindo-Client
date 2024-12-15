package me.miki.shindo.api.cosmetic;

import java.util.ArrayList;

public class CosmeticManager {
	
	
	   public ArrayList<Cosmetic> cosmetics = new ArrayList();

	   public CosmeticManager() {
		   
		   
	   }
	   
	    public void addCosmetic(Cosmetic cos) {
	        cosmetics.add(cos);
	    }
	    
	    public Cosmetic getMod(String name) {
	        for (Cosmetic cos : cosmetics) {
	            if (cos.getName().equals(name)) {
	                return cos;
	            }
	        }

	        return null;
	    }
}
