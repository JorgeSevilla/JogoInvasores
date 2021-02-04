package br.com.spacesinvadres.lgj;

import java.awt.Color;
import java.awt.Graphics2D;

import br.com.spacesinvadres.base.Elemento;

public class Tiro extends Elemento {

	private boolean inimigo;

	public Tiro() {
		setLargura(5);
		setAltura(5);
	}

	public Tiro(boolean inimigo) {
		this();
		this.inimigo = inimigo;
	}

	@Override
	public void desenha(Graphics2D g) {
		if (!isAtivo()) {
			return;
		}

		g.fillRect(getPx(), getPx(), getLargura(), getAltura());
		g.setColor(inimigo ? Color.RED : Color.WHITE);
	}
}
