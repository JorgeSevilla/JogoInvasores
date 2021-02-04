package br.com.spacesinvadres.lgj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.media.sound.EmergencySoundbank;

import br.com.spacesinvadres.base.Elemento;
import br.com.spacesinvadres.base.Texto;
import br.com.spacesinvadres.base.Util;
import br.com.spacesinvadres.lgj.Invader.Tipos;

public class Jogo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int FPS = 100 / 20;
	private static final int JANELA_ALTURA = 680;
	private static final int JANELA_LARGURA = 540;

	private JPanel tela;
	private Graphics2D g2d;
	private BufferedImage buffer;

	private boolean[] controleTecla = new boolean[5];

	public Jogo() {
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				setaTecla(e.getKeyCode(), false);
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				setaTecla(e.getKeyCode(), true);
				
			}
		});
		
		buffer = new BufferedImage(JANELA_LARGURA, JANELA_ALTURA, BufferedImage.TYPE_INT_RGB);
		
		g2d = buffer.createGraphics();
		
		tela = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(buffer, 0, 0, null);
			}
		};
		
		getContentPane().add(tela);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(JANELA_LARGURA, JANELA_ALTURA);
		setVisible(true);
	}

	private void setaTecla(int tecla, boolean pressionada) {
		switch (tecla) {
		case KeyEvent.VK_UP:
			controleTecla[0] = pressionada;
			break;
		case KeyEvent.VK_DOWN:
			controleTecla[1] = pressionada;
			break;
		case KeyEvent.VK_LEFT:
			controleTecla[2] = pressionada;
			break;
		case KeyEvent.VK_RIGHT:
			controleTecla[3] = pressionada;
			break;
		case KeyEvent.VK_SPACE:
			controleTecla[4] = pressionada;
			break;
		}
	}

	private int vidas = 3;
	private Elemento vida = new Tanque();
	private Elemento tiroTanque;
	private Elemento tiroChefe;
	private Elemento[] tiros = new Tiro[3];
	private Texto texto = new Texto();
	private Invader chefe;
	private Elemento tanque;
	private Invader[][] invasores = new Invader[11][5];
	private Invader.Tipos[] tipoPorLinha = { Tipos.PEQUENO, Tipos.MEDIO, Tipos.MEDIO, Tipos.GRANDE, Tipos.GRANDE };
	private int linhaBase = 60;
	private int espacamento = 15;
	private int destruidos = 0;
	private int dir;
	private int totalinimigos;
	private int contadorEspera;
	boolean novaLinha;
	boolean moverInimigos;
	private int contador;
	private int pontos;
	private int level = 1;
	private Random rand = new Random();
	
	private void carregarJogo() {
		tanque = new Tanque();
		//tanque.seVel(3);
		tanque.setAtivo(true);
		tanque.setPx(tela.getWidth() / 2 - tanque.getLargura() / 2);
		tanque.setPy(tela.getHeight() - tanque.getAltura() - linhaBase);
		
		tiroTanque = new Tiro();
		tiroTanque.setVel(-15);
		
		chefe = new Invader(Invader.Tipos.CHEFE);
		
		tiroChefe = new Tiro(true);
		tiroChefe.setVel(29);
		tiroChefe.setAltura(15);
		
		for (int i = 0; i < tiros.length; i++) {
			tiros[i] = new Tiro(true);
		}
		
		for (int i = 0; i < invasores.length; i++) {
			for (int j = 0; j < invasores[i].length; j++) {
				Invader e = new Invader(tipoPorLinha[j]);
				
				e.setAtivo(true);
				
				e.setPx(i * e.getLargura() + (i + 1) * espacamento);
				e.setPy(i * e.getAltura() + j * espacamento + linhaBase);
				
				invasores[i][j] = e;
			}
		}
		
		dir = 1;
		
		totalinimigos = invasores.length * invasores[0].length;
		contadorEspera = totalinimigos / level;
		
	}
	
	public void iniciarJogo() {
		long prxAtualizacao = 0;
		
		while(true){
			if(System.currentTimeMillis() >= prxAtualizacao) {
				
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, JANELA_LARGURA, JANELA_ALTURA);
				
				if (destruidos == totalinimigos) {
					destruidos = 0;
					level++;
					carregarJogo();
					
					continue;
				}
				
				if (contador > contadorEspera) {
					moverInimigos = true;
					contador = 0;
					contadorEspera = totalinimigos - destruidos - level * level;
				} else {
					contador++;
				}
				
				if (tanque.isAtivo()) {
					if (controleTecla[2]) {
						tanque.setPx(tanque.getPx() - tanque.getVel());
					} else if(controleTecla[3]) {
						tanque.setPx(tanque.getPx() + tanque.getVel());
					}
				}
				
				if (controleTecla[4] && !tiroTanque.isAtivo()) {
					tiroTanque.setPx(tanque.getPx() + tanque.getLargura() / 2 - tiroTanque.getLargura() / 2);
					tiroTanque.setPy(tanque.getPy() - tiroTanque.getAltura());
					tiroTanque.setAtivo(true);
				}
				
				if (chefe.isAtivo()) {
					chefe.intcPx(tanque.getVel() - 1);
					
					if (!tiroChefe.isAtivo() && Util.colideX(chefe, tanque)) {
						addTiroInimigo(chefe, tiroChefe);
					}
					
					if (chefe.getPx() > tela.getWidth()) {
						chefe.setAtivo(false);
					}
				}
				
				boolean colideBordas = false;
				for (int j = invasores[0].length - 1; j >= 0; j--) {
					
					for (int i = 0; i < invasores.length; i++) {
						Invader inv = invasores[i][j];
						
						if (!inv.isAtivo()) {
							continue;
						}
						
						if (Util.colide(tiroTanque, inv)) {
							inv.setAtivo(false);
							tiroTanque.setAtivo(false);
							
							destruidos++;
							pontos = pontos + inv.getPremio() * level;
							
							continue;
						}
						
						if (moverInimigos) {
							inv.atualiza();
							
							if (novaLinha) {
								inv.setPy(inv.getPy() + inv.getAltura() + espacamento);
							} else {
								inv.intcPx(espacamento * dir);
							}
							
							if (!novaLinha && !colideBordas) {
								int pxEsq = inv.getPx() - espacamento;
								int pxDir = inv.getPx() + inv.getLargura() + espacamento;
								
								if (pxEsq <= 0 || pxDir >+ tela.getWidth()) {
									colideBordas = true;
								}
								
							}
							
							if (!tiros[0].isAtivo() && inv.getPx() < tanque.getPx()) {
								addTiroInimigo(inv, tiros[0]);
								
							} else if (!tiros[0].isAtivo() && inv.getPx() > tanque.getPx() && inv.getPx() < tanque.getPx() + tanque.getLargura()) {
								addTiroInimigo(inv, tiros[1]);
							
							}
							
							if (!chefe.isAtivo() && rand.nextInt(500) == destruidos) {
								chefe.setPx(0);
								chefe.setAtivo(true);
							}
						}
					}
				}
				
				if (moverInimigos && novaLinha) {
					dir *= -1;
					novaLinha = false;
				
				} else if (moverInimigos && colideBordas) {
					novaLinha = true;
				}
				
				moverInimigos = false;
				
				if (tiroTanque.isAtivo()) {
					tiroTanque.incPy(tiroTanque.getVel());
					
					if (Util.colide(tiroTanque, chefe)) {
						pontos = pontos + chefe.getPremio() * level;
						chefe.setAtivo(false);
						tiroTanque.setAtivo(false);
					
					} else if (tiroTanque.getPy() < 0) {
						tiroTanque.setAtivo(false);
					}
					
					tiroTanque.desenha(g2d);
					
				}
				
				if (tiroChefe.isAtivo()) {
					tiroChefe.incPy(tiroChefe.getVel());
					
					if (Util.colide(tiroChefe, tanque)) {
						vidas--;
						tiroChefe.setAtivo(false);
						
					} else if (tiroChefe.getPy() > tela.getHeight() - linhaBase - tiroChefe.getAltura()) {
						tiroChefe.setAtivo(false);
					
					} else
						tiroChefe.desenha(g2d);
				}
				
				for (int i = 0; i < tiros.length; i++) {
					if (tiros[i].isAtivo()) {
						tiros[i].incPy(+10);
						
						if (Util.colide(tiros[i], tanque)) {
							vidas--;
							tiros[i].setAtivo(false);
						
						} else if (tiros[i].getPy() > tela.getHeight() - linhaBase - tiros[i].getAltura()) {
							tiros[i].setAtivo(false);
							
							tiros[i].desenha(g2d);
						}
					}
				}
				
				for (int i = 0; i < invasores.length; i++) {
					for (int j = 0; j < invasores[i].length; j++) {
						Invader e = invasores[i][j];
						e.desenha(g2d);
					}
				}
				
				tanque.atualiza();
				tanque.desenha(g2d);
				
				chefe.atualiza();
				chefe.desenha(g2d);
				
				g2d.setColor(Color.WHITE);
				
				texto.desenha(g2d, String.valueOf(pontos), 10, 20);
				texto.desenha(g2d, "Level " + level, tela.getWidth() - 100, 20);
				texto.desenha(g2d, String.valueOf(vidas), 10, tela.getHeight() - 10);
				
				g2d.setColor(Color.GREEN);
				g2d.drawLine(0, tela.getHeight() - linhaBase, tela.getHeight(), tela.getHeight() - linhaBase);
				
				for (int i = 0; i < vidas; i++) {
					vida.setPx(i * vida.getLargura() + i * espacamento);
					vida.setPy(tela.getHeight() - vida.getAltura());
					
					vida.desenha(g2d);
				}
				
				tela.repaint();
				
				prxAtualizacao = System.currentTimeMillis() + FPS;
			}
		}
	}
	
	public void addTiroInimigo(Elemento inimigo, Elemento tiro) {
		tiro.setAtivo(true);
		tiro.setPx(inimigo.getPx() + inimigo.getLargura() / 2 - tiro.getLargura() / 2);
		tiro.setPy(inimigo.getPy() + inimigo.getAltura());
		
	}
	
	public static void main(String[] args) {
		Jogo jogo = new Jogo();
		jogo.carregarJogo();
		jogo.iniciarJogo();
	}
}
