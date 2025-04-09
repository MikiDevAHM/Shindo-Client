package me.miki.shindo.features.changelog;

import java.awt.*;


public enum ChangelogType {
	ADDED(0, "ADICIONADO", new Color(68, 200, 129)),
	FIXED(1, "CORRIGIDO", new Color(220, 220, 68)),
	REMOVED(2, "REMOVIDO", new Color(220, 68, 68)),
	ERROR(999, "ERRO", new Color(220, 68, 68));
	
	private int id;
	private String text;
	private Color color;
	
	private ChangelogType(int id, String text, Color color) {
		this.id = id;
		this.text = text;
		this.color = color;
	}
	
	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Color getColor() {
		return color;
	}
	
	public static ChangelogType getTypeById(int id) {
		
		for(ChangelogType type : ChangelogType.values()) {
			if(type.getId() == id) {
				return type;
			}
		}
		
		return ChangelogType.ERROR;
	}
}
