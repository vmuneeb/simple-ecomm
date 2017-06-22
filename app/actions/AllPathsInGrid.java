package actions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AllPathsInGrid {

    static int paths = 0;
    static final int N = 4;
    static final int M = 4;
    //static int[][] matrix = {};
    static List<Node> visitedPath = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        DFS(0,0);  //starting point 0,0
        System.out.println(paths);
    }

    public static void DFS(int i,int j) {
        Node node = new Node(i,j);

        if(i==N-1 && j == M-1) {            //Reached destination
            for (Node node1 : visitedPath) {
                System.out.print(node1.toString()+"->");
            }
            paths++;
            //System.out.print(N+","+M);
            //System.out.println(" => Number of path so far :"+paths);
            return;
        } else {
            visitedPath.add(node);                                //Add this to visited path
            if(i>0 && !visitedPath.contains(new Node(i-1,j))) {  //top element
                DFS(i-1,j);
            }
            if(j>0 && !visitedPath.contains(new Node(i,j-1))) {  //left element
                DFS(i,j-1);
            }
            if(i<N-1 && !visitedPath.contains(new Node(i+1,j))) { // right element
                DFS(i+1,j);
            }
            if(j<M-1 && !visitedPath.contains(new Node(i,j+1))) { //bottom element
                DFS(i,j+1);
            }
        }

        visitedPath.remove(node);  //Remove node from visited path
    }
}

class Node {
    int i,j;

    public Node(int j, int i) {
        this.j = j;
        this.i = i;
    }

    @Override
    public boolean equals(Object obj) {
        return (i == ((Node) obj).i && j == ((Node) obj).j);
    }

    @Override
    public String toString() {
        return i+","+j;
    }
}