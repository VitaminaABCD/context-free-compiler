package com.compilatore.grammar;

import java.util.List;
import java.util.Set;

public interface IGrammar {

	public List<String> getV();

	public void setV(List<String> v);

	public List<String> getT();

	public void setT(List<String> e);

	public List<Production> getP();

	public void setP(List<Production> p);
	
	public void setS(String s);
	
	public String getS();
	
	public Set<String>[] getFirst();

	public Set<String>[] getFollow();
	
	public Set<String> getFirst(String A);
}
