/**
 * 
 * The class SimulationData data contains all the data used to run the simulation.
 * This includes the number of first, second and third stage matrices, the number of
 * inlets of matrices in the first and thirds stage and the total number of inlets
 * and outlets. All method are getters and setters so they are not commented.
 *
 * @author Maretti, Elnor, Huamani
 *
 */

package source;


public class SimulationData {

	private int r1;
	private int r2;
	private int r3;
	
	private int n;
	private int m;
	private int N;
	private int M;
	
	private int policy;
	
	public SimulationData(int r1, int r2, int r3, int n, int m, int N, int M, int policy) {
		
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
	
	public void setNumStage1Matrices(int r1) {
		
		this.r1=r1;
		
	}
	
	public void setNumStage2Matrices(int r2) {
		
		this.r2=r2;
		
	}
	
	public void setNumStage3Matrices(int r3) {
		
		this.r3=r3;
		
	}
	
	public void setNumInletsFirstStage(int n) {
		
		this.n = n;
		
	}
	
	public void setNumOutletsThirdStage(int m) {
		
		this.m = m;
		
	}
	
	public void setNumTotalInlets(int N) {
		
		this.N=N;
	}
	
	public void setNumTotalOutlets(int M) {
		
		this.M=M;
		
	}
	
	public int getNumStage1Matrices() {
		
		return r1;
		
	}
	
	public int getNumStage2Matrices() {
		
		return r2;
		
	}
	
	
	public int getNumStage3Matrices() {
		
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
