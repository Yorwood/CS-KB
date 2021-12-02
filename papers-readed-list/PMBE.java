/*
 *The code was created by Aman Abidi.
 *Swinburne University of Technology, Melbourne.
 *For the implementation of the paper "Pivot-based Maximal Biclique Enumeration" (https://www.ijcai.org/proceedings/2020/492). 
 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import java.util.Map.Entry;
class Node{
	ArrayList<Node> neighbors;
	ArrayList<Node> parent;
	int id;
	long children;
	Boolean visited, visited_2;
	TreeMap<Long,Long> range;
	TreeMap<Long,Long> range_2;
	public Node()
	{
		neighbors= new ArrayList<Node>();
		parent=new ArrayList<Node>();
		range=new TreeMap<Long,Long>();
		range_2=new TreeMap<Long,Long>();
		id=0;
		children=0;
		visited=false;
		visited_2=false;
	}
}

class Edge2{
	int u;
	int v;
	public Edge2(int _u,int _v) {
		u = _u;
		v = _v;
	}
	
	@Override
	public int hashCode() {
		return u*37+v*37;
	}
	
	@Override
	public boolean equals(Object obj)
	{	
		if(obj == this) return true;
		
		if(!(obj instanceof Edge2))
			return false;
		
		Edge2 e = (Edge2)obj;
		return this.u == e.u && this.v == e.v;
	}

}


class MBC implements Comparable<MBC>{
	ArrayList<Integer> leftV;
	ArrayList<Integer> rightV;
	int size;
	
	public MBC(ArrayList<Integer> _l , ArrayList<Integer> _r) {
		// TODO Auto-generated constructor stub
		leftV = _l;
		rightV = _r;
		size = _l.size() * _r.size();
	}

	@Override
	public int compareTo(MBC o) {
		// TODO Auto-generated method stub
		if(this.size == o.size) return 0;
		return this.size > o.size ? -1 : 1;
	}
	
	
	
}

public class PMBE {
	
	//ArrayList<MBC> mbcs = new ArrayList<>();
	
	private static int[] mapsub;
	private static TreeMap<Integer, ArrayList<Integer>> map;
	private static TreeMap<Integer, ArrayList<Integer>> maprev;
	private static TreeMap<Integer, Node> mapnode;
	private static long subset_number;
	static TreeMap<Integer, ArrayList<Integer>> sort;
	private static ArrayList<Integer> tail_sort;
	static int index;
	static long Time;
	public PMBE()
	{
		map = new TreeMap<Integer, ArrayList<Integer>>(); //u - neighbors
	    maprev = new TreeMap<Integer, ArrayList<Integer>>(); //v -neighbors
	    mapnode = new TreeMap<Integer, Node>();
	    subset_number=1;
	    tail_sort=new ArrayList<Integer>();
	    index=0;
	    Time=0;
	}
	public void PMBE1()
	   {
	      tail_sort=new ArrayList<Integer>();
	      subset_number=1;
	      index=0;
	      Time=0;
	   }
	public void add(int key, int value)
	{
	   	ArrayList<Integer> values = map.getOrDefault(key, new ArrayList());
	   	values.add(value);
	   	Collections.sort(values);
	   	map.put(key, values);
	}
	public void addrev(int key, int value)
	{
		ArrayList<Integer> values = maprev.getOrDefault(key, new ArrayList());
	   	values.add(value);
	   	Collections.sort(values);
	   	maprev.put(key, values);
	 }
	public boolean isnotsubset(int x)throws IOException//,ArrayList<Integer> adj
	{
			Set set = map.entrySet();
			Iterator i = set.iterator();
			ArrayList<Integer> common=new ArrayList<Integer>(map.get(x));//copylist((ArrayList)map.get(x));
			int flag=0;
			while(i.hasNext())
			{
				Map.Entry pair = (Map.Entry)i.next();
				if(x!=(int)pair.getKey())
				{
					ArrayList<Integer> com=new ArrayList<Integer>((ArrayList)pair.getValue());//copylist((ArrayList)pair.getValue());
					boolean isSubset = com.containsAll(common);
					if(isSubset && !common.isEmpty())
						flag=1;
				}
				
			}
			if(flag==0)
				return true;
			else
				return false;
	}
	public boolean isnotsubsetrev(int x)throws IOException//,ArrayList<Integer> adj
	{
			Set set = maprev.entrySet();
			Iterator i = set.iterator();
			ArrayList<Integer> common=new ArrayList<Integer>(maprev.get(x));//copylist((ArrayList)map.get(x));
			int flag=0;
			while(i.hasNext())
			{
				Map.Entry pair = (Map.Entry)i.next();
				if(x!=(int)pair.getKey())
				{
					ArrayList<Integer> com=new ArrayList<Integer>((ArrayList)pair.getValue());//copylist((ArrayList)pair.getValue());
					boolean isSubset = com.containsAll(common);
					if(isSubset && !common.isEmpty())
						flag=1;
				}
				
			}
			if(flag==0)
				return true;
			else
				return false;
	}
	public static boolean issubset(int x,int y)throws IOException//y is subset of x
	{
		ArrayList<Integer> common = new ArrayList<Integer>();
		ArrayList<Integer> common1 = new ArrayList<Integer>();
		common=new ArrayList<Integer>(map.get(x));
		common1=new ArrayList<Integer>(map.get(y));
		boolean isSubset = common.containsAll(common1);
		
		if(isSubset && common.size()!=common1.size())
			return true;
		return false;
	}
	public void ini_hashmap()
	 {
		 sort = new TreeMap<Integer, ArrayList<Integer>>();
	 }
	public static void create_dag(ArrayList<Integer> x) throws IOException
	{
		Iterator<Integer> it=x.iterator();
		while(it.hasNext())
		{
			Node temp=new Node();
			temp.id=it.next();
			mapnode.put(temp.id, temp);
		}
		ListIterator<Integer> iter=x.listIterator();
		while(iter.hasNext())
		{
			int user1=iter.next();
			ListIterator<Integer> iter_2=x.listIterator(iter.previousIndex());
			while(iter_2.hasNext())
			{
				int user2=iter_2.next();
				if(user1==user2)
					continue;
				if(issubset(user1,user2) )
				{
					mapnode.get(user1).neighbors.add(mapnode.get(user2));
					mapnode.get(user2).parent.add(mapnode.get(user1));
				}
				else if(issubset(user2,user1)  )
				{
					mapnode.get(user2).neighbors.add(mapnode.get(user1));
					mapnode.get(user1).parent.add(mapnode.get(user2));
				}
			}
		}
	}
 	public static void create_dagrev(ArrayList<Integer> x) throws IOException
	{
		Iterator<Integer> it=x.iterator();
		while(it.hasNext())
		{
			Node temp=new Node();
			temp.id=it.next();
			mapnode.put(temp.id, temp);
		}
		ListIterator<Integer> iter=x.listIterator();
		while(iter.hasNext())
		{
			int user1=iter.next();
			ListIterator<Integer> iter_2=x.listIterator(iter.previousIndex());
			while(iter_2.hasNext())
			{
				
				int user2=iter_2.next();
				if(user1==user2)
					continue;
				if(issubsetrev(user1,user2)  )
				{
					mapnode.get(user1).neighbors.add(mapnode.get(user2));
					mapnode.get(user2).parent.add(mapnode.get(user1));
				}
				else if(issubsetrev(user2,user1)  )
				{
					mapnode.get(user2).neighbors.add(mapnode.get(user1));
					mapnode.get(user1).parent.add(mapnode.get(user2));
				}
			}
		}
	}
	public static void balance_dag() throws IOException
	{
		Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
		Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
		while (i_node1.hasNext()) 
		{
			Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
			int user=pair.getKey();
			if(!mapnode.get(user).neighbors.isEmpty())
			{
				ArrayList<Node> temp1=new ArrayList<Node>();
				Node temp=mapnode.get(user);
				ArrayList<Node>temp_nbr=temp.neighbors;
				int size_temp=temp.neighbors.size();
				for(int  j=0;j<size_temp;j++)
					for(int k=0;k<size_temp;k++)
						if( temp_nbr.get(j)!=null && temp_nbr.get(k)!=null && temp_nbr.get(j)!=temp_nbr.get(k)  )
							if(issubset(temp_nbr.get(j).id , temp_nbr.get(k).id))
								temp1.add(temp_nbr.get(k));
				mapnode.get(user).neighbors.removeAll(temp1);	
			}
		}
	}
	public static void balance_dagrev() throws IOException
	{
		Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
		Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
		while (i_node1.hasNext()) {
			Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
			//int user=it.next();
			int user=pair.getKey();
			if(!mapnode.get(user).neighbors.isEmpty())
			{
				ArrayList<Node> temp1=new ArrayList<Node>();
				Node temp=mapnode.get(user);
				ArrayList<Node>temp_nbr=temp.neighbors;
				int size_temp=temp.neighbors.size();
				for(int  j=0;j<size_temp;j++)
					for(int k=0;k<size_temp;k++)
						if( temp_nbr.get(j)!=null && temp_nbr.get(k)!=null && temp_nbr.get(j)!=temp_nbr.get(k)  )
							if(issubsetrev(temp_nbr.get(j).id , temp_nbr.get(k).id))
								temp1.add(temp_nbr.get(k));
				mapnode.get(user).neighbors.removeAll(temp1);	
			}
		}
	}
	public static boolean issubsetrev(int x,int y)throws IOException//y is subset of x
	{
		ArrayList<Integer> common = new ArrayList<Integer>();
		ArrayList<Integer> common1 = new ArrayList<Integer>();
		common=new ArrayList<Integer>(maprev.get(x));
		common1=new ArrayList<Integer>(maprev.get(y));
		boolean isSubset = common.containsAll(common1);
		if(isSubset && common.size()!=common1.size())
		{
			return true;
		}
		return false;
	}
	public static Node traverse_dag(Node root)
	{
		if(root.visited==true)// when the child is already visited, 
				{
					subset_number++;
					return root;//if node is visited then return the node
				}//*/
		if(root.neighbors.isEmpty() )//leaf node
		{
			root.range.put(subset_number, subset_number);
			subset_number++;
			root.visited=true;
			return null;
		}
		else//internal nodes
		{
			long x=subset_number;
			for(int i=0;i<root.neighbors.size();i++)//for each node we visit all its subsets 
			{
				Node ptrn=traverse_dag(root.neighbors.get(i));
				if(ptrn!=null )//if the subset is already visited the add all the range
				{
					//if the node is already visited add the range index of child to the parent range index 2
						root.range_2.putAll(ptrn.range);
						root.range_2.putAll(ptrn.range_2);
				}//*/
			}
			root.range.put(x, subset_number);//after visiting its subset complete the index for the root
			root.visited=true;//mark node is visited
			subset_number++;
			return null;//
		}
	}
	private static void sort_dag(Node root) {
		if(root.visited_2==true)// when the child is already visited, 
			return;//if node is visited then return the node
		root.visited_2=true;//mark node is visited
		if(root.neighbors.isEmpty() )//leaf node
		{
				tail_sort.add(root.id);
				root.visited_2=true;
				return;
		}
		for(int i=0;i<root.neighbors.size();i++)//for each node we visit all its subsets 
			sort_dag(root.neighbors.get(i));
					
		tail_sort.add(root.id);
		return;
		
	}
	private int PMBE_inbuilt(ArrayList<Integer> X, ArrayList<Integer> adj, ArrayList<Integer> cand) {
		Iterator<Integer> it;
		ArrayList<Integer> V1 = null;
		TreeMap <Integer,ArrayList<Integer>>V = new TreeMap<Integer, ArrayList<Integer>>();
		it=cand.iterator();
		while (it.hasNext()) 
		{
			Integer user = it.next();
			V1=retainallelement(adj,map.get(user));
			if(V1.isEmpty())// pruning the right side of bipartite graph
				it.remove();
			else
				V.put(user, V1);
		}
		if(cand.isEmpty())
			return 0;
		int pivot=cand.get(cand.size()-1);// comment this line
		Iterator<Integer> it1 = cand.iterator();
		label:
		while (it1.hasNext())
		{
			int user = it1.next();
			//pivot pruning is applied if the current vertex is a subset and is not a pivot
			if( mapsub[user]==1 && user!=pivot)
			{
				Iterator<Entry<Long, Long>> it_user=mapnode.get(user).range.entrySet().iterator();
				if(it_user.hasNext())
				{
				Entry<Long, Long> pair_user=it_user.next();
				Iterator<Entry<Long, Long>> it_pivot=mapnode.get(pivot).range.entrySet().iterator();
				if(it_pivot.hasNext())
				{
					Entry<Long, Long> pair_pivot=it_pivot.next();
						if(pair_pivot.getValue()-pair_pivot.getKey()!=0)
							if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
								continue label;
					it_pivot=mapnode.get(pivot).range_2.entrySet().iterator();
					while(it_pivot.hasNext() )//Pruning using the range index
					{								
						pair_pivot=it_pivot.next();
						if(pair_pivot.getValue()-pair_pivot.getKey()==0)//if the range index does not contain any subset we skip to the next
							continue ;
						if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
							continue label;
							
					}
				}
				}
			}
			it1.remove();
			ArrayList<Integer> V2=new ArrayList<Integer>();
			V2=V.get(user);//
			ArrayList<Integer> Y = new ArrayList<Integer>(maprev.get(V2.get(0)));
			if(V2.size()>1)
				for(int i=0;i<V2.size();i++)
				{
					ArrayList<Integer> adj1 =  maprev.get(V2.get(i));
					Y=retainallelement(adj1,Y);
				}
			ArrayList<Integer> Z=new ArrayList<Integer>(Y);
			ArrayList<Integer> W=new ArrayList<Integer>(X);
			W.add(user);
			
			Z.removeAll(W);
			
			
			boolean isSubset = cand.containsAll(Z);
			if(isSubset )
			{
				index++;
//				ArrayList<Integer> l = new ArrayList<>();
//				ArrayList<Integer> r = new ArrayList<>();
//				for(int i =0; i < Y.size(); i++) {
//					l.add(Y.get(i));
//				}
//				
//				for(int i =0; i < V2.size(); i++) {
//					r.add(V2.get(i));
//				}
				
				//mbcs.add(new MBC(l, r));
				//System.out.print(l);
				//System.out.println(r);
				
				
				
				
				ArrayList<Integer> T=new ArrayList<Integer>(cand);
				T.removeAll(Y);
				if(!T.isEmpty())
					PMBE_inbuilt(Y,V2,T);
			}
		}
		return 0;
	}
	private int PMBE_inbuilt_rev(ArrayList<Integer> X, ArrayList<Integer> adj, ArrayList<Integer> cand) {
		Iterator<Integer> it;
		ArrayList<Integer> V1 = null;
		HashMap <Integer,ArrayList<Integer>>V = new HashMap<Integer, ArrayList<Integer>>();
		it=cand.iterator();
		while (it.hasNext()) 
		{
			
			Integer user = it.next();
			V1=retainallelement(adj,maprev.get(user));
			if(V1.isEmpty())// pruning the right side of bipartite graph
				it.remove();
			else
				V.put(user, V1);
		}
		if(cand.isEmpty())
			return 0;
		int pivot=cand.get(cand.size()-1);
		Iterator<Integer> it1 = cand.iterator();
		label:
		while (it1.hasNext())
		{
			int user = it1.next();
			//pivot pruning is applied if the current vertex is a subset and is not a pivot
			if( mapsub[user]==1 && user!=pivot)
			{
				Iterator<Entry<Long, Long>> it_user=mapnode.get(user).range.entrySet().iterator();
				if(it_user.hasNext())
				{
				Entry<Long, Long> pair_user=it_user.next();
				Iterator<Entry<Long, Long>> it_pivot=mapnode.get(pivot).range.entrySet().iterator();
				if(it_pivot.hasNext())
				{
				Entry<Long, Long> pair_pivot=it_pivot.next();
					if(pair_pivot.getValue()-pair_pivot.getKey()!=0)
						if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
							continue label;
				it_pivot=mapnode.get(pivot).range_2.entrySet().iterator();
				while(it_pivot.hasNext() )//Pruning using the range index
				{								
					pair_pivot=it_pivot.next();
					if(pair_pivot.getValue()-pair_pivot.getKey()==0)//if the range index does not contain any subset we skip to the next
						continue ;
					if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
						continue label;
				}
				}
				}
			}//*/
			it1.remove();
			ArrayList<Integer> V2=new ArrayList<Integer>();
			V2=V.get(user);//
			ArrayList<Integer> Y = new ArrayList<Integer>(map.get(V2.get(0)));
			if(V2.size()>1)
				{
					for(int i=0;i<V2.size();i++)
					{
						ArrayList<Integer> adj1 =  map.get(V2.get(i));
						Y=retainallelement(adj1,Y);
					}
				}
			ArrayList<Integer> Z=new ArrayList<Integer>(Y);
			ArrayList<Integer> W=new ArrayList<Integer>(X);
			W.add(user);
			Z.removeAll(W);
			boolean isSubset = cand.containsAll(Z);
			if(isSubset )
			{
				
//				ArrayList<Integer> l = new ArrayList<>();
//				ArrayList<Integer> r = new ArrayList<>();
//				for(int i =0; i < Y.size(); i++) {
//					l.add(Y.get(i));
//				}
//				
//				for(int i =0; i < V2.size(); i++) {
//					r.add(V2.get(i));
//				}
				
				//mbcs.add(new MBC(l, r));
				
				index++;
				ArrayList<Integer> T=new ArrayList<Integer>(cand);
				T.removeAll(Y);
				if(!T.isEmpty())
					PMBE_inbuilt_rev(Y,V2,T);
			}
		}
		return 0;
	}
	private ArrayList<Integer> retainallelement(ArrayList<Integer> adj1, ArrayList<Integer> common) 
	{
		ArrayList<Integer> inter = new ArrayList<Integer>();
		int i = 0, j = 0;
		while (i < common.size() && j < adj1.size()) 
	      { 
	        if (common.get(i) < adj1.get(j)) 
	          i++; 
	        else if (adj1.get(j) < common.get(i)) 
	          j++;
	        else 
	        { 
	          inter.add(adj1.get(j));
	          i++; 
	          j++;
	        } 
	      }
		 return inter;
	}
	private int PMBE_inbuilt_cand(ArrayList<Integer> X, ArrayList<Integer> adj, ArrayList<Integer> cand,int ms1,int ms2) {
		Iterator<Integer> it;
		ArrayList<Integer> V1 = null;
		HashMap <Integer,ArrayList<Integer>>V = new HashMap<Integer, ArrayList<Integer>>();
		it=cand.iterator();
		while (it.hasNext()) 
		{
			
			Integer user = it.next();
			V1=retainallelement(adj,map.get(user));
			if(V1.size()<ms2)
				it.remove();
			else
				V.put(user, V1);
		}
		if(cand.isEmpty()||X.size()+cand.size()<ms1)
			return 0;
		int pivot=cand.get(cand.size()-1);
		Iterator<Integer> it1 = cand.iterator();
		label:
		while (it1.hasNext())
		{
			
			int user = it1.next();
			if(((X.size())+cand.size()+1)>= ms1)
			{
				//pivot pruning is applied if the current vertex is a subset and is not a pivot
				if( mapsub[user]==1 && user!=pivot)
				{
					Iterator<Entry<Long, Long>> it_user=mapnode.get(user).range.entrySet().iterator();
					if(it_user.hasNext())
					{
						Entry<Long, Long> pair_user=it_user.next();
						Iterator<Entry<Long, Long>> it_pivot=mapnode.get(pivot).range.entrySet().iterator();
						if(it_pivot.hasNext())
						{
							Entry<Long, Long> pair_pivot=it_pivot.next();
							if(pair_pivot.getValue()-pair_pivot.getKey()!=0)
								if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
									continue label;
							it_pivot=mapnode.get(pivot).range_2.entrySet().iterator();
							while(it_pivot.hasNext() )//Pruning using the range index
							{	
								pair_pivot=it_pivot.next();
								if(pair_pivot.getValue()-pair_pivot.getKey()==0)//if the range index does not contain any subset we skip to the next
									continue ;
								if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
									continue label;
							}
						}
					}
				}
				it1.remove();
				ArrayList<Integer> V2=new ArrayList<Integer>();
				V2=V.get(user);
				ArrayList<Integer> Y = new ArrayList<Integer>(maprev.get(V2.get(0)));
				if(V2.size()>1)
					{
						for(int i=0;i<V2.size();i++)
						{
							ArrayList<Integer> adj1 =  maprev.get(V2.get(i));
							Y=retainallelement(adj1,Y);
						}
					}
				ArrayList<Integer> Z=new ArrayList<Integer>(Y);
				ArrayList<Integer> W=new ArrayList<Integer>(X);
				W.add(user);
				Z.removeAll(W);
				boolean isSubset = cand.containsAll(Z);
				if(isSubset )
				{
					if(Y.size()>=ms1 && V2.size()>=ms2) {
						index++;
//						ArrayList<Integer> l = new ArrayList<>();
//						ArrayList<Integer> r = new ArrayList<>();
//						for(int i =0; i < Y.size(); i++) {
//							l.add(Y.get(i));
//						}
//						
//						for(int i =0; i < V2.size(); i++) {
//							r.add(V2.get(i));
//						}
//						
//						mbcs.add(new MBC(Y, V2));
					}
						
					ArrayList<Integer> T=new ArrayList<Integer>(cand);
					T.removeAll(Y);
					if( V2.size()>=ms2)
						PMBE_inbuilt_cand(Y,V2,T,ms1,ms2);
				}
			}
		}
		return 0;
	}
	private int PMBE_inbuilt_candrev(ArrayList<Integer> X, ArrayList<Integer> adj, ArrayList<Integer> cand,int ms1,int ms2) {
		Iterator<Integer> it;
		ArrayList<Integer> V1 = null;
		HashMap <Integer,ArrayList<Integer>>V = new HashMap<Integer, ArrayList<Integer>>();
		it=cand.iterator();
		while (it.hasNext()) 
		{
			
			Integer user = it.next();
			V1=retainallelement(adj,maprev.get(user));
			if(V1.size()<ms2)
				it.remove();
			else
				V.put(user, V1);
		}
		if(cand.isEmpty()||X.size()+cand.size()<ms1)
			return 0;
		int pivot=cand.get(cand.size()-1);
		Iterator<Integer> it1 = cand.iterator();
		label:
		while (it1.hasNext())
		{
			int user = it1.next();
			if(((X.size())+cand.size()+1)>= ms1)
			{
				//pivot pruning is applied if the current vertex is a subset and is not a pivot
				if( mapsub[user]==1 && user!=pivot)
				{
					Iterator<Entry<Long, Long>> it_user=mapnode.get(user).range.entrySet().iterator();
					if(it_user.hasNext())
					{
						Entry<Long, Long> pair_user=it_user.next();
						Iterator<Entry<Long, Long>> it_pivot=mapnode.get(pivot).range.entrySet().iterator();
						if(it_pivot.hasNext())
						{
							Entry<Long, Long> pair_pivot=it_pivot.next();
								if(pair_pivot.getValue()-pair_pivot.getKey()!=0)
									if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
										continue label;
							it_pivot=mapnode.get(pivot).range_2.entrySet().iterator();
							while(it_pivot.hasNext() )//Pruning using the range index
							{								
								pair_pivot=it_pivot.next();
								if(pair_pivot.getValue()-pair_pivot.getKey()==0)//if the range index does not contain any subset we skip to the next
									continue ;
								if( pair_user.getKey()>=pair_pivot.getKey() && pair_user.getValue()<=pair_pivot.getValue())
									continue label;
							}
						}
					}
				}
				it1.remove();
				ArrayList<Integer> V2=new ArrayList<Integer>();
				V2=V.get(user);
				ArrayList<Integer> Y = new ArrayList<Integer>(map.get(V2.get(0)));
				if(V2.size()>1)
					for(int i=0;i<V2.size();i++)
					{
						ArrayList<Integer> adj1 =  map.get(V2.get(i));
						Y=retainallelement(adj1,Y);
					}
				ArrayList<Integer> Z=new ArrayList<Integer>(Y);
				ArrayList<Integer> W=new ArrayList<Integer>(X);
				W.add(user);
				Z.removeAll(W);
				boolean isSubset = cand.containsAll(Z);
				if(isSubset )
				{
					if(Y.size()>=ms1 && V2.size()>=ms2) {
						index++;
//						ArrayList<Integer> l = new ArrayList<>();
//						ArrayList<Integer> r = new ArrayList<>();
//						for(int i =0; i < Y.size(); i++) {
//							l.add(Y.get(i));
//						}
//						
//						for(int i =0; i < V2.size(); i++) {
//							r.add(V2.get(i));
//						}
//						
//						mbcs.add(new MBC(Y, V2));
					}
					
					
					ArrayList<Integer> T=new ArrayList<Integer>(cand);
					T.removeAll(Y);
					if(!T.isEmpty())
						PMBE_inbuilt_candrev(Y,V2,T,ms1,ms2);
				}
			}
		}
		return 0;
	}
	
//	public void retrieveEdgeCover(int leftNum, int rightNum, String fileName) throws IOException {
//		String[] strings = fileName.split("/");
//		String outFileString  =  "./result/"+strings[2]+"_MEC";
//		
//		int edgesNum = 0;
//		int mbcNum = 0;
//		
//		ArrayList<MBC> mec = new ArrayList<>();
//		HashSet<Edge2> vis = new HashSet<>();
//		//int vis[][] = new int[leftNum+1][rightNum+1];
//		
//		Collections.sort(mbcs);
//		
//		for (int i = 0; i < mbcs.size(); i++) {
//			MBC mbc =  mbcs.get(i);
//			ArrayList<Integer> l_V = mbc.leftV;
//			ArrayList<Integer> r_V = mbc.rightV;
//			
//			boolean isDuplicated = false;
//			for(int l = 0; l < l_V.size(); l++) {
//				for(int r = 0; r < r_V.size(); r++) {
//					if(vis.contains(new Edge2(l_V.get(l), r_V.get(r)))) {
//						isDuplicated  = true;
//						break;
//					}
//				}
//				if(isDuplicated) break;
//			}
//			
//			if(!isDuplicated) {
//				mec.add(mbc);
//				mbcNum ++;
//				edgesNum += mbc.size;
//				for(int l = 0; l < l_V.size(); l++) {
//					for(int r = 0; r < r_V.size(); r++) {
//						vis.add(new Edge2(l_V.get(l), r_V.get(r)));
//					}
//				}	
//			}
//		}
//		
//		BufferedWriter out  =  new BufferedWriter(new FileWriter(outFileString));
//		
//		out.write("#maximal bicliques :" + mbcNum);
//		out.newLine();
//		out.write("#covered edges :" + edgesNum);
//		out.newLine();
//		for(int i = 0; i < mec.size(); i++) {
//			MBC mbc = mec.get(i);
//			out.write(""+mbc.leftV);
//			out.write(" | ");
//			out.write(""+mbc.rightV);
//			out.newLine();
//		}
//		
//		out.close();
//		
//		
//		
//	}
//	
	
	public static void main(String args[])throws IOException
	{	
		
		Runtime.getRuntime().maxMemory();
		
		//int len=0;
		//String input_path="input";
		//String output_path="output/output_PMBE.txt";
		//File folder = new File(input_path);
		//File[] listOfFiles = folder.listFiles();
		//for (int i = 0; i < listOfFiles.length; i++) 
		  //System.out.println("File " + listOfFiles[i].getName());
		String fileName = args[0];
		int p_thr = Integer.parseInt(args[1]);
		int q_thr = Integer.parseInt(args[2]);
		String outFiles = "result/";
		String outfileName = "out_"+args[1] +"-"+args[2];
		Writer output = null;
		boolean flag2 =true;
		while(flag2)
		{
			flag2 = false;
			BufferedReader object=new BufferedReader(new FileReader(fileName));///file containing input,soc-advogato.txt,c-fat,new,input_kellar4,johnson16-2-4
			String sCurrentLine;
			sCurrentLine = object.readLine();
			String[] vertices = sCurrentLine.split(" ");
			int threshold=Integer.parseInt(vertices[6]);
			if(threshold==0)// for enumerating all maximal bicliques without any threshold
			{
				ArrayList<Integer> X=new ArrayList<Integer>();
				ArrayList<Integer> tail=new ArrayList<Integer>();//saving the candidate in a bipartite graph B i.e. U
				ArrayList<Integer> tailrev=new ArrayList<Integer>();//saving the other side of bipartite graph i.e. V
				PMBE obj=new PMBE();
				int count=Integer.parseInt(vertices[3]);//执行次数
				if(Integer.parseInt(vertices[0]) > Integer.parseInt(vertices[1]))
				   mapsub=new int[Integer.parseInt(vertices[0])+1];
				else 
				   mapsub=new int[Integer.parseInt(vertices[1])+1];
				
				while ((sCurrentLine = object.readLine()) != null) // adding vertices to tail and tailrev
				{
					String[] nodes = sCurrentLine.split(" ");
					obj.add(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1])); //添加u -neib
					obj.addrev(Integer.parseInt(nodes[1]), Integer.parseInt(nodes[0])); //添加v -neib
				}
				tail = new ArrayList<Integer>(map.keySet()); // order_U
				tailrev = new ArrayList<Integer>(maprev.keySet());// order_V
				//*****************************************
				// creating the CDAG using create_dag() and balance_dag() 
				long startTime_dag  = System.nanoTime();
				//The CDAG is created for which ever side is smaller 
				if(tail.size()<tailrev.size())
				{
					Set<Entry<Integer, ArrayList<Integer>>> set = map.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> i = set.iterator();
					while (i.hasNext()) 
					{
						Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)i.next();
						boolean test=obj.isnotsubset((int)pair.getKey());
						if(test==true)
							mapsub[(int)pair.getKey()]=0;	
						else
							mapsub[(int)pair.getKey()]=1;
					}
					
					create_dag(tail);//creates a nodes for a CDAG and add neighbors
					balance_dag();//remove the redundancy relationships
					Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
					Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
					while (i_node1.hasNext()) {
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
						if(mapsub[pair.getValue().id]==0)
							traverse_dag(pair.getValue());//performs indexing
						}
				}
				else
				{
					Set<Entry<Integer, ArrayList<Integer>>> set = maprev.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> i = set.iterator();
					while (i.hasNext()) 
					{
						Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)i.next();
						boolean test=obj.isnotsubsetrev((int)pair.getKey());
						if(test==true)
							mapsub[(int)pair.getKey()]=0;
						else
							mapsub[(int)pair.getKey()]=1;
							
					}	
					create_dagrev(tailrev);//creates a nodes for a CDAG and add neighbors
					balance_dagrev();//remove the redundancy relationships
					Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
					Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
					while (i_node1.hasNext()) 
					{
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
						if(mapsub[pair.getValue().id]==0)
							traverse_dag(pair.getValue());//performs indexing
					}
					set_node1 = mapnode.entrySet();
					i_node1 = set_node1.iterator();
					while (i_node1.hasNext()) 
					{
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
						if(mapsub[pair.getValue().id]==0)
							traverse_dag(pair.getValue());//performs indexing
					}
				}
				long endTime_dag   = System.nanoTime();
				long totalTime_dag = endTime_dag - startTime_dag;
				//**************************** CDAG and the Rev-Topological Ordering is complete, now we can start PMBE
				obj.ini_hashmap();
				Set<Entry<Integer, Node>> set_node = mapnode.entrySet();
				Iterator<Entry<Integer, Node>> i_node = set_node.iterator();
				////////////////////////sort based on children (SORTING)
				long starttime_sortTime = System.nanoTime();
				if(tail.size()<tailrev.size())
					{
					set_node = mapnode.entrySet();
					i_node = set_node.iterator();
					while (i_node.hasNext()) {
						Map.Entry<Integer,Node> pair = (Entry<Integer, Node>)i_node.next();
						sort_dag(pair.getValue());
					}
					tail=tail_sort;
					}
				else
					{
					set_node = mapnode.entrySet();
					i_node = set_node.iterator();
					while (i_node.hasNext()) {
						Map.Entry<Integer,Node> pair = (Entry<Integer, Node>)i_node.next();
						sort_dag(pair.getValue());
					}
					tailrev=tail_sort;
					}
				long endtime_sortTime = System.nanoTime();
				long total_sorttime = endtime_sortTime-starttime_sortTime;
				File file = new File(outfileName);
				output = new BufferedWriter(new FileWriter(file,true));
				long offline_time=total_sorttime+totalTime_dag;
				if(tail.size()<tailrev.size())
					output.write("Filename = "+fileName+"\n"+"Time for CDAG (U) "+totalTime_dag+" ns, sort time "+total_sorttime+" ns, Total offline time "+offline_time+" ns, Candidate Size "+tail.size()+", Adjacency Size "+tailrev.size()+"\n");
				else
					output.write("Filename = "+fileName+"\n"+"Time for CDAG (V) "+totalTime_dag+" ns, sort time "+total_sorttime+" ns, Total offline time "+offline_time+" ns, Candidate Size "+tailrev.size()+", Adjacency Size "+tail.size()+"\n");
				output.close();
				////////////////////////////////////Biclique Enumeration
				while(count>0)
				{
					file = new File(fileName);
					output = new BufferedWriter(new FileWriter(file,true));
					ArrayList<Integer> tail1=new ArrayList<Integer>(tail);
					ArrayList<Integer> tailrev1=new ArrayList<Integer>(tailrev);
					ArrayList<Integer> tail2=new ArrayList<Integer>(tail);
					ArrayList<Integer> tailrev2=new ArrayList<Integer>(tailrev);
					index=0;
					long totalTime=0;
					if(tail1.size()<tailrev1.size()) {
						obj.PMBE1();
						long startTime = System.nanoTime();
						obj.PMBE_inbuilt(X,tailrev2,tail2);
						long endTime   = System.nanoTime();
						totalTime = endTime - startTime;
					}							
					else
					{
						//System.out.println("tailrev is cand");
						obj.PMBE1();
						long startTime = System.nanoTime();
						obj.PMBE_inbuilt_rev(X,tail2,tailrev2); 
						long endTime   = System.nanoTime();
						totalTime = endTime - startTime;
					}
					System.out.println(" Dataset = "+fileName+", Time Taken = "+totalTime+ ", ns number of bicliques B1= "+index+"\n");
					output.write(" Time for Bicliques T= "+totalTime+"\t" +" ns, number of Bilciques = "+index+"\n");
					count--;
					output.close();
					//obj.retrieveEdgeCover(Integer.parseInt(vertices[0]),Integer.parseInt(vertices[1]),fileName+outfileName);
				}
			}
			else
			{
				
				ArrayList<Integer> X=new ArrayList<Integer>();
				ArrayList<Integer> tail=new ArrayList<Integer>();
				ArrayList<Integer> tailrev=new ArrayList<Integer>();
				PMBE obj=new PMBE();
				int count=Integer.parseInt(vertices[3]);
				int ms1 = p_thr;
				int ms2 = q_thr;
				//int ms1=Integer.parseInt(vertices[4]);
				//int ms2=Integer.parseInt(vertices[5]);
				if(Integer.parseInt(vertices[0]) > Integer.parseInt(vertices[1]))
					   mapsub=new int[Integer.parseInt(vertices[0])+1];
				else 
					   mapsub=new int[Integer.parseInt(vertices[1])+1];
				//mapsub=new int[Integer.parseInt(vertices[0])+1];
				while ((sCurrentLine = object.readLine()) != null) {
					String[] nodes = sCurrentLine.split(" ");
					obj.add(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1]));
					obj.addrev(Integer.parseInt(nodes[1]), Integer.parseInt(nodes[0]));
				}
				tail = new ArrayList<Integer>(map.keySet());
				tailrev = new ArrayList<Integer>(maprev.keySet());
				//trimming the vertices
				while(true)
				{
					int flag=0;
					Set<Entry<Integer, ArrayList<Integer>>> set = map.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> i = set.iterator();
					while(i.hasNext())
					{
						Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)i.next();
						if(map.get(pair.getKey()).size()<ms2)
						{
							Iterator<Integer> it=((ArrayList<Integer>) pair.getValue()).iterator();
							while(it.hasNext())
							{
								int user=it.next();
								if(maprev.containsKey(user))
								maprev.get(user).remove(pair.getKey());
							}
							flag=1;
							tail.remove(pair.getKey());
							i.remove();
						}
					}
					Set<Entry<Integer, ArrayList<Integer>>> setrev = maprev.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> irev = setrev.iterator();
					while(irev.hasNext())
					{
						Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)irev.next();
						if(maprev.get(pair.getKey()).size()<ms1)
						{
							Iterator<Integer> it=((ArrayList<Integer>) pair.getValue()).iterator();
							while(it.hasNext())
							{
								int user =it.next();
								if(map.containsKey(user))
								map.get(user).remove(pair.getKey());
							}
							tailrev.remove(pair.getKey());
							irev.remove();
							flag=1;
						}
					}
					if(flag==0)
						break;
				}
				long startTime_dag  = System.nanoTime();
				if(tail.size()<tailrev.size())
				{
					Set<Entry<Integer, ArrayList<Integer>>> set = map.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> i = set.iterator();
					while (i.hasNext()) 
					{
						Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)i.next();
						boolean test=obj.isnotsubset((int)pair.getKey());
						if(test==true)
							mapsub[(int)pair.getKey()]=0;	
						else
							mapsub[(int)pair.getKey()]=1;
					}
					create_dag(tail);//creates a nodes for a CDAG and add neighbors
					balance_dag();//remove the redundancy relationships
					Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
					Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
					while (i_node1.hasNext()) {
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
						if(mapsub[pair.getValue().id]==0)
							traverse_dag(pair.getValue());//performs indexing
						}						
				}
				else
				{
					Set<Entry<Integer, ArrayList<Integer>>> set = maprev.entrySet();
					Iterator<Entry<Integer, ArrayList<Integer>>> i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<Integer,ArrayList<Integer>> pair = (Entry<Integer, ArrayList<Integer>>)i.next();
						boolean test=obj.isnotsubsetrev((int)pair.getKey());
						if(test==true)
							mapsub[(int)pair.getKey()]=0;//	
						else
							mapsub[(int)pair.getKey()]=1;//		
					}
					create_dagrev(tailrev);//creates a nodes for a CDAG and add neighbors
					balance_dagrev();//remove the redundancy relationships
					Set<Entry<Integer, Node>> set_node1 = mapnode.entrySet();
					Iterator<Entry<Integer, Node>> i_node1 = set_node1.iterator();
					while (i_node1.hasNext()) {
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node1.next();
						if(mapsub[pair.getValue().id]==0)
							traverse_dag(pair.getValue());//performs indexing
						}
				}
				long endTime_dag   = System.nanoTime();
				long totalTime_dag = endTime_dag - startTime_dag;
				obj.ini_hashmap();
				Set<Entry<Integer, Node>> set_node = mapnode.entrySet();
				Iterator<Entry<Integer, Node>> i_node = set_node.iterator();
				////////////////////////Topological sort
				long starttime_sortTime = System.nanoTime();
				if(tail.size()<tailrev.size())
				{
					set_node = mapnode.entrySet();
					i_node = set_node.iterator();
					while (i_node.hasNext()) 
					{
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node.next();
						sort_dag(pair.getValue());
					}
					tail=tail_sort;
				}
			else
				{
					set_node = mapnode.entrySet();
					i_node = set_node.iterator();
					while (i_node.hasNext()) 
					{
						Map.Entry<Integer,Node> pair = (Map.Entry)i_node.next();
						sort_dag(pair.getValue());
					}
					tailrev=tail_sort;
				}
			
			long endtime_sortTime = System.nanoTime();
			
			long total_sorttime = endtime_sortTime-starttime_sortTime;
				
			output = null;
			
			////////////////////////////////////Biclique Enumeration
			long totalTime=0;
			long totalTime1=0;
			File file = new File(outFiles+outfileName);
			output = new BufferedWriter(new FileWriter(file,true));
			long offline_time=totalTime_dag+total_sorttime;
			if(tail.size()<tailrev.size()) 
				output.write("Filename = "+fileName+"\n"+"Time for CDAG (U)"+totalTime_dag+" ns, sort time "+total_sorttime+" ns, Total offline time "+offline_time+" ns, Candidate Size "+tail.size()+", Adjacency Size "+tailrev.size()+" ms1 = "+ms1+" ms2 = "+ms2+"\n");	
			else
				output.write("Filename = "+fileName+"\n"+"Time for CDAG (V)"+totalTime_dag+" ns, sort time "+total_sorttime+" ns, Total offline time "+offline_time+" ns, Candidate Size "+tailrev.size()+", Adjacency Size "+tail.size()+" ms1 = "+ms2+" ms2 = "+ms1+"\n");	
			output.close();
			while(count>0)
			{
				file = new File(outFiles+outfileName);
				output = new BufferedWriter(new FileWriter(file,true));
				ArrayList<Integer> tail1=new ArrayList<Integer>(tail);
				ArrayList<Integer> tailrev1=new ArrayList<Integer>(tailrev);
				ArrayList<Integer> tail2=new ArrayList<Integer>(tail);
				ArrayList<Integer> tailrev2=new ArrayList<Integer>(tailrev);
				index=0;
				if(tail1.size()<tailrev1.size()) {
					//System.out.println(tail2.size()+"tail is cand ms "+tail1.size());
					obj.PMBE1();
					long startTime = System.nanoTime();
					obj.PMBE_inbuilt_cand(X,tailrev2,tail2,ms1,ms2);
					long endTime   = System.nanoTime(); 	
					totalTime = endTime - startTime;//
				}		
				else
				{
					//System.out.println("tailrev is cand ms "+tailrev1.size());
					obj.PMBE1();
					long startTime = System.nanoTime();
					obj.PMBE_inbuilt_candrev(X,tail2,tailrev2,ms2,ms1);
					long endTime   = System.nanoTime();
					totalTime = endTime - startTime;
				}
				System.out.println(" Dataset = "+fileName+outfileName+", Time Taken = "+totalTime+ " ns, number of Bicliques B= "+index+"\n");
				output.write(" Time for Bicliques T= "+totalTime+"\t" +" ns, number of Bilciques = "+index+"\n");
				count--;
				output.close();
				//obj.retrieveEdgeCover(Integer.parseInt(vertices[0]),Integer.parseInt(vertices[1]),fileName+""+p_thr+"_"+q_thr);
				}
			}
			object.close();
			//len++;
		}
		}
}
