package j.combot.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PartContainer<P extends CommandPart<?>>
{
	private List<P> childs;

	public PartContainer( List<P> childs )
	{
		this.childs = childs;
	}

	public void add( P part ) {
		childs.add( part );
	}

	public void addAll( Collection<P> parts ) {
		childs.addAll( parts );
	}

	public void addAll( P... parts ) {
		for ( P part : parts ) {
			childs.add( part );
		}
	}


	public PartContainer() {
		this( new ArrayList<P>( 2 ) );
	}

	public List<ValEntry> validate()
	{
		List<ValEntry> result = makeResultList();
		for ( P c : childs ) {
			result.addAll( c.validate() );
		}
		return result;
	}

	public List<String> getArgStrings()
	{
		List<String> result = makeResultList();
		for ( P c : childs ) {
			result.addAll( c.getArgStrings() );
		}
		return result;
	}


	public List<String> getNames()
	{
		List<String> result = makeResultList();
		for ( P c : childs ) {
			result.add( c.getName() );
		}
		return result;
	}


	private <S> List<S> makeResultList() {
		return new ArrayList<S>( childs.size() );
	}
}
