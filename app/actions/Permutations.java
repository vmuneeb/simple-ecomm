package actions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by muneeb on 15/06/17.
 */

public class Permutations {

    List<Integer> items =  Arrays.asList(1,2,3,4);

    List<List<Integer>> combinationList = new LinkedList<>();

    public static void main(String[] args){
        Permutations ps = new Permutations();

        for(int i = 0; i< ps.items.size();i++){ //traverse through all elements
            List<Integer> startList = new LinkedList<>();
            ps.getCombinations(i,startList);
        }

        for (int i = 0; i < ps.combinationList.size(); i++) {
            List<Integer> items = ps.combinationList.get(i);
            for (int j = 0; j < items.size(); j++) {
                System.out.print(items.get(j)+"");
            }
            System.out.println("");
        }
    }


    void getCombinations(int index,List<Integer> subList)
    {
        List<Integer> s = new LinkedList<>(); //create a list on each stack
        s.add(items.get(index));

        for (int i = 0; i < subList.size(); i++) {
            s.add(subList.get(i));
        }

        combinationList.add(s);

        for(int i=index+1;i<items.size();i++)
            getCombinations(i,s); //call recursively

    }
}