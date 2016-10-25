import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RushHourMain {
	static int[][] tablero = new int[6][6]; //Tablero es 15 ancho 12 largo
    static ArrayList<int[][]> cola = new ArrayList<int[][]>(); //combinaciones de tablero
    static ArrayList<int[]> movs = new ArrayList<int[]>(); //posicion (2 ints) y su movimiento (1 int) paralelo a cola
    static ArrayList<Integer> back = new ArrayList<Integer>(); //desde que posicion viene
    static ArrayList<int[][]> colavic = new ArrayList<int[][]>(); //combinaciones de tablero de la estrategia ganadora
    static ArrayList<int[]> movsvic = new ArrayList<int[]>();
	
	public static void main(String[] args) throws IOException{
		BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
		String input;
		String[] strin = new String[6];
		tablero = new int[6][6];
		for(int i = 0; i<6; i++){
			input = in.readLine();
			strin = input.split(" ");
			for(int j = 0; j<6; j++){
				tablero[i][j] = Integer.parseInt(strin[j]);
			}
		}
		resolver(tablero);
		int[] linea = new int[3];
		String str;
		for(int i=0; i<movsvic.size(); i++){
			str = "";
			linea = movsvic.get(i);
			for(int j=0; j<3; j++){
				str = str+" "+linea[j];
			}
			str = str.substring(1);
			System.out.println(str);
		}
	}
	
	static void resolver(int[][] a){ //a: tablero inicial
		//código de resolución base
		        //1:HH 2:HHH 3:VV 4:VVV 5:objetivo
		        int i = 0;
		        int o;
		        cola = new ArrayList<int[][]>();
		        movs = new ArrayList<int[]>();
		        back = new ArrayList<Integer>();
		        o = encontrarobjetivo(a)[0];
		        agregar(a, 0, 0, 0, -1);
		        while (!cola.isEmpty()){ //mientras queden elementos en la cola
		            a = cola.get(i); //obtener el siguiente elemento
		            if(victoria(a, o)){ //si se puede ganar en un movimiento
		                ganar(i); //realizar el último movimiento
		                crearcolasvic(); //
		                break;
		            }
		            posibles(a,i); //agregar nuevos tableros a la cola de iteración
		            i += 1; //avanzar al siguiente elemento
		        }
		    }
		
		static void agregar(int[][] tab1, int i, int j, int nmov, int n){//tablero, x, y, #mov, backtrack
		// agrega un tablero a la cola, junto a su movimiento y el indice del tablero de donde vino
		        int [][] auxtab = copiador(tab1);
		        if(!contenedor(cola,auxtab)){ //si no esta ya en la cola
		            int[] a = {i,j,nmov};
		            cola.add(auxtab); //agregar el tablero generado
		            movs.add(a); // agregar el movimiento por hacer
		            back.add(n); //agregar el indice del tablero que lo generó
		        }
		    }
		
		static void posibles(int[][] tab, int bt){ //revisa los movimientos posibles de todas las fichas dadas un tablero
	        int c;
	        int [][] auxtab = copiador(tab);
	        for (int i=0; i<6; i++){
	            for (int j=0; j<6; j++){
	                if (tab[i][j] == 0){ //si esta vacio
	                    continue;
	                }
	                c = posplus(i,j,tab);
	                for(int k=1; k<=c; k++){
	                    auxtab = copiador(tab);
	                    agregar(mov(auxtab,i,j,k), i, j, k, bt);
	                }
	                c = posneg(i,j,tab);
	                for(int k=1; k<=c; k++){
	                    auxtab = copiador(tab);
	                    agregar(mov(auxtab,i,j,-k), i, j, -k, bt);
	                }

	            }
	        }
	    }
		
		static boolean victoria(int[][] tab, int j){ //revisa si ya ganó
	        boolean found = false;
	        for (int k=0; k<6; k++){
	            if (!found){
	                if (tab[j][k] == 5){
	                    found = true;
	                }
	            }
	            else{
	                if (tab[j][k] != 0){
	                    return false;
	                }
	                if (j >= 1){
	                    if (tab[j-1][k] == 3 || tab[j-1][k] == 4){
	                        return false;
	                    }
	                    if (j >= 2){
	                        if (tab[j-2][k] == 4){
	                            return false;
	                        }
	                    }
	                }
	            }
	        }
	        return true;
	    }
		
		static int posplus(int i, int j, int[][] tab){
	        int[][] tabaux = copiador(tab);
	        int l,p,a,h,v,b;
	        int t = tabaux[i][j];
	        if (t == 1 || t == 3 || t == 5){ //largo de la pieza
	            l = 2;
	        }
	        else{
	            l = 3;
	        }
	        if(t==1 || t==2 || t==5){ //orientación de la pieza
	            p = j; //horizontales
	            a = i;
	            h = 1;
	            v = 0;
	        }
	        else{
	            p = i; //verticales
	            a = j;
	            h = 0;
	            v = 1;
	        }
	        for(int k = 0; k <= 5-l; k++){ //muy a un lado
	            if(p >= 6-l-k){
	                return k;
	            }
	            if(tab[i+((l+k)*v)][j+((l+k)*h)]!=0){//algo bloqueando
	                return k;
	            }
	            if (a >= 1){ //algo transversal corto bloqueando
	                b = tab[ i + ((l+k)*v) - h ][ j + ((l+k)*h) - v ];
	                if((t == 1 || t == 2 || t == 5) && (b == 3 || b == 4)){
	                    return k;
	                }
	                if((t == 3 || t == 4) && (b == 1 || b == 2 || b == 5)){
	                    return k;
	                }
	                if (a >= 2){ //algo transversal largo bloqueando
	                    b = tab[ i + ((l+k)*v) - 2*h ][ j + ((l+k)*h) - 2*v ];
	                    if((t == 1 || t == 2 || t == 5) && (b == 4)){
	                        return k;
	                    }
	                    if((t == 3 || t == 4) && (b == 2)){
	                        return k;
	                    }
	                }
	            }
	        }
	        return 6-l;
	    }
		
		static int posneg(int i, int j, int[][] tab){
	        int [][] tabaux = tab.clone();
	        int l,p,a,h,v,b;
	        int t = tabaux[i][j];
	        if (t == 1 || t == 3 || t == 5){ //largo de la pieza
	            l = 2;
	        }
	        else{
	            l = 3;
	        }
	        if(t==1 || t==2 || t==5){ //orientación de la pieza
	            p = j;
	            a = i;
	            h = 1;
	            v = 0;
	        }
	        else{
	            p = i;
	            a = j;
	            h = 0;
	            v = 1;
	        }
	        for(int k = 0; k <= 5-l; k++){ //muy a un lado
	            if(p <= k){
	                return k;
	            }
	            if(p>=k+2){
	                b = tab[ i - ((2+k)*v) ][ j - ((2+k)*h) ];
	                if(h==1 && (b == 1 || b == 2 || b == 5)){
	                    return k;
	                }
	                if(v==1 && (b == 3 || b ==4)){
	                    return k;
	                }
	            }
	            if(p >= k+3){
	                b = tab[ i - ((3+k)*v) ][ j - ((3+k)*h) ];
	                if(h==1 && b == 2){
	                    return k;
	                }
	                if(v==1 && b ==4){
	                    return k;
	                }
	            }
	            if(tab[i-((k+1)*v)][j-((k+1)*h)]!=0){//algo bloqueando
	                return k;
	            }
	            if (a >= 1){ //algo transversal corto bloqueando
	                b = tab[ i - ((1+k)*v) - h ][ j - ((1+k)*h) - v ];
	                if((t == 1 || t == 2 || t == 5) && (b == 3 || b == 4)){
	                    return k;
	                }
	                if((t == 3 || t == 4) && (b == 1 || b == 2 || b == 5)){
	                    return k;
	                }
	                if (a >= 2){ //algo transversal largo bloqueando
	                    b = tab[ i - ((1+k)*v) - 2*h ][ j - ((1+k)*h) - 2*v ];
	                    if((t == 1 || t == 2 || t == 5) && (b == 4)){
	                        return k;
	                    }
	                    if((t == 3 || t == 4) && (b == 2)){
	                        return k;
	                    }
	                }
	            }
	        }
	        return 6-l;
	    }

		static int[] encontrarobjetivo(int[][] pos){ //revisa la fila donde está la ficha objetivo
	        boolean found = false;
	        int[] aux = new int[2];
	        for (int j=0; j<6; j++){
	            for (int k=0; k<6; k++){
	                if (pos[j][k] == 5){
	                    found = true;
	                    aux[0] = j;
	                    aux[1] = k;
	                    break;
	                }
	            }
	            if (found) {
	                return aux;
	            }
	        }
	        return aux;
	    }
		
		static void crearcolasvic() {
		//entrega los tableros de los movimientos ganadores
		        int a = movs.size();
		        int n = a-1; //indice del tablero final (ganador)
		        while (n != -1){ //hasta llegar al primer tablero
		            movsvic.add(movs.get(n)); //agregar movimiento
		            colavic.add(cola.get(n)); //agregar tablero
		            n = back.get(n); //retroceder al movimiento anterior
		        }
		    }
		
		static boolean contenedor(ArrayList<int[][]> colas, int[][] tab2){
	        int n = colas.size();
	        for(int i = 0; i < n; i++){
	            if(comparador(colas.get(i),tab2)){
	                return true;
	            }
	        }
	        return false;
	    }
		
		static boolean comparador(int[][] a, int[][] b){
	        for(int i = 0; i < 6; i++){
	            for(int j =0; j<6; j++){
	                if (a[i][j] != b[i][j]){ 
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
		
		static void ganar(int i) {
	        int[] p = new int[2];
	        int[][] aux = copiador(cola.get(i));
	        p = encontrarobjetivo(aux);
	        aux = copiador(mov(aux, p[0], p[1], 4-p[1]));
	        agregar(aux, p[0], p[1], 4-p[1],i);
	    }
		
		static int[][] mov(int[][] tab, int i, int j, int n){ //tablero, y, x, #mov
	        int[][] auxmov = new int[6][6];
	        int t = tab[i][j];
	        auxmov = copiador(tab);
	        if (t == 1 || t == 2 || t == 5){
	            auxmov[i][j] = 0;
	            auxmov[i][j+n] = t;
	        }
	        else if (t == 3 || t == 4){
	            auxmov[i][j] = 0;
	            auxmov[i+n][j] = t;
	        }
	        return auxmov;
	    }
		
		static int[][] traducirRvrs(int[][] tabbase){
	        int [][] auxtab = new int[6][6];
	        for(int i = 0; i<6; i++){
	            for(int j=0; j<6; j++){
	                auxtab[i][j] = -1;
	            }
	        }
	        for(int i = 0; i<6; i++){
	            for(int j=0; j<6; j++){
	                if (auxtab[i][j] != -1){
	                    continue;
	                }
	                if (tabbase[i][j] == 0){
	                    auxtab[i][j] = 0;
	                }
	                if (tabbase[i][j] == 1){
	                    auxtab[i][j] = 1;
	                    auxtab[i][j+1] = 1;
	                }
	                if (tabbase[i][j] == 2){
	                    auxtab[i][j] = 2;
	                    auxtab[i][j+1] = 2;
	                    auxtab[i][j+2] = 2;
	                }
	                if (tabbase[i][j] == 3){
	                    auxtab[i][j] = 3;
	                    auxtab[i+1][j] = 3;
	                }
	                if (tabbase[i][j] == 4){
	                    auxtab[i][j] = 4;
	                    auxtab[i+1][j] = 4;
	                    auxtab[i+2][j] = 4;
	                }
	                if (tabbase[i][j] == 5){
	                    auxtab[i][j] = 5;
	                    auxtab[i][j+1] = 5;
	                }
	            }
	        }
	        return auxtab;
	    }
		
		static int[][] copiador(int[][] base){
	        int[][] obj = new int[6][6];
	        for(int i = 0; i < 6; i++){
	            for(int j =0; j<6; j++){
	                obj[i][j] = base[i][j];
	            }
	        }
	        return obj;
	    }
}

