package j.combot.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

// Make a annotation processor that creates classes like this?
public class ArgGroup implements Iterable<Arg<?>>
{
	private Collection<Arg<?>> args;

	public ArgGroup( Collection<Arg<?>> childs )
	{
		this.args = childs;
	}

	public ArgGroup( Arg<?>... args ) {
		this( Lists.newArrayList( args ) );
	}

	public void add( Arg<?> part ) {
		args.add( part );
	}

	public void addAll( Collection<Arg<?>> parts ) {
		args.addAll( parts );
	}

	public void addAll( Arg<?>... parts ) {
		for ( Arg<?> part : parts ) {
			args.add( part );
		}
	}

	public boolean isEmpty() {
		return args.isEmpty();
	}

	public Collection<Arg<?>> getArgs() {
		return args;
	}


	public ArgGroup() {
		this( new ArrayList<Arg<?>>( 2 ) );
	}

	public void setArgs( Collection<Arg<?>> args ) {
		this.args = args;
	}

	public List<ValEntry> validate()
	{
		List<ValEntry> result = makeResultList();
		for ( Arg<?> c : args ) {
			result.addAll( c.validate() );
		}
		return result;
	}

	public List<String> getArgStrings()
	{
		List<String> result = makeResultList();
		for ( Arg<?> c : args ) {
			result.addAll( c.getArgStrings() );
		}
		return result;
	}

	public List<String> getNames()
	{
		List<String> result = makeResultList();
		for ( Arg<?> c : args ) {
			result.add( c.getName() );
		}
		return result;
	}

	public List<String> getTitles()
	{
		List<String> result = makeResultList();
		for ( Arg<?> c : args ) {
			result.add( c.getTitle() );
		}
		return result;
	}

	private <S> List<S> makeResultList() {
		return new ArrayList<>( args.size() );
	}

	@Override
	public Iterator<Arg<?>> iterator() {
		return args.iterator();
	}

	public ArgGroup clone() {
		List<Arg<?>> result = makeResultList();
		for ( Arg<?> a : args ) {
			result.add( (Arg<?>) a.clone() );
		}
		return new ArgGroup( result );
	}


}
