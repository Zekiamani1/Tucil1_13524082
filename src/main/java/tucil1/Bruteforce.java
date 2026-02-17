package tucil1;
public class Bruteforce{
    public static long iterasi = 0;
    public static char[][][] currentmap = null;
    
    public static boolean cek(int x,int y, char map[][][]){
        //cek sekolom
        for (int i = 0; i < map.length; i++) {
            if(map[i][y][1]=='Y' && i!=x){
                return false;
            }
        }
        //cek serow 
        for (int i = 0; i < map.length; i++) {
            if(map[x][i][1]=='Y' && i!=y){
                return false;
            }
        }
        //cek adjacent diagonal
        if(x+1 < map.length && y+1 < map.length && map[x+1][y+1][1]=='Y') 
            {return false;}
        if(x+1 < map.length && y-1 >= 0 && map[x+1][y-1][1]=='Y') 
            {return false;}
        if(x-1 >= 0 && y+1 < map.length && map[x-1][y+1][1]=='Y') 
            {return false;}
        if(x-1 >= 0 && y-1 >= 0 && map[x-1][y-1][1]=='Y') 
            {return false;}
        
        //cek warna
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if(map[i][j][1]=='Y' && map[i][j][0]==map[x][y][0] && i!=x && j!=y){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean isvalid(char[][][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if(map[i][j][1]!='Y') continue;
                if(!cek(i,j,map)){
                    return false;
                }
            }
        }
        return true;
    }
    public static char[][][] bruteforce(char[][][] map){
        int n=map.length;
        for (long count = 0; count < (long) Math.pow(n, n); count++) {
            iterasi++;
            long tempCount = count;
            for (int row = 0; row < n; row++) {
                map[row][(int) tempCount % n][1] = 'Y';
                tempCount /= n;
            }            
            currentmap = copy(map);
            if (isvalid(map)) {
                return map;
            }
            tempCount = count;
            for (int row = 0; row < n; row++) {
                map[row][(int)(tempCount % n)][1] = 'N';
                tempCount /= n;
            }
        }
        return null;
    }
    
    private static char[][][] copy(char[][][] map) {
        if (map == null) return null;
        int n = map.length;
        char[][][] copy = new char[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j][0] = map[i][j][0];
                copy[i][j][1] = map[i][j][1];
            }
        }
        return copy;
    }

    public static char[][][] solve(String[] input, int n) {
        char[][][] map = new char[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j][1] = 'N';
                map[i][j][0] = input[i].charAt(j);
            }
        }    
        return bruteforce(map);
       
    }
}

