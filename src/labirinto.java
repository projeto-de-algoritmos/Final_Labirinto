import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map;
import java.util.HashMap;

public class labirinto {
	public final int linhas;
	public final int colunas;
	public final int qtdKeys;
	public byte lab[][];
	public int pontosImportantesX[];
	public int pontosImportantesY[];
	public int best;
	private Map<Integer, Integer> m;
	private graph g;
	
	public labirinto(final int lado) {
		this.linhas = lado;
		this.colunas = lado;
		this.qtdKeys = lado/3;
		this.lab = new byte[lado][lado];
		this.pontosImportantesX = new int[this.qtdKeys];
		this.pontosImportantesY = new int[this.qtdKeys];
		
		for(int i = 0; i < lado; ++i) {
			for(int j = 0; j < lado; ++j) {
				lab[i][j] = 0;
			}
		}
		
		final LinkedList<int[]> frontiers = new LinkedList<>();
		int x = 0;
        int y = 0;
        frontiers.add(new int[]{x,y,x,y});
        
        while (!frontiers.isEmpty() ){
            final int[] f = frontiers.remove(ThreadLocalRandom.current().nextInt(0, frontiers.size()));
            x = f[2];
            y = f[3];
            if (lab[x][y] == 0) {
                lab[f[0]][f[1]] = lab[x][y] = 1;
                if ( x >= 2 && lab[x-2][y] == 0)
                    frontiers.add( new int[]{x-1,y,x-2,y} );
                if ( y >= 2 && lab[x][y-2] == 0)
                    frontiers.add( new int[]{x,y-1,x,y-2} );
                if ( x < linhas-2 && lab[x+2][y] == 0)
                    frontiers.add( new int[]{x+1,y,x+2,y} );
                if ( y < colunas-2 && lab[x][y+2] == 0)
                    frontiers.add( new int[]{x,y+1,x,y+2} );
            }
        }
        
        lab[0][0] = 2;
        
        int contador = 0;
        
        this.m = new HashMap<Integer, Integer>();
        m.put(0, 0);
        
        while(contador < this.qtdKeys) {
        	final int posX = ThreadLocalRandom.current().nextInt(0, this.linhas);
        	final int posY = ThreadLocalRandom.current().nextInt(0, this.colunas);
        	
        	if(lab[posX][posY] == 1) {
        		lab[posX][posY] = 4;
        		
        		this.pontosImportantesX[contador] = posX;
        		this.pontosImportantesY[contador] = posY;
        		
        		contador++;
        		
        		m.put(this.converter(posX, posY), contador);
        	}
        }
        
        contador = 0;
        
        while(contador < this.qtdKeys+2) {
        	final int posX = ThreadLocalRandom.current().nextInt(0, this.linhas);
        	final int posY = ThreadLocalRandom.current().nextInt(0, this.colunas);
        	
        	if(this.canMakeShortcut(posX, posY)) {
        		lab[posX][posY] = 1;
        		contador++;
        	}
        }
               
        this.g = new graph(this.qtdKeys+1);
        
        this.calcularDistancias(0, 0);
        
        for(int i = 0; i < this.qtdKeys; ++i)
        	this.calcularDistancias(this.pontosImportantesX[i], this.pontosImportantesY[i]);
        
        this.best = g.tsp(0, 1, 0, Integer.MAX_VALUE);
	}
	
	private void calcularDistancias(final int x, final int y) {
		boolean [][]visited = new boolean[this.linhas][this.colunas];
		visited[x][y] = true;
		final byte rowNum[] = {-1, 0, 0, 1}; 
		final byte colNum[] = {0, -1, 1, 0};

		Queue<int[]> q = new LinkedList<>();
		int[] xyd = {x,y,0};
		q.add(xyd);
		
		while (!q.isEmpty()) {
			xyd = q.peek();

			if(this.lab[xyd[0]][xyd[1]] == 4) {
				// usar xyd[2]
				this.g.setDistance(m.get(converter(x,y)), m.get(converter(xyd[0], xyd[1])), xyd[2]);
			}
			
			q.remove();
			
			for(int i = 0; i < 4; ++i) {
				final int row = xyd[0] + rowNum[i]; 
	            final int col = xyd[1] + colNum[i];
	            
	            if (isValid(row, col) && this.lab[row][col] != 0 && !visited[row][col]) {
	            	visited[row][col] = true;
	            	final int[] temp = {row,col,xyd[2]+1};
	            	q.add(temp);
	            }
			}
		}
	}
	
	private boolean isValid(final int x, final int y) { 
	    return (x >= 0) && (x < this.linhas) && (y >= 0) && (y < this.colunas); 
	}
	
	private int converter(final int x, final int y) {
		return x*this.linhas + y;
	}
	
	private boolean canMakeShortcut(final int x, final int y) {
		if(this.lab[x][y] == 0) {
			if(isValid(x-1, y) && isValid(x+1, y) && isValid(x, y-1) && isValid(x, y+1)) {
				if(this.lab[x-1][y] == 0 && this.lab[x+1][y] == 0 && this.lab[x][y-1] == 1 && this.lab[x][y+1] == 1)
					return true;
				else if(this.lab[x-1][y] == 1 && this.lab[x+1][y] == 1 && this.lab[x][y-1] == 0 && this.lab[x][y+1] == 0)
					return true;
			}
		}
		
		return false;
	}
}