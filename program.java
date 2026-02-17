import java.util.Scanner;

public class program{
    public static long iterasi = 0;
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

    public static void main(String[] args) {
        
        Scanner in=new Scanner(System.in);
        int n=in.nextInt();
        char map[][][]=new char[n][n][2];

        in.nextLine();
        for (int i = 0; i < n; i++) {
            String temp=in.nextLine();
            for (int j = 0; j < n; j++) {
                map[i][j][1]='N';
                map[i][j][0]=temp.charAt(j);
            }
        }

        char[][][] map2;
        
        long start = System.nanoTime();
        map2=bruteforce(map);
        long end = System.nanoTime();
        
        double interval = (end - start) / 1_000_000_000.0;
        
        System.out.println("jumlah iterasi: " + iterasi);
        System.out.printf("waktu pencarian: %.6f detik%n", interval);
        System.out.println();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(map2[i][j][1]=='Y'){
                    System.out.print('#');
                } else{
                    System.out.print(map2[i][j][0]);
                }
            }
            System.out.println();
        }

        in.close();
    }
}
