import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

public class mapa extends Canvas{
	private int lado;
	private int level;
	private labirinto l;
	private int atualX;
	private int atualY;
	private int vertical;
	private int horizontal;
	private int raio;
	private int contador;
	private int player;
	private int min_score;
	private int best;
	private int keys;
	private long tempo;
	
	public mapa() {
		this.lado = 15;
		this.level = 1;
		this.atualX = 0;
		this.atualY = 0;
		this.contador = 0;
		this.player = 0;
		this.min_score = 0;
	}
	
	@Override
	public void paint(Graphics g) {
		// Desenha área do labirinto
		
		g.clearRect(0, 0, 1651, 1101);
		
		this.l = new labirinto(lado);
		this.vertical = 1050/lado;
		this.horizontal = 1600/lado;
		this.raio = 600/lado;
		this.keys = l.qtdKeys;
		this.best = this.l.best;
		
		for(int i = 0; i < lado; ++i) {
			for(int j = 0; j < lado; ++j) {
				byte v = l.lab[i][j];
				
				if(v == 0) // wall
					g.setColor(Color.BLACK);
				else if(v == 1) // area
					g.setColor(Color.WHITE);
				else if(v == 2) // start
					g.setColor(Color.GREEN);
				else if(v == 4) // key
					g.setColor(Color.YELLOW);
				
				g.fillRect(5 + j*horizontal, 5 + i*vertical, horizontal, vertical);
				
				g.setColor(Color.BLACK);
				g.drawRect(5 + j*horizontal, 5 + i*vertical, horizontal, vertical);
			}
		}
		
		g.fillOval(5 + horizontal/3, 5 + vertical/3, raio, raio);
		
		// Desenha informações
		
		g.setFont(new Font("Arial", Font.BOLD, 40));
		
		g.setColor(Color.WHITE);
		g.fillRect(1685, 55, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 54, 151, 101);
		g.drawString("LEVEL", 1695, 90);
		g.drawString(String.valueOf(this.level), 1690, 145);
		
		g.setColor(Color.WHITE);
		g.fillRect(1685, 170, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 169, 151, 101);
		g.drawString("SCORE", 1690, 205);
		
		final float porcentagem = this.level == 1 ? 100.0f : ((float)this.min_score/(float)this.player)*100;
        final DecimalFormat decimalFormat = new DecimalFormat("#.0");
        final String s = decimalFormat.format(porcentagem);
		
		g.drawString(s + "%", 1695, 260);
		
		g.setColor(Color.WHITE);
		g.fillRect(1685, 285, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 284, 151, 101);
		g.drawString("BEST", 1705, 325);
		g.drawString(String.valueOf(this.best), 1690, 375);
		
		g.setColor(Color.WHITE);
		g.fillRect(1685, 400, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 399, 151, 101);
		g.drawString("YOU", 1715, 440);
		g.drawString("0", 1690, 490);
		
		g.setColor(Color.WHITE);
		g.fillRect(1685, 515, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 514, 151, 101);
		g.drawString("KEYS", 1705, 550);
		g.drawString(String.valueOf(this.keys), 1690, 610);
		
		this.tempo = System.nanoTime();
	}
	
	public void mover(final int direcao) {
		Graphics g = getGraphics();
		final int oldX = atualX;
		final int oldY = atualY;
		
		if(direcao == 0) {
			// cima
			if(this.atualX != 0 && l.lab[atualX-1][atualY] != 0) {
				atualX--;
			}
			else return;
		}
		else if(direcao == 1) {
			// baixo
			if(this.atualX != l.linhas-1 && l.lab[atualX+1][atualY] != 0) {
				atualX++;
			}
			else return;
		}
		else if(direcao == 2) {
			// esquerda
			if(this.atualY != 0 && l.lab[atualX][atualY-1] != 0) {
				atualY--;
			}
			else return;
		}
		else if(direcao == 3) {
			// direita
			if(this.atualY != l.colunas-1 && l.lab[atualX][atualY+1] != 0) {
				atualY++;
			}
			else return;
		}
		else
			return;
		
		final byte v = l.lab[atualX][atualY];
		final byte old = l.lab[oldX][oldY];
		
		if(v == 1) // area
			g.setColor(Color.WHITE);
		else if(v == 2) // start
			g.setColor(Color.GREEN);
		else if(v == 3) // end
			g.setColor(Color.RED);
		else if(v == 4) { // key
			l.lab[atualX][atualY] = 1;
			this.keys--;
			
			if(this.keys == 0) {
				g.setColor(Color.RED);
				g.fillRect(5, 5, horizontal, vertical);
				g.setColor(Color.BLACK);
				g.drawRect(5, 5, horizontal, vertical);
				this.l.lab[0][0] = 3;
			}
			
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.setColor(Color.WHITE);
			g.fillRect(1685, 515, 150, 100);
			g.setColor(Color.BLACK);
			g.drawRect(1684, 514, 151, 101);
			g.drawString("KEYS", 1705, 550);
			g.drawString(String.valueOf(this.keys), 1690, 610);
			
			g.setColor(Color.WHITE);
		}
		
		g.fillRect(5 + atualY*horizontal, 5 + atualX*vertical, horizontal, vertical);
		g.setColor(Color.BLACK);
		g.drawRect(5 + atualY*horizontal, 5 + atualX*vertical, horizontal, vertical);
		g.fillOval(5 + (horizontal/3)+(atualY*horizontal), 5 + (vertical/3)+(atualX*vertical), raio, raio);
		
		if(old == 1 || old == 4) // area or key
			g.setColor(Color.WHITE);
		else if(old == 2) // start
			g.setColor(Color.GREEN);
		else if(old == 3) // exit
			g.setColor(Color.RED);
		
		g.fillRect(5 + oldY*horizontal, 5 + oldX*vertical, horizontal, vertical);
		g.setColor(Color.BLACK);
		g.drawRect(5 + oldY*horizontal, 5 + oldX*vertical, horizontal, vertical);
		
		this.contador++;
		
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		g.fillRect(1685, 400, 150, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1684, 399, 151, 101);
		g.drawString("YOU", 1715, 440);
		g.drawString(String.valueOf(this.contador), 1690, 490);
		
		if(v == 3 && this.keys == 0) {
			this.tempo = System.nanoTime() - tempo;
			this.terminarJogo();
		}
	}
	
	private void terminarJogo() {
		// mostrar tela com dados
		final String html = "<html><h1 style='font-family: Calibri; font-size: 20pt;'>";
		final String parabens = html + "Parabéns, você completou o level " + String.valueOf(this.level) + "\n\n";
		final String tempo = html + "Tempo: " + String.valueOf((float)this.tempo/1000000000f) + "s\n";
		final String passos = html + "Passos: " + String.valueOf(this.contador) + "\n";
		final String precisaoLevel = html + "Precisão (Level): " + String.valueOf(((float)this.best/(float)this.contador)*100) + "%\n";
		
		this.player += this.contador;
		this.min_score += this.best;
		
		final String precisaoGeral = html + "Precisão (Geral): " + String.valueOf(((float)this.min_score/(float)this.player)*100) + "%\n";
		
		JOptionPane.showMessageDialog(null,parabens+tempo+passos+precisaoLevel+precisaoGeral);
		
		// atualizar dados para a próxima fase
		this.lado += 4;
		this.atualX = 0;
		this.atualY = 0;
		this.level++;
		this.contador = 0;
		
		this.paint(getGraphics());
	}
}