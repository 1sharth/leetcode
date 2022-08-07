package leetcode;

/*
            f1(i) => smallest a[i+1,n-1] >= a[i]
            f2(i) => largest a[i+1,n-1]  <= a[i]

            f3(i) => a[i] is good

            solver(i)=>{
            if i==n-1:
            return 1

            j1=f1(i)
            j2=f2(i)
            count=0
            count+=(f3(j1) + f3(j2))
            return count

            }

*/

import java.util.*;

public class OddEvenJump {

    private static List<List<Integer>> getSuffices(List<Integer> list){
        if(list.size()==0)
            return new ArrayList<>();

        List<Integer> suffixSmallestGreater=new ArrayList<>(Collections.nCopies(list.size(),-1));
        List<Integer> suffixGreatestSmaller=new ArrayList<>(Collections.nCopies(list.size(),-1));
        NavigableMap<Integer, Integer> bst = new TreeMap<>();
        bst.put(list.get(list.size()-1),list.size()-1);
        for(int i=list.size()-2; i>=0; i--){
            Integer el=list.get(i);
            Integer key = bst.ceilingKey(el);
            System.out.printf("smallest elem greater than %d is %d%n", el,key);
            if(key!=null){
                suffixSmallestGreater.set(i,bst.get(key));
            }
            key = bst.floorKey(el);
            if(key!=null){
                suffixGreatestSmaller.set(i,bst.get(key));
            }
            bst.put(el,i);
        }

        List<List<Integer>> reqSuffices = new ArrayList<>();
        reqSuffices.add(suffixSmallestGreater);
        reqSuffices.add(suffixGreatestSmaller);
        return reqSuffices;
    }

    private static Integer noOfGoodIndices(List<Integer> list, int s,
                                    int jumpParity,
                                    List<List<Integer>> memo,
                                    List<Integer> suffSmallestGreater,
                                    List<Integer> suffGreatestSmaller){
//        if(s==list.size()-1)
//            return 1;

        if(memo.get(jumpParity).get(s)!=-1)
            return memo.get(jumpParity).get(s);

        Integer j = -1;
        if(jumpParity==1){
            j = suffSmallestGreater.get(s);
        }
        else{
            j = suffGreatestSmaller.get(s);
        }

        Integer count=0;
        if(j!=-1)
            count+=noOfGoodIndices(list, j, 1-jumpParity,  memo, suffSmallestGreater, suffGreatestSmaller);

        memo.get(jumpParity).set(s,count);
        return count;
    }

    public int oddEvenJumps(int[] arr) {
        List<Integer> inp = Arrays.stream(arr).boxed().toList();
        List<List<Integer>> suffixes = getSuffices(inp);
        List<List<Integer>> memo=new ArrayList<>();
        memo.add(new ArrayList<>(Collections.nCopies(inp.size(),-1)));
        memo.add(new ArrayList<>(Collections.nCopies(inp.size(),-1)));
        memo.get(0).set(inp.size()-1,1);
        memo.get(1).set(inp.size()-1,1);
//        System.out.println(suffixes);
//        System.out.println(memo);
        Integer allCount=0;
        for(int i=0; i<inp.size(); i++){
            allCount+=noOfGoodIndices(inp,i,1, memo, suffixes.get(0), suffixes.get(1));
        }
        return allCount;
    }

    public static void main(String[] args) {
        System.out.println(getSuffices(List.of(1,3,5,8,9)));
    }
}
