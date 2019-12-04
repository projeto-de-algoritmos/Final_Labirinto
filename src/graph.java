
public class graph {
	private int[][] g;
	private final int n;
	private boolean[] v;
	
	public graph(final int v) {
		this.n = v;
		
		this.g = new int[v][v];
		for(int i = 0; i < v; ++i) {
			for(int j = 0; j < v; ++j) {
				this.g[i][j] = 0;
			}
		}
		
		this.v = new boolean[n];
		this.v[0] = true;
	}
	
	public int tsp(final int currPos, final int count, final int cost, int ans) {
		if (count == this.n && this.g[currPos][0] > 0) { 
            return Math.min(ans, cost + this.g[currPos][0]);  
        }
		
		for (int i = 0; i < this.n; i++) { 
            if (this.v[i] == false && this.g[currPos][i] > 0) { 
            	this.v[i] = true; 
                ans = tsp(i, count + 1, cost + this.g[currPos][i], ans); 

                this.v[i] = false; 
            } 
        }
		
		return ans;
	}
	
	public void setDistance(final int src, final int dest, final int dist) {
		this.g[src][dest] = dist;
		this.g[dest][src] = dist;
	}
}