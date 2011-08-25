package j.combot.command;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CombinedValidator<T> extends Validator<T>  {

	private List<Validator<? super T>> vals;

	@SafeVarargs
	public CombinedValidator( Validator<? super T>... vals )
	{
		this.vals = Arrays.asList( vals );
	}

	@Override public List<ValEntry> validate( final T value ) {
		return Lists.newArrayList( Iterables.concat(
				Lists.transform( vals,
						new Function<Validator<? super T>, List<ValEntry>>() {
							public List<ValEntry> apply( Validator<? super T> input ) {
								return input.validate( value );
							}

						} ) ) );

	}



}
