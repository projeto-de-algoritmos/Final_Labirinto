import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class tela extends JFrame{
	private mapa m;
	private JPanel topPanel;
	
	public tela() {
		preparar_mapa();
		preparar_tela();
	}
	
	private void preparar_mapa() {
		m = new mapa();
		m.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				final int key = e.getKeyCode();
				switch(key) { 
		        case KeyEvent.VK_UP:
		            m.mover(0);
		            break;
		        case KeyEvent.VK_DOWN:
		        	m.mover(1);
		            break;
		        case KeyEvent.VK_LEFT:
		        	m.mover(2);
		            break;
		        case KeyEvent.VK_RIGHT :
		        	m.mover(3);
		            break;
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
	
	private void preparar_tela() {
		setTitle("Lista 6 - Labirinto");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		prepara_top();
		
		add(topPanel, BorderLayout.PAGE_START);
	}
	
	private void prepara_top() {
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(1910, 1070));
		topPanel.setLayout(new BorderLayout());
		topPanel.add(m, BorderLayout.CENTER);
	}
}