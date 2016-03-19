/**
 * 
 * The SimulationData class contains all the parameters of the Slepian-Duguid network such that 
 * the number of first/second/third stage matrices, the number of inlets and outlets of each matrix.
 * It specifies also the policy that the rearrangement method has to follow.
 *
 * @authorDe Silva, Pébrier, Caballero 
 *
 */

package source;


public class Data {

	private int r1;
	private int r2;
	private int r3;
	
	private int n;
	private int m;
	private int N;
	private int M;
	
	private int policy;
	
	public Data(int r1, int r2, int r3, int n, int m, int N, int M, int policy) {
		
		this.r1=r1;
		this.r2=r2;
		this.r3=r3;
		this.n = n;
		this.m = m;
		this.N = N;
		this.M = M;
		this.policy = policy;
	}
	
	
	public boolean isInputDataCorrect() {
		
		if( (r1 == 0) ||  (r2 == 0) || (r3 == 0) || (n == 0) || (m == 0) || (N == 0) || (M == 0) ) {
		
			return false;
		
		}
		if(policy < 0) {
			
			return false;
		
		}
		
		if(r2 < Math.max(n, m)) {
			
			return false;
			
		}
		
		if(n*r1 != N) {
			
			return false;
		}
		
		if(r3*m != M) {
			
			return false;
		
		}

		return true;
	}
	
	
	public int getPolicy() {
		
		return policy;
		
	}
	

	
	public int getR1() {
		
		return r1;
		
	}
	
	public int getR2() {
		
		return r2;
		
	}
	
	
	public int getR3() {
		
		return r3;
		
	}
	
	public int getNumInletsFirstStage() {
		
		return n;
		
	}
	
	public int getNumOutletsThirdsStage() {
		
		return m;
		
	}
	
	public int getNumTotalInlets() {
		
		return N;
	}
	
	public int getNumTotalOutlets() {
		
		return M;
	}
	
	
}
