package br.com.spacesinvadres.lgj;

import java.awt.Color;
import java.awt.Graphics2D;

import br.com.spacesinvadres.base.Elemento;

public class Invader extends Elemento {

	enum Tipos {
		PEQUENO, GRANDE, MEDIO, CHEFE
	}

	private Tipos tipo;
	private boolean aberto;

	public Invader(Tipos t) {
		this.tipo = t;
		
		setLargura(20);
		setAltura(20);
	}

	@Override
	public void atualiza() {
		aberto = !aberto;
	}
	
	@Override
	public void desenha(Graphics2D g) {
		
		if(!isAtivo())
			return;
		int larg = getLargura();
		
		if(tipo == Tipos.PEQUENO) {
			larg = larg - 2;
			g.setColor(Color.BLUE);
			
			if(aberto) {
				g.fillOval(getPx(), getPy(), larg, getAltura());
				
				g.fillRect(getPx() - 5, getPy() - 5, 5, 5 );
				g.fillRect(getPx() + larg, getPy() -5, 5, 5);
				
				g.fillRect(getPx() -5, getPy() + getLargura(), 5, 5);
				g.fillRect(getPx() + larg, getPy() + larg,5,5);
			}
			else {
				g.fillRect(getPx(), getPy(), larg, getAltura());
			}
		}else if (tipo == Tipos.MEDIO) {
			g.setColor(Color.ORANGE);
			
			if(aberto)
				g.drawRect(getPx(), getPy(), larg, getAltura());
			else {
				g.fillRect(getPx(), getPy(), larg, getAltura());
			}
		} else if (tipo == Tipos.GRANDE){
			larg = larg + 4;
			
			if(aberto) {
				g.setColor(Color.DARK_GRAY);
				g.fillRect(getPx(), getPy(), getAltura(), larg);
			} else {
				g.setColor(Color.GRAY);
				g.fillRect(getPx(), getPy(), larg, getAltura());
			}
			
		} else {
			larg = larg + 10;
			
			g.setColor(Color.RED);
			g.fillOval(getPx(), getPy(), larg, getAltura());
			
			if(aberto) {
				g.setColor(Color.WHITE);
				
				g.fillRect(getPx() + 7, getPy() + getAltura() / 2 - 2, 4, 4);
				g.fillRect(getPx() + 13, getPy() + getAltura() / 2 - 2, 4, 4);
				g.fillRect(getPx() + 19, getPy() + getAltura() / 2 - 2, 4, 4);
			}
		}
	}
	
	public int getPremio() {
		switch (tipo) {
		case PEQUENO:
			return 300;
		case MEDIO:
			return 200;
		case GRANDE:
			return 100;

		default:
			return 1000;
		}
	}

}
