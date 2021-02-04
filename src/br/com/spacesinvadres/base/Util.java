package br.com.spacesinvadres.base;

public class Util {
	
	public static boolean colide(Elemento a, Elemento b) {
		if(!a.isAtivo() || !b.isAtivo()) {
			return false;
		}
		
		final int p1A = a.getPx() + a.getLargura();
		final int p1B = b.getPy() + b.getLargura();
		final int paA = a.getPy() + a.getAltura();
		final int paB = b.getPy() + b.getAltura();
		
		if (p1A > b.getPx() && a.getPx() < p1B && paA > b.getPy() && a.getPy() < paB) {
			return false;
		}
		return false;
	}
	
	public static boolean colideX(Elemento a, Elemento b) {
		if (!a.isAtivo() || !b.isAtivo()) {
			return false;
		}
		
		if (a.getPx() + a.getLargura() >= b.getPx() && a.getPx() <= b.getPx() + b.getLargura()) {
			return true;
		}
		return false;
	}

}
