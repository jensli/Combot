package j.combot.command;

import j.combot.validator.Validator;
import j.util.prefs.PrefNode;

import java.util.List;

/**
 * Delegating Arg
 */
public class DelArg<T> extends Arg<T> {

	@PrefNode
	private Arg<?> child;

	public DelArg( String title, String name, Arg<?> child ) {
		super( "delegate", "delegate", null, Validator.NULL_VALIDATOR );
		this.child = child;
	}

	public Arg<?> getChild() {
		return child;
	}

	@Override
	public void setDefaultFromVisual() {
		super.setDefaultFromVisual();
		child.setDefaultFromVisual();
	}

	@Override
	public List<String> getArgStrings() {
		return child.getArgStrings();
	}

	@Override
	public DelArg<T> clone() {
		DelArg<T> clone = (DelArg<T>) super.clone();
		clone.child = child.clone();
		return clone;
	}

}
