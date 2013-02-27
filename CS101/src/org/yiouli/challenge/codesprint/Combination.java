package org.yiouli.challenge.codesprint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 1 <= n <= 1000,000,000
 * 1 <= r <= n
 *
 */
public class Combination {
    static int mod = 142857;
    
    static LinkedList<Integer> val = new LinkedList<Integer>();
    
    static void set() {
    	val.clear();
    	val.add(1);
    }
    
    //multiple val by num
    static void mul(int num) {
    	int carry = 0;
    	ListIterator<Integer> it = val.listIterator(val.size());
    	while(it.hasPrevious()) {
    		long tmp = (long)it.previous()*num+carry;
    		it.set((int)(tmp&Integer.MAX_VALUE));
    		carry = (int)(tmp>>31);
    	}
    	if(carry != 0)
    		val.addFirst(carry);
    }
    
    static long div(int num) {
    	long remain = 0;
    	ListIterator<Integer> it = val.listIterator();
    	while(it.hasNext()) {
    		long tmp = (remain<<31)|it.next();
    		it.set((int)(tmp/num));
    		remain = tmp%num;
    	}
    	return remain;
    } 
    
    static long combination(int n, int r) {
    	set();
        r = Math.min(r, n-r);
        for(int i=0;i<r;i++)
        	mul(n-i);
        for(int i=1;i<=r;i++)
        	div(i);
        return div(mod);
    }
    
    public static void main(String[] args) throws IOException {
    	InputStreamReader in = null;
    	if(args.length > 0)
    		in = new FileReader(args[0]);
    	else
    		in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        int t = Integer.parseInt(br.readLine());
        for(int ti=0;ti<t;ti++) {
            String[] tmp = br.readLine().split(" ");
            int n = Integer.parseInt(tmp[0]);
            int r = Integer.parseInt(tmp[1]);
            System.out.println(combination(n,r));
        }
    }
}
